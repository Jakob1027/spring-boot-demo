package com.jakob.concurrency.test;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import static java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class ReadWriteData {
    private String mData = "data";
    private ReentrantReadWriteLock mlock = new ReentrantReadWriteLock();
    private ReadLock rlock = mlock.readLock();
    private WriteLock wlock = mlock.writeLock();

    public String readDate() {
        rlock.lock();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String data = mData;
        System.out.println("read data : " + data);
        rlock.unlock();
        return data;
    }

    public void writeData(String data) {
        wlock.lock();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mData = data;
        System.out.println("write data: " + mData);
        wlock.unlock();
    }

    public static void main(String[] args) {
        ReadWriteData d = new ReadWriteData();
        for (int i = 0; i < 10; i++) {
            new Thread(d::readDate).start();
        }
        new Thread(()-> d.writeData("update data")).start();
    }
}
