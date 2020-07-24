package com.jakob.leetcode;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TreeNode {
    public int val;
    public TreeNode left;
    public TreeNode right;

    public TreeNode(int val) {
        this.val = val;
    }

    public static TreeNode createTreeNode(List<Integer> nums) {
        if (nums.size() == 0) return null;
        TreeNode root = new TreeNode(nums.get(0));
        int count = 1;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (count < nums.size() && !queue.isEmpty()) {
            TreeNode node = queue.poll();
            Integer val = nums.get(count++);
            if (val != null) {
                node.left = new TreeNode(val);
                queue.offer(node.left);
            }
            if (count < nums.size()) {
                val = nums.get(count++);
                if (val != null) {
                    node.right = new TreeNode(val);
                    queue.offer(node.right);
                }
            }
        }
        return root;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(this);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if (node != null) sb.append(node.val).append(",");
            else sb.append("null").append(",");
            if (node != null) {
                queue.offer(node.left);
                queue.offer(node.right);
            }
        }
        return sb.substring(0, sb.length() - 1) + "]";
    }
}
