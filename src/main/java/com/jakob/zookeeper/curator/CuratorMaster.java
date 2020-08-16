package com.jakob.zookeeper.curator;

import com.jakob.zookeeper.recovery.RecoveredAssignments;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.api.UnhandledErrorListener;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.io.Closeable;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static com.jakob.zookeeper.recovery.RecoveredAssignments.RecoveryCallback.FAILED;

@Slf4j
public class CuratorMaster implements Closeable, LeaderSelectorListener {

    private final String myId;
    private final CuratorFramework client;
    private final LeaderSelector leaderSelector;
    private final CuratorCache workersCache;
    private final CuratorCache tasksCache;

    /*
     * We use one latch as barrier for the master selection
     * and another one to block the execution of master
     * operations when the ZooKeeper session transitions
     * to suspended.
     */
    private final CountDownLatch leaderLatch = new CountDownLatch(1);
    private final CountDownLatch closeLatch = new CountDownLatch(1);

    public CuratorMaster(String myId, String endpoint, RetryPolicy retryPolicy) {
        log.info(myId + ": " + endpoint);
        this.myId = myId;
        this.client = CuratorFrameworkFactory.newClient(endpoint, retryPolicy);
        this.leaderSelector = new LeaderSelector(client, "/master", this);
        this.workersCache = CuratorCache.builder(client, "/workers").build();
        this.tasksCache = CuratorCache.builder(client, "/tasks").build();
    }

    public void startZk() {
        this.client.start();
    }

    public void bootstrap() throws Exception {
        client.create().forPath("/workers");
        client.create().forPath("/tasks");
        client.create().forPath("/assign");
        client.create().forPath("/status");
    }

    public void runForMaster() {
        leaderSelector.setId(myId);
        log.info("Starting master selection: " + myId);
        leaderSelector.start();
    }

    public void awaitLeadership() throws InterruptedException {
        leaderLatch.await();
    }

    public boolean isLeader() {
        return leaderSelector.hasLeadership();
    }

    private CountDownLatch recoveryLatch = new CountDownLatch(0);

    @Override
    public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
        log.info("Mastership participants: " + myId + ", " + leaderSelector.getParticipants());

        client.getCuratorListenable().addListener(masterListener);
        client.getUnhandledErrorListenable().addListener(errorListener);

        workersCache.listenable().addListener(workersCacheListener);
        workersCache.start();

        (new RecoveredAssignments(client.getZookeeperClient().getZooKeeper())).recover((rc, tasks) -> {
            try {

                if (rc == FAILED) {
                    log.error("Recovery of assigned tasks failed.");
                } else {
                    log.info("Assigning recovered tasks");
                    recoveryLatch = new CountDownLatch(tasks.size());
                    assignTasks(tasks);
                }

                new Thread(() -> {
                    try {
                        recoveryLatch.await();

                        tasksCache.listenable().addListener(tasksCacheListener);
                        tasksCache.start();
                    } catch (Exception e) {
                        log.error("Exception while assigning and get tasks.", e);
                    }
                }).start();
                leaderLatch.countDown();
            } catch (Exception e) {
                log.error("Exception while executing the recovery callback", e);
            }
        });
        closeLatch.await();
    }

    private final CuratorListener masterListener = (client, event) -> {
        try {
            switch (event.getType()) {
                case CHILDREN:
                    if (event.getPath().contains("/assign")) {
                        log.info("Successfully got a list of assignments: " + event.getChildren().size() + " tasks");

                        List<String> tasks = event.getChildren();
                        for (String task : tasks) {
                            deleteAssignment(event.getPath() + "/" + task);
                        }
                        deleteAssignment(event.getPath());

                        assignTasks(tasks);
                    } else {
                        log.warn("Unexpected event: " + event.getPath());
                    }
                    break;
                case CREATE:
                    if (event.getPath().contains("/assign")) {
                        log.info("Task assigned correctly: " + event.getName());
                        deleteTask(event.getPath().substring(event.getPath().lastIndexOf("-") + 1));
                    }
                    break;
                case DELETE:
                    /*
                     * We delete znodes in two occasions:
                     * 1- When reassigning tasks due to a faulty worker;
                     * 2- Once we have assigned a task, we remove it from
                     *    the list of pending tasks.
                     */
                    if (event.getPath().contains("/tasks")) {
                        log.info("Result of delete operation: " + event.getResultCode() + ", " + event.getPath());
                    } else if (event.getPath().contains("/assign")) {
                        log.info("Task correctly deleted: " + event.getPath());
                        break;
                    }
                    break;
                case WATCHED:
                    // There is no case implemented currently.

                    break;
                default:
                    log.error("Default case: " + event.getType());
                    break;
            }
        } catch (Exception e) {
            log.error("Exception while processing event", e);
            close();
        }
    };
    private final UnhandledErrorListener errorListener = (message, e) -> {
        log.error("Unrecoverable exception: " + e.getMessage(), e);
        close();
    };

    private final CuratorCacheListener workersCacheListener = (type, oldData, data) -> {
        if (type == CuratorCacheListener.Type.NODE_DELETED) {
            try {
                getAbsentWorkerTasks(oldData.getPath().replace("/workers", ""));
            } catch (Exception e) {
                log.error("Exception while trying to re-assign tasks", e);
            }
        }
    };

    private void getAbsentWorkerTasks(String worker) throws Exception {
        client.getChildren().inBackground().forPath("/assign/" + worker);
    }

    private void deleteAssignment(String path) throws Exception {
        log.info("Delete assignment: " + path);
        client.delete().inBackground().forPath(path);
    }

    private final CuratorCacheListener tasksCacheListener = (type, oldData, data) -> {
        if (type == CuratorCacheListener.Type.NODE_CREATED) {
            try {
                assignTask(data.getPath().replaceFirst("/tasks/", ""), data.getData());
            } catch (Exception e) {
                log.error("Exception while assigning task");
            }
        }
    };

    private void assignTasks(List<String> tasks) throws Exception {
        for (String task : tasks) {
            assignTask(task, client.getData().forPath("/tasks/" + task));
        }
    }

    private final Random random = new Random(System.currentTimeMillis());

    private void assignTask(String task, byte[] data) throws Exception {
        int size = workersCache.size();
        String worker = workersCache.stream().collect(Collectors.toList()).get(random.nextInt(size)).getPath().replace("/workers/", "");
        log.info("Assign task {} to worker {}", task, worker);
        client.create().creatingParentsIfNeeded().inBackground().forPath("/assign/" + worker + "/" + task, data);
    }

    private void deleteTask(String number) throws Exception {
        log.info("Deleting task: " + number);
        client.delete().inBackground().forPath("/tasks/task-" + number);
        recoveryLatch.countDown();
    }

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState) {
        switch (newState) {
            case CONNECTED:
                //Nothing to do in this case.

                break;
            case RECONNECTED:
                // Reconnected, so I should
                // still be the leader.

                break;
            case SUSPENDED:
                log.warn("Session suspended");

                break;
            case LOST:
                try {
                    close();
                } catch (Exception e) {
                    log.warn("Exception while closing", e);
                }

                break;
            case READ_ONLY:
                // We ignore this case

                break;
        }
    }

    @Override
    public void close() {
        log.info("Closing");
        closeLatch.countDown();
        leaderSelector.close();
        client.close();
    }

    public static void main(String[] args) {
        try {
            CuratorMaster master = new CuratorMaster(args[0], args[1], new ExponentialBackoffRetry(1000, 5));
            master.startZk();
            master.bootstrap();
            master.runForMaster();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
