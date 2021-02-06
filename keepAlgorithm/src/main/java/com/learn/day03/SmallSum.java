package com.learn.day03;

import java.util.Arrays;

//在一个数组中，一个数左边比它小的数的总和，叫数的小和，所有数的小和累加起来，叫数组小和。求数组小和。
//例子： [1,3,4,2,5]
//1左边比1小的数：没有
//3左边比3小的数：1
//4左边比4小的数：1、3
//2左边比2小的数：1
//5左边比5小的数：1、3、4、 2
//所以数组的小和为1+1+3+1+1+3+4+2=16
//归并排序本质，组内不再进行比较，在左右组pk的时候产生排序，（把比较行为变的有序）
// 哪一类问题需要用 归并
// 1、 数组中求所有的降序对 -> 左边的数有多少个数比右边的小
public class SmallSum {
    public static int sum = 0;
    public static int smallSum(int[] arr){
        if (arr == null || arr.length <2){
            return 0;
        }
        int len = arr.length;
        process(arr,0,len-1);
        return sum;
    }
    // 二分
    public static void process(int[] arr,int L,int R){
        if (L == R){
            return;
        }

        int mid = L + ((R-L) >> 1);
        process(arr,L,mid);
        process(arr,mid+1,R);
        merge(arr,L,R,mid);
    }
    // 合并
    public static void merge(int[] arr ,int L ,int R, int mid){
        int[] help = new int[R-L+1];
        int index = 0;
        int i = L;
        int j = mid+1;
        while (i <=mid && j <=R){
            sum += arr[i] > arr[j] ? (L-i+1) * arr[j] : 0;
            sum += arr[i] < arr[j] ? (R-j+1) * arr[i] : 0;
            help[index++] = arr[i] > arr[j] ? arr[j++]: arr[i++];

        }

        while (i<=mid){
            help[index++] = arr[i++];
        }

        while (j<=R){
            help[index++] = arr[j++];
        }

        index--;
        while (index>=0){
            arr[L+index] = help[index--];
        }

    }

    public static void main(String[] args) {
//        int[] arr = {4,2,1,3,6,1,7,8,1,5,3,6,4,1,77,2,32};
//        int[] arr = {4,2,1};
        int[] arr = {1,3,4,2,5};
        smallSum(arr);
        System.out.println(Arrays.toString(arr));
        System.out.println(sum);
    }
}
