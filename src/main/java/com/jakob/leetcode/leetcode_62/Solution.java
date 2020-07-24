package com.jakob.leetcode.leetcode_62;

/**
 * A robot is located at the top-left corner of a m x n grid (marked 'Start' in the diagram below).
 * <p>
 * The robot can only move either down or right at any point in time. The robot is trying to reach the bottom-right corner of the grid (marked 'Finish' in the diagram below).
 * <p>
 * How many possible unique paths are there?
 */
public class Solution {
    public int uniquePaths(int m, int n) {
        int[] path = new int[n];
        for (int i = 0; i < n; i++) {
            path[i] = 1;
        }
        for (int i = 1; i < m; i++) {
            path[0] = 1;
            for (int j = 1; j < n; j++) {
                path[j] = path[j] + path[j - 1];
            }
        }
        return path[n - 1];
    }
}
