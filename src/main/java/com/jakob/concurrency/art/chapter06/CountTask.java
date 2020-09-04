package com.jakob.concurrency.art.chapter06;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class CountTask extends RecursiveTask<Integer> {

    private static final int THRESHOLD = 2;
    private final int start;
    private final int end;

    public CountTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int sum = 0;
        boolean canCompute = (end - start) < THRESHOLD;
        if (canCompute) {
            for (int i = start; i <= end; i++) {
                sum += i;
            }
        } else {
            int mid = (start + end) / 2;
            CountTask t1 = new CountTask(start, mid);
            CountTask t2 = new CountTask(mid + 1, end);
            t1.fork();
            t2.fork();
            Integer r1 = t1.join();
            Integer r2 = t2.join();

            sum = r1 + r2;
        }
        return sum;
    }

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask<Integer> result = pool.submit(new CountTask(1, 4));
        if (result.isCompletedAbnormally()) {
            result.getException().printStackTrace();
        }
        try {
            System.out.println(result.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
