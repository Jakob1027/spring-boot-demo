package com.jakob.concurrency.art.chapter05;

import java.util.concurrent.Semaphore;

public class ABC_Semaphore {
    private static Semaphore s1 = new Semaphore(1);
    private static Semaphore s2 = new Semaphore(0);
    private static Semaphore s3 = new Semaphore(0);

    private static class PrintThread extends Thread {

        private Semaphore s1;
        private Semaphore s2;
        private String c;

        public PrintThread(Semaphore s1, Semaphore s2, String c) {
            this.s1 = s1;
            this.s2 = s2;
            this.c = c;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    s1.acquire();
                    System.out.print(c);
                    s2.release();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new PrintThread(s1, s2, "A").start();
        new PrintThread(s2, s3, "B").start();
        new PrintThread(s3, s1, "C").start();
        Thread.sleep(1000);
    }
}
