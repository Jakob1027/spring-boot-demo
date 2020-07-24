package com.jakob.leetcode.leetcode_25;

import com.jakob.leetcode.ListNode;

/**
 * Given a linked list, reverse the nodes of a linked list k at a time and return its modified list.
 * <p>
 * k is a positive integer and is less than or equal to the length of the linked list. If the number of nodes is not a multiple of k then left-out nodes in the end should remain as it is.
 * <p>
 * Example:
 * <p>
 * Given this linked list: 1->2->3->4->5
 * <p>
 * For k = 2, you should return: 2->1->4->3->5
 * <p>
 * For k = 3, you should return: 3->2->1->4->5
 * <p>
 * Note:
 * <p>
 * Only constant extra memory is allowed.
 * You may not alter the values in the list's nodes, only nodes itself may be changed.
 */
public class Solution {
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode pre = dummy, cur = head;
        while (cur != null) {
            for (int i = 1; i < k && cur != null; i++) {
                cur = cur.next;
            }
            if (cur != null) {
                cur = cur.next;
                ListNode newHead = cur;
                ListNode p = pre.next;
                while (p != cur) {
                    ListNode temp = p.next;
                    p.next = newHead;
                    newHead = p;
                    p = temp;
                }
                ListNode newPre = pre.next;
                pre.next = newHead;
                pre = newPre;
            }
        }
        return dummy.next;
    }

    public static void main(String[] args) {
        ListNode head = ListNode.CreateListNode(new int[]{1, 2, 3, 4, 5});
        ListNode res = new Solution().reverseKGroup(head, 3);
        ListNode.print(res);
    }
}
