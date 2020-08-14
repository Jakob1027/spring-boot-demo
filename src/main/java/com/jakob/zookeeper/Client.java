package com.jakob.zookeeper;

import org.apache.zookeeper.*;

import java.io.IOException;

import static org.apache.zookeeper.KeeperException.*;
import static org.apache.zookeeper.ZooDefs.Ids.*;

public class Client implements Watcher {

    private ZooKeeper zk;
    private final String endpoint;

    public Client(String endpoint) {
        this.endpoint = endpoint;
    }

    public void startZK() throws IOException {
        zk = new ZooKeeper(endpoint, 15000, this);
    }

    public String queueCommand(String command) throws Exception {
        while (true) {
            String name = "";
            try {
                name = zk.create("/tasks/task-", command.getBytes(), OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
                return name;
            } catch (NodeExistsException e) {
                throw new Exception(name + " already appears to be running");
            } catch (ConnectionLossException ignored) {
            }
        }
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println(event);
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client(args[0]);
        client.startZK();
        String name = client.queueCommand(args[1]);
        System.out.println("Created " + name);
    }
}
