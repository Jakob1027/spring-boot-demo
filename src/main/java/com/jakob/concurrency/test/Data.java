package com.jakob.concurrency.test;

import java.util.concurrent.locks.ReentrantLock;

public class Data {
    private String mData = "data";
    private ReentrantLock mlock = new ReentrantLock();

    public String readDate() {
        mlock.lock();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String data = mData;
        System.out.println("read data : " + data);
        mlock.unlock();
        return data;
    }

    public void writeData(String data) {
        mlock.lock();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mData = data;
        System.out.println("write data: " + mData);
        mlock.unlock();
    }

    public static void main(String[] args) {
        Data d = new Data();
        for (int i = 0; i < 10; i++) {
            new Thread(d::readDate).start();
        }
        new Thread(()->{
            d.writeData("update data");
        }).start();
    }
}
