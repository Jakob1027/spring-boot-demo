package com.jakob.leetcode.leetcode_38;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jakob
 */
class Solution {
    public static void main(String[] args) {
        System.out.println(new Solution().countAndSay(4));
    }
    public String countAndSay(int n) {
        if(n==1) {
            return "1";
        }
        String result = new String();
        Map<Character,Integer> map = new HashMap<>();
        String pre = countAndSay(n-1);
        for(int i=0;i<pre.length();i++) {
            char ch = pre.charAt(i);
            if(i!=0&&ch==pre.charAt(i-1)) {
                Integer fre = map.get(ch);
                map.put(ch,fre+1);
            } else if(i==0) {
                map.put(ch,1);
            } else {
                char preChar = pre.charAt(i-1);
                result+=map.get(preChar).toString()+preChar;
                map.put(ch,1);
            }
        }
        result+=map.get(pre.charAt(pre.length()-1)).toString()+pre.charAt(pre.length()-1);
        return result;
    }
}
