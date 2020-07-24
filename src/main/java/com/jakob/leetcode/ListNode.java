package com.jakob.leetcode;

public class ListNode {
    public int val;
    public ListNode next;

    public ListNode(int x) {
        val = x;
    }

    public static void print(ListNode node) {
        if (node == null) {
            System.out.println("null");
            return;
        }
        StringBuilder sb = new StringBuilder(String.valueOf(node.val));
        while (node.next != null) {
            sb.append("->").append(node.next.val);
            node = node.next;
        }
        System.out.println(sb.toString());
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
