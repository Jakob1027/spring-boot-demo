package com.jakob.leetcode.leetcode_279;

/**
 * Given a positive integer n, find the least number of perfect square numbers (for example, 1, 4, 9, 16, ...) which sum to n.
 * <p>
 * Example 1:
 * <p>
 * Input: n = 12
 * Output: 3
 * Explanation: 12 = 4 + 4 + 4.
 * Example 2:
 * <p>
 * Input: n = 13
 * Output: 2
 * Explanation: 13 = 4 + 9.
 */
public class Solution {
    public int numSquares(int n) {
        // 动态规划版本
        int[] dp = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            dp[i] = Integer.MAX_VALUE;
            for (int j = 1; j * j <= i; j++) {
                dp[i] = Math.min(dp[i], dp[i - j * j] + 1);
            }
        }
        return dp[n];

        // BFS版本
//        Queue<SimpleEntry<Integer, Integer>> queue = new LinkedList<>();
//        queue.offer(new SimpleEntry<>(n, 0));
//        boolean[] visited = new boolean[n];
//        while (!queue.isEmpty()) {
//            SimpleEntry<Integer, Integer> entry = queue.poll();
//            int num = entry.getKey();
//            int step = entry.getValue();
//            visited[num] = true;
//
//            for (int i = 0; ; i++) {
//                int a = num - i * i;
//                if (a == 0) {
//                    return step + 1;
//                }
//                if (!visited[a]) {
//                    queue.offer(new SimpleEntry<>(a, step + 1));
//                }
//            }
//        }
//        throw new RuntimeException("无解");
    }

    public static void main(String[] args) {
        int res = new Solution().numSquares(10);
        System.out.println(res);
    }
}
