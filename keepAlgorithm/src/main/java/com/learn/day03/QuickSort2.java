package com.learn.day03;

import java.util.Arrays;

public class QuickSort2 {
    public static void quickSort(int[] arr){
        if (arr == null && arr.length < 2){
            return;
        }
        process(arr,0,arr.length-1);

    }

    public static void process(int[] arr,int L,int R){
        if (L >= R){
            return;
        }
        //0、随机选一个值，放在右边作为num
        swap(arr,R ,L + (int)(Math.random() * (R-L+1)));
        //1、划分三路
        int[] row = netherlandsFlag(arr,L,R);
        //2、左子问题递归处理
        process(arr,L,row[0]-1);
        //3、右子问题递归处理
        process(arr,row[1]+1,R);
    }

    public static int[] netherlandsFlag(int[] arr,int L,int R){
        if (L>R){
            return new int[]{-1,-1};
        }
        if (L==R){
            return new int[]{L,R};
        }
        int less = L-1; // 左边界的左边
        int more = R;   // 右边界
        int move = L;   // 指针移动位置  移动范围[L....R)


        // 跳出条件  move 指针追赶 more，知道追上跳出
        while (move <more){
            if (arr[move] == arr[R]){
                // 1、index 位置的value 等于num 当前值应该在[less...more) 之间
                move++;


            }else if(arr[move] < arr[R]) {
                // 2、index 位置的value 小于num  当前值应该跟less先左移动，然后数据交换,move可以移动是因为当前值已经看过并处理过了
                swap(arr,move++,++less);

            }else {
                // 3、index 位置的value 大于num  交换不移动，当前换来的值从来没看过
                swap(arr,move,--more);
            }
        }
        swap(arr,more,R);
        return new int[]{less+1,more};
    }

    public static void swap(int[] arr,int i,int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        for(int i=0;i<3000;i++){
            int[] arr = {11,4,2,1,1,3,6,1};
            quickSort(arr);
            System.out.println(Arrays.toString(arr));
        }
    }
}
