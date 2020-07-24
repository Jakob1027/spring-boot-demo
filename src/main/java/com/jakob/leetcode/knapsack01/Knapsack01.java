package com.jakob.leetcode.knapsack01;

public class Knapsack01 {

    // private static int[][] memo;

    public static void main(String[] args) {
        int[] w = { 1, 2, 3 };
        int[] v = { 6, 10, 12 };
        int c = 5;
        int maxValue = knapsack01(w, v, c);
        System.out.println(maxValue);
    }

    private static int knapsack01(int[] w, int[] v, int c) {
        int n = w.length;
        if (n == 0)
            return 0;
        int[] memo = new int[c + 1];
        for (int i = 0; i <= c; i++) {
            memo[i] = w[0] <= i ? v[0] : 0;
        }
        for (int i = 1; i < n; i++) {
            for (int j = c; j >= w[i]; j--) {
                memo[j] = Math.max(memo[j - w[i]] + v[i], memo[j]);
            }
        }
        return memo[c];
    }

    // private static int bestValue(int[] w, int[] v, int index, int c) {
    // if (index == 0) {
    // return w[0] <= c ? v[0] : 0;
    // }

    // if (memo[index][c] != -1)
    // return memo[index][c];

    // int bestValue = bestValue(w, v, index - 1, c);
    // if (w[index] <= c) {
    // bestValue = Math.max(bestValue, bestValue(w, v, index - 1, c - w[index]) +
    // v[index]);
    // }
    // memo[index][c] = bestValue;
    // return bestValue;
    // }
}