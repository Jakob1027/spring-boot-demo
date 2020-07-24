package com.jakob.redisinaction;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class Chapter03 {
    public static void main(String[] args) throws InterruptedException {
        Jedis conn = new Jedis("localhost");
        conn.select(13);

        new Chapter03().pubsub(conn, 3);
        conn.close();
    }

    public void publisher(Jedis conn, int n) throws InterruptedException {
        Thread.sleep(1);
        for (int i = 0; i < n; i++) {
            conn.publish("channel", String.valueOf(i));
            Thread.sleep(1);
        }
    }

    public void pubsub(Jedis conn, int n) throws InterruptedException {
        new Thread(() -> {
            try {
                Jedis jedis = new Jedis("localhost");
                Thread.sleep(1);
                for (int i = 0; i < n; i++) {
                    jedis.publish("channel", String.valueOf(i));
                    Thread.sleep(1);
                }
                jedis.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        PubsubListener pubSub = new PubsubListener();
        conn.subscribe(pubSub, "channel");
        Thread.sleep(3000);
        pubSub.unsubscribe();
    }

    private static class PubsubListener extends JedisPubSub {
        private int count = 0;

        @Override
        public void onMessage(String channel, String message) {
            System.out.println("channel " + channel + " number " + count++ + ": " + message);
        }
    }
}
