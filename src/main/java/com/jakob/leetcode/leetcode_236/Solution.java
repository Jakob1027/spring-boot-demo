package com.jakob.leetcode.leetcode_236;

import com.jakob.leetcode.TreeNode;

import java.util.*;

public class Solution {


    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        Map<TreeNode, TreeNode> parents = new HashMap<>();
        parents.put(root, null);
        while (!parents.containsKey(p) || !parents.containsKey(q)) {
            TreeNode node = queue.poll();
            if (node.left != null) {
                queue.offer(node.left);
                parents.put(node.left, node);
            }
            if (node.right != null) {
                queue.offer(node.right);
                parents.put(node.right, node);
            }
        }

        Set<TreeNode> set = new HashSet<>();
        while (p != null) {
            set.add(p);
            p = parents.get(p);
        }
        while (!set.contains(q)) {
            q = parents.get(q);
        }
        return q;
    }

    public static void main(String[] args) {
        TreeNode root = TreeNode.createTreeNode(Arrays.asList(3, 5, 1, 6, 2, 0, 8, null, null, 7, 4));
        TreeNode p = root.left;
        TreeNode q = root.right;
        new Solution().lowestCommonAncestor(root, p, q);
    }
}
