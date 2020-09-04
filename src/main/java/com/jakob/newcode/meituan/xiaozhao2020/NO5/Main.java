package com.jakob.newcode.meituan.xiaozhao2020.NO5;

import java.util.Scanner;

/**
 * 已知一种新的火星文的单词由英文字母（仅小写字母）组成，但是此火星文中的字母先后顺序未知。给出一组非空的火星文单词，且此组单词已经按火星文字典序进行好了排序（从小到大），请推断出此火星文中的字母先后顺序。
 *
 *
 * 输入描述:
 * 一行文本，为一组按火星文字典序排序好的单词(单词两端无引号)，单词之间通过空格隔开
 *
 *
 * 输出描述:
 * 按火星文字母顺序输出出现过的字母，字母之间无其他字符，如果无法确定顺序或者无合理的字母排序可能，请输出"invalid"(无需引号)
 *
 *
 *
 *
 * 输入例子1:
 * z x
 *
 * 输出例子1:
 * zx
 *
 * 输入例子2:
 * wrt wrf er ett rftt
 *
 * 输出例子2:
 * wertf
 *
 * 输入例子3:
 * z x z
 *
 * 输出例子3:
 * invalid
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        String[] words = input.split(" ");

    }
}
