package com.learn.day01;

import java.util.Arrays;

public class Eor {
    // 一个数组中有两种数出现奇数次，其他出现了偶数次，怎么打印这两种数
    public static int[] Eor(int[] arr){
        if (arr == null || arr.length < 2){
            return null;
        }
        int eor = 0;
        for (int i =0;i<arr.length;i++){
            eor ^= arr[i];
        }
        //区分两个
        int dif = eor & (~eor+1);
        int eor2 = 0 ;
        for (int i=0;i<arr.length;i++){
            if ((dif & arr[i]) == dif){
                eor2 ^= arr[i];
            }
        }
        return new int[]{eor2,eor ^ eor2};
    }
    public static void main(String[] args) {
        int[] arr = {1,1,1,1,2,2,33,33,33,45,45,45,5,5,7,7,7,7};
        int[] result = Eor(arr);
        System.out.println(Arrays.toString(result));
    }
}
