package com.jakob.leetcode.leetcode_230;

import com.jakob.leetcode.TreeNode;

/**
 * Given a binary search tree, write a function kthSmallest to find the kth smallest element in it.
 * <p>
 * Note:
 * You may assume k is always valid, 1 ≤ k ≤ BST's total elements.
 * <p>
 * Example 1:
 * <p>
 * Input: root = [3,1,4,null,2], k = 1
 * 3
 * / \
 * 1   4
 * \
 * 2
 * Output: 1
 * Example 2:
 * <p>
 * Input: root = [5,3,6,2,4,null,null,1], k = 3
 * 5
 * / \
 * 3   6
 * / \
 * 2   4
 * /
 * 1
 * Output: 3
 * Follow up:
 * What if the BST is modified (insert/delete operations) often and you need to find the kth smallest frequently? How would you optimize the kthSmallest routine?
 */
public class Solution {
//    public int kthSmallest(TreeNode root, int k) {
//        int leftNum = count(root.left);
//        if (leftNum == k - 1) return root.val;
//        if (leftNum < k - 1) return kthSmallest(root.right, k - leftNum - 1);
//        else return kthSmallest(root.left, k);
//    }
//
//    private int count(TreeNode root) {
//        if (root == null) return 0;
//        return count(root.left) + count(root.right) + 1;
//    }

    private int count = 0;
    private int result = Integer.MAX_VALUE;

    public int kthSmallest(TreeNode root, int k) {
        traverse(root, k);
        return result;
    }

    private void traverse(TreeNode root, int k) {
        if (root == null) return;
        traverse(root.left, k);
        count++;
        if (count == k) {
            result = root.val;
            return;
        }
        traverse(root.right, k);
    }
}
