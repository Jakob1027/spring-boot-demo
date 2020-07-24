package com.jakob.leetcode.leetcode_118;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 给定一个非负整数?numRows，生成杨辉三角的前?numRows?行。
 *
 *
 *
 * 在杨辉三角中，每个数是它左上方和右上方的数的和。
 *
 * 示例:
 *
 * 输入: 5
 * 输出:
 * [
 *      [1],
 *     [1,1],
 *    [1,2,1],
 *   [1,3,3,1],
 *  [1,4,6,4,1]
 * ]
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/pascals-triangle
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> res = new ArrayList<>();
        if (numRows == 0) return res;
        res.add(Collections.singletonList(1));
        for (int rowNum = 1; rowNum < numRows; rowNum++) {
            List<Integer> pre = res.get(rowNum - 1);
            List<Integer> cur = new ArrayList<>();
            for (int i = 0; i <= rowNum; i++) {
                int left = i == 0 ? 0 : pre.get(i - 1);
                int right = i == rowNum ? 0 : pre.get(i);
                cur.add(left + right);
            }
            res.add(cur);
        }
        return res;
    }

    public static void main(String[] args) {
        List<List<Integer>> res = new Solution().generate(5);
        System.out.println(res);
    }
}
