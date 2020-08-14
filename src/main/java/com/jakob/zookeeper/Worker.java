package com.jakob.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.apache.zookeeper.AsyncCallback.*;
import static org.apache.zookeeper.CreateMode.EPHEMERAL;
import static org.apache.zookeeper.CreateMode.PERSISTENT;
import static org.apache.zookeeper.KeeperException.Code.get;
import static org.apache.zookeeper.Watcher.Event.EventType.NodeChildrenChanged;
import static org.apache.zookeeper.Watcher.Event.EventType.None;
import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

@Slf4j
public class Worker implements Watcher, AutoCloseable {

    private ZooKeeper zk;
    private String status;
    private final String endpoint;
    private final Random random = new Random(this.hashCode());
    private final String serverId = Integer.toHexString(random.nextInt());
    private volatile boolean connected = false;
    private volatile boolean expired = false;
    private final ThreadPoolExecutor executor;


    public Worker(String endpoint) {
        this.endpoint = endpoint;
        executor = new ThreadPoolExecutor(1, 1, 1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(200));
    }

    public void startZK() throws IOException {
        zk = new ZooKeeper(endpoint, 15000, this);
    }

    @Override
    public void process(WatchedEvent event) {
        log.info(event.toString() + ", " + endpoint);
        if (event.getType() == None) {
            switch (event.getState()) {
                case SyncConnected:
                    connected = true;
                    break;
                case Disconnected:
                    connected = false;
                    break;
                case Expired:
                    expired = true;
                    connected = false;
                    log.error("Session expired");
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 是否已连接
     * @return connected or not
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * session 是否过期
     * @return session expired or not
     */
    public boolean isExpired() {
        return expired;
    }

    /**
     * 在 assign 节点下创建此 worker 节点以接收任务
     */
    public void bootstrap() {
        createAssignNode();
    }

    private void createAssignNode() {
        zk.create("/assign/worker-" + serverId, new byte[0], OPEN_ACL_UNSAFE, PERSISTENT, createAssignCallback, null);
    }

    private final StringCallback createAssignCallback = (rc, path, ctx, name) -> {
        switch (get(rc)) {
            case CONNECTIONLOSS:
                createAssignNode();
                break;
            case OK:
                log.info("Assign node created");
                break;
            case NODEEXISTS:
                log.warn("Assign node already exists");
                break;
            default:
                log.error("Something went wrong", KeeperException.create(get(rc), path));
        }
    };

    private final StringCallback registerCallback = (rc, path, ctx, name) -> {
        switch (get(rc)) {
            case CONNECTIONLOSS:
                register();
                break;
            case OK:
                log.info("Registered successfully: " + serverId);
                break;
            case NODEEXISTS:
                log.warn("Already registered: " + serverId);
                break;
            default:
                log.error("Something went wrong: ", KeeperException.create(Code.get(rc), path));
        }
    };

    /**
     * 在 /workers 节点下注册当前 worker
     */
    public void register() {
        zk.create("/workers/worker-" + serverId, "Idle".getBytes(), OPEN_ACL_UNSAFE, EPHEMERAL, registerCallback, null);
    }

    private final StatCallback statusUpdateCallback = (rc, path, ctx, stat) -> {
        if (get(rc) == Code.CONNECTIONLOSS) {
            updateStatus((String) ctx);
        }
    };

    private synchronized void updateStatus(String status) {
        if (status == this.status) {
            zk.setData("/workers/worker-" + serverId, status.getBytes(), -1, statusUpdateCallback, status);
        }
    }

    public void setStatus(String status) {
        this.status = status;

        updateStatus(status);
    }

    private int executionCount;

    private synchronized void changeExecutionCount(int changeCount) {
        executionCount += changeCount;
        if (executionCount == 0 && changeCount < 0) {
            setStatus("Idle");
        }
        if (executionCount == 1 && changeCount > 0) {
            setStatus("Working");
        }
    }

    /*
     ***************************************
     ***************************************
     * Methods to wait for new assignments.*
     ***************************************
     ***************************************
     */

    private final Watcher newTaskWatcher = event -> {
        if (event.getType() == NodeChildrenChanged) {
            assert ("/assign/" + serverId).equals(event.getPath());
            getTasks();
        }
    };

    public void getTasks() {
        zk.getChildren("/assign/" + serverId, newTaskWatcher, taskGetChildrenCallback, null);
    }

    private final ChildrenCache assignedTasksCache = new ChildrenCache();

    private final ChildrenCallback taskGetChildrenCallback = new ChildrenCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, List<String> children) {
            switch (get(rc)) {
                case CONNECTIONLOSS:
                    getTasks();
                    break;
                case OK:
                    if (children != null) {
                        executor.execute(new Runnable() {
                            private List<String> children;
                            private DataCallback cb;

                            public Runnable init(List<String> children, DataCallback cb) {
                                this.children = children;
                                this.cb = cb;
                                return this;
                            }

                            @Override
                            public void run() {
                                if (children == null)
                                    return;
                                log.info("Looping into tasks");
                                setStatus("Working");
                                for (String task : children) {
                                    log.trace("New task: {}", task);
                                    zk.getData("/assign/worker-" + serverId + "/" + task, false, cb, null);
                                }
                            }
                        }.init(assignedTasksCache.addAndSet(children), taskDataCallback));
                    }
                    break;
                default:
                    log.error("get children failed", KeeperException.create(get(rc), path));
            }
        }
    };

    private final DataCallback taskDataCallback = new DataCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
            switch (get(rc)) {
                case CONNECTIONLOSS:
                    zk.getData(path, false, taskDataCallback, ctx);
                    break;
                case OK:
                    if (data != null) {
                        executor.execute(new Runnable() {
                            private byte[] data;
                            private Object ctx;

                            private Runnable init(byte[] data, Object ctx) {
                                this.data = data;
                                this.ctx = ctx;
                                return this;
                            }

                            @Override
                            public void run() {
                                log.info("Executing your task: " + new String(data));
                                zk.create("/status/" + ctx, "done".getBytes(), OPEN_ACL_UNSAFE, PERSISTENT, statusCreateCallback, null);
                                zk.delete("/assign/worker-" + serverId + "/" + ctx, -1, taskDeleteCallback, null);
                            }
                        }.init(data, ctx));
                    }
                    break;
                default:
                    log.error("get task data failed", KeeperException.create(get(rc), path));
            }
        }
    };

    private final StringCallback statusCreateCallback = new StringCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            switch (get(rc)) {
                case CONNECTIONLOSS:
                    zk.create(path, "done".getBytes(), OPEN_ACL_UNSAFE, PERSISTENT, statusCreateCallback, ctx);
                    break;
                case OK:
                    log.info("Created status znode correctly: " + name);
                    break;
                case NODEEXISTS:
                    log.warn("Node exists: " + path);
                    break;
                default:
                    log.error("Failed to create task status", KeeperException.create(get(rc), path));
            }
        }
    };

    private final VoidCallback taskDeleteCallback = new VoidCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx) {
            switch (get(rc)) {
                case CONNECTIONLOSS:
                    zk.delete("/assign/worker-" + serverId + "/" + ctx, -1, taskDeleteCallback, null);
                    break;
                case OK:
                    log.info("Task correctly deleted: " + path);
                    break;
                default:
                    log.error("Failed to delete task" + KeeperException.create(Code.get(rc), path));
            }
        }
    };

    @Override
    public void close() {
        log.info("Closing");
        try {
            zk.close();
        } catch (InterruptedException e) {
            log.warn("ZooKeeper interrupted while closing");
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Worker worker = new Worker(args[0]);
        worker.startZK();

        while (!worker.isConnected()) {
            Thread.sleep(100);
        }

        worker.bootstrap();

        worker.register();

        worker.getTasks();

        while (!worker.isExpired()) {
            Thread.sleep(1000);
        }
    }

}
