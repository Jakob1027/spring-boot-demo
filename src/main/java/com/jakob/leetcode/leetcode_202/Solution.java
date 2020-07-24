package com.jakob.leetcode.leetcode_202;

import java.util.HashSet;
import java.util.Set;

public class Solution {
    public boolean isHappy(int n) {
        Set<Integer> record = new HashSet<>();
        while (n != 1) {
            int temp = 0;
            while (n > 0) {
                int digit = n % 10;
                temp += digit * digit;
                n /= 10;
            }
            n = temp;
            if (!record.add(n)) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        boolean isHappy = new Solution().isHappy(19);
        System.out.println(isHappy);
    }
}
