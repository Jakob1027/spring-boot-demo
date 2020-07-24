package com.jakob.leetcode.leetcode_404;

import com.jakob.leetcode.TreeNode;

/**
 * Find the sum of all left leaves in a given binary tree.
 * <p>
 * Example:
 * <p>
 * 3
 * / \
 * 9  20
 * /  \
 * 15   7
 * <p>
 * There are two left leaves in the binary tree, with values 9 and 15 respectively. Return 24.
 */
public class Solution {
    public int sumOfLeftLeaves(TreeNode root) {
        if (root == null) return 0;
        return helper(root.left, true) + helper(root.right, false);
    }

    public int helper(TreeNode root, boolean left) {
        if (root == null) return 0;
        if (root.left == null && root.right == null) {
            if (left) return root.val;
            else return 0;
        }
        return helper(root.left, true) + helper(root.right, false);
    }
}
