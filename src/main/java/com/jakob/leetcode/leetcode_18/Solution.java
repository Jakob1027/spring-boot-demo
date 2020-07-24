package com.jakob.leetcode.leetcode_18;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Given an array nums of n integers and an integer target, are there elements a, b, c, and d in nums such that a + b + c + d = target? Find all unique quadruplets in the array which gives the sum of target.
 *
 * Note:
 *
 * The solution set must not contain duplicate quadruplets.
 *
 * Example:
 *
 * Given array nums = [1, 0, -1, 0, -2, 2], and target = 0.
 *
 * A solution set is:
 * [
 *   [-1,  0, 0, 1],
 *   [-2, -1, 1, 2],
 *   [-2,  0, 0, 2]
 * ]
 */
public class Solution {
    public List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);
        return kSum(nums, target, 4, 0);
    }

    private List<List<Integer>> kSum(int[] nums, int sum, int k, int start) {
        List<List<Integer>> result = new ArrayList<>();
        if (start >= nums.length) return result;
        if (k == 2) {
            int i = start, j = nums.length - 1;
            while (i < j) {
                int temp = nums[i] + nums[j];
                if (temp < sum) {
                    i++;
                } else if (temp > sum) {
                    j--;
                } else {
                    result.add(new ArrayList(Arrays.asList(nums[i], nums[j])));
                    i++;
                    j--;
                    while (i < j && nums[i] == nums[i - 1]) i++;
                    while (i < j && nums[j] == nums[j + 1]) j--;
                }
            }
        } else {
            for (int i = start; i < nums.length - k + 1; i++) {
                if (i == start || nums[i] != nums[i - 1]) {
                    List<List<Integer>> temp = kSum(nums, sum - nums[i], k - 1, i + 1);
                    for (List<Integer> l : temp) {
                        l.add(0, nums[i]);
                    }
                    result.addAll(temp);
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        int[] input = {1, 0, -1, 0, -2, 2};
        System.out.println(new Solution().fourSum(input, 0));
    }
}
