package com.jakob.newcode.huawei.hj101;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            int count = sc.nextInt();
            int[] nums = new int[count];
            for (int i = 0; i < count; i++) {
                nums[i] = sc.nextInt();
            }
            int order = sc.nextInt();
            Arrays.sort(nums);
            if (order == 1) {
                for (int i = count - 1; i >= 0; i--) {
                    System.out.print(nums[i] + " ");
                }
            } else {
                for (int i = 0; i < count; i++) {
                    System.out.print(nums[i] + " ");
                }
            }
            System.out.println();
        }
    }
}
