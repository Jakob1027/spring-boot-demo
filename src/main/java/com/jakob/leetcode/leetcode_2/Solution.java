package com.jakob.leetcode.leetcode_2;

import com.jakob.leetcode.ListNode;

/**
 * You are given two non-empty linked lists representing two non-negative integers. The digits are stored in reverse order and each of their nodes contain a single digit. Add the two numbers and return it as a linked list.
 * <p>
 * You may assume the two numbers do not contain any leading zero, except the number 0 itself.
 * <p>
 * Example:
 * <p>
 * Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)
 * Output: 7 -> 0 -> 8
 * Explanation: 342 + 465 = 807.
 */
public class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        int sum = 0;
        while (l1 != null || l2 != null) {
            if (l1 != null) {
                sum += l1.val;
                l1 = l1.next;
            }
            if (l2 != null) {
                sum += l2.val;
                l2 = l2.next;
            }
            cur.next = new ListNode(sum % 10);
            sum /= 10;
            cur = cur.next;
        }
        if (sum != 0) {
            cur.next = new ListNode(sum);
        }

        return dummy.next;
    }

    public static void main(String[] args) {
        ListNode l1 = ListNode.CreateListNode(new int[]{0, 1});
        ListNode l2 = ListNode.CreateListNode(new int[]{0, 2, 2});
        ListNode res = new Solution().addTwoNumbers(l1, l2);
        ListNode.print(res);
    }
}
