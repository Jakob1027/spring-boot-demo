package com.jakob.leetcode.validpalindrome125;

import java.math.BigDecimal;

import static java.lang.Character.isLetterOrDigit;
import static java.lang.Character.toUpperCase;

public class Solution {

    public static void main(String[] args) {
        System.out.println(new BigDecimal(233.0-233.0).toPlainString());
    }

    public boolean isPalindrome(String s) {
        int l = 0, r = s.length() - 1;
        while (l < r) {
            char c1 = s.charAt(l);
            char c2 = s.charAt(r);
            if(!isLetterOrDigit(c1)) l++;
            else if(!isLetterOrDigit(c2)) r--;
            else {
                if(toUpperCase(c1) != toUpperCase(c2)) return false;
                l++;
                r--;
            }
        }
        char[] arr = s.toCharArray();

        return true;
    }
}
