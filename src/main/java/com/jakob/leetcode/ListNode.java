package com.jakob.leetcode;

public class ListNode {
    public int val;
    public ListNode next;

    public ListNode(int x) {
        val = x;
    }

    public static void print(ListNode head) {
        ListNode temp = head;
        StringBuilder sb = new StringBuilder();
        while (temp != null) {
            sb.append(temp.val).append("->");
            temp = temp.next;
        }
        sb.append("NULL");
        System.out.println(sb);
    }

    public static ListNode CreateListNode(int[] nums) {
        if (nums.length == 0) return null;
        ListNode head = new ListNode(nums[0]);
        ListNode cur = head;
        for (int i = 1; i < nums.length; i++) {
            cur.next = new ListNode(nums[i]);
            cur = cur.next;
        }
        return head;
    }

    public static ListNode create(int... n) {
        ListNode dummy = new ListNode(0);
        ListNode temp = dummy;
        for (int i : n) {
            temp.next = new ListNode(i);
            temp = temp.next;
        }
        return dummy.next;
    }
}
