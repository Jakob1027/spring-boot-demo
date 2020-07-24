package com.jakob.newcode.huawei.hj92;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String s = sc.nextLine();
            int[] dp = new int[s.length()];
            dp[0] = Character.isDigit(s.charAt(0)) ? 1 : 0;
            int max = 0;
            for (int i = 1; i < s.length(); i++) {
                dp[i] = Character.isDigit(s.charAt(i)) ? dp[i - 1] + 1 : 0;
                max = Math.max(max, dp[i]);
            }
            for (int i = 0; i < dp.length; i++) {
                if (dp[i] == max) {
                    System.out.print(s.substring(i - max + 1, i + 1));
                }
            }
            System.out.println("," + max);
        }
    }
}
