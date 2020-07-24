package com.jakob.leetcode.剑指offer.n34;

import com.jakob.leetcode.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 输入一棵二叉树和一个整数，打印出二叉树中节点值的和为输入整数的所有路径。从树的根节点开始往下一直到叶节点所经过的节点形成一条路径。
 */
public class Solution {
    private List<List<Integer>> result = new ArrayList<>();

    public List<List<Integer>> pathSum(TreeNode root, int sum) {
        List<Integer> list = new ArrayList<>();
        backtrack(root, sum, list);
        return result;
    }

    private void backtrack(TreeNode root, int sum, List<Integer> list) {
        if (root == null) return;
        list.add(root.val);
        sum -= root.val;
        if (root.left == null && root.right == null && sum == 0) result.add(new ArrayList<>(list));
        backtrack(root.left, sum, list);
        backtrack(root.right, sum, list);
        list.remove(list.size() - 1);
    }
}
