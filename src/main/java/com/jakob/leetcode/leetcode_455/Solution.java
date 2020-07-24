package com.jakob.leetcode.leetcode_455;

import java.util.Arrays;

/**
 * Assume you are an awesome parent and want to give your children some cookies. But, you should give each child at most one cookie. Each child i has a greed factor gi, which is the minimum size of a cookie that the child will be content with; and each cookie j has a size sj. If sj >= gi, we can assign the cookie j to the child i, and the child i will be content. Your goal is to maximize the number of your content children and output the maximum number.
 * <p>
 * Note:
 * You may assume the greed factor is always positive.
 * You cannot assign more than one cookie to one child.
 * <p>
 * Example 1:
 * Input: [1,2,3], [1,1]
 * <p>
 * Output: 1
 * <p>
 * Explanation: You have 3 children and 2 cookies. The greed factors of 3 children are 1, 2, 3.
 * And even though you have 2 cookies, since their size is both 1, you could only make the child whose greed factor is 1 content.
 * You need to output 1.
 * Example 2:
 * Input: [1,2], [1,2,3]
 * <p>
 * Output: 2
 * <p>
 * Explanation: You have 2 children and 3 cookies. The greed factors of 2 children are 1, 2.
 * You have 3 cookies and their sizes are big enough to gratify all of the children,
 * You need to output 2.
 */
public class Solution {
    public int findContentChildren(int[] g, int[] s) {
        Arrays.sort(g);
        Arrays.sort(s);
        int gi = 0, si = 0;
        for (; gi < g.length && si < s.length; si++) {
            if (g[gi] <= s[si]) gi++;
        }
        return gi;
    }
}
