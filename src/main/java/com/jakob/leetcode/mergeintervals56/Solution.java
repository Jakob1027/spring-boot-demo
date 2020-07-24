package com.jakob.leetcode.mergeintervals56;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution {
    public static void main(String[] args) {

    }

    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, (i1, i2) -> Integer.compare(i1[0], i2[0]));

        List<int[]> list = new ArrayList<>();
        int[] cur = intervals[0];
        list.add(cur);

        for (int[] interval : intervals) {
            if (interval[0] <= cur[1]) {
                cur[1] = Math.max(cur[1], interval[1]);
            } else {
                cur = interval;
                list.add(cur);
            }
        }
        return list.toArray(new int[0][]);

    }
}