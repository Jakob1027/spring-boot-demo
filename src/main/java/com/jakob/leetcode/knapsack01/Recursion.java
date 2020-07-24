package com.jakob.leetcode.knapsack01;

import java.util.Arrays;

/**
 * 递归 + 记忆搜索
 */
public class Recursion {

    public static void main(String[] args) {
        int[] w = {1, 2, 3};
        int[] v = {6, 10, 12};
        int c = 5;
        int res = new Recursion().knapsack01(w, v, c);
        System.out.println(res);
    }

    private int[][] memo;

    private int knapsack01(int[] w, int[] v, int c) {
        assert w.length == v.length;
        int n = w.length;
        memo = new int[n][c + 1];
        for (int[] row : memo)
            Arrays.fill(row, -1);
        return knapsack01Helper(w, v, c, n - 1);
    }

    private int knapsack01Helper(int[] w, int[] v, int c, int index) {
        if (c <= 0 || index < 0) {
            return 0;
        }

        if (memo[index][c] != -1) {
            return memo[index][c];
        }

        int res = knapsack01Helper(w, v, c, index - 1);
        if (c >= w[index]) {
            res = Math.max(res, v[index] + knapsack01Helper(w, v, c - w[index], index - 1));
        }
        memo[index][c] = res;
        return res;
    }
}
