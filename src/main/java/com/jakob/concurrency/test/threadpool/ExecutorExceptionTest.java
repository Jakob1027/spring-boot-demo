package com.jakob.concurrency.test.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorExceptionTest {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            executor.submit(() -> {
                System.out.println("current thread: " + Thread.currentThread().getName());
                try {
                    Object obj = null;
                    System.out.println("result##" + obj.toString());
                } catch (Exception e) {
                    System.out.println("程序异常");
                }
            });
        }
    }
}
