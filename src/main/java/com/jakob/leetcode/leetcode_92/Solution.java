package com.jakob.leetcode.leetcode_92;

import com.jakob.leetcode.ListNode;

/**
 * Reverse a linked list from position m to n. Do it in one-pass.
 * <p>
 * Note: 1 ≤ m ≤ n ≤ length of list.
 * <p>
 * Example:
 * <p>
 * Input: 1->2->3->4->5->NULL, m = 2, n = 4
 * Output: 1->4->3->2->5->NULL
 */
public class Solution {
    public ListNode reverseBetween(ListNode head, int m, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode cur = dummy;
        int i = 0;
        for (; i < m - 1; i++) {
            cur = cur.next;
        }
        ListNode pre = cur;
        cur = cur.next;
        ListNode newHead = null;
        for (; i < n; i++) {
            ListNode temp = cur.next;
            cur.next = newHead;
            newHead = cur;
            cur = temp;
        }
        pre.next.next = cur;
        pre.next = newHead;
        return dummy.next;
    }
}
