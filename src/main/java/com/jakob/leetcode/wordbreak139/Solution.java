package com.jakob.leetcode.wordbreak139;

import java.util.Arrays;
import java.util.List;

public class Solution {
    public static void main(String[] args) {
        System.out.println(wordBreak("", Arrays.asList("a", "p")));
    }

    public static boolean wordBreak(String s, List<String> wordDict) {
        int n = s.length();
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;
        for (int i = 1; i <= n; i++) {
            String sub = s.substring(0, i);
            for (String w : wordDict) {
                if (sub.endsWith(w) && dp[i - w.length()]) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[n];
    }
}