package com.jakob.newcode.huawei.hj107;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

/**
 * 题目描述
 * •计算一个数字的立方根，不使用库函数
 *
 * 详细描述：
 *
 * •接口说明
 *
 * 原型：
 *
 * public static double getCubeRoot(double input)
 *
 * 输入:double 待求解参数
 *
 * 返回值:double  输入参数的立方根，保留一位小数
 *
 *
 * 输入描述:
 * 待求解参数 double类型
 *
 * 输出描述:
 * 输入参数的立方根 也是double类型
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double v = sc.nextDouble();
        double result = getCubeRoot(v);
        BigDecimal decimal = BigDecimal.valueOf(result).setScale(1, RoundingMode.HALF_UP);
        System.out.println(decimal.toString());
    }

    public static double getCubeRoot(double input) {
        double min = 0;
        double max = input;
        double mid;
        while (max - min > 0.001) {
            mid = (max + min) / 2;
            double cube = mid * mid * mid;
            if (cube < input) {
                min = mid;
            } else if (cube > input) {
                max = mid;
            } else {
                return mid;
            }
        }
        return max;
    }
}
