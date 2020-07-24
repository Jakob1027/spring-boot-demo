package com.jakob.leetcode.剑指offer.n04;

/**
 * 在一个 n * m 的二维数组中，每一行都按照从左到右递增的顺序排序，每一列都按照从上到下递增的顺序排序。请完成一个函数，输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。
 * <p>
 * ?
 * <p>
 * 示例:
 * <p>
 * 现有矩阵 matrix 如下：
 * <p>
 * [
 * [1,   4,  7, 11, 15],
 * [2,   5,  8, 12, 19],
 * [3,   6,  9, 16, 22],
 * [10, 13, 14, 17, 24],
 * [18, 21, 23, 26, 30]
 * ]
 * 给定 target?=?5，返回?true。
 * <p>
 * 给定?target?=?20，返回?false。
 * <p>
 * ?
 * <p>
 * 限制：
 * <p>
 * 0 <= n <= 1000
 * <p>
 * 0 <= m <= 1000
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/er-wei-shu-zu-zhong-de-cha-zhao-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {
    public boolean findNumberIn2DArray(int[][] matrix, int target) {
//        int n = matrix.length;
//        if (n == 0) return false;
//        int m = matrix[0].length;
//        if (m == 0) return false;
//        int left = 0, right = n - 1;
//        while (left < right) {
//            int mid = (left + right) / 2;
//            if (matrix[mid][0] == target) return true;
//            else if (matrix[mid][0] > target) right = mid - 1;
//            else left = mid + 1;
//        }
//        if (left < 1 || left > n) return false;
//        right = n - 1;
//        for (int i = 0; i < left; i++) {
//            int index = Arrays.binarySearch(matrix[i], 0, right, target);
//            if (index >= 0) return true;
//            right = -index - 1;
//        }
//        return false;

        int n = matrix.length;
        if (n == 0) return false;
        int m = matrix[0].length;
        int row = 0, col = m - 1;
        while (row < n && col >= 0) {
            int num = matrix[row][col];
            if (num == target) return true;
            else if (num > target) col--;
            else row++;
        }
        return false;
    }
}
