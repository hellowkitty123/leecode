package com.leecode.array.twovector.suffix;
/**
 * 303. 区域和检索 - 数组不可变
 * 给定一个整数数组  nums，求出数组从索引 i 到 j  (i ≤ j) 范围内元素的总和，包含 i,  j 两点。
 *
 * 示例：
 *
 * 给定 nums = [-2, 0, 3, -5, 2, -1]，求和函数为 sumRange()
 *
 * sumRange(0, 2) -> 1
 * sumRange(2, 5) -> -1
 * sumRange(0, 5) -> -3
 * 说明:
 *
 * 你可以假设数组不可变。
 * 会多次调用 sumRange 方法。
 *
 */
public class leecode_303 {
    private static int[] sum;
    public static void main(String[] args) {
        int[] arr = {-2, 0, 3, -5, 2, -1};
        sum = preCompute(arr);
        int result = funcOne(2,5);
        System.out.println(result);
    }

    private static int[] preCompute(int[] arr){
        int[] sum = new int[arr.length+1];
        sum[0] = 0;
        for (int i=0;i<arr.length;i++){
            sum[i+1] = sum[i]+ arr[i];
        }
        return sum;
    }
    private static int funcOne(int start,int end){
        return sum[end+1]-sum[start];
    }
}
