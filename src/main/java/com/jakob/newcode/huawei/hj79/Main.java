package com.jakob.newcode.huawei.hj79;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String s1 = sc.nextLine();
            String s2 = sc.nextLine();
            int d = distance(s1, s2);
            System.out.println("1/" + (d + 1));
        }
    }

    private static int distance(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();
        if (m * n == 0) return m + n;
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        for (int i = 0; i <= n; i++) {
            dp[0][i] = i;
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int leftUp = dp[i - 1][j - 1];
                if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                    leftUp++;
                }
                dp[i][j] = Math.min(Math.min(dp[i][j - 1] + 1, dp[i - 1][j] + 1), leftUp);
            }
        }
        return dp[m][n];
    }
}
