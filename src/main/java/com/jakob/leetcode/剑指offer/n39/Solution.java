package com.jakob.leetcode.剑指offer.n39;

/**
 * 数组中有一个数字出现的次数超过数组长度的一半，请找出这个数字。
 *
 *  
 *
 * 你可以假设数组是非空的，并且给定的数组总是存在多数元素。
 *
 *  
 *
 * 示例 1:
 *
 * 输入: [1, 2, 3, 2, 2, 2, 5, 4, 2]
 * 输出: 2
 *  
 *
 * 限制：
 *
 * 1 <= 数组长度 <= 50000
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/shu-zu-zhong-chu-xian-ci-shu-chao-guo-yi-ban-de-shu-zi-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {
    public int majorityElement(int[] nums) {
        // Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        // for (int num : nums) {
        //     Integer freq = map.getOrDefault(num, 0);
        //     if (freq >= nums.length / 2) return num;
        //     map.put(num, freq + 1);
        // }
        // throw new RuntimeException("");


        // Arrays.sort(nums);
        // return nums[nums.length/2];

        int x = nums[0], vote = 0;
        for(int num: nums) {
            if(vote==0) x = num;
            if(x==num) {
                vote++;
            } else {
                vote--;
            }
        }
        return x;
    }
}
