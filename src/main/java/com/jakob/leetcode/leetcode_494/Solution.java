package com.jakob.leetcode.leetcode_494;

/**
 * You are given a list of non-negative integers, a1, a2, ..., an, and a target, S. Now you have 2 symbols + and -. For each integer, you should choose one from + and - as its new symbol.
 * <p>
 * Find out how many ways to assign symbols to make sum of integers equal to target S.
 * <p>
 * Example 1:
 * Input: nums is [1, 1, 1, 1, 1], S is 3.
 * Output: 5
 * Explanation:
 * <p>
 * -1+1+1+1+1 = 3
 * +1-1+1+1+1 = 3
 * +1+1-1+1+1 = 3
 * +1+1+1-1+1 = 3
 * +1+1+1+1-1 = 3
 * <p>
 * There are 5 ways to assign symbols to make the sum of nums be target 3.
 * Note:
 * The length of the given array is positive and will not exceed 20.
 * The sum of elements in the given array will not exceed 1000.
 * Your output answer is guaranteed to be fitted in a 32-bit integer.
 */
public class Solution {

    public int findTargetSumWays(int[] nums, int S) {
        int sum = 0;
        for (int n : nums)
            sum += n;

        return (S > sum || (S + sum) % 2 != 0) ? 0 : subsetSum(nums, (sum + S) >>> 1);
    }

    private int subsetSum(int[] nums, int S) {
        int[] dp = new int[S + 1];
        dp[0] = 1;
        for (int n : nums) {
            for (int i = S; i >= 1; i--) {
                dp[i] += dp[i - n];
            }
        }
        return dp[S];
    }

    public static void main(String[] args) {
        int[] nums = {1, 1, 1, 1, 1};
        int S = 3;
        int res = new Solution().findTargetSumWays(nums, S);
        System.out.println(res);
    }
}
