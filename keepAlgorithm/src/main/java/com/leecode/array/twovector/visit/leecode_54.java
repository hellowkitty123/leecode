package com.leecode.array.twovector.visit;

import java.util.Arrays;

/**
 * 54. 螺旋矩阵
 *给定一个包含 m x n 个元素的矩阵（m 行, n 列），请按照顺时针螺旋顺序，返回矩阵中的所有元素。
 *
 * 示例 1:
 *
 * 输入:
 * [
 *  [ 1, 2, 3 ],
 *  [ 4, 5, 6 ],
 *  [ 7, 8, 9 ]
 * ]
 * 输出: [1,2,3,6,9,8,7,4,5]
 * 示例 2:
 *
 * 输入:
 * [
 *   [1, 2, 3, 4],
 *   [5, 6, 7, 8],
 *   [9,10,11,12]
 * ]
 * 输出: [1,2,3,4,8,12,11,10,9,5,6,7]
 * 上下界移动法
 */
public class leecode_54 {
    public static void main(String[] args) {
        int[][] arr = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9,10,11,12}
        };
        int[] result = funcOne(arr);
        System.out.println(Arrays.toString(result));

    }
    private static int[] funcOne(int[][] arr){
        int up = 0;
        int down = arr.length-1;
        int left = 0;
        int right = arr[0].length-1;
        int count = 0;
        int[] result = new int[(down+1)*(right+1)];
        while (true){
            for (int idx = left; idx <= right;idx ++){

                result[count] = arr[up][idx];
                count++;
            }
            if (++up>down){
                break;
            }
            for (int idx = up; idx <= down;idx ++){

                result[count] = arr[idx][right];
                count++;
            }
            if (--right<left){
                break;
            }
            for (int idx = right; idx >= left;idx --){

                result[count] = arr[down][idx];
                count++;
            }
            if (--down<up){
                break;
            }
            for (int idx = down; idx >= up;idx --){

                result[count] = arr[idx][left];
                count++;
            }
            if (++left>right){
                break;
            }
        }
        return result;
    }
}
