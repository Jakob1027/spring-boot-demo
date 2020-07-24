package com.jakob.leetcode.剑指offer.n11;

/**
 * 把一个数组最开始的若干个元素搬到数组的末尾，我们称之为数组的旋转。输入一个递增排序的数组的一个旋转，输出旋转数组的最小元素。例如，数组?[3,4,5,1,2] 为 [1,2,3,4,5] 的一个旋转，该数组的最小值为1。??
 * <p>
 * 示例 1：
 * <p>
 * 输入：[3,4,5,1,2]
 * 输出：1
 * 示例 2：
 * <p>
 * 输入：[2,2,2,0,1]
 * 输出：0
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/xuan-zhuan-shu-zu-de-zui-xiao-shu-zi-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {
    public int minArray(int[] numbers) {
        int left = 0, right = numbers.length - 1;
        int mid;
        while (left < right) {
            mid = (left + right) / 2;
            if (numbers[mid] < numbers[right]) right = mid;
            else if (numbers[mid] > numbers[right]) left = mid + 1;
            else right = right - 1;
        }
        return numbers[left];
    }

    public static void main(String[] args) {
        int[] numbers = {1, 2, 3, 4, 5};
        int i = new Solution().minArray(numbers);
        System.out.println(i);
    }
}
