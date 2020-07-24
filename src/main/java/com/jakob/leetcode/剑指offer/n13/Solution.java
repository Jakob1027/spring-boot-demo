package com.jakob.leetcode.剑指offer.n13;

/**
 * 地上有一个m行n列的方格，从坐标 [0,0] 到坐标 [m-1,n-1] 。一个机器人从坐标 [0, 0] 的格子开始移动，它每次可以向左、右、上、下移动一格（不能移动到方格外），也不能进入行坐标和列坐标的数位之和大于k的格子。例如，当k为18时，机器人能够进入方格 [35, 37] ，因为3+5+3+7=18。但它不能进入方格 [35, 38]，因为3+5+3+8=19。请问该机器人能够到达多少个格子？
 * <p>
 * ?
 * <p>
 * 示例 1：
 * <p>
 * 输入：m = 2, n = 3, k = 1
 * 输出：3
 * 示例 1：
 * <p>
 * 输入：m = 3, n = 1, k = 0
 * 输出：1
 * 提示：
 * <p>
 * 1 <= n,m <= 100
 * 0 <= k?<= 20
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/ji-qi-ren-de-yun-dong-fan-wei-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {

    private boolean[][] visited;
    private int[][] dir = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    public int movingCount(int m, int n, int k) {
        visited = new boolean[m][n];
        return backtrack(m, n, 0, 0, k);
    }

    private int backtrack(int m, int n, int x, int y, int k) {
        if (digitSum(x) + digitSum(y) > k) {
            return 0;
        }
        int result = 1;
        visited[x][y] = true;
        for (int[] ints : dir) {
            int nx = x + ints[0];
            int ny = y + ints[1];
            if (nx >= 0 && nx < m && ny >= 0 && ny < n && !visited[nx][ny] && (digitSum(nx) + digitSum(ny)) <= k) {
                result += backtrack(m, n, nx, ny, k);
            }
        }
        return result;
    }

    private int digitSum(int x) {
        int sum = 0;
        while (x != 0) {
            sum += x % 10;
            x /= 10;
        }
        return sum;
    }

    public static void main(String[] args) {
//        int movingCount = new Solution().movingCount(3, 2, 17);
//        System.out.println(movingCount);
        String s = "";
    }
}
