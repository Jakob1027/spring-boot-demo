package com.jakob.leetcode.剑指offer.n07;

import com.jakob.leetcode.TreeNode;

import java.util.HashMap;
import java.util.Map;

/**
 * 输入某二叉树的前序遍历和中序遍历的结果，请重建该二叉树。假设输入的前序遍历和中序遍历的结果中都不含重复的数字。
 * <p>
 * ?
 * <p>
 * 例如，给出
 * <p>
 * 前序遍历 preorder =?[3,9,20,15,7]
 * 中序遍历 inorder = [9,3,15,20,7]
 * 返回如下的二叉树：
 * <p>
 * 3
 * / \
 * 9  20
 * /  \
 * 15   7
 * ?
 * <p>
 * 限制：
 * <p>
 * 0 <= 节点个数 <= 5000
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/zhong-jian-er-cha-shu-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {
    Map<Integer, Integer> idxMap = new HashMap<>();

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        if (preorder.length == 0) return null;
        for (int i = 0; i < inorder.length; i++) {
            idxMap.put(inorder[i], i);
        }
        return buildTree(preorder, 0, preorder.length, inorder, 0, inorder.length);
    }

    private TreeNode buildTree(int[] preorder, int s1, int e1, int[] inorder, int s2, int e2) {
        if (s1 >= e1) return null;
        int root = preorder[s1];
        TreeNode n = new TreeNode(root);
        int idx = idxMap.get(root);
        int lNum = idx - s2;
        TreeNode left = buildTree(preorder, s1 + 1, s1 + lNum + 1, inorder, s2, idx);
        TreeNode right = buildTree(preorder, s1 + lNum + 1, e1, inorder, idx + 1, e2);
        n.left = left;
        n.right = right;
        return n;
    }
}
