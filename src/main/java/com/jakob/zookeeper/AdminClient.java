package com.jakob.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.sql.Timestamp;

public class AdminClient implements Watcher {

    private String endpoint;
    private ZooKeeper zk;

    public AdminClient(String endpoint) {
        this.endpoint = endpoint;
    }

    public void start() throws IOException {
        zk = new ZooKeeper(endpoint, 15000, this);
    }

    public void listState() throws KeeperException, InterruptedException {
        try {
            Stat stat = new Stat();
            byte[] data = zk.getData("/master", false, stat);
            Timestamp time = new Timestamp(stat.getCtime());
            System.out.println("Master: " + new String(data) + " since " + time);
        } catch (KeeperException.NoNodeException e) {
            System.out.println("No master");
        }
        System.out.println("Workers: ");
        for (String w : zk.getChildren("/workers", false)) {
            byte[] data = zk.getData("/workers/" + w, false, null);
            System.out.println("\t" + w + ": " + new String(data));
        }
        System.out.println("Tasks: ");
        for (String t : zk.getChildren("/assign", false)) {
            System.out.println("\t" + t);
        }
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println(event);
    }

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        AdminClient adminClient = new AdminClient(args[0]);
        adminClient.start();
        adminClient.listState();
    }
}
