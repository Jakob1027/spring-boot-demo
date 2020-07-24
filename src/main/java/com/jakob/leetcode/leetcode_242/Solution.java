package com.jakob.leetcode.leetcode_242;

import java.util.HashMap;
import java.util.Map;

public class Solution {
    public boolean isAnagram(String s, String t) {
        int count = s.length();
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : s.toCharArray()) {
            int f = freq.getOrDefault(c, 0);
            freq.put(c, f + 1);
        }
        for (char c : t.toCharArray()) {
            int f = freq.getOrDefault(c, 0);
            if (f > 0) {
                count--;
                freq.put(c, f - 1);
            } else return false;
        }
        return count == 0;
    }
}
