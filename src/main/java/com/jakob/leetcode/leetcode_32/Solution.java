package com.jakob.leetcode.leetcode_32;

/**
 * Given a string containing just the characters '(' and ')', find the length of the longest valid (well-formed) parentheses substring.
 * <p>
 * Example 1:
 * <p>
 * Input: "(()"
 * Output: 2
 * Explanation: The longest valid parentheses substring is "()"
 * Example 2:
 * <p>
 * Input: ")()())"
 * Output: 4
 * Explanation: The longest valid parentheses substring is "()()"
 */
public class Solution {
    public int longestValidParentheses(String s) {
        /**
         * 动态规划
         */
//        int result = 0;
//        int[] dp = new int[s.length()];
//        for (int i = 1; i < s.length(); i++) {
//            if (s.charAt(i) == ')') {
//                if (s.charAt(i - 1) == '(') {
//                    dp[i] = (i >= 2 ? dp[i - 2] : 0) + 2;
//                } else if (i - dp[i - 1] >= 1 && s.charAt(i - dp[i - 1] - 1) == '(') {
//                    dp[i] = dp[i - 1] + (i - dp[i - 1] >= 2 ? dp[i - dp[i - 1] - 2] : 0) + 2;
//                }
//            }
//            result = Math.max(result, dp[i]);
//        }
//        return result;
        /**
         * 栈
         */
//        int result = 0;
//        Stack<Integer> stack = new Stack<>();
//        stack.push(-1);
//        for (int i = 0; i < s.length(); i++) {
//            char c = s.charAt(i);
//            if (c == '(') {
//                stack.push(i);
//            } else {
//                stack.pop();
//                if (stack.isEmpty()) {
//                    stack.push(i);
//                } else {
//                    result = Math.max(result, i - stack.peek());
//                }
//            }
//        }
        /**
         * 遍历
         */
        int left = 0, right = 0, maxLen = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') left++;
            else right++;
            if (left == right) maxLen = Math.max(maxLen, 2 * right);
            if (left < right) {
                left = right = 0;
            }
        }
        left = right = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            char c = s.charAt(i);
            if (c == '(') left++;
            else right++;
            if (left == right) maxLen = Math.max(maxLen, 2 * right);
            if (left > right) {
                left = right = 0;
            }
        }
        return maxLen;
    }

    public static void main(String[] args) {
        int length = new Solution().longestValidParentheses("(()");
        System.out.println(length);
    }
}
