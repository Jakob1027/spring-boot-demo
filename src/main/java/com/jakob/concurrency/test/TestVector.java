package com.jakob.concurrency.test;

import java.util.Vector;

public class TestVector {

    public static Vector<Integer> vector = new Vector<>();

    public static void main(String[] args) {
//        for (int i = 0; i < 10; i++) {
//            vector.add(i);
//        }
//
//        Iterator<Integer> it = vector.iterator();
//        while (it.hasNext()) {
//            it.next();
//            it.remove();
//        }
//        System.out.println(vector.size());
        while (true) {
            for (int i = 0; i < 10; i++) {
                vector.add(i);
            }

            Thread removeThread = new Thread(() -> {
                synchronized (vector) {
                    for (int i = 0; i < vector.size(); i++) {
                        vector.remove(i);
                    }
                }

            });

            Thread printThread = new Thread(() -> {
                synchronized (vector) {
                    for (int i = 0; i < vector.size(); i++) {
                        System.out.println((vector.get(i)));
                    }
                }
            });

            removeThread.start();
            printThread.start();

            while (true) {
                if (Thread.activeCount() <= 20) break;
            }
        }
    }
}
