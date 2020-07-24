package com.jakob.leetcode.leetcode_226;

import com.jakob.leetcode.TreeNode;

public class Solution {
    public TreeNode invertTree(TreeNode root) {
        if (root==null) return null;
        TreeNode left = invertTree(root.left);
        root.left = invertTree(root.right);
        root.right = left;
        return root;
    }
}
