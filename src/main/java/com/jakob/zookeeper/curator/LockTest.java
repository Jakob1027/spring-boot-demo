package com.jakob.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;

public class LockTest {
    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(args[0], new ExponentialBackoffRetry(1000, 5));
        client.start();
        InterProcessMutex lock = new InterProcessMutex(client, args[1]);
        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add(new Thread(() -> {
                try {
                    lock.acquire();
                    System.out.println(Thread.currentThread().getName() + " gets the lock!");
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        lock.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, "Thread-" + i));
        }
        list.forEach(Thread::start);
        for (Thread thread : list) {
            thread.join();
        }
        client.close();
    }
}
