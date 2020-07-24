package com.jakob.leetcode.leetcode_113;

import com.jakob.leetcode.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Given a binary tree and a sum, find all root-to-leaf paths where each path's sum equals the given sum.
 * <p>
 * Note: A leaf is a node with no children.
 * <p>
 * Example:
 * <p>
 * Given the below binary tree and sum = 22,
 * <p>
 * 5
 * / \
 * 4   8
 * /   / \
 * 11  13  4
 * /  \    / \
 * 7    2  5   1
 * Return:
 * <p>
 * [
 * [5,4,11,2],
 * [5,8,4,5]
 * ]
 */
public class Solution {
    public List<List<Integer>> pathSum(TreeNode root, int sum) {
        List<List<Integer>> res = new ArrayList<>();
        pathThrough(root, sum, new ArrayList<>(), res);
        return res;
    }

    private void pathThrough(TreeNode root, int sum, List<Integer> records, List<List<Integer>> res) {
        if (root != null) {
            records.add(root.val);
            if (root.left == null && root.right == null && sum == root.val) {
                res.add(new ArrayList<>(records));
            }
            pathThrough(root.left, sum - root.val, records, res);
            pathThrough(root.right, sum - root.val, records, res);
            records.remove(records.size() - 1);
        }
    }
}
