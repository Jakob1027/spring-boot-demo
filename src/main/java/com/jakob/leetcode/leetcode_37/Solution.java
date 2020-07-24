package com.jakob.leetcode.leetcode_37;

/**
 * Write a program to solve a Sudoku puzzle by filling the empty cells.
 * <p>
 * A sudoku solution must satisfy all of the following rules:
 * <p>
 * Each of the digits 1-9 must occur exactly once in each row.
 * Each of the digits 1-9 must occur exactly once in each column.
 * Each of the the digits 1-9 must occur exactly once in each of the 9 3x3 sub-boxes of the grid.
 * Empty cells are indicated by the character '.'.
 */
public class Solution {

    private boolean[][] rows;
    private boolean[][] cols;
    private boolean[][] subBoxes;

    public void solveSudoku(char[][] board) {
        int n = board.length;
        assert n == 9;
        rows = new boolean[9][9];
        cols = new boolean[9][9];
        subBoxes = new boolean[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                char c = board[i][j];
                if (c >= '1' && c <= '9') {
                    rows[i][c - '1'] = true;
                    cols[j][c - '1'] = true;
                    subBoxes[i / 3 + j / 3][c - '1'] = true;
                }
            }
        }
        backtrack(board);
    }

    private boolean backtrack(char[][] board) {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (board[x][y] == '.') {
                    for (int i = 1; i <= 9; i++) {
                        if (!rows[x][i - 1] && !cols[y][i - 1] && !subBoxes[x / 3 + y / 3][i - 1]) {
                            board[x][y] = (char) ('0' + i);
                            rows[x][i - 1] = true;
                            cols[y][i - 1] = true;
                            subBoxes[x / 3 + y / 3][i - 1] = true;
                            if (backtrack(board)) return true;
                            else {
                                rows[x][i - 1] = false;
                                cols[y][i - 1] = false;
                                subBoxes[x / 3 + y / 3][i - 1] = false;
                                board[x][y] = '.';
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        char[][] input = {{'5', '3', '.', '.', '7', '.', '.', '.', '.'}, {'6', '.', '.', '1', '9', '5', '.', '.', '.'}, {'.', '9', '8', '.', '.', '.', '.', '6', '.'}, {'8', '.', '.', '.', '6', '.', '.', '.', '3'}, {'4', '.', '.', '8', '.', '3', '.', '.', '1'}, {'7', '.', '.', '.', '2', '.', '.', '.', '6'}, {'.', '6', '.', '.', '.', '.', '2', '8', '.'}, {'.', '.', '.', '4', '1', '9', '.', '.', '5'}, {'.', '.', '.', '.', '8', '.', '.', '7', '9'}};
        new Solution().solveSudoku(input);
    }
}
