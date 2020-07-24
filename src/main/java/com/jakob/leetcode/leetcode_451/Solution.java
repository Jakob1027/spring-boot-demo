package com.jakob.leetcode.leetcode_451;

import java.util.HashMap;
import java.util.Map;

/**
 * Given a string, sort it in decreasing order based on the frequency of characters.
 * <p>
 * Example 1:
 * <p>
 * Input:
 * "tree"
 * <p>
 * Output:
 * "eert"
 * <p>
 * Explanation:
 * 'e' appears twice while 'r' and 't' both appear once.
 * So 'e' must appear before both 'r' and 't'. Therefore "eetr" is also a valid answer.
 * Example 2:
 * <p>
 * Input:
 * "cccaaa"
 * <p>
 * Output:
 * "cccaaa"
 * <p>
 * Explanation:
 * Both 'c' and 'a' appear three times, so "aaaccc" is also a valid answer.
 * Note that "cacaca" is incorrect, as the same characters must be together.
 * Example 3:
 * <p>
 * Input:
 * "Aabb"
 * <p>
 * Output:
 * "bbAa"
 * <p>
 * Explanation:
 * "bbaA" is also a valid answer, but "Aabb" is incorrect.
 * Note that 'A' and 'a' are treated as two different characters.
 */
public class Solution {
    public String frequencySort(String s) {
        StringBuilder sb = new StringBuilder();
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : s.toCharArray()) {
            int f = freq.getOrDefault(c, 0);
            freq.put(c, f + 1);
        }
        freq.entrySet().stream().sorted((e1, e2) -> e2.getValue() - e1.getValue()).forEach(e -> {
            for (int i = 0; i < e.getValue(); i++) sb.append(e.getKey());
        });
        return sb.toString();
    }

    public static void main(String[] args) {
        String input = "tree";
        System.out.println(new Solution().frequencySort(input));
    }
}
