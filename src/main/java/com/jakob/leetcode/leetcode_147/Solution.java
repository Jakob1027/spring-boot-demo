package com.jakob.leetcode.leetcode_147;

import com.jakob.leetcode.ListNode;

/**
 * Sort a linked list using insertion sort.
 */
public class Solution {
    public ListNode insertionSortList(ListNode head) {
        if (head == null) return null;
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode cur = head.next;
        while (cur != null) {
            ListNode pre = dummy;
            while (pre.next.val < cur.val) {
                pre = pre.next;
            }
            if (pre.next != cur) {
                head.next = cur.next;
                ListNode temp = pre.next;
                pre.next = cur;
                cur.next = temp;
            } else {
                head = cur;
            }
            cur = head.next;
        }
        return dummy.next;
    }

    public void insertionSort(int[] nums) {
        for (int i = 1; i < nums.length; i++) {
            for (int j = i; j > 0; j--) {
                if (nums[j] < nums[j - 1]) {
                    int temp = nums[j];
                    nums[j] = nums[j - 1];
                    nums[j - 1] = temp;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] input = {4, 2, 1, 3};
        ListNode head = ListNode.CreateListNode(input);
        ListNode res = new Solution().insertionSortList(head);
        ListNode.print(res);
    }
}
