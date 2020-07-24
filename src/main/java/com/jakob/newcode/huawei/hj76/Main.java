package com.jakob.newcode.huawei.hj76;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            int n = sc.nextInt();
            int start = n * n - n + 1;
            for (int i = 0; i < n - 1; i++) {
                System.out.print(start + "+");
                start += 2;
            }
            System.out.println(start);
        }
    }
}
