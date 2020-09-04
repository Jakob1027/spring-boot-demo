package com.jakob.concurrency.art.chapter05;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ABC_Condition {
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition c1 = lock.newCondition();
    private static Condition c2 = lock.newCondition();
    private static Condition c3 = lock.newCondition();
    private static int count = 0;

    private static class T1 extends Thread {
        @Override
        public void run() {
            lock.lock();
            try {
                for (int i = 0; i < 10; i++) {
                    while (count % 3 != 0) {
                        c1.await();
                    }
                    System.out.print("A");
                    count++;
                    c2.signal();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    private static class T2 extends Thread {
        @Override
        public void run() {
            lock.lock();
            try {
                for (int i = 0; i < 10; i++) {
                    while (count % 3 != 1) {
                        c2.await();
                    }
                    System.out.print("B");
                    count++;
                    c3.signal();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    private static class T3 extends Thread {
        @Override
        public void run() {
            lock.lock();
            try {
                for (int i = 0; i < 10; i++) {
                    while (count % 3 != 2) {
                        c3.await();
                    }
                    System.out.print("C");
                    count++;
                    c1.signal();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new T1().start();
        new T2().start();
        new T3().start();
        Thread.sleep(1000);
    }
}
