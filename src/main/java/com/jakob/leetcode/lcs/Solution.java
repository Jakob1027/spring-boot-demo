package com.jakob.leetcode.lcs;

public class Solution {

    public static void main(String[] args) {
        String s1 = "ABCD";
        String s2 = "DEF";
        System.out.println(lcs(s1, s2));
    }

    public static int lcs(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();

        int[][] dp = new int[2][n + 1];
        for (int i = 1; i <= m; i++) {
            int cur = i % 2;
            int pre = (i - 1) % 2;
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[cur][j] = 1 + dp[pre][j - 1];
                } else {
                    dp[cur][j] = Math.max(dp[cur][j - 1], dp[pre][j]);
                }
            }
        }

        return dp[m % 2][n];
    }
}