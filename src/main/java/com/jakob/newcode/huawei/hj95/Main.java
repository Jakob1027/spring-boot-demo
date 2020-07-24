package com.jakob.newcode.huawei.hj95;

import java.util.Scanner;

/**
 * 题目描述
 * 考试题目和要点：
 *
 * 1、中文大写金额数字前应标明“人民币”字样。中文大写金额数字应用壹、贰、叁、肆、伍、陆、柒、捌、玖、拾、佰、仟、万、亿、元、角、分、零、整等字样填写。（30分）
 *
 * 2、中文大写金额数字到“元”为止的，在“元”之后，应写“整字，如￥ 532.00应写成“人民币伍佰叁拾贰元整”。在”角“和”分“后面不写”整字。（30分）
 *
 * 3、阿拉伯数字中间有“0”时，中文大写要写“零”字，阿拉伯数字中间连续有几个“0”时，中文大写金额中间只写一个“零”字，如￥6007.14，应写成“人民币陆仟零柒元壹角肆分“。（
 *
 *
 * 输入描述:
 * 输入一个double数
 *
 * 输出描述:
 * 输出人民币格式
 *
 * 示例1
 * 输入
 * 151121.15
 * 输出
 * 人民币拾伍万壹仟壹佰贰拾壹元壹角伍分
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            double num = sc.nextDouble();
            System.out.println(read(num));
        }
    }

    private static String read(double num) {
        String[] name = new String[]{"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
        String[] unit1 = new String[]{"", "拾", "佰", "仟"};
        String[] unit2 = new String[]{"", "万", "亿"};
        long i = (long) num;
        int d = (int) ((num - i) * 100);
        StringBuilder res = new StringBuilder("元");
        int count = 0;
        while (i > 0) {
            int digit = (int) (i % 10);
            res.insert(0, name[digit] + unit1[count % 4] + unit2[count / 4]);
            i /= 10;
            count++;
        }
        if (d == 0) {
            res.append("整");
        } else {
            res.append(name[d / 10]).append("角").append(name[d % 10]).append("分");
        }
        return res.toString();
    }
}
