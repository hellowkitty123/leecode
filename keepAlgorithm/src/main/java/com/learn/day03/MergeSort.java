package com.learn.day03;

import java.util.Arrays;

public class MergeSort {

    public static void mergesSort(int[] arr){
        if (arr == null || arr.length <2){
            return;
        }
        int len = arr.length;
        process(arr,0,len-1);

    }
    // 进行子数组排序
    public static void process(int[] arr,int L, int R){

        if (L==R){
            return;
        }
        int mid = L + ((R-L)>>1);
        process(arr,L,mid);
        process(arr,mid+1,R);
        merge2(arr,L,R,mid);

    }
    // 归并两个有序数组
    public static void merge(int[] arr,int L,int R ,int mid){
        int i=L;
        int j=mid+1;
        int index=0; // help数组有效数据的长度
        int[] help = new int[R-L+1];
        //左边没走完或者右边没走完，都继续

        while (i<=mid || j<=R){

            while (i > mid && j<=R){
                help[index++] = arr[j++];
                break;
            }

            while (j > R && i<=mid){
                help[index++] = arr[i++];
                break;
            }
            if (i<=mid && j<=R){
                help[index++] = (arr[i] < arr [j]) ? arr[i++]:arr[j++];
            }
        }
        index--;
        while (index >= 0){
            arr[L+index] = help[index--];
        }
    }


    // 归并两个有序数组
    public static void merge2(int[] arr,int L,int R ,int mid){
        int i=L;
        int j=mid+1;
        int index=0; // help数组有效数据的长度
        int[] help = new int[R-L+1];
        //左边没走完或者右边没走完，都继续

        while (i<=mid && j<=R){
            help[index++] = (arr[i] < arr [j]) ? arr[i++]:arr[j++];
        }
        // 左边下标没有走完
        while (i <= mid){
            help[index++] = arr[i++];

        }
        // 右边下标没有走完
        while (j <= R ){
            help[index++] = arr[j++];
        }

        index--;
        while (index >= 0){
            arr[L+index] = help[index--];
        }
    }


    public static void main(String[] args) {
        int[] arr = {4,2,1,3,6,1,7,8,1,5,3,6,4,1,77,2,32};
//        int[] arr = {4,2,1};
        mergesSort(arr);
        System.out.println(Arrays.toString(arr));
    }

}
