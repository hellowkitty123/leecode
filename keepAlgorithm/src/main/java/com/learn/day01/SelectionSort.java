package com.learn.day01;

import java.util.Arrays;
// 选择排序
public class SelectionSort {

    public static void SelectionSort(int[] arr){
        if (arr == null || arr.length < 2){
            return;
        }
        int len = arr.length;


        for (int i =0;i<len;i++){
           int minindex = i;
           for (int j=i;j<len;j++){
               minindex = arr[j] < arr[minindex] ? j : minindex;
           }
           swap(arr,i,minindex);
        }
    }

    public static void swap(int[] arr ,int i ,int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        int[] arr = {2,4,3,6,1,7,8,1,5,6};
//        int[] arr = {2,4,3,6};
        SelectionSort(arr);
        System.out.println(Arrays.toString(arr));
    }
}

