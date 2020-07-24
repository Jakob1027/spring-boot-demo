package com.jakob.leetcode;

public class Main {
    public static void main(String[] args) {
        ListNode node = ListNode.create(0, 5, 4, 3, 2, 1);
        ListNode.print(node);
        node = reverse(node, 6);
        ListNode.print(node);
    }

    private static ListNode reverse(ListNode root, int k) {
        if (k == 0) return root;
        ListNode newHead = null;
        ListNode temp = root;
        for (int i = 0; i < k; i++) {
            ListNode next = temp.next;
            temp.next = newHead;
            newHead = temp;
            temp = next;
        }
        root.next = temp;
        return newHead;
    }
}
