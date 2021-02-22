package com.leecode.array.statistics;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 448. 找到所有数组中消失的数字 难度：简单
 * 给定一个范围在  1 ≤ a[i] ≤ n ( n = 数组大小 ) 的 整型数组，数组中的元素一些出现了两次，另一些只出现一次。
 *
 * 找到所有在 [1, n] 范围之间没有出现在数组中的数字。
 *
 * 您能在不使用额外空间且时间复杂度为O(n)的情况下完成这个任务吗? 你可以假定返回的数组不算在额外空间内。
 *
 * 示例:
 *
 * 输入:
 * [4,3,2,7,8,2,3,1]
 *
 * 输出:
 * [5,6]
 */
public class leecode_448 {
    public static void main(String[] args) {
        int[] arr = {4,3,2,7,8,2,3,1};
        Set<Integer> result = funcOne(arr);
        System.out.println(Arrays.toString(result.toArray()));
    }

    private static Set<Integer> funcOne(int[] arr){
        Set<Integer> result = new HashSet<Integer>();
        for (int i =0;i<arr.length;i++){

            while (arr[i] != arr[arr[i]-1]){
                int temp = arr[arr[i]-1];
                arr[arr[i]-1] = arr[i];
                arr[i] = temp;
            }
        }

        for (int i =0;i<arr.length;i++){
            if (arr[i]!= i+1){
                result.add(i+1);
            }
        }
        return result;
    }
}
