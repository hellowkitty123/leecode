package com.learn.day02;

public class GetMax {


    public static int GetMax(int[] arr){

        return process(arr,0,arr.length-1);
    }

    public static int process(int[] arr ,int L ,int R){
        if (L == R){
            return arr[L];
        }

        int mid = L + ((R-L) >> 1);

        int leftMax = process(arr,L,mid);
        int rightMax = process(arr,mid+1,R);

        return Math.max(leftMax,rightMax);
    }


    public static void main(String[] args) {

        int[] arr = {1,3,5,67,8,9,5,6,8};
        int max = GetMax(arr);
        System.out.println(max);
    }
}
