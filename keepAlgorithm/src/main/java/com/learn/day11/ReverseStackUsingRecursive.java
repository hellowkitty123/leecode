package com.learn.day11;

import java.util.Stack;

/**
 * 给你一个栈，请你逆序这个栈，
 * 不能申请额外的数据结构，
 * 只能使用递归函数。 如何实现?
 */
public class ReverseStackUsingRecursive {

    //反转 stack
    public static void reverse(Stack<Integer> stack){
        if (stack.isEmpty()){
            return;
        }
        int i = func(stack);
        // 规模缩小之后的子问题
        // 反转剩余的stack
        reverse(stack);
        // 底部压回到stack
        stack.push(i);
    }
    // 剔除最底部的值，并返回
    public static Integer func(Stack<Integer> stack){
        Integer i = stack.pop();
        // 如果是最后一个，直接返回
        if (stack.isEmpty()){
            return i;
        }else {
            // 如果i 不是最后一个， last 是最后一个，把i压回到栈里
            Integer last = func(stack);
            stack.push(i);
            return last;
        }
    }


    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(5);

        reverse(stack);
        while (!stack.isEmpty()) {
            System.out.println(stack.pop());
        }
    }
}
