package com.jakob.redisinaction;

import org.javatuples.Pair;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;

import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Chapter05 {

    public static final String DEBUG = "debug";
    public static final String INFO = "info";
    public static final String WARNING = "warning";
    public static final String ERROR = "error";
    public static final String CRITICAL = "critical";

    public static final Collator COLLATOR = Collator.getInstance();

    public static final SimpleDateFormat TIMESTAMP =
            new SimpleDateFormat("EEE MMM dd HH:00:00 yyyy");
    private static final SimpleDateFormat ISO_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:00:00");

    static {
        ISO_FORMAT.setTimeZone(TimeZone.getDefault());
    }

    public static void main(String[] args) {
        new Chapter05().run();
    }

    public void run() {
        Jedis conn = new Jedis("localhost");
        conn.select(15);
        testLogRecent(conn);
        testLogCommon(conn);
    }

    public void testLogRecent(Jedis conn) {
        System.out.println("\n----- testLogRecent -----");
        System.out.println("Let's write a few logs to the recent log");
        for (int i = 0; i < 5; i++) {
            logRecent(conn, "test", "this is message " + i);
        }
        List<String> msgs = conn.lrange("recent:test:info", 0, -1);
        System.out.println("The current recent message log has this many messages: " + msgs.size());
        System.out.println("Those messages include: ");
        for (String message : msgs) {
            System.out.println(message);
        }
        assert msgs.size() >= 5;
    }

    public void testLogCommon(Jedis conn) {
        System.out.println("\n----- testLogCommon -----");
        System.out.println("Let's write some items to the common log");
        for (int count = 1; count < 6; count++) {
            for (int i = 0; i < count; i++) {
                logCommon(conn, "test", "message-" + count);
            }
        }
        Set<Tuple> common = conn.zrevrangeWithScores("common:test:info", 0, -1);
        System.out.println("The current number of common messages is: " + common.size());
        System.out.println("Those common messages are:");
        for (Tuple tuple : common) {
            System.out.println("  " + tuple.getElement() + ", " + tuple.getScore());
        }
        assert common.size() >= 5;
    }

    public void logRecent(Jedis conn, String name, String message) {
        logRecent(conn, name, message, INFO);
    }

    public void logRecent(Jedis conn, String name, String message, String severity) {
        String key = "recent:" + name + ":" + severity;
        Pipeline pipeline = conn.pipelined();
        pipeline.lpush(key, TIMESTAMP.format(new Date()) + " " + message);
        pipeline.ltrim(key, 0, 99);
        pipeline.sync();
    }

    public void logCommon(Jedis conn, String name, String message) {
        logCommon(conn, name, message, INFO, 5000);
    }

    public void logCommon(Jedis conn, String name, String message, String severity, int timeout) {
        String commonDest = "common:" + name + ":" + severity;
        String startKey = commonDest + ":start";
        long end = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < end) {
            conn.watch(startKey);
            String hourStart = ISO_FORMAT.format(new Date());
            String existing = conn.get(startKey);
            Transaction trans = conn.multi();
            if (existing != null && COLLATOR.compare(existing, hourStart) < 0) {
                trans.rename(commonDest, commonDest + ":last");
                trans.rename(startKey, commonDest + ":pstart");
                trans.set(startKey, hourStart);
            }
            trans.zincrby(commonDest, 1, message);

            String recentDest = "recent:" + name + ":" + severity;
            trans.lpush(recentDest, TIMESTAMP.format(new Date()) + ' ' + message);
            trans.ltrim(recentDest, 0, 99);

            List<Object> result = trans.exec();
            if (result == null) {
                continue;
            }
            return;
        }
    }

    public static final int[] PRECISION = {1, 5, 60, 300, 3600, 18000, 86400};

    public void updateCounter(Jedis conn, String name, int count, long now) {
        Transaction tran = conn.multi();
        for (int precision : PRECISION) {
            long pnow = (now / precision) * precision;
            String key = precision + ":" + name;
            tran.zadd("known:", 0, key);
            tran.hincrBy("count:" + key, String.valueOf(pnow), count);
        }
        tran.exec();
    }

    public List<Pair<Integer, Integer>> getCounter(Jedis conn, String name, int precision) {
        String hash = precision + ":" + name;
        Map<String, String> values = conn.hgetAll("count:" + hash);
        return values.entrySet().stream()
                .map(e -> new Pair<>(Integer.parseInt(e.getKey()), Integer.parseInt(e.getValue())))
                .sorted()
                .collect(Collectors.toList());
    }


    private long lastChecked;
    private boolean underMaintenance;

    public boolean isUnderMaintenance(Jedis conn) {
        if (lastChecked < System.currentTimeMillis() - 1000) {
            lastChecked = System.currentTimeMillis();
            String flag = conn.get("is-under-maintenance");
            underMaintenance = "yes".equals(flag);
        }
        return underMaintenance;
    }


}
