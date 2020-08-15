package com.jakob.newcode.wangyi.huyu3;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int count = sc.nextInt();
        for (int i = 0; i < count; i++) {
            int m = sc.nextInt();
            int t = sc.nextInt();
            int m1 = sc.nextInt();
            int t1 = sc.nextInt();
            int m2 = sc.nextInt();
            int t2 = sc.nextInt();
            int ans = 0;
            for (int j = 0; j < t; j++) {
                int p1 = (j / t1) % 2 == 0 ? 1 : 0;
                int p2 = (j / t2) % 2 == 0 ? 1 : 0;
                ans = ans + m1 * p1 - m2 * p2;
                if (ans < 0) ans = 0;
                if (ans > m) ans = m;
            }
            System.out.println(ans);
        }

    }
}
