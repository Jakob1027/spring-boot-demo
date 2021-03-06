package com.jakob.leetcode.leetcode_33;

/**
 * Suppose an array sorted in ascending order is rotated at some pivot unknown to you beforehand.
 * <p>
 * (i.e., [0,1,2,4,5,6,7] might become [4,5,6,7,0,1,2]).
 * <p>
 * You are given a target value to search. If found in the array return its index, otherwise return -1.
 * <p>
 * You may assume no duplicate exists in the array.
 * <p>
 * Your algorithm's runtime complexity must be in the order of O(log n).
 * <p>
 * Example 1:
 * <p>
 * Input: nums = [4,5,6,7,0,1,2], target = 0
 * Output: 4
 * Example 2:
 * <p>
 * Input: nums = [4,5,6,7,0,1,2], target = 3
 * Output: -1
 */
public class Solution {
    public int search(int[] nums, int target) {
        if (nums.length == 0) return -1;
        int lo = 0, hi = nums.length - 1;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (nums[mid] > nums[hi]) lo = mid + 1;
            else hi = mid;
        }
        int pivot = lo;

        if (nums[pivot] == target) return pivot;
        boolean right = target>nums[pivot]&&target<=nums[nums.length-1];
        lo = right?pivot+1:0;
        hi = right?nums.length-1:pivot-1;
        int mid;
        while (lo <= hi) {
            mid = (lo + hi) / 2;
            if (nums[mid] == target) return mid;
            else if (nums[mid] > target) hi = mid - 1;
            else lo = mid + 1;
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] nums = {4};
        int res = new Solution().search(nums, 4);
        System.out.println(res);
    }
}
