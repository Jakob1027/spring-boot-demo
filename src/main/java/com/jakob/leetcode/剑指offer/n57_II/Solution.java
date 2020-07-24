package com.jakob.leetcode.剑指offer.n57_II;

import edu.emory.mathcs.backport.java.util.Arrays;

import java.util.ArrayList;
import java.util.List;

/**
 * 输入一个正整数 target ，输出所有和为 target 的连续正整数序列（至少含有两个数）。
 *
 * 序列内的数字由小到大排列，不同序列按照首个数字从小到大排列。
 *
 *  
 *
 * 示例 1：
 *
 * 输入：target = 9
 * 输出：[[2,3,4],[4,5]]
 * 示例 2：
 *
 * 输入：target = 15
 * 输出：[[1,2,3,4,5],[4,5,6],[7,8]]
 *  
 *
 * 限制：
 *
 * 1 <= target <= 10^5
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/he-wei-sde-lian-xu-zheng-shu-xu-lie-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {
    public int[][] findContinuousSequence(int target) {
         List<int[]> result = new ArrayList<>();
         int left = 1, right = 2;
         while (left < target) {
             int sum = (left + right) * (right - left + 1) / 2;
             if (sum == target) {
                 int[] arr = new int[right - left + 1];
                 for (int i = left; i <= right; i++) {
                     arr[i - left] = i;
                 }
                 result.add(arr);
                 left++;
             } else if (sum < target) {
                 right++;
             } else {
                 left++;
             }
         }
         return result.toArray(new int[0][]);
    }

    public static void main(String[] args) {
        int[][] res = new Solution().findContinuousSequence(15);
        System.out.println(Arrays.toString(res));
    }
}
