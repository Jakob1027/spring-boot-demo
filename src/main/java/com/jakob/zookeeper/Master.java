package com.jakob.zookeeper;

import com.jakob.zookeeper.recovery.RecoveredAssignments;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import static com.jakob.zookeeper.Master.MasterState.*;
import static com.jakob.zookeeper.recovery.RecoveredAssignments.RecoveryCallback;
import static org.apache.zookeeper.AsyncCallback.*;
import static org.apache.zookeeper.CreateMode.EPHEMERAL;
import static org.apache.zookeeper.CreateMode.PERSISTENT;
import static org.apache.zookeeper.KeeperException.Code.get;
import static org.apache.zookeeper.KeeperException.create;
import static org.apache.zookeeper.Watcher.Event.EventType.NodeChildrenChanged;
import static org.apache.zookeeper.Watcher.Event.EventType.NodeDeleted;
import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

@Slf4j
public class Master implements Watcher, Closeable {

    enum MasterState {
        ELECTED, NOTELECTED, RUNNING
    }

    private volatile MasterState state = RUNNING;

    public MasterState getState() {
        return state;
    }

    private ZooKeeper zk;
    private final String endpoint;
    private final Random random = new Random(this.hashCode());
    private final String serverId = Integer.toHexString(random.nextInt());
    private volatile boolean connected = false;
    private volatile boolean expired = false;

    private ChildrenCache workersCache;
    private ChildrenCache tasksCache;


