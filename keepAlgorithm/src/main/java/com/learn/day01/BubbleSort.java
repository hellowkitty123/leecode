package com.learn.day01;

import java.util.Arrays;

// o 排序
public class BubbleSort {

    public static void BubbleSort(int[] arr){
        if (arr == null || arr.length <2) {
            return;
        }
        int len = arr.length;
        for (int i = len-1;i >= 0 ;i--){
            for (int j = 0;j < i; j++){
                if (arr[j] > arr[j + 1]) {
                    swap(arr,j,j+1);
                }
            }
        }

    }

    public static void swap(int[] arr, int i ,int j){
        arr[i] = arr[i] ^ arr[j];
        arr[j] = arr[i] ^ arr[j];
        arr[i] = arr[i] ^ arr[j];
    }

    public static void main(String[] args) {
        int[] arr = {2,4,3,6,1,7,8,1,5,6,4,1,77,32};
        BubbleSort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
