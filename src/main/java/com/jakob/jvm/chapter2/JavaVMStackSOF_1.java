package com.jakob.jvm.chapter2;

/**
 * -Xss180k
 */
public class JavaVMStackSOF_1 {

    private int stackLength = 0;

    private void stackLeak() {
        stackLength++;
        stackLeak();
    }

    public static void main(String[] args) {
        JavaVMStackSOF_1 instance = new JavaVMStackSOF_1();
        try {
            instance.stackLeak();
        } catch (Throwable e) {
            System.out.println("栈深度： " + instance.stackLength);
            throw e;
        }
    }
}
