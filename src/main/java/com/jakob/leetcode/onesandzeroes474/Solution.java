package com.jakob.leetcode.onesandzeroes474;

class Solution {

    public static void main(String[] args) {
        String[] strs = { "10", "0001", "111001", "1", "0" };
        int m = 5;
        int n = 3;
        System.out.println(new Solution().findMaxForm(strs, m, n));
    }

    public int findMaxForm(String[] strs, int m, int n) {
        int[][] dp = new int[m + 1][n + 1];

        int firstZero = getZero(strs[0]);
        for (int i = 0; i <= m && i >= firstZero; i++) {
            for (int j = 0; j <= n && j >= strs[0].length() - firstZero; j++) {
                dp[i][j] = 1;
            }
        }

        for (int k = 1; k < strs.length; k++) {
            int zero = getZero(strs[k]);
            int one = strs[k].length() - zero;
            for (int i = m; i >= zero; i--) {
                for (int j = n; j >= one; j--) {
                    dp[i][j] = Math.max(dp[i - zero][j - one] + 1, dp[i][j]);
                }
            }
        }
        return dp[m][n];
    }

    private int getZero(String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '0')
                count++;
        }
        return count;
    }
}