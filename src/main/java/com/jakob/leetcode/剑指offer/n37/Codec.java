package com.jakob.leetcode.剑指offer.n37;

import com.jakob.leetcode.TreeNode;

import java.util.*;
import java.util.stream.Collectors;

public class Codec {

    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        StringBuilder ser = new StringBuilder("[");
        if (root != null)
            queue.offer(root);
        while (queue.peek() != null) {
            TreeNode node = queue.poll();
            if (node == root) {
                ser.append("[").append(root.val);
            }
            if (node.left != null) {
                ser.append(",").append(node.left.val);
                queue.offer(node.left);
            } else {
                ser.append(",").append("null");
            }
            if (node.right != null) {
                ser.append(",").append(node.right.val);
                queue.offer(node.right);
            } else {
                ser.append(",").append("null");
            }
        }
        ser.append("]");
        return ser.toString();
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        String substring = data.substring(1, data.length() - 1);
        if (substring.isEmpty()) return null;
        String[] strings = substring.split(",");
        List<TreeNode> list = Arrays.stream(strings).map(e -> {
            String val = e.trim();
            if (val.equals("null")) return null;
            else {
                return new TreeNode(Integer.parseInt(val));
            }
        }).collect(Collectors.toList());
        int visited = 1;
        for (int i = 0; visited < list.size() && i < list.size(); i++) {
            TreeNode node = list.get(i);
            if (node != null) {
                node.left = list.get(visited++);
                node.right = list.get(visited++);
            }
        }

        return list.isEmpty() ? null : list.get(0);
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        TreeNode left = new TreeNode(2);
        TreeNode right = new TreeNode(3);
        root.left = left;
        root.right = right;
        right.left = new TreeNode(4);
        right.right = new TreeNode(5);

        Codec codec = new Codec();
        String s = codec.serialize(root);
        System.out.println(s);

        TreeNode node = codec.deserialize("[]");
        System.out.println(node);
    }
}

// Your Codec object will be instantiated and called as such:
// Codec codec = new Codec();
// codec.deserialize(codec.serialize(root));
