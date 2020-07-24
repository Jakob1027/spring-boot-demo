package com.jakob.redisinaction;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ZParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Chapter01 {
    private static final int ONE_WEEK_IN_SECONDS = 7 * 86400;
    private static final int VOTE_SCORE = 432;
    private static final int ARTICLES_PER_PAGE = 25;

    public static void main(String[] args) {
        new Chapter01().run();
    }

    public void run() {
        Jedis conn = new Jedis("localhost");
        conn.select(15);

        String articleId = postArticle(conn, "username", "A title", "http://www.google.com");
        System.out.println("We posted a new article with id: " + articleId);
        System.out.println("Its HASH looks like:");
        Map<String, String> articleData = conn.hgetAll("article:" + articleId);
        for (Map.Entry<String, String> entry : articleData.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }

        System.out.println();

        articleVote(conn, "other_user", "article:" + articleId, true);
        String votes = conn.hget("article:" + articleId, "votes");
        System.out.println("We voted for the article, it now has votes: " + votes);
        assert Integer.parseInt(votes) > 1;

        articleVote(conn, "another_user", "article:" + articleId, false);
        String devotes = conn.hget("article:" + articleId, "devotes");
        System.out.println("We devoted for the article, it now has devotes: " + devotes);
        assert Integer.parseInt(devotes) > 0;

        System.out.println("The currently highest-scoring articles ar: ");
        List<Map<String, String>> articles = getArticles(conn, 1);
        printArticles(articles);
        assert articles.size() >= 1;

        addGroups(conn, articleId, new String[]{"new-group"});
        System.out.println("We added the article to a new group, other articles include:");
        List<Map<String, String>> groupArticles = getGroupArticles(conn, "new-group", 1);
        printArticles(groupArticles);
        assert groupArticles.size() > 1;
    }

    public void articleVote(Jedis conn, String user, String article, boolean up) {
        long cutoff = System.currentTimeMillis() / 1000 - ONE_WEEK_IN_SECONDS;
        if (conn.zscore("time:", article) < cutoff) {
            return;
        }
        String articleId = article.substring(article.indexOf(":") + 1);
        if (up) {
            if (conn.smove("devoted:" + articleId, "voted:" + articleId, user) == 1) {
                conn.zincrby("score:", VOTE_SCORE * 2, article);
                conn.hincrBy(article, "votes", 1);
                conn.hincrBy(article, "devotes", -1);
            } else if (conn.sadd("voted:" + articleId, user) == 1) {
                conn.zincrby("score:", VOTE_SCORE, article);
                conn.hincrBy(article, "votes", 1);
            }
        } else {
            if (conn.smove("voted:" + articleId, "devoted:" + articleId, user) == 1) {
                conn.zincrby("score:", -VOTE_SCORE * 2, article);
                conn.hincrBy(article, "votes", -1);
                conn.hincrBy(article, "devotes", 1);
            } else if (conn.sadd("devoted:" + articleId, user) == 1) {
                conn.zincrby("score:", -VOTE_SCORE, article);
                conn.hincrBy(article, "devotes", 1);
            }
        }
    }

    public String postArticle(Jedis conn, String user, String title, String link) {
        String articleId = conn.incr("article:").toString();

        String voted = "voted:" + articleId;
        conn.sadd(voted, user);
        conn.expire(voted, ONE_WEEK_IN_SECONDS);

        long now = System.currentTimeMillis() / 1000;
        String article = "article:" + articleId;
        Map<String, String> articleData = new HashMap<>();
        articleData.put("title", title);
        articleData.put("link", link);
        articleData.put("poster", user);
        articleData.put("time", String.valueOf(now));
        articleData.put("votes", "1");
        articleData.put("devotes", "0");
        conn.hmset(article, articleData);

        conn.zadd("time:", now, article);
        conn.zadd("score:", now + VOTE_SCORE, article);

        return articleId;
    }

    public List<Map<String, String>> getArticles(Jedis conn, int page) {
        return getArticles(conn, page, "score:");
    }

    public List<Map<String, String>> getArticles(Jedis conn, int page, String order) {
        int start = (page - 1) * ARTICLES_PER_PAGE;
        int end = start + ARTICLES_PER_PAGE - 1;

        Set<String> ids = conn.zrevrange(order, start, end);
        return ids.stream().map(id -> {
            Map<String, String> articleData = conn.hgetAll(id);
            articleData.put("id", id);
            return articleData;
        }).collect(Collectors.toList());
    }

    public void addGroups(Jedis conn, String articleId, String[] toAdd) {
        String article = "article:" + articleId;
        for (String group : toAdd) {
            conn.sadd("group:" + group, article);
        }
    }

    public List<Map<String, String>> getGroupArticles(Jedis conn, String group, int page) {
        return getGroupArticles(conn, group, page, "score:");
    }

    public List<Map<String, String>> getGroupArticles(Jedis conn, String group, int page, String order) {
        String key = order + group;
        if (!conn.exists(key)) {
            ZParams zParams = new ZParams().aggregate(ZParams.Aggregate.MAX);
            conn.zinterstore(key, zParams, order, "group:" + group);
            conn.expire(key, 60);
        }
        return getArticles(conn, page, key);
    }

    private void printArticles(List<Map<String, String>> articles) {
        for (Map<String, String> article : articles) {
            System.out.println("  id: " + article.get("id"));
            for (Map.Entry<String, String> entry : article.entrySet()) {
                if ("id".equals(entry.getKey())) continue;
                System.out.println("    " + entry.getKey() + ": " + entry.getValue());
            }
        }
    }


}
