package com.jakob.springbootdemo.concurrency;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class FairAndUnfairTest {
    @Test
    public void fair() {
        ReentrantLock2 fairLock = new ReentrantLock2(true);
        testLock(fairLock);
    }

    @Test
    public void unfair() {
        ReentrantLock2 unFairLock = new ReentrantLock2(false);
        testLock(unFairLock);
    }

    private void testLock(ReentrantLock2 lock) {
        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Job job = new Job(lock);
            list.add(job);
        }
        list.forEach(Thread::start);
        for (Thread thread : list) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Job extends Thread {

        private final ReentrantLock2 lock;
        private final Condition condition;

        public Job(ReentrantLock2 lock) {
            this.lock = lock;
            this.condition = lock.newCondition();
        }

        @Override
        public void run() {
            for (int i = 0; i < 2; i++) {
                lock.lock();
                try {
                    Thread.sleep(1000);
                    System.out.print("lock by " + Thread.currentThread().getName());
                    System.out.println(", waiting by " + lock.getQueuedThreads());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }

        }
    }

    private static class ReentrantLock2 extends ReentrantLock {
        public ReentrantLock2(boolean fair) {
            super(fair);
        }

        public Collection<Thread> getQueuedThreads() {
            List<Thread> list = new ArrayList<>(super.getQueuedThreads());
            Collections.reverse(list);
            return list;
        }
    }
}
