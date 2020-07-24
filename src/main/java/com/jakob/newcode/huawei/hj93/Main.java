package com.jakob.newcode.huawei.hj93;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

//    public Main() {
//
//    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            int count = sc.nextInt();
            List<Integer> ng = new ArrayList<>();
            int sum = 0;
            int fs = 0;
            for (int i = 0; i < count; i++) {
                int n = sc.nextInt();
                sum += n;
                if (n % 5 == 0) {
                    fs += n;
                } else if (n % 3 != 0) ng.add(n);
            }
            if (sum % 2 != 0) {
                System.out.println(false);
            } else {
                int target = sum / 2 - fs;
                boolean res = pick(ng, target);
                System.out.println(res);
            }
        }
    }

    static boolean pick(List<Integer> list, int target) {
        if (target == 0) return true;
        for (int i = 0; i < list.size(); i++) {
            int n = list.get(i);
            list.remove(i);
            if (pick(list, target - n)) return true;
            list.add(i, n);
        }
        return false;
    }
}
