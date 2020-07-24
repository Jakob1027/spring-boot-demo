package com.jakob.leetcode.leetcode_51;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The n-queens puzzle is the problem of placing n queens on an n×n chessboard such that no two queens attack each other.
 * Given an integer n, return all distinct solutions to the n-queens puzzle.
 * <p>
 * Each solution contains a distinct board configuration of the n-queens' placement, where 'Q' and '.' both indicate a queen and an empty space respectively.
 * <p>
 * 分别考察每一行的每个位置能否放置皇后
 */
public class Solution {

    private List<List<String>> res = new ArrayList<>();
    private boolean[] col, dia1, dia2;

    public List<List<String>> solveNQueens(int n) {
        if (n == 0) {
            res.add(Collections.emptyList());
            return res;
        }
        col = new boolean[n];
        dia1 = new boolean[2 * n - 1];
        dia2 = new boolean[2 * n - 1];
        backtrack(n, 0, new ArrayList<>());
        return res;
    }

    private void backtrack(int n, int row, List<Integer> records) {
        if (row == n) {
            res.add(generateString(records));
            return;
        }
        for (int i = 0; i < n; i++) {
            if (!col[i] && !dia1[row + i] && !dia2[row - i + n - 1]) {
                col[i] = true;
                dia1[row + i] = true;
                dia2[row - i + n - 1] = true;
                records.add(i);
                backtrack(n, row + 1, records);
                records.remove(records.size() - 1);
                col[i] = false;
                dia1[row + i] = false;
                dia2[row - i + n - 1] = false;
            }
        }
    }

    private List<String> generateString(List<Integer> records) {
        List<String> res = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < records.size(); j++) {
                if (j != records.get(i)) sb.append(".");
                else sb.append("Q");
            }
            res.add(sb.toString());
        }
        return res;
    }

    public static void main(String[] args) {
        List<List<String>> res = new Solution().solveNQueens(10);
        System.out.println(res);
    }
}
