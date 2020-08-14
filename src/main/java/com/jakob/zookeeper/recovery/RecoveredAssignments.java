package com.jakob.zookeeper.recovery;

import org.apache.zookeeper.ZooKeeper;

public class RecoveredAssignments {
    private final ZooKeeper zk;

    public RecoveredAssignments(ZooKeeper zk) {
        this.zk = zk;
    }
}
