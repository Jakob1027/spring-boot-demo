package com.jakob.leetcode.leetcode_234;

import com.jakob.leetcode.ListNode;

/**
 * Given a singly linked list, determine if it is a palindrome.
 * <p>
 * Example 1:
 * <p>
 * Input: 1->2
 * Output: false
 * Example 2:
 * <p>
 * Input: 1->2->2->1
 * Output: true
 * Follow up:
 * Could you do it in O(n) time and O(1) space?
 */
public class Solution {
    public boolean isPalindrome(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        if (fast != null) slow = slow.next;
        ListNode newHead = null;
        while (slow != null) {
            ListNode temp = slow.next;
            slow.next = newHead;
            newHead = slow;
            slow = temp;
        }
        while (newHead != null) {
            if (newHead.val != head.val) return false;
            newHead = newHead.next;
            head = head.next;
        }
        return true;
    }

    public static void main(String[] args) {
        ListNode head = ListNode.CreateListNode(new int[]{1, 1});
        boolean res = new Solution().isPalindrome(head);
        System.out.println(res);
    }
}
