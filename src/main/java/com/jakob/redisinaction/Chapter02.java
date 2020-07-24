package com.jakob.redisinaction;

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Chapter02 {
    public static void main(String[] args) throws InterruptedException {
        new Chapter02().run();
    }

    public void run() throws InterruptedException {
        Jedis conn = new Jedis("localhost");
        conn.select(14);

        testLoginCookies(conn);
        testShoppingCartCookies(conn);
        testCacheRows(conn);
        testCacheRequest(conn);
    }

    public void testLoginCookies(Jedis conn) throws InterruptedException {
        System.out.println("\n-----test login cookies-----");
        String token = UUID.randomUUID().toString();

        updateToken(conn, token, "username", "itemX");
        System.out.println("We just login/update token: " + token);
        System.out.println("For user: 'username'");
        System.out.println();

        String user = checkToken(conn, token);
        System.out.println("Check token, we get user: " + user);
        System.out.println();

        assert user != null;

        System.out.println("Clean cookies");
        CleanSessionsThread thread = new CleanSessionsThread(0);
        thread.start();
        Thread.sleep(1000);
        thread.quit();
        Thread.sleep(2000);
        if (thread.isAlive()) {
            throw new RuntimeException("The clean sessions thread is still alive!");
        }

        Long len = conn.hlen("login:");
        System.out.println("The current number of alive sessions is " + len);
        assert len == 0;
    }

    public void testShoppingCartCookies(Jedis conn) throws InterruptedException {
        System.out.println("\n-----test shopping cart cookies-----");
        String token = UUID.randomUUID().toString();

        System.out.println("Refresh our session");
        updateToken(conn, token, "username", "itemX");
        System.out.println("And add a item to the shopping cart");
        addToCart(conn, token, "itemY", 3);
        System.out.println("Our shopping cart currently has: ");
        Map<String, String> cart = conn.hgetAll("cart:" + token);
        cart.forEach((k, v) -> System.out.println("  " + k + ": " + v));

        System.out.println("Now we clean the session and cart");
        CleanFullSessionsThread thread = new CleanFullSessionsThread(0);
        thread.start();
        Thread.sleep(1000);
        thread.quit();
        Thread.sleep(2000);
        if (thread.isAlive()) {
            throw new RuntimeException("The clean full sessions thread is still alive!");
        }

        cart = conn.hgetAll("cart:" + token);
        System.out.println("Our shopping cart currently has: ");
        cart.forEach((k, v) -> System.out.println("  " + k + ": " + v));
        assert cart.size() == 0;
    }

    public void testCacheRows(Jedis conn) throws InterruptedException {
        System.out.println("\n-----test cache rows-----");
        System.out.println("First let's schedule itemX every 5 seconds");
        scheduleRowCache(conn, "itemX", 5);
        Set<Tuple> tuples = conn.zrangeWithScores("schedule:", 0, -1);
        System.out.println("Now the schedule looks like");
        tuples.forEach(t -> System.out.println("  " + t.getElement() + ": " + t.getScore()));
        assert tuples.size() != 0;

        System.out.println("We'll start a caching thread that will cache the data...");
        CacheRowsThread thread = new CacheRowsThread();
        thread.start();

        Thread.sleep(1000);
        System.out.println("Now the cached rows look like");
        String row1 = conn.get("inv:itemX");
        System.out.println(row1);
        System.out.println();
        assert row1 != null;

        System.out.println("We will check again in 5 seconds");
        System.out.println("Notice that the data has changed");
        Thread.sleep(5000);
        String row2 = conn.get("inv:itemX");
        System.out.println(row2);
        System.out.println();
        assert row2 != null;
        assert !row1.equals(row2);

        System.out.println("Let's force un-caching");
        scheduleRowCache(conn, "itemX", -1);
        Thread.sleep(1000);
        System.out.println("Now let's see if the cache still exists");
        String row3 = conn.get("inv:itemX");
        System.out.println("The cache was cleared? " + (row3 == null));
        assert row3 == null;

        thread.quit();
        Thread.sleep(2000);
        if (thread.isAlive()) {
            throw new RuntimeException("The clean full sessions thread is still alive!");
        }
    }

    public void testCacheRequest(Jedis conn) {
        System.out.println("\n----- testCacheRequest -----");
        String token = UUID.randomUUID().toString();

        Callback callback = request -> "content for: " + request;

        updateToken(conn, token, "username", "itemX");
        String request = "http://test.com/?item=itemX";
        String result = cacheRequest(conn, request, callback);
        System.out.println("Initial result:\n" + result);
        System.out.println();
        assert result != null;

        System.out.println("To test that we've cached the request, we'll pass a bad callback");
        String result2 = cacheRequest(conn, request, null);
        System.out.println("We ended up getting the same response!\n" + result2);
        assert result.equals(result2);

        assert !canCache(conn, "http://test.com");
        assert !canCache(conn, "http://test.com?item=itemX&_=123456");
    }

    public String checkToken(Jedis conn, String token) {
        return conn.hget("login:", token);
    }

    public void updateToken(Jedis conn, String token, String user, String item) {
        long timestamp = now();
        conn.hset("login:", token, user);
        conn.zadd("recent:", timestamp, token);

        if (item != null) {
            conn.lrem("viewed:" + token, 0, item);
            conn.rpush("viewed:" + token, item);
            conn.ltrim("viewed:" + token, -25, -1);
            conn.zincrby("viewed:", -1, item);
        }
    }

    public void addToCart(Jedis conn, String token, String item, int count) {
        if (count <= 0) {
            conn.hdel("cart:" + token, item);
        } else {
            conn.hset("cart:" + token, item, String.valueOf(count));
        }
    }

    public void scheduleRowCache(Jedis conn, String rowId, int delay) {
        conn.zadd("delay:", delay, rowId);
        conn.zadd("schedule:", now(), rowId);
    }

    public String cacheRequest(Jedis conn, String request, Callback callback) {
        if (!canCache(conn, request)) {
            return callback == null ? null : callback.call(request);
        }
        String key = "request:" + hashCode(request);
        String content = conn.get(key);
        if (content == null && callback != null) {
            content = callback.call(request);
            conn.setex(key, 300, content);
        }
        return content;
    }

    private String hashCode(String request) {
        return String.valueOf(request.hashCode());
    }

    private boolean canCache(Jedis conn, String request) {
        try {
            URL url = new URL(request);
            HashMap<String, String> params = new HashMap<>();
            if (url.getQuery() != null) {
                for (String query : url.getQuery().split("&")) {
                    String[] pair = query.split("=", 2);
                    params.put(pair[0], pair.length == 2 ? pair[1] : null);
                }
            }

            String itemId = extractItemId(params);
            if (itemId == null || isDynamic(params)) {
                return false;
            }
            Long rank = conn.zrank("viewed:", itemId);
            return rank != null && rank < 10000;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    private String extractItemId(Map<String, String> params) {
        return params.get("item");
    }

    private boolean isDynamic(Map<String, String> params) {
        return params.containsKey("_");
    }

    private long now() {
        return System.currentTimeMillis() / 1000;
    }

    public class CleanSessionsThread extends Thread {
        private Jedis conn;
        private int limit;
        private boolean quit;

        public CleanSessionsThread(int limit) {
            this.conn = new Jedis("localhost");
            this.conn.select(14);
            this.quit = false;
            this.limit = limit;
        }

        public void quit() {
            quit = true;
        }

        @Override
        public void run() {
            while (!quit) {
                Long size = conn.zcard("recent:");
                if (size <= limit) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    continue;
                }
                long endIndex = Math.min(size - limit, 100);
                Set<String> tokenSet = conn.zrange("recent:", 0, endIndex - 1);
                String[] sessionKeys = tokenSet.stream().map(e -> "viewed:" + e).toArray(String[]::new);
                String[] tokens = tokenSet.toArray(new String[0]);
                conn.del(sessionKeys);
                conn.hdel("login:", tokens);
                conn.zrem("recent:", tokens);
            }
        }
    }

    public class CleanFullSessionsThread extends Thread {
        private Jedis conn;
        private int limit;
        private boolean quit;

        public CleanFullSessionsThread(int limit) {
            this.conn = new Jedis("localhost");
            this.conn.select(14);
            this.quit = false;
            this.limit = limit;
        }

        public void quit() {
            quit = true;
        }

        @Override
        public void run() {
            while (!quit) {
                Long size = conn.zcard("recent:");
                if (size <= limit) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    continue;
                }
                long endIndex = Math.min(size - limit, 100);
                Set<String> tokenSet = conn.zrange("recent:", 0, endIndex - 1);
                List<String> sessionKeys = new ArrayList<>();
                tokenSet.forEach(e -> {
                    sessionKeys.add("viewed:" + e);
                    sessionKeys.add("cart:" + e);
                });
                String[] tokens = tokenSet.toArray(new String[0]);
                conn.del(sessionKeys.toArray(new String[0]));
                conn.hdel("login:", tokens);
                conn.zrem("recent:", tokens);
            }
        }
    }

    public class CacheRowsThread extends Thread {
        private Jedis conn;
        private boolean quit;

        public CacheRowsThread() {
            this.conn = new Jedis("localhost");
            this.conn.select(14);
        }

        public void quit() {
            quit = true;
        }

        @Override
        public void run() {
            Gson gson = new Gson();
            while (!quit) {
                Set<Tuple> range = conn.zrangeWithScores("schedule:", 0, 0);
                Tuple next = range.size() > 0 ? range.iterator().next() : null;
                long now = now();
                if (next == null || next.getScore() < now) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    continue;
                }

                String rowId = next.getElement();
                Double delay = conn.zscore("delay:", rowId);
                if (delay <= 0) {
                    conn.zrem("delay:", rowId);
                    conn.zrem("schedule:", rowId);
                    conn.del("inv:" + rowId);
                    continue;
                }

                Inventory row = Inventory.get(rowId);
                conn.zadd("schedule:", now + delay, rowId);
                conn.set("inv:" + rowId, gson.toJson(row));
            }
        }
    }

    public interface Callback {
        String call(String request);
    }

    public static class Inventory {
        private String id;
        private String data;
        private long time;

        private Inventory(String id) {
            this.id = id;
            this.data = "data to cache...";
            this.time = System.currentTimeMillis() / 1000;
        }

        public static Inventory get(String id) {
            return new Inventory(id);
        }
    }
}
