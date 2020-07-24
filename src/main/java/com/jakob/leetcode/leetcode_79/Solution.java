package com.jakob.leetcode.leetcode_79;

/**
 * Given a 2D board and a word, find if the word exists in the grid.
 * <p>
 * The word can be constructed from letters of sequentially adjacent cell, where "adjacent" cells are those horizontally or vertically neighboring. The same letter cell may not be used more than once.
 * <p>
 * Example:
 * <p>
 * board =
 * [
 * ['A','B','C','E'],
 * ['S','F','C','S'],
 * ['A','D','E','E']
 * ]
 * <p>
 * Given word = "ABCCED", return true.
 * Given word = "SEE", return true.
 * Given word = "ABCB", return false.
 */
public class Solution {

    private boolean[][] used;
    private int[][] d = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
    private int m, n;

    public boolean exist(char[][] board, String word) {
        if (word.length() == 0) return false;
        m = board.length;
        if (m == 0) return false;
        n = board[0].length;
        used = new boolean[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++) {
                if (backtrack(board, word, i, j, 0)) return true;
            }
        return false;
    }

    private boolean backtrack(char[][] board, String word, int x, int y, int index) {
        if (index == word.length()) {
            return true;
        }
        if (board[x][y] == word.charAt(index)) {
            used[x][y] = true;
            for (int i = 0; i < 4; i++) {
                int newx = x + d[i][0];
                int newy = y + d[i][1];
                if (isValid(newx, newy) && !used[newx][newy] &&
                        backtrack(board, word, newx, newy, index + 1))
                    return true;
            }
            used[x][y] = false;
        }
        return false;
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < m && y >= 0 && y < n;
    }

    public static void main(String[] args) {
        char[][] board = {{'A', 'B', 'C', 'E'},
                {'S', 'F', 'C', 'S'},
                {'A', 'D', 'E', 'E'}};
        boolean res = new Solution().exist(board, "");
        System.out.println(res);
    }
}
