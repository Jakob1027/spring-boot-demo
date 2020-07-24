package com.jakob.newcode.huawei.hj85;

import java.util.Scanner;

public class Main {
//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        while (sc.hasNext()) {
//            String line = sc.nextLine();
//            int[] dp = new int[line.length()];
//            dp[0] = 1;
//            for (int i = 1; i < line.length(); i++) {
//                int max = dp[i - 1];
//                for (int j = i - max; j >= 0; j--) {
//                    if (palindrome(line, j, i)) {
//                        max = Math.max(max, i - j + 1);
//                    }
//                }
//                dp[i] = max;
//            }
//            System.out.println(dp[line.length() - 1]);
//        }
//    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String line = sc.nextLine();
            int res = 0;
            for (int i = 0; i < line.length(); i++) {
                int max = maxLen(line, i, i);
                if (i != line.length() - 1 && line.charAt(i) == line.charAt(i + 1)) {
                    max = Math.max(max, maxLen(line, i, i + 1));
                }
                res = Math.max(res, max);
            }
            System.out.println(res);
        }
    }

    private static int maxLen(String s, int l, int r) {
        while (l >= 0 && r <= s.length() - 1 && s.charAt(l) == s.charAt(r)) {
            l--;
            r++;
        }
        return r - l - 1;
    }

//    private static boolean palindrome(String s, int start, int end) {
//        while (start <= end) {
//            if (s.charAt(start++) != s.charAt(end--)) return false;
//        }
//        return true;
//    }
}
