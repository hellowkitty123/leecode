package com.learn.day04;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/*
已知一个几乎有序的数组。几乎有序是指，如果把数组排好顺序的话，每个元素移动的距离一定不超过k，并且k相对于数组长度来说是比较小的。

请选择一个合适的排序策略，对这个数组进行排序。
 */
public class SortArrayDistanceLessK {


    public static void sortArrayDistanceLessK(int[] arr ,int k ){
        PriorityQueue<Integer> heap = new PriorityQueue<>(new myCompare());
        int index = 0;
        //把前k个数放到小根堆
        for (int i=0;i<Math.min(arr.length-1,k);i++){
            heap.add(arr[i]);
            index++;
        }

        //弹出一个加一下
        for (int i =index;i<arr.length;i++){
            System.out.println(heap.poll());
            heap.add(arr[index++]);
        }

        //  剩余的全部弹出
        while (!heap.isEmpty()){
            System.out.println(heap.poll());
        }
    }

    public static class Student{
        int age;
        String name;
        int score;
        Student(int age ,String name ,int score){
            this.age = age;
            this.name = name;
            this.score = score;
        }
    }

    public static class myCompare implements Comparator<Integer>{


        @Override
        public int compare(Integer o1, Integer o2) {
            return o2-o1;
        }
    }

    public static void main(String[] args) {
//        int[] arr = new int[]{3,2,1,4,7,6,5,10,9,8,14,13};
        int[] arr = new int[]{3,2,1,4};
        sortArrayDistanceLessK(arr,5);
        System.out.println(Arrays.toString(arr));



    }
}
