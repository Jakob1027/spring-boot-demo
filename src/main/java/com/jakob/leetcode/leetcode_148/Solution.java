package com.jakob.leetcode.leetcode_148;

import com.jakob.leetcode.ListNode;

/**
 * Sort a linked list in O(n log n) time using constant space complexity.
 * <p>
 * Example 1:
 * <p>
 * Input: 4->2->1->3
 * Output: 1->2->3->4
 * Example 2:
 * <p>
 * Input: -1->5->3->4->0
 * Output: -1->0->3->4->5
 */
public class Solution {
    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        fast = sortList(slow.next);
        slow.next = null;
        slow = sortList(head);

        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        while (slow != null && fast != null) {
            if (slow.val <= fast.val) {
                cur.next = slow;
                slow = slow.next;
            } else {
                cur.next = fast;
                fast = fast.next;
            }
            cur = cur.next;
        }
        cur.next = slow == null ? fast : slow;
        return dummy.next;
    }
}
