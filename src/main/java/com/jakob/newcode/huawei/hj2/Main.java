package com.jakob.newcode.huawei.hj2;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine().toLowerCase();
        char ch = Character.toLowerCase(sc.nextLine().charAt(0));
        int count = 0;
        for (char c : str.toCharArray()) {
            if (c == ch) count++;
        }
        System.out.println(count);
    }
}
