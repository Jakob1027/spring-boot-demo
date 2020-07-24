package com.jakob.leetcode.leetcode_93;

import java.util.ArrayList;
import java.util.List;

/**
 * Given a string containing only digits, restore it by returning all possible valid IP address combinations.
 * <p>
 * Example:
 * <p>
 * Input: "25525511135"
 * Output: ["255.255.11.135", "255.255.111.35"]
 */
public class Solution {

    private List<String> res = new ArrayList<>();

    public List<String> restoreIpAddresses(String s) {
        findIp(s, 4, "");
        return res;
    }

    private void findIp(String s, int count, String ip) {
        if (count == 0 || s.isEmpty()) {
            if (count == 0 && s.isEmpty()) {
                res.add(ip.substring(1));
            }
            return;
        }
        for (int i = 1; i <= (s.charAt(0) == '0' ? 1 : 3) && i <= s.length(); i++) {
            String sub = s.substring(0, i);
            if (Integer.parseInt(sub) <= 255) {
                findIp(s.substring(i), count - 1, ip + "." + sub);
            }
        }
    }

    public static void main(String[] args) {
        List<String> res = new Solution().restoreIpAddresses("010010");
        System.out.println(res);
    }
}
