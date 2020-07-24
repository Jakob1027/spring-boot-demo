package com.jakob.leetcode.剑指offer.n56_I;

import java.util.Arrays;

/**
 * 一个整型数组 nums 里除两个数字之外，其他数字都出现了两次。请写程序找出这两个只出现一次的数字。要求时间复杂度是O(n)，空间复杂度是O(1)。
 *
 *  
 *
 * 示例 1：
 *
 * 输入：nums = [4,1,4,6]
 * 输出：[1,6] 或 [6,1]
 * 示例 2：
 *
 * 输入：nums = [1,2,10,4,1,4,3,3]
 * 输出：[2,10] 或 [10,2]
 *  
 *
 * 限制：
 *
 * 2 <= nums <= 10000
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/shu-zu-zhong-shu-zi-chu-xian-de-ci-shu-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {
    public int[] singleNumbers(int[] nums) {
        // 所有数字异或的结果
        int x = 0;
        int a = 0, b = 0;
        for (int num : nums) {
            x ^= num;
        }
        // 找到第一位不是0的
        int h = 1;
        while ((x & h) == 0) {
            h <<= 1;
        }
        for (int num : nums) {
            // 根据该位是否为0将其分为两组
            if ((num & h) == 0) {
                a ^= num;
            } else {
                b ^= num;
            }
        }
        return new int[]{a, b};
    }

    public static void main(String[] args) {
        int[] nums = {1,2,10,4,1,4,3,3};
        int[] singleNumbers = new Solution().singleNumbers(nums);
        System.out.println(Arrays.toString(singleNumbers));
    }
}
