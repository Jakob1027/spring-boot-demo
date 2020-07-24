package com.jakob.newcode.huawei.hj75;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String s1 = sc.nextLine();
            String s2 = sc.nextLine();
            System.out.println(lcs(s1, s2));
        }
    }

    private static int lcs(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();
        if (m == 0 || n == 0) return 0;
        int[][] dp = new int[m][n];
        int res = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int pre = 0;
                if (i > 0 && j > 0) {
                    pre = dp[i - 1][j - 1];
                }
                if (Character.toLowerCase(s1.charAt(i)) == Character.toLowerCase(s2.charAt(j))) {
                    dp[i][j] = pre + 1;
                } else {
                    dp[i][j] = 0;
                }
                if (dp[i][j] > res) res = dp[i][j];
            }
        }
        return res;
    }
}
