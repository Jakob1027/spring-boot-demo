package com.jakob.leetcode.leetcode_401;

import java.util.ArrayList;
import java.util.List;

/**
 * A binary watch has 4 LEDs on the top which represent the hours (0-11), and the 6 LEDs on the bottom represent the minutes (0-59).
 * <p>
 * Each LED represents a zero or one, with the least significant bit on the right.
 * <p>
 * Given a non-negative integer n which represents the number of LEDs that are currently on, return all possible times the watch could represent.
 * <p>
 * Example:
 * <p>
 * Input: n = 1
 * Return: ["1:00", "2:00", "4:00", "8:00", "0:01", "0:02", "0:04", "0:08", "0:16", "0:32"]
 * Note:
 * The order of output does not matter.
 * The hour must not contain a leading zero, for example "01:00" is not valid, it should be "1:00".
 * The minute must be consist of two digits and may contain a leading zero, for example "10:2" is not valid, it should be "10:02".
 */
public class Solution {

    private List<String> res = new ArrayList<>();

    public List<String> readBinaryWatch(int num) {
        backtrack(num, 0, 0, 0);
        return res;
    }

    private void backtrack(int num, int start, int hour, int min) {
        if (num == 0) {
            res.add(hour + ":" + (min < 10 ? "0" + min : min));
            return;
        }
        for (int i = start; 10 - i >= num; i++) {
            int tempMin = min, tempHour = hour;
            if (i < 6) tempMin += 1 << i;
            else tempHour += 1 << i - 6;
            if (tempMin < 60 && tempHour < 12)
                backtrack(num - 1, i + 1, tempHour, tempMin);
        }
    }

    public static void main(String[] args) {
        List<String> list = new Solution().readBinaryWatch(2);
        System.out.println(list);
    }
}
