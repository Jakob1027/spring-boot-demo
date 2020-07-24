package com.jakob.leetcode.leetcode_35;

/**
 * @author Jakob
 */
class Solution {
    public static void main(String[] args) {
        int[] nums = {1,3,5,6};
        System.out.println(new Solution().searchInsert(nums,7));
    }
    public int searchInsert(int[] nums, int target) {
        int left = 0,right = nums.length-1;
        int center;
        while (left<=right){
            center = (left+right)/2;
            if(nums[center]<target) {
                left = center+1;
            } else if (nums[center]>target) {
                right = center-1;
            } else {
                return center;
            }
        }
        return left;
    }
}
