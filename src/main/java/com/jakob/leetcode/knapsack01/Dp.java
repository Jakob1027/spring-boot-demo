package com.jakob.leetcode.knapsack01;

/**
 * 动态规划
 */
public class Dp {

    public static void main(String[] args) {
        int[] w = {1, 2, 3};
        int[] v = {6, 10, 12};
        int c = 5;
        int res = new Dp().knapsack01(w, v, c);
        System.out.println(res);
    }

    private int knapsack01(int[] w, int[] v, int c) {
        assert w.length == v.length;
        int n = w.length;
        int[] dp = new int[c + 1];
        for (int i = 0; i <= c; i++) {
            dp[i] = i >= w[0] ? v[0] : 0;
        }
        for (int i = 1; i < n; i++) {
            for (int j = c; j >= w[i]; j--) {
                dp[j] = Math.max(dp[j], dp[j - w[i]] + v[i]);
            }
        }
        return dp[c];
    }
}
