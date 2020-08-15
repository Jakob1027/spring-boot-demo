package com.jakob.newcode.wangyi.huyu1;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * 小A刚学了二进制，他十分激动。为了确定他的确掌握了二进制，你给他出了这样一道题目：给定N个非负整数，将这N个数字按照二进制下1的个数分类，二进制下1的个数相同的数字属于同一类。求最后一共有几类数字？
 *
 *
 * 输入描述:
 * 输入的第一行是一个正整数T（0<T<=10），表示样例个数。对于每一个样例，第一行是一个正整数N（0<N<=100），表示有多少个数字。接下来一行是N个由空格分隔的非负整数，大小不超过2^31 – 1。，
 *
 * 输出描述:
 * 对于每一组样例，输出一个正整数，表示输入的数字一共有几类。
 *
 * 输入例子1:
 * 1
 * 5
 * 8 3 5 7 2
 *
 * 输出例子1:
 * 3
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int cases = sc.nextInt();
        for (int i = 0; i < cases; i++) {
            int count = sc.nextInt();
            Set<Integer> set = new HashSet<>();
            for (int j = 0; j < count; j++) {
                int n = sc.nextInt();
                set.add(countOne(n));
            }
            System.out.println(set.size());
            set.clear();
        }
    }

    private static int countOne(int num) {
        int count = 0;
        while (num > 0) {
            if (num % 2 == 1) count++;
            num /= 2;
        }
        return count;
    }
}
