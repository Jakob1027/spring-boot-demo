package com.jakob.sort;

import java.util.Scanner;

/**
 * @author Jakob
 */
public class Main {
    private static int n;

    public static void main(String[] args) {
        Long[] arr = input();
        Sort.bubbleSort(arr);
        // insertionSort1(arr);
        //shellSort(arr);
        // heapSort(arr);
        // mergeSort(arr);
        output(arr);
    }

    public static Long[] input() {
        Scanner in = new Scanner(System.in);
        n = in.nextInt();
        Long[] arr = new Long[n];
        for (int i = 0; i < n; i++) {
            arr[i] = in.nextLong();
        }
        return arr;
    }

    private static void output(Long[] arr) {
        int i;
        for (i = 0; i < n - 1; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println(arr[i]);
    }
}
