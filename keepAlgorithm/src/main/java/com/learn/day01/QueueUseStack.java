package com.learn.day01;

import java.util.Stack;

public class QueueUseStack {
    public static class Queue2 {
        Stack<Integer> push;
        Stack<Integer> pop;
        Queue2(){
            push = new Stack<Integer>();
            pop = new Stack<Integer>();
        }

        public void push(Integer date){
            while (!pop.empty()){
                push.push(pop.pop());
            }
            push.push(date);
        }

        public Integer pop(){
            while (!push.empty()){
                pop.push(push.pop());
            }
            return pop.pop();
        }
    }
    public static void main(String[] args) {
        Queue2 stack = new Queue2();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        stack.push(11);
        stack.push(12);
        stack.push(13);
        stack.push(14);
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
    }
}
