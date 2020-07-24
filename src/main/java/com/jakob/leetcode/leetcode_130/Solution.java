package com.jakob.leetcode.leetcode_130;

/**
 * Given a 2D board containing 'X' and 'O' (the letter O), capture all regions surrounded by 'X'.
 * <p>
 * A region is captured by flipping all 'O's into 'X's in that surrounded region.
 * <p>
 * Example:
 * <p>
 * X X X X
 * X O O X
 * X X O X
 * X O X X
 * After running your function, the board should be:
 * <p>
 * X X X X
 * X X X X
 * X X X X
 * X O X X
 * Explanation:
 * <p>
 * Surrounded regions shouldn’t be on the border, which means that any 'O' on the border of the board are not flipped to 'X'. Any 'O' that is not on the border and it is not connected to an 'O' on the border will be flipped to 'X'.
 * Two cells are connected if they are adjacent cells connected horizontally or vertically.
 */
public class Solution {

    private int[][] d = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
    int m, n;

    public void solve(char[][] board) {
        m = board.length;
        if (m == 0) return;
        n = board[0].length;

        for (int i = 0; i < m; i++) {
            backtrack(board, i, 0);
            backtrack(board, i, n - 1);
        }
        for (int j = 0; j < n; j++) {
            backtrack(board, 0, j);
            backtrack(board, m - 1, j);
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 'O') board[i][j] = 'X';
                else if (board[i][j] == '*') board[i][j] = 'O';
            }
        }
    }

    private void backtrack(char[][] board, int x, int y) {
        if (board[x][y] != 'O') return;
        board[x][y] = '*';
        for (int i = 0; i < 4; i++) {
            int newx = x + d[i][0];
            int newy = y + d[i][1];
            if (isValid(newx, newy)) backtrack(board, newx, newy);
        }
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < m && y >= 0 && y < n;
    }
}
