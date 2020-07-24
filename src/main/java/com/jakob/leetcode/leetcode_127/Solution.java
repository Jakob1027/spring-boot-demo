package com.jakob.leetcode.leetcode_127;

import javafx.util.Pair;

import java.util.*;

/**
 * Given two words (beginWord and endWord), and a dictionary's word list, find the length of shortest transformation sequence from beginWord to endWord, such that:
 * <p>
 * Only one letter can be changed at a time.
 * Each transformed word must exist in the word list. Note that beginWord is not a transformed word.
 * Note:
 * <p>
 * Return 0 if there is no such transformation sequence.
 * All words have the same length.
 * All words contain only lowercase alphabetic characters.
 * You may assume no duplicates in the word list.
 * You may assume beginWord and endWord are non-empty and are not the same.
 * Example 1:
 * <p>
 * Input:
 * beginWord = "hit",
 * endWord = "cog",
 * wordList = ["hot","dot","dog","lot","log","cog"]
 * <p>
 * Output: 5
 * <p>
 * Explanation: As one shortest transformation is "hit" -> "hot" -> "dot" -> "dog" -> "cog",
 * return its length 5.
 * Example 2:
 * <p>
 * Input:
 * beginWord = "hit"
 * endWord = "cog"
 * wordList = ["hot","dot","dog","lot","log"]
 * <p>
 * Output: 0
 * <p>
 * Explanation: The endWord "cog" is not in wordList, therefore no possible transformation.
 */
public class Solution {
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        int l = beginWord.length();
        Map<String, List<String>> adj = new HashMap<>();
        wordList.forEach(word -> {
            for (int i = 0; i < l; i++) {
                String pattern = word.substring(0, i) + "*" + word.substring(i + 1, l);
                List<String> words = adj.getOrDefault(pattern, new ArrayList<>());
                words.add(word);
                adj.put(pattern, words);
            }
        });


        Queue<Pair<String, Integer>> queue = new LinkedList<>();
        queue.offer(new Pair<>(beginWord, 1));
        Map<String, Boolean> visited = new HashMap<>();
        while (!queue.isEmpty()) {
            Pair<String, Integer> cur = queue.poll();
            String word = cur.getKey();
            int step = cur.getValue();

            for (int i = 0; i < l; i++) {
                String pattern = word.substring(0, i) + "*" + word.substring(i + 1, l);
                for (String s : adj.getOrDefault(pattern, new ArrayList<>())) {
                    if (s.equals(endWord)) return step + 1;

                    if (!visited.getOrDefault(s, false)) {
                        visited.put(s, true);
                        queue.offer(new Pair<>(s, step + 1));
                    }
                }
            }
        }
        return 0;
    }

    private int diff(String a, String b) {
        int count = 0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) count++;
        }
        return count;
    }
}