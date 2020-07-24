package com.jakob.leetcode.leetcode_70;

/**
 * You are climbing a stair case. It takes n steps to reach to the top.
 * <p>
 * Each time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?
 * <p>
 * Note: Given n will be a positive integer.
 */
public class Solution {
    public int climbStairs(int n) {
        if (n == 0 || n == 1) return 1;
        int first = 1;
        int second = 1;
        for (int i = 2; i <= n; i++) {
            int temp = first;
            first = second;
            second = temp + first;
        }
        return second;
    }
}
