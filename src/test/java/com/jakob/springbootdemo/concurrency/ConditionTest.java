package com.jakob.springbootdemo.concurrency;

import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionTest {

    ReentrantLock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    @Test
    public void test() throws InterruptedException {
        new Thread(this::conditionWait).start();
        Thread.sleep(1000);
        new Thread(this::conditionSignal).start();
        Thread.sleep(5000);
    }

    private void conditionWait() {
        lock.lock();
        try {
            Thread.sleep(1000);
            System.out.println("wait 1s");
            condition.await();
            System.out.println("actions after await");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private void conditionSignal() {
        lock.lock();
        try {
            Thread.sleep(2000);
            System.out.println("wait 2s");
            condition.signal();
            System.out.println("actions after signal");
            Thread.sleep(1000);
            System.out.println("wait another 1s");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
