package com.leecode.array.twovector.reverse;

import java.util.Arrays;

/**
 * 396. 旋转函数
 *给定一个长度为 n 的整数数组 A 。
 *
 * 假设 Bk 是数组 A 顺时针旋转 k 个位置后的数组，我们定义 A 的“旋转函数” F 为：
 *
 * F(k) =   0 * Bk[0] + 1 * Bk[1] + ... + (n-1) * Bk[n-1]。
 * F(k+1) = 0 * Bk[n-1] + 1 * Bk[0] + ... + (n-1) * Bk[n-2]。
 * 计算F(0), F(1), ..., F(n-1)中的最大值。
 *
 * 注意:
 * 可以认为 n 的值小于 10^5。
 *
 * 示例:
 *
 * A = [4, 3, 2, 6]
 * A = [6, 4, 3, 2]
 * A = [2, 3, 4, 3]
 * A = [3, 2, 6, 4]
 *
 * F(0) = (0 * 4) + (1 * 3) + (2 * 2) + (3 * 6) = 0 + 3 + 4 + 18 = 25
 * F(1) = (0 * 6) + (1 * 4) + (2 * 3) + (3 * 2) = 0 + 4 + 6 + 6 = 16
 * F(2) = (0 * 2) + (1 * 6) + (2 * 4) + (3 * 3) = 0 + 6 + 8 + 9 = 23
 * F(3) = (0 * 3) + (1 * 2) + (2 * 6) + (3 * 4) = 0 + 2 + 12 + 12 = 26
 *
 * 所以 F(0), F(1), F(2), F(3) 中的最大值是 F(3) = 26 。
 *
 */
public class leecode_396 {
    public static void main(String[] args) {
        int[] arr = {4, 3, 2, 6};
        int result = funcOne(arr);
        System.out.println(result);
    }
    private static int funcOne(int[] arr){
        //F(k+1) = F(k) + S - n * Bk[n-1]
        int sum = 0;
        int fn = 0;

        for (int i=0;i<arr.length;i++){
            sum = sum+arr[i];
            fn = fn+ i*arr[i];
        }
        int max = fn;
        for (int i=arr.length-1;i>=0;i--){

            fn += sum-arr.length*arr[i];
            max = Math.max(max,fn);
        }
        return max;
    }
}
