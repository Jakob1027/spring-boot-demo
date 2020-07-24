package com.jakob.leetcode.leetcode_126;

import javafx.util.Pair;

import java.util.*;

/**
 * Given two words (beginWord and endWord), and a dictionary's word list, find all shortest transformation sequence(s) from beginWord to endWord, such that:
 * <p>
 * Only one letter can be changed at a time
 * Each transformed word must exist in the word list. Note that beginWord is not a transformed word.
 * Note:
 * <p>
 * Return an empty list if there is no such transformation sequence.
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
 * Output:
 * [
 * ["hit","hot","dot","dog","cog"],
 * ["hit","hot","lot","log","cog"]
 * ]
 * Example 2:
 * <p>
 * Input:
 * beginWord = "hit"
 * endWord = "cog"
 * wordList = ["hot","dot","dog","lot","log"]
 * <p>
 * Output: []
 * <p>
 * Explanation: The endWord "cog" is not in wordList, therefore no possible transformation.
 */
public class Solution {
    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
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
        Map<String, Integer> dist = new HashMap<>();
        dist.put(beginWord, 1);
        List<List<String>> res = new ArrayList<>();
        Map<String, List<String>> path = new HashMap<>();
        boolean endFound = false;
        while (!endFound && !queue.isEmpty()) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                Pair<String, Integer> cur = queue.poll();
                String word = cur.getKey();
                int step = cur.getValue();
                for (int j = 0; j < l; j++) {
                    String pattern = word.substring(0, j) + "*" + word.substring(j + 1, l);
                    for (String s : adj.getOrDefault(pattern, new ArrayList<>())) {
                        if (s.equals(endWord)) endFound = true;

                        if (dist.getOrDefault(s, step + 1) == step + 1) {
                            List<String> list = path.getOrDefault(s, new ArrayList<>());
                            list.add(word);
                            path.put(s, list);

                            if (dist.get(s) == null) {
                                dist.put(s, step + 1);
                                queue.offer(new Pair<>(s, step + 1));
                            }
                        }
                    }
                }
            }
        }
        dfs(endWord, beginWord, path, res, new ArrayList<>());
        return res;
    }

    private void dfs(String cur, String beginWord, Map<String, List<String>> path, List<List<String>> res, List<String> records) {
        records.add(0, cur);
        if (cur.equals(beginWord)) {
            res.add(new ArrayList<>(records));
        } else {
            for (String s : path.get(cur)) {
                dfs(s, beginWord, path, res, records);
            }
        }
        records.remove(0);
    }

    public static void main(String[] args) {
        List<List<String>> res = new Solution().findLadders("hit", "cog", Arrays.asList("hot", "dot", "dog", "lot", "log", "cog"));
        System.out.println(res);
    }
}
