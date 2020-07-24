package com.jakob.springbootdemo.metrics;

import com.codahale.metrics.*;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.codahale.metrics.MetricRegistry.name;


public class QueueManager {
    private final Queue<String> queue;
    private final Counter pendingJobs;
    private final Histogram queueSize;
    private final Timer timer;


    public QueueManager(MetricRegistry metrics, String name) {
        queue = new LinkedBlockingQueue();
        metrics.register(name(QueueManager.class, name, "size"), (Gauge<Integer>) queue::size);
        pendingJobs = metrics.counter(name(QueueManager.class, "pending-jobs"));
        queueSize = metrics.histogram(name(QueueManager.class, "queue-size"));
        timer = metrics.timer(name(QueueManager.class, "timer"));
    }

    public void addJob(String job) {
        pendingJobs.inc();
        queueSize.update(queue.size());
        queue.add(job);
    }

    public String takeJob() {
        pendingJobs.dec();
        queueSize.update(queue.size());
        return queue.poll();
    }

    public void time() {
        Timer.Context context = timer.time();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        } finally {
            context.stop();
        }
    }

}
