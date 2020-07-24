package com.jakob.leetcode.剑指offer.n17;

/**
 * 输入数字 n，按顺序打印出从 1 到最大的 n 位十进制数。比如输入 3，则打印出 1、2、3 一直到最大的 3 位数 999。
 * <p>
 * 示例 1:
 * <p>
 * 输入: n = 1
 * 输出: [1,2,3,4,5,6,7,8,9]
 *  
 * <p>
 * 说明：
 * <p>
 * 用返回一个整数列表来代替打印
 * n 为正整数
 */
public class Solution {
    public int[] printNumbers(int n) {
        int size = (int) Math.pow(10, n) - 1;
        int[] res = new int[size];
        for (int i = 1; i <= size; i++) {
            res[i - 1] = i;
        }
        return res;
    }
}
