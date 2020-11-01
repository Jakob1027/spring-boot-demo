package com.jakob.springbootdemo;

import java.util.Arrays;

public class Sort {
    public static void main(String[] args) {
        int[] arr = {1, 3, 5, 3, 1};
//        bubble(arr);
//        insertion(arr);
        quick(arr, 0, 4);
        System.out.println(Arrays.toString(arr));
    }

    public static void bubble(int[] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            boolean flag = true;
            for (int j = 0; j < i; j++) {
                if (arr[j] > arr[j + 1]) {
                    flag = false;
                    swap(arr, j, j + 1);
                }
            }
            if (flag) break;
        }
    }

    public static void insertion(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int temp = arr[i];
            int j;
            for (j = i - 1; j >= 0 && arr[j] > temp; j--) {
                arr[j + 1] = arr[j];
            }
            arr[j + 1] = temp;
        }
    }

    public static void quick(int[] arr, int left, int right) {
        if (left < right) {
            int i = left, j = right, pivot = arr[i];
            while (i < j) {
                while (i < j && arr[j] >= pivot) j--;
                if (i < j) arr[i++] = arr[j];
                while (i < j && arr[i] <= pivot) i++;
                if (i < j) arr[j--] = arr[i];
            }
            arr[i] = pivot;
            quick(arr, left, i - 1);
            quick(arr, i + 1, right);
        }
    }

    private static void median(int[] arr, int left, int right) {
        int center = (left + right) / 2;
        if (arr[center] < arr[left]) swap(arr, left, center);
        if (arr[right] < arr[left]) swap(arr, left, right);
        if (arr[center] > arr[right]) swap(arr, center, right);
        swap(arr, center, right - 1);

    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
