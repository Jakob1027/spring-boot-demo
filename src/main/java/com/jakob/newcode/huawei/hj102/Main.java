package com.jakob.newcode.huawei.hj102;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        HashMap<Character, Integer> dict = new HashMap<>();
//        TreeMap<Integer, List<Character>> map = new TreeMap<>(Comparator.reverseOrder());
//        while (sc.hasNextLine()) {
//            dict.clear();
//            map.clear();
//            String s = sc.nextLine();
//            for (char c : s.toCharArray()) {
//                if (Character.isLetterOrDigit(c) || c == ' ') {
//                    Integer count = dict.getOrDefault(c, 0);
//                    dict.put(c, count + 1);
//                }
//            }
//            for (Map.Entry<Character, Integer> e : dict.entrySet()) {
//                List<Character> l = map.computeIfAbsent(e.getValue(), t -> new ArrayList<>());
//                l.add(e.getKey());
//            }
//            StringBuilder sb = new StringBuilder();
//            for (Map.Entry<Integer, List<Character>> e : map.entrySet()) {
//                List<Character> list = e.getValue();
//                Collections.sort(list);
//                list.forEach(sb::append);
//            }
//            System.out.println(sb.toString());
//        }

        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            try {
                count(line);
            } catch (Exception e) {

            }
        }
    }

    private static void count(String line) {
        int[] chars = new int[128];
        for (char c : line.toCharArray()) {
            if (Character.isLetterOrDigit(c) || c == ' ') {
                chars[(int) c]++;
            }
        }
        int max = 0;
        for (int i = 0; i < 128; i++) {
            max = Math.max(chars[i], max);
        }
        StringBuilder sb = new StringBuilder();
        while (max > 0) {
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == max) {
                    sb.append((char) i);
                }
            }
            max--;
        }
        System.out.println(sb.toString());
    }
}
