package com.leecode.array.statistics;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *  442. 数组中重复的数据 难度：中等
 *  给定一个整数数组 a，其中1 ≤ a[i] ≤ n （n为数组长度）, 其中有些元素出现两次而其他元素出现一次。
 *
 * 找到所有出现两次的元素。
 *
 * 你可以不用到任何额外空间并在O(n)时间复杂度内解决这个问题吗？
 *
 * 示例：
 *
 * 输入:
 * [4,3,2,7,8,2,3,1]
 *
 * 输出:
 * [2,3]
 * 本地哈希
 */
public class leecode_442 {
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
                result.add(arr[i]);
            }
        }
        return result;
    }
}
