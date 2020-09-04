package com.jakob.concurrency.art.chapter05;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedQueue<T> {
    private Object[] items;
    private int addIndex, removeIndex, count;

    private Lock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();

    public BoundedQueue(int length) {
        this.items = new Object[length];
    }

    public void addItem(T item) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length) {
                notFull.await();
            }
            items[addIndex] = item;
            System.out.println("Add " + item);
            if (++addIndex == items.length) {
                addIndex = 0;
            }
            count++;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public T getItem() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                notEmpty.await();
            }
            Object value = items[removeIndex];
            System.out.println("Get " + value);
            if (++removeIndex == items.length) {
                removeIndex = 0;
            }
            count--;
            notFull.signal();
            return (T) value;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BoundedQueue<Integer> queue = new BoundedQueue<>(10);
        new Thread(() -> {
            try {
                for (int i = 0; i < 50; i++) {
                    queue.addItem(i);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                for (int i = 0; i < 50; i++) {
                    queue.getItem();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(2000);
    }
}
