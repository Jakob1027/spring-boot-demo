package com.jakob.leetcode.leetcode_34;

/**
 * Given an array of integers nums sorted in ascending order, find the starting and ending position of a given target value.
 * <p>
 * Your algorithm's runtime complexity must be in the order of O(log n).
 * <p>
 * If the target is not found in the array, return [-1, -1].
 * <p>
 * Example 1:
 * <p>
 * Input: nums = [5,7,7,8,8,10], target = 8
 * Output: [3,4]
 * Example 2:
 * <p>
 * Input: nums = [5,7,7,8,8,10], target = 6
 * Output: [-1,-1]
 */
public class Solution {
    public int[] searchRange(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1;
        int mid = lo;
        while (lo <= hi) {
            mid = (lo + hi) / 2;
            if (target == nums[mid]) break;
            else if (target > nums[mid]) lo = mid + 1;
            else hi = mid - 1;
        }
        if (lo > hi) return new int[]{-1, -1};
        lo = mid;
        hi = mid;
        while (lo >= 0 && nums[lo] == target) lo--;
        while (hi < nums.length && nums[hi] == target) hi++;
        return new int[]{lo + 1, hi - 1};
    }
}
