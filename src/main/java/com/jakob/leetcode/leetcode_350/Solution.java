package com.jakob.leetcode.leetcode_350;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {
    public int[] intersect(int[] nums1, int[] nums2) {
//        Map<Integer, Long> counting = Arrays.stream(nums2).boxed().collect(groupingBy(i -> i, counting()));
//
//        return Arrays.stream(nums1).filter(i -> {
//            long freq = counting.getOrDefault(i, 0L);
//            if (freq > 0) {
//                counting.put(i, freq - 1);
//            }
//            return freq > 0;
//        }).toArray();

        Map<Integer, Integer> map = new HashMap();
        for (int i : nums2) {
            int freq = map.getOrDefault(i, 0);
            map.put(i, freq + 1);
        }
        List<Integer> list = new ArrayList<>();
        for (int i : nums1) {
            int freq = map.getOrDefault(i, 0);
            if (freq > 0) {
                list.add(i);
                map.put(i, freq - 1);
            }
        }
        return list.stream().mapToInt(i -> i).toArray();
    }
}
