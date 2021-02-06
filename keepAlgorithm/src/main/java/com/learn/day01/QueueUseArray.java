package com.learn.day01;

public class QueueUseArray {
    // 用固定长度数组实现队列
    public static class Queue{
        int size;
        int pushIndex;
        int popIndex;
        int[] heap;
        Queue(int cap){
            heap = new int[cap];
            size = 0;
            pushIndex = 0;
            popIndex = 0;
        }

        public void push(int date){
            if (this.size >= heap.length){
                System.out.println("队列已满，无法插入");
                return;
            }
            this.pushIndex = getIndex(this.pushIndex);
            this.size++;
            heap[this.pushIndex] = date;
            this.pushIndex++;
        }

        public int pop(){
            if (size == 0){
                System.out.println("队列中没有数据");
                return -1;
            }
            this.popIndex = getIndex(this.popIndex);
            this.size--;
            return heap[this.popIndex++];
        }

        public int getIndex(int index){
            return index == heap.length ? 0 : index;
        }
    }




    public static void main(String[] args) {

        Queue queue = new Queue(7);
        queue.push(1);
        queue.push(2);
        queue.push(3);
        queue.push(4);
        queue.push(5);
        queue.push(6);
        queue.push(7);
        System.out.println(queue.pop());
        System.out.println(queue.pop());
        System.out.println(queue.pop());
        queue.push(8);
        queue.push(9);
        queue.push(10);
        System.out.println(queue.pop());
        System.out.println(queue.pop());
        System.out.println(queue.pop());
        queue.push(18);
        queue.push(19);
        queue.push(110);
        System.out.println(queue.pop());
        System.out.println(queue.pop());
        System.out.println(queue.pop());
        queue.push(18);
        queue.push(19);
        queue.push(110);
        System.out.println(queue.pop());
        System.out.println(queue.pop());
        System.out.println(queue.pop());
        queue.push(18);
        queue.push(19);
        queue.push(110);
    }
}
