package com.jakob.sort;


import org.junit.Test;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Random;

/**
 * @author Jakob
 */
public class SortTest {

    private static final int N = 10000000;

    private Integer[] b = {1};

    public static void main(String[] args) {

        Integer[] a = array(N);
        /*try {
            Class clazz = Class.forName("com.jakob.sort.Sort");
            clazz.getMethod()
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/
        try {
            test(a,Sort.class.getMethod("insertionSort", new Class[]{Array.class}));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试插入排序
     */
    @Test
    public void insertionTest() {
        Sort.insertionSort1(b);
        System.out.println(Arrays.toString(b));

    }

    /**
     * 测试冒泡排序
     */
    @Test
    public void bubbleTest() {
        Sort.bubbleSort(b);
        System.out.println(Arrays.toString(b));
    }

    /**
     * 测试希尔排序
     */
    @Test
    public void shellTest() {
        Sort.shellSort(b);
        System.out.println(Arrays.toString(b));
    }

    /**
     * 测试堆排序
     */
    @Test
    public void heapTest() {
        Sort.heapSort(b);
        System.out.println(Arrays.toString(b));
    }

    /**
     * 测试归并排序
     */
    @Test
    public void mergeTest() {
        Sort.mergeSort(b);
        System.out.println(Arrays.toString(b));
    }

    /**
     * 测试快速排序
     */
    @Test
    public void quickTest() {
        Sort.quickSort(b);
        System.out.println(Arrays.toString(b));
    }

    private static Integer[] array(int n) {
        Integer[] a = new Integer[n];
        for (int i = 0; i < n; i++) {
            a[i] = new Random().nextInt(n);
        }
        return a;
    }

    private static void test(Integer[] arr, Method method) {
        Integer[] a = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            a[i] = arr[i];
        }
        long st = System.currentTimeMillis();
        try {
            method.invoke(null,a);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        long en = System.currentTimeMillis();
        System.out.println(Arrays.toString(a));
        System.out.println("运行时间: " + (en - st) + "ms");
    }

}
