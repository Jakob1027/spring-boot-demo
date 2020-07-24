package com.jakob.leetcode.leetcode_435;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Given a collection of intervals, find the minimum number of intervals you need to remove to make the rest of the intervals non-overlapping.
 */
public class Solution {
    public int eraseOverlapIntervals(int[][] intervals) {
        //==========================================================
        //  贪心算法
        //==========================================================
        if (intervals.length == 0) return 0;
        Arrays.sort(intervals, Comparator.comparingInt(i -> i[1]));
        int res = 1;
        int pre = 0;
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] >= intervals[pre][1]) {
                res++;
                pre = i;
            }
        }
        return intervals.length - res;


        //==========================================================
        //  动态规划，类似LIS问题
        //==========================================================
        /*if (intervals.length == 0) return 0;
        Arrays.sort(intervals, Comparator.comparingInt(i -> i[0]));
        int n = intervals.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (intervals[i][0] >= intervals[j][1])
                    dp[i] = Math.max(dp[i], dp[j] + 1);
            }
        }
        int res = 1;
        for (int i = 0; i < n; i++) {
            res = Math.max(res, dp[i]);
        }
        return intervals.length - res;*/
    }
}
