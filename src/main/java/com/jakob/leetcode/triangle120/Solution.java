package com.jakob.leetcode.triangle120;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Solution {
    public int minimumTotal(List<List<Integer>> triangle) {
        int n = triangle.size();
        List<List<Integer>> state = new ArrayList<>(n);
        state.add(Arrays.asList(triangle.get(0).get(0)));
        for (int i = 1; i < n; i++) {
            List<Integer> l = new ArrayList<>();
            l.add(state.get(i - 1).get(0) + triangle.get(i).get(0));
            for (int j = 1; j < triangle.get(i).size(); j++) {
                int val = triangle.get(i).get(j);
                int min;
                if (j == triangle.get(i).size() - 1) {
                    min = triangle.get(i - 1).get(j - 1) + val;
                } else {
                    min = Math.min(state.get(i - 1).get(j), triangle.get(i - 1).get(j - 1)) + val;
                }
                l.add(min);
            }
            state.add(l);
        }
        List<Integer> l = state.get(n - 1);
        int min = l.get(0);
        for (int i = 1; i < l.size(); i++) {
            if (l.get(i) < min) min = l.get(i);
        }
        return min;
    }

    private int[] minimumPart(List<List<Integer>> triangle, int row) {
        List<Integer> l = triangle.get(row);
        int n = l.size();
        int[] res = new int[n];
        if (row == 0) {
            res[0] = triangle.get(0).get(0);
            return res;
        }

        int[] pre = minimumPart(triangle, row - 1);
        for (int i = 0; i < n; i++) {
            res[i] = Math.max(pre[i] + l.get(i), i >= 1 ? pre[i - 1] + l.get(i) : -1);
        }
        return res;
    }
}
