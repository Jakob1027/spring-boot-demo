package com.jakob.newcode.huawei.hj81;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String sh = sc.nextLine();
            String lo = sc.nextLine();
            boolean flag = true;
            int[] chars = new int[128];
            for (char c : lo.toCharArray()) {
                chars[c] = 1;
            }
            for (char c : sh.toCharArray()) {
                if (chars[c] != 1) {
                    flag = false;
                    break;
                }
            }
//            for (int i = 0; i < sh.length(); i++) {
//                if (!lo.contains(sh.substring(i, i + 1))) {
//                    flag = false;
//                    break;
//                }
//            }
            System.out.println(flag);
        }
    }
}
