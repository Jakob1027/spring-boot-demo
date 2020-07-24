package com.jakob.leetcode.leetcode_39;

import java.util.ArrayList;
import java.util.List;

/**
 * Given a set of candidate numbers (candidates) (without duplicates) and a target number (target), find all unique combinations in candidates where the candidate numbers sums to target.
 * <p>
 * The same repeated number may be chosen from candidates unlimited number of times.
 * <p>
 * Note:
 * <p>
 * All numbers (including target) will be positive integers.
 * The solution set must not contain duplicate combinations.
 * Example 1:
 * <p>
 * Input: candidates = [2,3,6,7], target = 7,
 * A solution set is:
 * [
 * [7],
 * [2,2,3]
 * ]
 * Example 2:
 * <p>
 * Input: candidates = [2,3,5], target = 8,
 * A solution set is:
 * [
 * [2,2,2,2],
 * [2,3,3],
 * [3,5]
 * ]
 */
public class Solution {

    private List<List<Integer>> res = new ArrayList<>();

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
//        Arrays.sort(candidates);
        backtrack(candidates, target, new ArrayList<>(), 0);
        return res;
    }

    private void backtrack(int[] candidates, int target, List<Integer> records, int start) {
        if (target <= 0) {
            if (target == 0)
                res.add(new ArrayList<>(records));
            return;
        }

        for (int i = start; i < candidates.length; i++) {
            records.add(candidates[i]);
            backtrack(candidates, target - candidates[i], records, i);
            records.remove(records.size() - 1);
        }
    }

    public static void main(String[] args) {
        int[] nums = {2, 6, 3, 7,};
        List<List<Integer>> res = new Solution().combinationSum(nums, 7);
        System.out.println(res);
    }
}
