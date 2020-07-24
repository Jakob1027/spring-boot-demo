package com.jakob.leetcode.leetcode_131;

import java.util.ArrayList;
import java.util.List;

/**
 * Given a string s, partition s such that every substring of the partition is a palindrome.
 * <p>
 * Return all possible palindrome partitioning of s.
 * <p>
 * Example:
 * <p>
 * Input: "aab"
 * Output:
 * [
 * ["aa","b"],
 * ["a","a","b"]
 * ]
 */
public class Solution {

    private List<List<String>> res = new ArrayList<>();

    public List<List<String>> partition(String s) {
        dfs(s, new ArrayList<>());
        return res;
    }

    private void dfs(String s, List<String> subs) {
        if (s.isEmpty()) {
            res.add(new ArrayList<>(subs));
        }
        for (int i = 1; i <= s.length(); i++) {
            String sub = s.substring(0, i);
            if (isPalindrome(sub)) {
                subs.add(sub);
                dfs(s.substring(i), subs);
                subs.remove(subs.size() - 1);
            }
        }
    }

    private boolean isPalindrome(String s) {
        int start = 0, end = s.length() - 1;
        while (start < end) {
            if (s.charAt(start++) != s.charAt(end--)) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        List<List<String>> result = new Solution().partition("");
        System.out.println(result);
    }
}
