package com.jakob.leetcode.leetcode_438;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> res = new ArrayList<>();
        List<Character> state = new ArrayList<>(p.length());
        for (char c : p.toCharArray()) state.add(c);
        int l = 0, r = -1;
        while (r < s.length() - 1) {
            int idx = state.indexOf(s.charAt(r + 1));
            if (idx != -1) {
                state.remove(idx);
                r++;
                if (state.isEmpty()) {
                    res.add(l);
                }
            } else {
                if (l <= r)
                    state.add(s.charAt(l));
                else r++;
                l++;
            }
        }

        return res;
    }

    public static void main(String[] args) {
        String s = "aba";
        String p = "ab";
        List<Integer> res = new Solution().findAnagrams(s, p);
        System.out.println(res);
    }
}
