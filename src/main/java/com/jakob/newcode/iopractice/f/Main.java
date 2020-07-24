package com.jakob.newcode.iopractice.f;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] nums = line.trim().split(" ");
            int sum = 0;
            for (String s : nums) {
                sum += Integer.parseInt(s);
            }
            System.out.println(sum);
        }
    }
}
