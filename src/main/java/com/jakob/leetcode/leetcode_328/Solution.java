package com.jakob.leetcode.leetcode_328;

import com.jakob.leetcode.ListNode;

/**
 * Given a singly linked list, group all odd nodes together followed by the even nodes. Please note here we are talking about the node number and not the value in the nodes.
 * <p>
 * You should try to do it in place. The program should run in O(1) space complexity and O(nodes) time complexity.
 * <p>
 * Example 1:
 * <p>
 * Input: 1->2->3->4->5->NULL
 * Output: 1->3->5->2->4->NULL
 * Example 2:
 * <p>
 * Input: 2->1->3->5->6->4->7->NULL
 * Output: 2->3->6->7->1->5->4->NULL
 * Note:
 * <p>
 * The relative order inside both the even and odd groups should remain as it was in the input.
 * The first node is considered odd, the second node even and so on ...
 */
public class Solution {
    public ListNode oddEvenList(ListNode head) {
        ListNode odd = new ListNode(0);
        ListNode even = new ListNode(0);
        ListNode p1 = odd, p2 = even;
        ListNode cur = head;
        for (int i = 1; cur != null; i++, cur = cur.next) {
            if (i % 2 == 0) {
                p2.next = cur;
                p2 = p2.next;
            } else {
                p1.next = cur;
                p1 = p1.next;
            }
        }
        p1.next = even.next;
        p2.next = null;
        return odd.next;
    }
}
