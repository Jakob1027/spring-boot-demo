package com.jakob.leetcode.剑指offer.n38;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 输入一个字符串，打印出该字符串中字符的所有排列。
 *
 *  
 *
 * 你可以以任意顺序返回这个字符串数组，但里面不能有重复元素。
 *
 *  
 *
 * 示例:
 *
 * 输入：s = "abc"
 * 输出：["abc","acb","bac","bca","cab","cba"]
 *  
 *
 * 限制：
 *
 * 1 <= s 的长度 <= 8
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/zi-fu-chuan-de-pai-lie-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {
    private List<String> list = new ArrayList<>();
    private boolean[] visited;

    public String[] permutation(String s) {
        visited = new boolean[s.length()];
        char[] charArray = s.toCharArray();
        Arrays.sort(charArray);
        backtrack(charArray, "");
        return list.toArray(new String[0]);
    }

    private void backtrack(char[] s, String cur) {
        if (cur.length() == s.length) {
            list.add(cur);
        }
        for (int i = 0; i < s.length; i++) {
            if (i > 0 && s[i] == s[i - 1] && visited[i - 1]) continue;
            if (!visited[i]) {
                visited[i] = true;
                backtrack(s, cur + s[i]);
                visited[i] = false;
            }
        }
    }

    public static void main(String[] args) {
        String[] permutation = new Solution().permutation("abca");
        System.out.println(Arrays.asList(permutation));
    }

}
