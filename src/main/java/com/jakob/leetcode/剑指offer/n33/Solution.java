package com.jakob.leetcode.剑指offer.n33;

/**
 * 输入一个整数数组，判断该数组是不是某二叉搜索树的后序遍历结果。如果是则返回 true，否则返回 false。假设输入的数组的任意两个数字都互不相同。
 *
 *  
 *
 * 参考以下这颗二叉搜索树：
 *
 *      5
 *     / \
 *    2   6
 *   / \
 *  1   3
 * 示例 1：
 *
 * 输入: [1,6,3,2,5]
 * 输出: false
 * 示例 2：
 *
 * 输入: [1,3,2,6,5]
 * 输出: true
 *  
 */
public class Solution {
    public boolean verifyPostorder(int[] postorder) {
        return helper(postorder, 0, postorder.length - 1);
    }

    private boolean helper(int[] postorder, int start, int end) {
        if (start >= end) return true;
        int root = postorder[end];
        int right = end - 1;
        while (right >= start && postorder[right] > root) right--;
        for (int i = right; i >= start; i--) {
            if (postorder[i] >= root) return false;
        }
        return helper(postorder, start, right) && helper(postorder, right + 1, end - 1);
    }
}
