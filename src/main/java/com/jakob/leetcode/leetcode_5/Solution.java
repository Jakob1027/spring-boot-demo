package com.jakob.leetcode.leetcode_5;

/**
 * 给定一个字符串 s，找到 s 中最长的回文子串。你可以假设?s 的最大长度为 1000。
 * <p>
 * 示例 1：
 * <p>
 * 输入: "babad"
 * 输出: "bab"
 * 注意: "aba" 也是一个有效答案。
 * 示例 2：
 * <p>
 * 输入: "cbbd"
 * 输出: "bb"
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/longest-palindromic-substring
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {
    public String longestPalindrome(String s) {
//        String result = "";
//        for (int i = 0; i < s.length(); i++) {
//            String str = getString(s, i, i);
//            if (str.length() > result.length()) result = str;
//            str = getString(s, i, i + 1);
//            if (str.length() > result.length()) result = str;
//        }
//        return result;

        int n = s.length();
        if (n == 0) return "";
        int start = 0, end = 0;
        boolean[] dp = new boolean[n];
        for (int i = n - 1; i >= 0; i--) {
            dp[i] = true;
            for (int j = n - 1; j > i; j--) {
                dp[j] = dp[j - 1] && s.charAt(i) == s.charAt(j);
                if (dp[j] && j - i > end - start) {
                    start = i;
                    end = j;
                }
            }
        }
        return s.substring(start, end + 1);
    }

    private String getString(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        return s.substring(left + 1, right);
    }

    public static void main(String[] args) {
        String s = "bb";
        String res = new Solution().longestPalindrome(s);
        System.out.println(res);
    }
}
