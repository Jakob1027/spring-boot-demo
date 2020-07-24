package com.jakob.leetcode.leetcode_76;

public class Solution {
    public String minWindow(String s, String t) {
        int[] freq = new int[256];
        for (char c : t.toCharArray()) freq[c]++;

        int count = t.length();
        int len = Integer.MAX_VALUE, start = 0, end = -1;
        int left = 0, right = -1;
        while (right < s.length() - 1) {
            if (freq[s.charAt(++right)]-- > 0) {
                count--;
            }

            while (count == 0) {
                if (len > right - left + 1) {
                    len = right - left + 1;
                    start = left;
                    end = right;
                }
                if (freq[s.charAt(left++)]++ >= 0) {
                    count++;
                }
            }

        }
        return s.substring(start, end + 1);
    }

    public static void main(String[] args) {
        String s = "ADOBECODEBANC";
        String t = "ABC";
        String res = new Solution().minWindow(s, t);
        System.out.println(res);
    }
}
