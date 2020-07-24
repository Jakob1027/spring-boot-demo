package com.jakob.leetcode.leetcode_290;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Solution {
    public boolean wordPattern(String pattern, String str) {
        String[] words = str.split(" ");
        if (pattern.length() != words.length) return false;
        Map<Character, String> record = new HashMap<>();
        Set<String> appearance = new HashSet<>();
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            String word = record.get(c);
            if (word != null) {
                if (!word.equals(words[i])) return false;
            } else if (!appearance.add(words[i])) {
                return false;
            } else record.put(c, words[i]);
        }
        return true;
    }

    public static void main(String[] args) {

    }
}
