package com.jakob.leetcode.leetcode_17;

import java.util.ArrayList;
import java.util.List;

/**
 * Given a string containing digits from 2-9 inclusive, return all possible letter combinations that the number could represent.
 */
public class Solution {

    private String[] letters = {"", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};

    private List<String> res = new ArrayList<>();

    public List<String> letterCombinations(String digits) {
        if (!digits.isEmpty()) {
            combineLetter(digits, 0, "");
        }
        return res;
    }

    private void combineLetter(String digits, int index, String path) {
        if (index == digits.length()) {
            res.add(path);
            return;
        }
        String letter = letters[digits.charAt(index) - '1'];
        for (char c : letter.toCharArray()) {
            combineLetter(digits, index + 1, path + c);
        }
    }

    public static void main(String[] args) {
        List<String> list = new Solution().letterCombinations("");
        System.out.println(list);
    }
}
