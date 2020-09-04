package com.jakob.newcode.meituan.xiaozhao2020.NO3;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * 打车派单场景, 假定有N个订单， 待分配给N个司机。每个订单在匹配司机前，会对候选司机进行打分，打分的结果保存在N*N的矩阵A， 其中Aij 代表订单i司机j匹配的分值。
 *
 * 假定每个订单只能派给一位司机，司机只能分配到一个订单。求最终的派单结果，使得匹配的订单和司机的分值累加起来最大，并且所有订单得到分配。
 *
 *
 * 输入描述:
 * 第一行包含一个整数N，2≤N≤10。
 *
 * 第二行至第N+1行包含N*N的矩阵。
 *
 *
 * 输出描述:
 * 输出分值累加结果和匹配列表，结果四舍五入保留小数点后两位
 * （注意如果有多组派单方式得到的结果相同，则有限为编号小的司机分配编号小的订单，比如：司机1得到1号单，司机2得到2号单，就比司机1得到2号单，司机2得到1号单要好）
 *
 * 输入例子1:
 * 3
 * 1.08 1.25 1.5
 * 1.5 1.35  1.75
 * 1.22 1.48 2.5
 *
 * 输出例子1:
 * 5.25
 * 1 2
 * 2 1
 * 3 3
 */
public class Main {

    private static double[][] scores;
    private static int[] result;
    private static double max = -1;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] arrange = new int[n];
        result = new int[n];
        scores = new double[n][n];
        List<Integer> drivers = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                scores[i][j] = sc.nextDouble();
            }
        }
        for (int i = 0; i < n; i++) {
            drivers.add(i);
        }
        backtrack(arrange, 0, 0, drivers);
        System.out.println(BigDecimal.valueOf(max).setScale(2, RoundingMode.HALF_UP));
        for (int i = 0; i < n; i++) {
            System.out.println((i + 1) + " " + (result[i] + 1));
        }
    }

    private static void backtrack(int[] arrange, int currentOrder, double currentScore, List<Integer> drivers) {

        if (currentOrder >= arrange.length) {
            if (currentScore > max) {
                max = currentScore;
                result = arrange;
            }
            return;
        }

        for (int i = 0; i < drivers.size(); i++) {
            Integer d = drivers.remove(i);
            arrange[currentOrder] = d;
            backtrack(arrange, currentOrder + 1, currentScore + scores[currentOrder][d], drivers);
            drivers.add(i, d);
        }
    }
}
