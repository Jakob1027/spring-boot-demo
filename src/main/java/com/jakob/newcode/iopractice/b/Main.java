package com.jakob.newcode.iopractice.b;

import java.util.Scanner;

/**
 * 链接：https://ac.nowcoder.com/acm/contest/5646/B
 * 来源：牛客网
 *
 * 题目描述
 * 计算a+b
 *
 * 输入描述:
 * 输入第一行包括一个数据组数t(1 <= t <= 100)
 * 接下来每行包括两个正整数a,b(1 <= a, b <= 10^9)
 * 输出描述:
 * 输出a+b的结果
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int count = sc.nextInt();
        for (int i = 0; i < count; i++) {
            int a = sc.nextInt();
            int b = sc.nextInt();
            System.out.println(a + b);
        }
    }

}
