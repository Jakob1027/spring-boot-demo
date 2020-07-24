package com.jakob.leetcode.leetcode_40;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Given a collection of candidate numbers (candidates) and a target number (target), find all unique combinations in candidates where the candidate numbers sums to target.
 * <p>
 * Each number in candidates may only be used once in the combination.
 * <p>
 * Note:
 * <p>
 * All numbers (including target) will be positive integers.
 * The solution set must not contain duplicate combinations.
 * Example 1:
 * <p>
 * Input: candidates = [10,1,2,7,6,1,5], target = 8,
 * A solution set is:
 * [
 * [1, 7],
 * [1, 2, 5],
 * [2, 6],
 * [1, 1, 6]
 * ]
 * Example 2:
 * <p>
 * Input: candidates = [2,5,2,1,2], target = 5,
 * A solution set is:
 * [
 * [1,2,2],
 * [5]
 * ]
 */
public class Solution {

    private List<List<Integer>> res = new ArrayList<>();

    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        Arrays.sort(candidates);
        backtrack(candidates, target, new ArrayList<>(), 0);
        return res;
    }

    private void backtrack(int[] candidates, int target, List<Integer> path, int start) {
        if (target < 0) return;
        if (target == 0) {
            res.add(new ArrayList<>(path));
        } else {
            for (int i = start; i < candidates.length; i++) {
                if (i == start || candidates[i] != candidates[i - 1]) {
                    path.add(candidates[i]);
                    backtrack(candidates, target - candidates[i], path, i + 1);
                    path.remove(path.size() - 1);
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] nums = {10, 1, 2, 7, 6, 1, 5};
        List<List<Integer>> res = new Solution().combinationSum2(nums, 8);
        System.out.println(res);
    }
}
