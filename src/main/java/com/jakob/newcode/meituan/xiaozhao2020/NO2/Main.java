package com.jakob.newcode.meituan.xiaozhao2020.NO2;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String pattern = sc.nextLine();
        String target = sc.nextLine();
        String s1 = "*145**5]6461";
        String s2 = "s5dwee1455]6461";
        boolean[][] dp = new boolean[pattern.length() + 1][target.length() + 1];
        dp[0][0] = true;
        for (int i = 1; i <= pattern.length(); i++) {
            for (int j = 1; j <= target.length(); j++) {
                if (pattern.charAt(i - 1) == '?') {
                    dp[i][j] = dp[i - 1][j - 1];
                } else if (pattern.charAt(i - 1) == '*') {
                    for (int k = 0; k <= j; k++) {
                        dp[i][j] |= dp[i - 1][k];
                    }
                } else {
                    dp[i][j] = (pattern.charAt(i - 1) == target.charAt(j - 1)) && dp[i - 1][j - 1];
                }
            }
        }
        System.out.println(dp[pattern.length()][target.length()] ? 1 : 0);
    }
}
