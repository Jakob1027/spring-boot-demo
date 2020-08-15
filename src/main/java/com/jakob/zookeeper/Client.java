package com.jakob.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import static org.apache.zookeeper.CreateMode.PERSISTENT_SEQUENTIAL;
import static org.apache.zookeeper.KeeperException.Code.get;
import static org.apache.zookeeper.Watcher.Event.EventType.NodeCreated;
import static org.apache.zookeeper.Watcher.Event.EventType.None;
import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

@Slf4j
public class Client implements Watcher, Closeable {

    private ZooKeeper zk;
    private final String endpoint;
    private boolean connected;
    private boolean expired;

    public Client(String endpoint) {
        this.endpoint = endpoint;
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isExpired() {
        return expired;
    }

    public void startZK() throws IOException {
        zk = new ZooKeeper(endpoint, 15000, this);
    }

    @Override
    public void process(WatchedEvent e) {
        log.info("Processing event: " + e.toString());
        if (e.getType() == None) {
            switch (e.getState()) {
                case SyncConnected:
                    connected = true;
                    break;
                case Disconnected:
                    connected = false;
                    break;
                case Expired:
                    expired = true;
                    connected = false;
                    log.error("Session expiration");
                default:
                    break;
            }
        }
    }

    public void submitTask(String task, TaskObject taskObject) {
        taskObject.setTask(task);
        zk.create("/tasks/task-", taskObject.getTask().getBytes(), OPEN_ACL_UNSAFE, PERSISTENT_SEQUENTIAL, submitTaskCallback, taskObject);
    }

    private final AsyncCallback.StringCallback submitTaskCallback = (rc, path, ctx, name) -> {
        switch (get(rc)) {
            case CONNECTIONLOSS:
                TaskObject taskObject = (TaskObject) ctx;
                submitTask(taskObject.getTask(), taskObject);
                break;
            case OK:
                log.info("My created task name: " + name);
                ((TaskObject) ctx).setTaskName(name);
                watchStatus(name.replace("/tasks/", "/status/"), ctx);
                break;
            case NODEEXISTS:
                log.warn("Task has been already created");
                break;
            default:
                log.error("Something went wrong when create task", KeeperException.create(get(rc), path));
        }
    };

    private final ConcurrentHashMap<String, TaskObject> ctxMap = new ConcurrentHashMap<>();

    private void watchStatus(String path, Object ctx) {
        ctxMap.put(path, (TaskObject) ctx);
        zk.exists(path, statusWatcher, statusCallback, ctx);
    }

    private final Watcher statusWatcher = new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            if (event.getType() == NodeCreated) {
                assert event.getPath().contains("/status/task-");
                assert ctxMap.containsKey(event.getPath());
                zk.getData(event.getPath(), false, getDataCallback, ctxMap.get(event.getPath()));
            }
        }
    };

    private final AsyncCallback.StatCallback statusCallback = new AsyncCallback.StatCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, Stat stat) {
            switch (get(rc)) {
                case CONNECTIONLOSS:
                    watchStatus(path, ctx);
                    break;
                case OK:
                    if (stat != null) {
                        zk.getData(path, false, getDataCallback, ctx);
                        log.info("Status node is there: " + path);
                    }
                    break;
                case NONODE:
                    break;
                default:
                    log.error("Something went wrong when " +
                            "checking if the status node exists: " +
                            KeeperException.create(get(rc), path));

                    break;
            }
        }
    };

    private final AsyncCallback.DataCallback getDataCallback = new AsyncCallback.DataCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
            switch (get(rc)) {
                case CONNECTIONLOSS:
                    zk.getData(path, false, getDataCallback, ctx);
                    break;
                case OK:
                    String status = new String(data);

                    TaskObject taskObject = (TaskObject) ctx;
                    assert taskObject != null;
                    log.info("Task: " + taskObject.getTaskName() + " status: " + status);
                    taskObject.setStatus(status.contains("done"));
                    zk.delete(path, -1, deleteCallback, null);
                    ctxMap.remove(path);
                    break;
                case NONODE:
                    log.warn("Status node has gong");
                    break;
                default:
                    log.error("Something went wrong here, ", KeeperException.create(get(rc), path));
                    break;

            }
        }
    };

    private final AsyncCallback.VoidCallback deleteCallback = new AsyncCallback.VoidCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx) {
            switch (get(rc)) {
                case CONNECTIONLOSS:
                    zk.delete(path, -1, deleteCallback, null);
                    break;
                case OK:
                    log.info("Successfully delete status node: " + path);
                    break;
                case NONODE:
                    log.warn("Status node has been deleted: " + path);
                    break;
                default:
                    log.error("Something went wrong when delete status node", KeeperException.create(get(rc), path));
            }
        }
    };

    static class TaskObject {
        private String task;
        private String taskName;
        private boolean successful;
        private boolean done;
        private final CountDownLatch latch = new CountDownLatch(1);

        public void setStatus(boolean status) {
            successful = status;
            done = true;
            latch.countDown();
        }

        public void waitUntilDone() {
            try {
                latch.await();
            } catch (InterruptedException e) {
                log.warn("Interrupted when waiting for task to get done");
            }
        }

        public String getTask() {
            return task;
        }

        public void setTask(String task) {
            this.task = task;
        }

        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        public boolean isSuccessful() {
            return successful;
        }

        public boolean isDone() {
            return done;
        }
    }

    @Override
    public void close() {
        log.info("Closing...");
        try {
            zk.close();
        } catch (InterruptedException e) {
            log.warn("ZooKeeper interrupted while closing");
        }
    }

    public static void main(String[] args) throws Exception {
        Client c = new Client(args[0]);
        c.startZK();

        while (!c.isConnected()) {
            Thread.sleep(100);
        }

        TaskObject task1 = new TaskObject();
        TaskObject task2 = new TaskObject();

        c.submitTask("Sample task", task1);
        c.submitTask("Another sample task", task2);

        task1.waitUntilDone();
        task2.waitUntilDone();
    }
}
