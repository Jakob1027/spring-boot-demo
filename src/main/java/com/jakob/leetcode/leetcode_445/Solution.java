package com.jakob.leetcode.leetcode_445;

import com.jakob.leetcode.ListNode;

import java.util.Stack;

/**
 * You are given two non-empty linked lists representing two non-negative integers. The most significant digit comes first and each of their nodes contain a single digit. Add the two numbers and return it as a linked list.
 * <p>
 * You may assume the two numbers do not contain any leading zero, except the number 0 itself.
 * <p>
 * Follow up:
 * What if you cannot modify the input lists? In other words, reversing the lists is not allowed.
 * <p>
 * Example:
 * <p>
 * Input: (7 -> 2 -> 4 -> 3) + (5 -> 6 -> 4)
 * Output: 7 -> 8 -> 0 -> 7
 */
public class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        Stack<Integer> s1 = new Stack<>();
        Stack<Integer> s2 = new Stack<>();
        ListNode head = new ListNode(0);
        while (l1 != null) {
            s1.push(l1.val);
            l1 = l1.next;
        }
        while (l2 != null) {
            s2.push(l2.val);
            l2 = l2.next;
        }
        int sum = 0;
        while (!s1.isEmpty() || !s2.isEmpty() || sum != 0) {
            int a = s1.isEmpty() ? 0 : s1.pop();
            int b = s2.isEmpty() ? 0 : s2.pop();
            sum += a + b;
            ListNode temp = head.next;
            head.next = new ListNode(sum % 10);
            head.next.next = temp;
            sum /= 10;
        }
        return head.next;
    }

    public static void main(String[] args) {
        int[] nums1 = {3, 9, 9, 9, 9, 9, 9, 9, 9, 9};
        int[] nums2 = {7};
        ListNode l1 = ListNode.CreateListNode(nums1);
        ListNode l2 = ListNode.CreateListNode(nums2);
        ListNode res = new Solution().addTwoNumbers(l1, l2);
        ListNode.print(res);
    }
}
