package com.jakob.sort;

/**
 * @author Jakob
 */
public class Sort {

    private static final int CUTOFF = 10;

    /**
     * 插入排序
     *
     * @param a
     * @param <T>
     */
    public static <T extends Comparable<? super T>> void insertionSort1(T[] a) {
        insertionSort1(a, 0, a.length - 1);
    }

    /**
     * 冒泡排序
     *
     * @param a
     * @param <T>
     */
    public static <T extends Comparable<? super T>> void bubbleSort(T[] a) {
        for (int i = 0; i < a.length; i++) {
            boolean flag = true;
            for (int j = 0; j < a.length - i - 1; j++) {
                if (a[j].compareTo(a[j + 1]) > 0) {
                    swap(a, j, j + 1);
                    flag = false;
                }
            }
            if (flag) {
                break;
            }
        }
    }

    /**
     * 希尔排序，用希尔增量
     *
     * @param a
     * @param <T>
     */
    public static <T extends Comparable<? super T>> void shellSort(T[] a) {
        int j;
        for (int gap = a.length / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < a.length; i++) {
                T temp = a[i];
                for (j = i; j >= gap && temp.compareTo(a[j - gap]) < 0; j -= gap) {
                    a[j] = a[j - gap];
                }
                a[j] = temp;
            }
        }
    }

    /**
     * 堆排序
     *
     * @param a
     * @param <T>
     */
    public static <T extends Comparable<? super T>> void heapSort(T[] a) {
        //创建最大堆
        for (int i = a.length/2-1; i >= 0; i--) {
            percDown(i, a.length, a);
        }
        //将最大元素放到最后
        for (int i = a.length - 1; i > 0; i--) {
            swap(a, 0, i);
            percDown(0, i, a);
        }
    }

    /**
     * 归并排序的驱动程序
     *
     * @param a
     * @param <T>
     */
    public static <T extends Comparable<? super T>> void mergeSort(T[] a) {
        T[] temp = (T[]) new Comparable[a.length];
        mergeSort(a, temp, 0, a.length - 1);
    }


    /**
     * 快速排序的驱动程序
     *
     * @param a
     * @param <T>
     */
    public static <T extends Comparable<? super T>> void quickSort(T[] a) {
        quickSort(a, 0, a.length - 1);
    }


    /**
     * 归并排序的递归方法
     *
     * @param a
     * @param temp
     * @param left
     * @param right
     * @param <T>
     */
    private static <T extends Comparable<? super T>> void mergeSort(T[] a, T[] temp, int left, int right) {
        if (left < right) {
            int center = (left + right) / 2;
            mergeSort(a, temp, left, center);
            mergeSort(a, temp, center + 1, right);
            merge(a, temp, left, right);
        }
    }

    /**
     * 归并方法
     *
     * @param a
     * @param temp
     * @param left
     * @param right
     * @param <T>
     */
    private static <T extends Comparable<? super T>> void merge(T[] a, T[] temp, int left, int right) {
        int center = (left + right) / 2;
        int lp = left, rp = center + 1, tp = left;
        while (lp <= center && rp <= right) {
            if (a[lp].compareTo(a[rp]) <= 0) {
                temp[tp++] = a[lp++];
            } else {
                temp[tp++] = a[rp++];
            }
        }
        while (lp <= center) {
            temp[tp++] = a[lp++];
        }
        while (rp <= right) {
            temp[tp++] = a[rp++];
        }
        for (int i = left; i <= right; i++) {
            a[i] = temp[i];
        }
    }

    /**
     * 最大堆下虑程序
     *
     * @param root
     * @param a
     * @param <T>
     */
    private static <T extends Comparable<? super T>> void percDown(int root, int n, T[] a) {
        T temp = a[root];
        for (int child; getLeftChild(root) < n; root = child) {
            child = getLeftChild(root);
            if (child != n - 1 && a[child].compareTo(a[child + 1]) < 0) {
                child++;
            }
            if (a[child].compareTo(a[root]) > 0) {
                a[root] = a[child];
            } else {
                break;
            }
        }
        a[root] = temp;
    }

    private static int getLeftChild(int parent) {
        return parent * 2 + 1;
    }

    /**
     * 三数中值分割
     *
     * @param a
     * @param left
     * @param right
     * @param <T>
     * @return
     */
    private static <T extends Comparable<? super T>> T median3(T[] a, int left, int right) {
        int center = (left + right) / 2;
        if (a[left].compareTo(a[center]) > 0) {
            swap(a, left, center);
        }
        if (a[left].compareTo(a[right]) > 0) {
            swap(a, left, right);
        }
        if (a[center].compareTo(a[right]) > 0) {
            swap(a, center, right);
        }
        swap(a, center, right - 1);
        return a[right - 1];
    }


    /**
     * 快速排序的递归调用程序
     *
     * @param a
     * @param left
     * @param right
     * @param <T>
     */
    private static <T extends Comparable<? super T>> void quickSort(T[] a, int left, int right) {
        if (right - left >= CUTOFF) {
            T pivot = median3(a, left, right);
            int i = left, j = right - 1;
            for (; ; ) {
                while (a[++i].compareTo(pivot) < 0) {
                }
                while (a[--j].compareTo(pivot) > 0) {
                }
                if (i < j) {
                    swap(a, i, j);
                } else {
                    break;
                }
            }
            swap(a, i, right - 1);

            quickSort(a, left, i - 1);
            quickSort(a, i + 1, right);

        } else {
            insertionSort1(a, left, right);
        }
    }

    /**
     * 插入排序的内部实现
     *
     * @param a
     * @param left
     * @param right
     * @param <T>
     */
    private static <T extends Comparable<? super T>> void insertionSort1(T[] a, int left, int right) {
        int j;
        for (int i = left + 1; i <= right; i++) {
            T temp = a[i];
            for (j = i; j > left && a[j - 1].compareTo(temp) > 0; j--) {
                a[j] = a[j - 1];
            }
            a[j] = temp;
        }
    }

    /**
     * 交换数组元素
     *
     * @param a
     * @param i
     * @param j
     * @param <T>
     */
    private static <T extends Comparable<? super T>> void swap(T[] a, int i, int j) {
        T temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }


}
