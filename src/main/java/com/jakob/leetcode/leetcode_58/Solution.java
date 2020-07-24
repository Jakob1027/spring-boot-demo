package com.jakob.leetcode.leetcode_58;

/**
 * @author Jakob
 */
public class Solution {
    public static void main(String[] args) {
        int i = new Solution().lengthOfLastWord("       fasdf     ff");
        System.out.println(i);
    }

    public int lengthOfLastWord(String s) {
        String[] strings = s.split("\\s+");
        int len = strings.length;
        if(len==0) {
            return 0;
        }
        String str = strings[len-1];
        return str.length();
    }
}
