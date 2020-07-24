package com.jakob.leetcode.leetcode_86;

import com.jakob.leetcode.ListNode;

/**
 * Given a linked list and a value x, partition it such that all nodes less than x come before nodes greater than or equal to x.
 * <p>
 * You should preserve the original relative order of the nodes in each of the two partitions.
 * <p>
 * Example:
 * <p>
 * Input: head = 1->4->3->2->5->2, x = 3
 * Output: 1->2->2->4->3->5
 */
public class Solution {
    public ListNode partition(ListNode head, int x) {
        ListNode less = new ListNode(0);
        ListNode lCur = less;
        ListNode greater = new ListNode(0);
        ListNode gCur = greater;
        ListNode cur = head;
        while (cur != null) {
            if (cur.val < x) {
                lCur.next = cur;
                lCur = lCur.next;
            } else {
                gCur.next = cur;
                gCur = gCur.next;
            }
            cur = cur.next;
        }
        lCur.next = greater.next;
        gCur.next = null;
        return less.next;
    }
}
