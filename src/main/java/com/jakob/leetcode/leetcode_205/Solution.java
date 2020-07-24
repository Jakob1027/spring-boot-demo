package com.jakob.leetcode.leetcode_205;

/**
 * Given two strings s and t, determine if they are isomorphic.
 * <p>
 * Two strings are isomorphic if the characters in s can be replaced to get t.
 * <p>
 * All occurrences of a character must be replaced with another character while preserving the order of characters. No two characters may map to the same character but a character may map to itself.
 * <p>
 * Example 1:
 * <p>
 * Input: s = "egg", t = "add"
 * Output: true
 * Example 2:
 * <p>
 * Input: s = "foo", t = "bar"
 * Output: false
 * Example 3:
 * <p>
 * Input: s = "paper", t = "title"
 * Output: true
 * Note:
 * You may assume both s and t have the same length.
 */
public class Solution {
    public boolean isIsomorphic(String s, String t) {
        int len = s.length();
        int[] left = new int[256];
        int[] right = new int[256];
        for (int i = 0; i < len; i++) {
            if (left[s.charAt(i)] != right[t.charAt(i)]) return false;
            left[s.charAt(i)] = i + 1;
            right[t.charAt(i)] = i + 1;
        }
        return true;
    }
}
