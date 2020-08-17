package com.leecode.array.visit;
/**
 * 485. 最大连续1的个数 难度：简单
给定一个二进制数组， 计算其中最大连续1的个数。

示例 1:

输入: [1,1,0,1,1,1]
输出: 3
解释: 开头的两位和最后的三位都是连续1，所以最大连续1的个数是 3.
注意：

输入的数组只包含 0 和1。
输入数组的长度是正整数，且不超过 10,000。
**/


public class leecode_485 {
    public static void main(String[] args) {
        Integer[] arr = {1,1,0,1,1,1,0,0,1,1,1,1,1,1,0,1,1,1,0};
        Integer max = funcOne(arr);
        System.out.println(max);
    }

    private static Integer funcOne(Integer[] arr){
        int max = 0;
        int temp = 0;
        for (int i=0 ;i<arr.length;i++){
           if(arr[i]==1){
                temp+=1;
           }
           if (arr[i]==0 || i == arr.length-1){
                if(max<temp){
                    max = temp;
                }
                temp = 0;
           }
        }
        return max;
    }
}
