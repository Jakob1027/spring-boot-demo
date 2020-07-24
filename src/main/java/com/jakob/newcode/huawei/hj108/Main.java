package com.jakob.newcode.huawei.hj108;

import java.util.Scanner;

/**
 * 题目描述
 * 正整数A和正整数B 的最小公倍数是指 能被A和B整除的最小的正整数值，设计一个算法，求输入A和B的最小公倍数。
 *
 * 输入描述:
 * 输入两个正整数A和B。
 *
 * 输出描述:
 * 输出A和B的最小公倍数。
 */
public class Main {
    public static void main(String[] args) {

        Main main = new Main();
        System.out.println(main.lcm());
    }

    public int lcm() {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        int b = sc.nextInt();
        return a * b / gcb(a, b);
    }

    public int gcb(int a, int b) {
        return b == 0 ? a : gcb(b, a % b);
    }
}
