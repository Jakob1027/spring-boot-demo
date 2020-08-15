package com.jakob.newcode.wangyi.huyu4;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int count = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < count; i++) {
            String s = sc.nextLine();
            List<Integer> list = new ArrayList<>();
            for (int j = 0; j < s.length(); j++) {
                if (s.charAt(j) != 'N') list.add(j);
            }
            if (list.size() <= 2) {
                System.out.println(s.length());
            } else {
                int max = list.get(2);
                list.add(s.length());
                for (int j = 1; j < list.size() - 2; j++) {
                    max = Math.max(list.get(j + 2) - list.get(j - 1) - 1, max);
                }
                System.out.println(max);
            }
            list.clear();
        }
    }
}
