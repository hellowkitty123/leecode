package com.learn.day04;

import com.mj.printer.BinaryTreeInfo;
import com.mj.printer.BinaryTrees;

import java.sql.Array;
import java.util.Arrays;

/*

 */
public class HeapSort  {
    //堆排序
    public static class HeapSort2 implements BinaryTreeInfo{
        int[] heap;
        int heapSize = 0;
        HeapSort2(int size){
            heap = new int[size];
        }
        // 从哪个位置开始heapInsert 往上看
        private void heapInsert(int index){
            //1、index 为根节点，停
            //2、index 比父节点小，停
            while (this.heap[index] > this.heap[(index-1) /2]){
                swap(index,(index-1) >>1);    // 跟父节点交换
                index = (index-1) >> 1;         // 下标指向父节点
            }
        }
        // 从哪个位置往下看
        private void heapify(int index){
            // 堆是完全二叉树，如果左孩子不存在，有孩子就不存在
            while ((index << 1 | 1) < this.heapSize){
                int leftIndex = (index << 1 | 1);
                int rightIndex = ((index << 1) + 2);
                int largest = rightIndex < this.heapSize
                        && this.heap[index] < this.heap[rightIndex] ? rightIndex : index;
                largest = this.heap[largest] < this.heap[leftIndex] ? leftIndex : largest;
                if (largest == index){
                    break;
                }
                swap(largest,index);
                index = largest;
            }
        }


        private void swap (int i,int j){
            int temp = this.heap[i];
            this.heap[i] = this.heap[j];
            this.heap[j] = temp;
        }
        // 从数组下表0位置弹出数值 向下做heapify
        public int pop(){
            if (this.heapSize == 0 ){
                throw new RuntimeException("对内没有数据");
            }
            int result = this.heap[0];
            this.swap(0,--this.heapSize);
            this.heapify(0);
            return result;
        }

        // 往数组尾部推入数据
        public void push(int date){
            if (heapSize >= this.heap.length){
                System.out.println("堆大小已满！");
                return ;
            }
            this.heap[heapSize++] = date;
            heapInsert(heapSize-1);
        }

        @Override
        public Object root() {
            return 0;
        }

        @Override
        public Object left(Object node) {
            int index = ((int)node << 1) + 1;
            return index >= this.heapSize ? null : index;
        }

        @Override
        public Object right(Object node) {
            int index = ((int)node << 1) + 2;
            return index >= this.heapSize ? null : index;
        }

        @Override
        public Object string(Object node) {
            return this.heap[(int)node];
        }
    }

    public static void main(String[] args) {
        int[] arr = new int[]{ 2,3,24,6,23,5,21,14,7,2};

        HeapSort2 heap = new HeapSort2(100);
        for (int i=0;i<arr.length;i++){
            heap.push(arr[i]);
//            System.out.println(Arrays.toString(heap.heap));
        }


        for (int i=0;i<arr.length;i++){
            System.out.println(heap.pop());
            System.out.println("---------------");
            BinaryTrees.println(heap);
        }
    }
}
