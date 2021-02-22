package com.learn.day01;

import java.util.Arrays;

public class InsertSort {
    public static void InsertSort(int[] arr){
        // [a b c d e m n p]
        //          i 位置往前看，小就换
        int len = arr.length;
        for (int i =0;i<len;i++){
            for (int j = i;j > 0 ;j--){
                if (arr[j] < arr[j-1]){
                    swap(arr ,j ,j-1);
                }
            }
        }

    }

    public static void swap(int[] arr, int i, int j){
        arr[i] = arr[i] ^ arr[j];
        arr[j] = arr[i] ^ arr[j];
        arr[i] = arr[i] ^ arr[j];
    }
    public static void main(String[] args) {
        int[] arr = {2,4,3,6,1,7,8,1,5,6,4,1,77,32};
        InsertSort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
