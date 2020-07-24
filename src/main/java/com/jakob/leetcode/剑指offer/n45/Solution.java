package com.jakob.leetcode.剑指offer.n45;

import java.util.Arrays;

/**
 * 输入一个正整数数组，把数组里所有数字拼接起来排成一个数，打印能拼接出的所有数字中最小的一个。
 *
 *  
 *
 * 示例 1:
 *
 * 输入: [10,2]
 * 输出: "102"
 * 示例 2:
 *
 * 输入: [3,30,34,5,9]
 * 输出: "3033459"
 *  
 *
 * 提示:
 *
 * 0 < nums.length <= 100
 * 说明:
 *
 * 输出结果可能非常大，所以你需要返回一个字符串而不是整数
 * 拼接起来的数字可能会有前导 0，最后结果不需要去掉前导 0
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/ba-shu-zu-pai-cheng-zui-xiao-de-shu-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {
    public String minNumber(int[] nums) {
        String[] strs = new String[nums.length];
        for (int i = 0; i < nums.length; i++) {
            strs[i] = String.valueOf(nums[i]);
        }
//        quickSort(strs, 0, strs.length - 1);
        Arrays.sort(strs, (x, y) -> (x + y).compareTo(y + x));
        StringBuilder sb = new StringBuilder();
        for (String s : strs) sb.append(s);
        return sb.toString();
    }

    private void quickSort(String[] strs, int l, int r) {
        if (l >= r) return;
        int i = l, j = r;
        String pivot = strs[l];
        while (i < j) {
            while (i < j && (strs[j] + pivot).compareTo(pivot + strs[j]) >= 0) j--;
            while (i < j && (strs[i] + pivot).compareTo(pivot + strs[i]) <= 0) i++;
            swap(strs, i, j);
        }
        swap(strs, i, l);
        quickSort(strs, l, i - 1);
        quickSort(strs, i + 1, r);
    }

    private void swap(String[] strs, int i, int j) {
        String tmp = strs[i];
        strs[i] = strs[j];
        strs[j] = tmp;
    }

    public static void main(String[] args) {
//        String[] nums = {"3", "30", "34", "5", "9"};
        int[] nums = {3, 30, 34, 5, 9};
        String minNumber = new Solution().minNumber(nums);
        System.out.println(minNumber);
    }
}
