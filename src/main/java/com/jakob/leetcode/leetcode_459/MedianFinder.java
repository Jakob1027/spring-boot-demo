package com.jakob.leetcode.leetcode_459;

import java.util.*;

public class MedianFinder {


    PriorityQueue<Integer> large = new PriorityQueue<>(Comparator.naturalOrder());
    PriorityQueue<Integer> small = new PriorityQueue<>(Comparator.reverseOrder());

    /**
     * initialize your data structure here.
     */
    public MedianFinder() {

    }

//    public static void main(String[] args) {
//        MedianFinder finder = new MedianFinder();
//        finder.addNum(1);
//        finder.addNum(2);
//        finder.addNum(3);
//        System.out.println(finder.findMedian());
//        Scanner scanner = new Scanner(System.in);
//        scanner.nextInt();
//
//        StringBuilder sb = new StringBuilder();
//    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine());
        List<String> input = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String s = scanner.nextLine();
            input.add(s);
        }

        for (String s : input) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                if (!con1(sb, s.charAt(i)) && !con2(sb, s.charAt(i))) {
                    sb.append(s.charAt(i));
                }
            }
            System.out.println(sb.toString());
        }
    }

    private static boolean con1(StringBuilder sb, char c) {
        int l = sb.length();
        return l >= 2 && sb.charAt(l - 1) == c && sb.charAt(l - 2) == c;
    }

    private static boolean con2(StringBuilder sb, char c) {
        int l = sb.length();
        return sb.length() >= 3 && sb.charAt(l - 1) == c && sb.charAt(l - 2) == sb.charAt(l - 3);
    }

    public void addNum(int num) {
        if (small.size() == 0) {
            small.offer(num);
            return;
        }
        if (small.size() == large.size()) {
            if (num <= small.peek()) {
                small.offer(num);
            } else {
                large.offer(num);
            }
        } else if (small.size() > large.size()) {
            if (num < small.peek()) {
                int toMove = small.poll();
                small.offer(num);
                large.offer(toMove);
            } else {
                large.offer(num);
            }
        } else {
            if (num > large.peek()) {
                int toMove = large.poll();
                large.offer(num);
                small.offer(toMove);
            } else {
                small.offer(num);
            }
        }
    }

    public double findMedian() {
        return ((double) (small.peek() + large.peek())) / 2;
    }
}
