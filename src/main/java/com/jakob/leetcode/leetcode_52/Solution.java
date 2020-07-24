package com.jakob.leetcode.leetcode_52;

/**
 * Given an integer n, return the number of distinct solutions to the n-queens puzzle.
 */
public class Solution {

    private boolean[] col, dia1, dia2;
    private int res;

    public int totalNQueens(int n) {
        if (n == 0) return 0;
        col = new boolean[n];
        dia1 = new boolean[2 * n - 1];
        dia2 = new boolean[2 * n - 1];
        backtrack(n, 0);
        return res;
    }

    private void backtrack(int n, int row) {
        if (row == n) {
            res++;
            return;
        }
        for (int i = 0; i < n; i++) {
            if (!col[i] && !dia1[row + i] && !dia2[row - i + n - 1]) {
                col[i] = true;
                dia1[row + i] = true;
                dia2[row - i + n - 1] = true;
                backtrack(n, row + 1);
                col[i] = false;
                dia1[row + i] = false;
                dia2[row - i + n - 1] = false;
            }
        }
    }

    public static void main(String[] args) {
        int res = new Solution().totalNQueens(10);
        System.out.println(res);
    }
}
