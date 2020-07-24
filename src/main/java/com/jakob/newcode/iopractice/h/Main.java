package com.jakob.newcode.iopractice.h;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String[] result = sc.nextLine().trim().split(",");
            Arrays.sort(result);
            String res = String.join(",", result);
            System.out.println(res);
        }
    }
}
