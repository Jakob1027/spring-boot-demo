package com.jakob.newcode.huawei.hj77;

import java.util.*;

public class Main {

    private static List<String> res = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            res.clear();
            int count = sc.nextInt();
            int[] nums = new int[count];
            for (int i = 0; i < count; i++) {
                nums[i] = sc.nextInt();
            }
            backtrack(nums, 0, new Stack<>(), "", 0);
            Collections.sort(res);
            res.forEach(System.out::println);
        }
    }

    private static void backtrack(int[] nums, int idx, Stack<Integer> stack, String s, int n) {
        if (n == nums.length) {
            res.add(s);
            return;
        }
        if (!stack.isEmpty()) {
            int val = stack.pop();
            backtrack(nums, idx, stack, s + val + " ", n + 1);
            stack.push(val);
        }
        if (idx < nums.length) {
            int val = nums[idx];
            stack.push(val);
            backtrack(nums, idx + 1, stack, s, n);
            stack.pop();
        }
    }
}