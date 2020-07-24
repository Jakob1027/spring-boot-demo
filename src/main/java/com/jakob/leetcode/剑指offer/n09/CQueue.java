package com.jakob.leetcode.剑指offer.n09;

import java.util.Stack;

/**
 * 用两个栈实现一个队列。队列的声明如下，请实现它的两个函数 appendTail 和 deleteHead ，分别完成在队列尾部插入整数和在队列头部删除整数的功能。(若队列中没有元素，deleteHead?操作返回 -1 )
 * <p>
 * ?
 * <p>
 * 示例 1：
 * <p>
 * 输入：
 * ["CQueue","appendTail","deleteHead","deleteHead"]
 * [[],[3],[],[]]
 * 输出：[null,null,3,-1]
 * 示例 2：
 * <p>
 * 输入：
 * ["CQueue","deleteHead","appendTail","appendTail","deleteHead","deleteHead"]
 * [[],[],[5],[2],[],[]]
 * 输出：[null,-1,null,null,5,2]
 * 提示：
 * <p>
 * 1 <= values <= 10000
 * 最多会对?appendTail、deleteHead 进行?10000?次调用
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/yong-liang-ge-zhan-shi-xian-dui-lie-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class CQueue {

    private Stack<Integer> stack;
    private Stack<Integer> temp;

    public CQueue() {
        stack = new Stack<>();
        temp = new Stack<>();
    }

    public void appendTail(int value) {
        while (!stack.isEmpty()) {
            temp.push(stack.pop());
        }
        stack.push(value);
        while (!temp.isEmpty()) {
            stack.push(temp.pop());
        }
    }

    public int deleteHead() {
        return stack.isEmpty() ? -1 : stack.pop();
    }
}
