package com.learn.day03;

import java.util.Arrays;

/*
快排
Partition过程

给定一个数组arr，和一个整数num。请把小于等于num的数放在数组的左边，大于num的数放在数组的右边。

要求额外空间复杂度O(1)，时间复杂度O(N)

 */
public class QuickSort {
    //3路快排

    // L...less  less+1...more-1  more...R-1  R
    //快排1.0  L...R partition  [ <=arr[R]   arr[R]   >arr[R] ]   O(n2)
    //快排2.0  L...R partition  [ <=arr[R]   arr[R]...   >arr[R] ]  搞定中间一批等于arr[R]  O(n2)
    //快排3.0  随机选一个数 跟R 位置交换 剩下流程 快排2.0 期望 时间复杂度O(n*logn) 空间复杂度O(logn)
    public static void quickSort(int[] arr){
        if (arr == null || arr.length<2){
            return;
        }
        process(arr,0,arr.length-1);
    }
    //处理
    public static void process(int[] arr,int L ,int R){
        if (L>=R){
            return;
        }
        int P =  (int) (L + Math.random() * (R-L+1));
        System.out.println("process = "+ P + " R = "+R);
        swap(arr,P,R);
        int[] row = netherlansFlag(arr,L,R);

        process(arr,L,row[0]-1);
        process(arr,row[1]+1,R);


    }

    //荷兰国旗问题 返回左边界和有边界
    //中间一批相同树的，左边界 和 有边界
    public static int[] netherlansFlag(int[] arr,int L ,int R){
        if (L > R){
            return new int[]{-1,-1};
        }
        if (L == R){
            return new int[]{L,L};
        }
        int less = L-1;
        int more = R;
        int index = L;

        while (index < more){
            //index位置等于R位置
            if (arr[index] == arr[R]){
                index++;
            }else if(arr[index] < arr[R]){

                swap(arr,index++,++less);
            }else{
                swap(arr,index,--more);

            }
        }
        swap(arr,R,more);
        return new int[]{less+1,more};
    }
    public static void swap(int[]arr , int i ,int j){

//        arr[i] = arr[i] ^ arr[j];
//        arr[j] = arr[i] ^ arr[j];
//        arr[i] = arr[i] ^ arr[j];

        int temp = arr[i];
        arr[i]  = arr[j];
        arr[j] = temp;

    }

    public static void main(String[] args) {
//        int[] arr = {4,2,1,3,6,1,7,8,1,5,3,6,4,1,77,2,32};
        for(int i=0;i<3000;i++){
            int[] arr = {11,4,2,1,1,3,6,1};
            quickSort(arr);
            System.out.println(Arrays.toString(arr));
        }
    }
}



