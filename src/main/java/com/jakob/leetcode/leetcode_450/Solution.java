package com.jakob.leetcode.leetcode_450;

import com.jakob.leetcode.TreeNode;

/**
 * Given a root node reference of a BST and a key, delete the node with the given key in the BST.
 * Return the root node reference (possibly updated) of the BST.
 */
public class Solution {
    public TreeNode deleteNode(TreeNode root, int key) {
        if (root == null) return null;
        if (key < root.val) {
            root.left = deleteNode(root.left, key);
        } else if (key > root.val) {
            root.right = deleteNode(root.right, key);
        } else if (root.left != null && root.right != null) {
            root.val = findMin(root.right);
            root.right = deleteNode(root.right, root.val);
        } else {
            return root.left == null ? root.right : root.left;
        }
        return root;
    }

    private int findMin(TreeNode root) {
        if (root.left == null) return root.val;
        return findMin(root.left);
    }
}
