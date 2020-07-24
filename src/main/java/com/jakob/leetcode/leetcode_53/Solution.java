package com.jakob.leetcode.leetcode_53;

/**
 * @author Jakob
 */
class Solution {

    public static void main(String[] args) {
        int[] nums = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        int maxSum = new Solution().maxSubArray1(nums, 0, 8);
        System.out.println(maxSum);
    }

    public int maxSubArray(int[] nums) {
        int sum = 0;
        int maxSum = nums[0];
        for (int num : nums) {
            sum += num;
            if (sum > maxSum) {
                maxSum = sum;
            }
            if (sum < 0) {
                sum = 0;
            }
        }
        return maxSum;
    }

    /**
     * 分治算法
     *
     * @param nums
     * @return
     */
    public int maxSubArray1(int[] nums, int left, int right) {
        int center = (left + right) / 2;
        int boardLeftSum = 0, boardRightSum = 0;
        int leftMaxSum, rightMaxSum, boardLeftMaxSum, boardRightMaxSum, boardMaxSum;
        int maxSum;
        if (right > left) {
            boardLeftMaxSum = nums[center];
            boardRightMaxSum = nums[center + 1];
            leftMaxSum = maxSubArray1(nums, left, center);
            rightMaxSum = maxSubArray1(nums, center + 1, right);
            for (int i = center; i >= left; i--) {
                boardLeftSum += nums[i];
                if (boardLeftSum > boardLeftMaxSum) {
                    boardLeftMaxSum = boardLeftSum;
                }
            }
            for (int i = center + 1; i <= right; i++) {
                boardRightSum += nums[i];
                if (boardRightSum > boardRightMaxSum) {
                    boardRightMaxSum = boardRightSum;
                }
            }
            boardMaxSum = boardLeftMaxSum + boardRightMaxSum;
            maxSum = boardMaxSum > Math.max(leftMaxSum, rightMaxSum) ? boardMaxSum : Math.max(leftMaxSum, rightMaxSum);
        } else {
            maxSum = nums[left];
        }

        return maxSum;
    }
}
