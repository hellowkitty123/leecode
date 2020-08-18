package com.leecode.array.visit;

import java.util.Arrays;

/**
 * 628. 三个数的最大乘积 难度：简单
 * 给定一个整型数组，在数组中找出由三个数组成的最大乘积，并输出这个乘积。
 *
 * 示例 1:
 *
 * 输入: [1,2,3]
 * 输出: 6
 * 示例 2:
 *
 * 输入: [1,2,3,4]
 * 输出: 24
 * 注意:
 *
 * 给定的整型数组长度范围是[3,104]，数组中所有的元素范围是[-1000, 1000]。
 * 输入的数组中任意三个数的乘积不会超出32位有符号整数的范围。
 *
 *
 * 如果数组中出现了负数，那么我们还需要考虑乘积中包含负数的情况，显然选择最小的两个负数和最大的一个正数是最优的
 * 即为前两个元素与最后一个元素的乘积
 */
public class leecode_628 {
    public static void main(String[] args) {
        int[] arr = {-10,-20,0,30,1000,-200};
        int max = funcOne(arr);
        System.out.println(max);
    }

    private static int funcOne(int[] arr){
        if(arr == null || arr.length == 0) throw new RuntimeException("...");
        Arrays.sort(arr);
        return Math.max(arr[0]*arr[1]*arr[arr.length-1],arr[arr.length-1]*arr[arr.length-2]*arr[arr.length-3]);
    }
}
