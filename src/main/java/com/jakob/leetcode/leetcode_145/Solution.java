package com.jakob.leetcode.leetcode_145;

import com.jakob.leetcode.Command;
import com.jakob.leetcode.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Given a binary tree, return the postorder traversal of its nodes' values.
 * <p>
 * Example:
 * <p>
 * Input: [1,null,2,3]
 * 1
 * \
 * 2
 * /
 * 3
 * <p>
 * Output: [3,2,1]
 * Follow up: Recursive solution is trivial, could you do it iteratively?
 */
public class Solution {
    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        Stack<Command> stack = new Stack<>();
        stack.push(new Command("go", root));
        while (!stack.isEmpty()) {
            Command c = stack.pop();
            if (c.s.equals("print") && c.node != null) {
                list.add(c.node.val);
            } else if (c.s.equals("go") && c.node != null) {
                stack.push(new Command("print", c.node));
                if (c.node.right != null) {
                    stack.push(new Command("go", c.node.right));
                }
                if (c.node.left != null) {
                    stack.push(new Command("go", c.node.left));
                }
            }
        }
        return list;
    }
}
