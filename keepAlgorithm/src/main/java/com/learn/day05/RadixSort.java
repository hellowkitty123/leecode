package com.learn.day05;

import java.util.Arrays;

public class RadixSort {

    public static void radixSort(int[] arr){
        if (arr == null || arr.length <=0){
            return;
        }
        int maxbits = maxBits(arr);
        radixSort(arr,0,arr.length-1,maxbits);

    }
    // arr 从 L...R 上 最大位数，基数排序
    private static void radixSort(int[] arr,int L,int R ,int digit){


        for (int d =0;d<=digit;d++){
            int[] help = new int[R-L +1];
            int[] count = new int[10];
            for (int i=0;i<arr.length;i++){
                int num = getDigit(arr[i],d);
                count[num]++;
            }

            for (int j=1;j<count.length;j++){
                count[j]+=count[j-1];
            }

            for (int i = help.length-1;i>=0;i--){
                int num = getDigit(arr[i],d);

                help[--count[num]] = arr[i];
            }

            for (int i=0;i<arr.length;i++){
                arr[i] = help[i];
            }
            System.out.println(Arrays.toString(arr));
        }
    }

    // 获得数组中最大数有多少位
    public static int maxBits(int[] arr){
        int max = Integer.MIN_VALUE;
        for (int i: arr){
            max = Math.max(i,max);
        }
        int bits = 0;
        while (max >= 10){
            bits++;
            max = max/10;
        }

        return bits;
    }
    // 获得第 num
    public static int getDigit(int num,int d){
        int power = (int)Math.pow(10,d);
        return (num/ power) % 10;
    }

    public static void main(String[] args) {
        int[]arr = new int[]{1,102,44,674,21,7,899,21};
        radixSort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
