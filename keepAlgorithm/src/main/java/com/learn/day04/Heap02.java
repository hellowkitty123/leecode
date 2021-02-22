package com.learn.day04;

import com.mj.example.BinarySearchTree;
import com.mj.printer.BinaryTreeInfo;
import com.mj.printer.BinaryTrees;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Heap02 {
    public static class Student{
        int age;
        String name;
        int score;
        Student(int age , String name, int score){
            this.age = age;
            this.name = name;
            this.score = score;
        }

        @Override
        public String toString() {
            return "Student{" +
                    "age=" + age +
                    ", name='" + name + '\'' +
                    ", score=" + score +
                    '}';
        }
    }
    //大根堆
    public  static class MyHeap<T> implements BinaryTreeInfo {
        ArrayList<T> heap;
        int heapSize;
        HashMap< T,Integer> heapMap;
        Comparator<? super T> comparator;
        MyHeap(Comparator<? super T> com){
            heap = new ArrayList<>();
            heapSize = 0;
            heapMap = new HashMap<>();
            comparator = com;
        }

        private void heapInsert(int index){
            int paretindex = (index-1)/2;
            // 大根堆 ，孩子小于父亲
            while (index >0 && comparator.compare(heap.get(index),heap.get(paretindex)) > 0){
                swap(this.heap,this.heap.get(index),this.heap.get(paretindex));
                index = paretindex;
                paretindex = (index-1)/2;
            }
        }

        private void heapifiy(int index){
            int leftIndex = index * 2 +1;
            while (leftIndex <=this.heapSize-1){
                T parent = this.heap.get(index);
                int largest = comparator.compare(this.heap.get(leftIndex),parent) > 0 ? leftIndex : index;
                largest = leftIndex+1 <= this.heapSize-1
                        &&  comparator.compare(this.heap.get(leftIndex+1),this.heap.get(largest)) > 0 ? leftIndex+1:largest ;
                if (this.heap.get(largest) == parent){
                    break;
                }
                index = largest;    //记住移动坐标
                swap(this.heap,this.heap.get(largest),parent);
                leftIndex = index * 2 +1;  //记住更新 leftindex


            }
        }

        private void swap(ArrayList<T> heap,T data1,T data2){
            int idx1 = this.heapMap.get(data1);
            int idx2 = this.heapMap.get(data2);

            heap.set(idx1,data2);
            heap.set(idx2,data1);
            this.heapMap.put(data1,idx2);
            this.heapMap.put(data2,idx1);

        }


        public T pop(){
            if (this.heapSize == 0){
                throw new RuntimeException("heap 已经是空！");
            }
            T top = heap.get(0);
            swap(this.heap,top,heap.get(--this.heapSize));
            heapifiy(0);
            return top;
        }

        public void push(T data){
            if (this.heapMap.containsKey(data)){
                this.heap.set(this.heapSize++,data);
            }else{
                this.heap.add(data);
                this.heapSize++;
            }
            this.heapMap.put(data,this.heapSize-1);
            heapInsert(this.heapSize-1);
        }

        public void resign(T data){
            int idx = heapMap.get(data);
            heapInsert(idx);
            heapifiy(idx);
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
            return this.heap.get((int) node).toString();
        }
    }


    public static void main(String[] args) {
        MyHeap<Student> heap = new MyHeap<>(new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return o1.age - o2.age;
            }
        });
        heap.push(new Student(11,"aaa",20));
        heap.push(new Student(1,"aaa",20));
        heap.push(new Student(4,"aaa",20));
        heap.push(new Student(6,"aaa",20));
        heap.push(new Student(7,"aaa",20));
        Student t = new Student(2,"aaa",20);
        heap.push(t);
        heap.push(new Student(10,"aaa",20));
        heap.push(new Student(3,"aaa",20));
        heap.push(new Student(9,"aaa",20));
        heap.push(new Student(5,"aaa",20));
        heap.pop();
        heap.pop();
        heap.pop();
        t.age = 5;
        heap.resign(t);

        BinaryTrees.println(heap);
    }
}
