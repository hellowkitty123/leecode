package com.leecode.array.twovector.reverse;

import java.util.Arrays;

/**
 * 189. 旋转数组
 *给定一个数组，将数组中的元素向右移动 k 个位置，其中 k 是非负数。
 *
 * 示例 1:
 *
 * 输入: [1,2,3,4,5,6,7] 和 k = 3
 *
 * [1,2,3,4,5,6,7]
 *
 * 输出: [5,6,7,1,2,3,4]
 * 解释:
 * 向右旋转 1 步: [7,1,2,3,4,5,6]
 * 向右旋转 2 步: [6,7,1,2,3,4,5]
 * 向右旋转 3 步: [5,6,7,1,2,3,4]
 * 示例 2:
 *
 * 输入: [-1,-100,3,99] 和 k = 2
 * 输出: [3,99,-1,-100]
 * 解释:
 * 向右旋转 1 步: [99,-1,-100,3]
 * 向右旋转 2 步: [3,99,-1,-100]
 * 说明:
 *
 * 尽可能想出更多的解决方案，至少有三种不同的方法可以解决这个问题。
 * 要求使用空间复杂度为 O(1) 的 原地 算法。
 *
 */
public class leecode_189 {
    public static void main(String[] args) {
        int[] arr = {1,2,3,4,5,6,7,8,9};
        int k =3;
        int[] result = funcOne(arr,k);
        System.out.println(Arrays.toString(result));
        result = funcTwo(arr,k);
        System.out.println(Arrays.toString(result));
        result = funcThree(arr,k);
        System.out.println(Arrays.toString(result));
        result = funcFor(arr,k);
        System.out.println(Arrays.toString(result));
    }
    private static int[] funcOne(int[] arr,int k){
        for (int i=0 ;i<k ;i++){
            for (int j=arr.length-1 ;j>0 ;j--){
                int temp = arr[j-1];
                arr[j-1] = arr[j];
                arr[j] = temp;
            }
        }
        return arr;
    }
    private static int[] funcTwo(int[] arr,int k){
        int[] temp = new int[k];
        int idx = 0;
        for (int i =arr.length-1;i>=0;i--){
            if (i>arr.length-1-k){
                temp[idx] = arr[i];
                idx++;
            }
        }
        for (int i =arr.length-1-k;i>=0;i--){
            arr[i+k] = arr[i] ;
        }
        for (int i =0;i<k;i++){
            arr[i] =  temp [k-i-1];
        }
        return arr;
    }

    //数组旋转
    private static int[] funcThree(int[] arr,int k){
        arr = reverse(arr,0,arr.length-1);
        arr = reverse(arr,0,k-1);
        arr = reverse(arr,k,arr.length-1);
        return arr;
    }

    private static int[] reverse(int[] arr,int start,int end){
        if (arr.length<start || arr.length<end) return null;
        int temp=0;
        int offset = (end-start+1)/2;
        for (int i=0;i<offset;i++){
            temp = arr[start+i];
            arr[start+i] = arr[end-i];
            arr[end-i] = temp;
        }
        return arr;
    }
    // 环状替换
    private static int[] funcFor(int[] arr,int k){
        int count =0 ;
        k = k % arr.length;
        for (int start=0;count<arr.length;start++){
            int pre = arr[start];
            int current = start;
            do {
                int next = (current+k)%arr.length;
                int temp = arr[next];
                arr[next] = pre;
                pre = temp;
                current = next;
                count++;
            }while (start!=current);
        }
        return arr;
    }
}