    public Master(String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * 启动zk
     */
    public void startZk() {
        try {
            zk = new ZooKeeper(endpoint, 14000, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭 zk session
     *
     */
    private void stopZk() throws InterruptedException {
        zk.close();
    }

    @Override
    public void process(WatchedEvent e) {
        log.info("Processing event: " + e.toString());
        if (e.getType() == Event.EventType.None) {
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

    public void bootstrap() {
        createParent("/workers", new byte[0]);
        createParent("/tasks", new byte[0]);
        createParent("/assign", new byte[0]);
        createParent("/status", new byte[0]);
    }

    private final StringCallback createParentCallback = (rc, path, ctx, name) -> {
        switch (Code.get(rc)) {
            case CONNECTIONLOSS:
                createParent(path, (byte[]) ctx);
                break;
            case OK:
                log.info("OK, parent created");
                break;
            case NODEEXISTS:
                log.warn("Parent already registered: " + path);
                break;
            default:
                log.error("Something went wrong: ", create(Code.get(rc), path));
        }
    };

    private void createParent(String path, byte[] data) {
        zk.create(path, data, OPEN_ACL_UNSAFE, PERSISTENT, createParentCallback, data);
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isExpired() {
        return expired;
    }

    /*
     **************************************
     **************************************
     * Methods related to master election.*
     **************************************
     **************************************
     */

    private final DataCallback masterCheckCallback = (rc, path, ctx, data, stat) -> {
        switch (get(rc)) {
            case CONNECTIONLOSS:
                checkMaster();
                return;
            case NONODE:
                runForMaster();
                break;
            case OK:
                if (serverId.equals(new String(data))) {
                    state = ELECTED;
                    takeLeaderShip();
                } else {
                    state = NOTELECTED;
                    masterExists();
                }
                break;
            default:
                log.error("Error when reading data", KeeperException.create(get(rc), path));
        }
    };

    private void checkMaster() {
        zk.getData("/master", false, masterCheckCallback, null);
    }

    StringCallback masterCreateCallback = (rc, path, ctx, name) -> {
        switch (get(rc)) {
            case CONNECTIONLOSS:
                checkMaster();
                break;
            case OK:
                state = ELECTED;
                takeLeaderShip();
                break;
            case NODEEXISTS:
                state = NOTELECTED;
                masterExists();
                break;
            default:
                state = NOTELECTED;
                log.error("Something went wrong when running for master", create(get(rc), "/master"));
        }
        log.info("I'm " + (state == ELECTED ? "" : "not ") + "the leader " + serverId);
    };

    /**
     * 尝试创建 /master 节点
     */
    public void runForMaster() {
        log.info("Running for master");
        zk.create("/master", serverId.getBytes(), OPEN_ACL_UNSAFE, EPHEMERAL, masterCreateCallback, null);
    }

    private void masterExists() {
        zk.exists("/master", masterExistsWatcher, masterExistsCallback, null);
    }

    private final Watcher masterExistsWatcher = event -> {
        if (event.getType() == NodeDeleted) {
            assert "/master".equals(event.getPath());
            runForMaster();
        }
    };

    private final StatCallback masterExistsCallback = (rc, path, ctx, stat) -> {
        switch (get(rc)) {
            case CONNECTIONLOSS:
                masterExists();
                break;
            case OK:
                if (stat == null) {
                    state = RUNNING;
                    runForMaster();
                }
                break;
            default:
                checkMaster();
                break;
        }
    };

    private void takeLeaderShip() {
        log.info("Going for list of workers");
        getWorkers();

        (new RecoveredAssignments(zk)).recover((rc, tasks) -> {
            if (rc == RecoveryCallback.FAILED) {
                log.error("Recovery of assigned tasks failed.");
            } else {
                log.info("Assigning recovered tasks");
                getTasks();
            }
        });
    }

    private final Watcher workersChangeWatcher = event -> {
        if (event.getType() == NodeChildrenChanged) {
            assert "/workers".equals(event.getPath());
            getWorkers();
        }
    };

    public void getWorkers() {
        zk.getChildren("/workers", workersChangeWatcher, workersGetChildrenCallback, null);
    }

    private final ChildrenCallback workersGetChildrenCallback = (rc, path, ctx, children) -> {
        switch (get(rc)) {
            case CONNECTIONLOSS:
                getWorkers();
                break;
            case OK:
                log.info("Successfully get a list of workers: " + children.size() + " workers");
                reassignAndGet(children);
                break;
            default:
                log.error("get workers failed", KeeperException.create(get(rc), path));
        }
    };

    private void reassignAndGet(List<String> children) {
        List<String> toProcess;
        if (workersCache == null) {
            workersCache = new ChildrenCache(children);
            toProcess = null;
        } else {
            log.info("Removing and setting");
            toProcess = workersCache.removeAndSet(children);
        }
        if (toProcess != null) {
            for (String worker : toProcess) {
                getAbsentWorkerTasks(worker);
            }
        }
    }

    private void getAbsentWorkerTasks(String worker) {
        zk.getChildren("/assign/" + worker, false, workerAssignmentCallback, null);
    }

    private final ChildrenCallback workerAssignmentCallback = (rc, path, ctx, children) -> {
        switch (get(rc)) {
            case CONNECTIONLOSS:
                getAbsentWorkerTasks(path);
                break;
            case OK:
                log.info("Successfully get a list of assignments: " + children.size() + " tasks");
                /*
                 * reassign the tasks of the absent workers.
                 */
                for (String task : children) {
                    getDataReassign(path + "/" + task, task);
                }
                break;
            default:
                log.error("getChildren failed", KeeperException.create(Code.get(rc), path));
        }
    };

    /*
     ************************************************
     * Recovery of tasks assigned to absent worker. *
     ************************************************
     */

    static class RecreateTaskCtx {
        String path;
        String task;
        byte[] data;

        public RecreateTaskCtx(String path, String task, byte[] data) {
            this.path = path;
            this.task = task;
            this.data = data;
        }
    }

    /**
     * 重新分配任务
     * @param path 路径
     * @param task 任务
     */
    private void getDataReassign(String path, String task) {
        zk.getData(path, false, getDataReassignCallback, task);
    }

    private final DataCallback getDataReassignCallback = (rc, path, ctx, data, stat) -> {
        switch (get(rc)) {
            case CONNECTIONLOSS:
                getDataReassign(path, (String) ctx);
                break;
            case OK:
                recreateTask(new RecreateTaskCtx(path, (String) ctx, data));
                break;
            default:
                log.error("Something went wrong when getting data", KeeperException.create(get(rc), path));
        }
    };

    /**
     * 重新创建任务
     * @param ctx 上下文
     */
    private void recreateTask(RecreateTaskCtx ctx) {
        zk.create("/tasks/" + ctx.task, ctx.data, OPEN_ACL_UNSAFE, PERSISTENT, recreateTaskCallback, ctx);
    }

    private final StringCallback recreateTaskCallback = (rc, path, ctx, name) -> {
        switch (get(rc)) {
            case CONNECTIONLOSS:
                recreateTask((RecreateTaskCtx) ctx);
                break;
            case OK:
                deleteAssignment(((RecreateTaskCtx) ctx).path);
                break;
            case NODEEXISTS:
                log.info("Node exists already, but if it hasn't been deleted, " +
                        "then it will eventually, so we keep trying: " + path);
                recreateTask((RecreateTaskCtx) ctx);
                break;
            default:
                log.error("Something went wrong when recreating task", KeeperException.create(Code.get(rc)));
        }
    };

    /**
     * 删除分配给已经下线的工作节点的任务
     * @param path 路径
     */
    private void deleteAssignment(String path) {
        zk.delete(path, -1, deleteAssignmentCallback, null);
    }

    private final VoidCallback deleteAssignmentCallback = (rc, path, ctx) -> {
        switch (get(rc)) {
            case CONNECTIONLOSS:
                deleteAssignment(path);
                break;
            case OK:
                log.info("Task correctly deleted: " + path);
                break;
            default:
                log.error("Failed to delete task dada", KeeperException.create(get(rc), path));
        }
    };

    private final Watcher taskChangeWatcher = event -> {
        if (event.getType() == NodeChildrenChanged) {
            assert "tasks".equals(event.getPath());
            getTasks();
        }
    };

    public void getTasks() {
        zk.getChildren("/tasks", taskChangeWatcher, getTasksCallback, null);
    }

    private final ChildrenCallback getTasksCallback = new ChildrenCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, List<String> children) {
            switch (get(rc)) {
                case CONNECTIONLOSS:
                    getTasks();
                    break;
                case OK:
                    List<String> toProcess;
                    if (tasksCache == null) {
                        tasksCache = new ChildrenCache(children);
                        toProcess = children;
                    } else {
                        toProcess = tasksCache.addAndSet(children);
                    }
                    if (toProcess != null) {
                        assignTasks(toProcess);
                    }
                    break;
                default:
                    log.error("Get tasks failed", KeeperException.create(get(rc), path));
            }
        }
    };

    public void assignTasks(List<String> tasks) {
        for (String task : tasks) {
            getTaskData(task);
        }
    }

    public void getTaskData(String task) {
        zk.getData("/tasks/" + task, false, getTaskDataCallback, task);
    }

    private final DataCallback getTaskDataCallback = new DataCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
            switch (get(rc)) {
                case CONNECTIONLOSS:
                    getTaskData((String) ctx);
                    break;
                case OK:
                    List<String> workers = workersCache.getList();
                    String worker = workers.get(random.nextInt(workers.size()));
                    String assignmentPath = "/assign/" + worker + "/" + ctx;
                    log.info("Assignment path: " + assignmentPath);
                    createAssignment(assignmentPath, data);
                    break;
                default:
                    log.error("Error when getting task data", KeeperException.create(get(rc), path));

            }
        }
    };

    private void createAssignment(String assignmentPath, byte[] data) {
        zk.create(assignmentPath, data, OPEN_ACL_UNSAFE, PERSISTENT, assignTaskCallback, data);
    }

    private final StringCallback assignTaskCallback = (rc, path, ctx, name) -> {
        switch (get(rc)) {
            case CONNECTIONLOSS:
                createAssignment(path, (byte[]) ctx);
                break;
            case OK:
                String[] paths = path.split("/");
                String worker = paths[2];
                String task = paths[3];
                log.info("Successfully assign task " + task + " to " + worker);
                deleteTask(task);
                break;
            case NODEEXISTS:
                log.warn("Task has been assigned");
                break;
            default:
                log.error("Error when trying to assign task.", KeeperException.create(get(rc), path));
        }
    };

    private void deleteTask(String task) {
        zk.delete("/tasks/" + task, -1, deleteTaskCallback, task);
    }

    private final VoidCallback deleteTaskCallback = (rc, path, ctx) -> {
        switch (get(rc)) {
            case CONNECTIONLOSS:
                deleteTask((String) ctx);
                break;
            case OK:
                log.info("Successfully delete task " + ctx);
                break;
            case NONODE:
                log.warn("Node do not exists: " + path);
                break;
            default:
                log.error("Error when delete node: " + path, KeeperException.create(get(rc), path));
        }
    };

    @Override
    public void close() {
        if (zk != null) {
            try {
                zk.close();
            } catch (InterruptedException e) {
                log.warn("Interrupted while closing ZooKeeper session.", e);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Master m = new Master(args[0]);
        m.startZk();

        while (!m.isConnected()) {
            Thread.sleep(100);
        }
        /*
         * bootstrap() creates some necessary znodes.
         */
        m.bootstrap();

        /*
         * now runs for master.
         */
        m.runForMaster();

        while (!m.isExpired()) {
            Thread.sleep(1000);
        }

        m.stopZk();
    }
}
