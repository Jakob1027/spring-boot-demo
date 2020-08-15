package com.jakob.newcode.wangyi.huyu2;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int eleCount = sc.nextInt();
        int[] elements = new int[eleCount];
        for (int i = 0; i < eleCount; i++) {
            elements[i] = sc.nextInt();
        }
        int floodCount = sc.nextInt();
        Map<Integer, Integer> cache = new HashMap<>();
        for (int i = 0; i < floodCount; i++) {
            int flood = sc.nextInt();
            if (cache.get(flood) != null) {
                System.out.println(cache.get(flood));
                continue;
            }
            int count = elements[0] > flood ? 1 : 0;
            for (int j = 1; j < eleCount; j++) {
                if (elements[j] > flood && elements[j - 1] <= flood) count++;
            }
            cache.put(flood, count);
            System.out.println(count);
        }
    }
}
