package com.jakob.concurrency.art.chapter05;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Cache {
    static Map<String, Object> map = new HashMap<>();
    static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    static Lock r = lock.readLock();
    static Lock w = lock.writeLock();

    public static Object get(String key) {
        r.lock();
        try {
            return map.get(key);
        } finally {
            r.unlock();
        }
    }

    public static Object set(String key, Object value) {
        w.lock();
        try {
            return map.put(key, value);
        } finally {
            w.unlock();
        }
    }

    public static void clear() {
        w.lock();
        try {
            map.clear();
        } finally {
            w.unlock();
        }
    }

    private static volatile boolean update;

    public void processData() {
        r.lock();
        if (!update) {
            r.unlock();
            w.lock();
            try {
                if (!update) {
                    System.out.println("Preparing data");
                    update = true;
                }
                r.lock();
            } finally {
                w.unlock();
            }
        }
        try {
            System.out.println("Processing data");
        } finally {
            r.unlock();
        }
    }


    public static void main(String[] args) {
        get("test");
    }
}
