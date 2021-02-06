package com.learn.day02;

import java.util.LinkedList;
import java.util.Queue;

public class StackUseQueue {

    public static class Stack2 {
        Queue<Integer> push;
        Queue<Integer> pop;
        Stack2(){
            push = new LinkedList<>();
            pop = new LinkedList<>();
        }
        public void push(Integer date){

            push.offer(date);
        }

        public Integer pop(){
            while (push.size() >1){
                pop.offer(push.poll());
            }
            if (pop.size() <= 0 ){
                return null;
            }
            Queue<Integer> temp = push;
            push = pop;
            pop = temp;
            return pop.poll();
        }
    }


    public static void main(String[] args) {
        Stack2 stack2 = new Stack2();
        stack2.push(1);
        stack2.push(2);
        stack2.push(3);
        stack2.push(4);
        stack2.push(5);
        stack2.push(6);
        System.out.println(stack2.pop());
        System.out.println(stack2.pop());
        System.out.println(stack2.pop());
        stack2.push(13);
        stack2.push(14);
        stack2.push(15);
        stack2.push(16);
        System.out.println(stack2.pop());
        System.out.println(stack2.pop());
        System.out.println(stack2.pop());
    }
}
