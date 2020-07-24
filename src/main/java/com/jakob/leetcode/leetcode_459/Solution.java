package com.jakob.leetcode.leetcode_459;

/**
 * @author Jakob
 */
public class Solution {
    public static void main(String[] args) {
//        String s = "aba";
//        String[] split = s.split("a");
//        System.out.println(Arrays.toString(split));
        int a = 2,b=3;
        double c = ((double) (a+b))/2;
        System.out.println(c);
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
