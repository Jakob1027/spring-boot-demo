package com.jakob.newcode.meituan.xiaozhao2020.NO1;

import java.util.Scanner;
import java.util.Stack;

/**
 * 给出一个布尔表达式的字符串，比如：true or false and false，表达式只包含true，false，and和or，
 * 现在要对这个表达式进行布尔求值，计算结果为真时输出true、为假时输出false，
 * 不合法的表达时输出error（比如：true true）。
 * 表达式求值是注意and 的优先级比 or 要高，比如：true or false and false，等价于 true or (false and false)，计算结果是 true。
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String expr = sc.nextLine();
        String[] elements = expr.split(" ");
        Stack<String> stack = new Stack<>();
        for (String e : elements) {
            if (e.equals("true") || e.equals("false")) {
                if (stack.isEmpty()) {
                    stack.push(e);
                } else {
                    String top = stack.peek();
                    if (top.equals("true") || top.equals("false")) {
                        System.out.println("error");
                        return;
                    }
                    if (top.equals("and")) {
                        stack.pop();
                        String pre = stack.pop();
                        if (pre.equals("true") && e.equals("true")) stack.push("true");
                        else stack.push("false");
                    } else {
                        stack.push(e);
                    }
                }
            } else {
                if (stack.isEmpty() || stack.peek().equals("and") || stack.peek().equals("or")) {
                    System.out.println("error");
                    return;
                }
                stack.push(e);
            }
        }

        if (!stack.isEmpty() && (stack.peek().equals("or") || stack.peek().equals("and"))) {
            System.out.println("error");
            return;
        }
        while (!stack.isEmpty()) {
            String value = stack.pop();
            if (value.equals("true")) {
                System.out.println("true");
                return;
            }
        }
        System.out.println("false");
    }
}
