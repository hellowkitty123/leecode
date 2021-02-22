package com.leecode.array.twovector.change;

import java.util.Arrays;

/**
 * 48. 旋转图像
 *给定一个 n × n 的二维矩阵表示一个图像。
 *
 * 将图像顺时针旋转 90 度。
 *
 * 说明：
 *
 * 你必须在原地旋转图像，这意味着你需要直接修改输入的二维矩阵。请不要使用另一个矩阵来旋转图像。
 *
 * 示例 1:
 *
 * 给定 matrix =
 * [
 *   [1,2,3],
 *   [4,5,6],
 *   [7,8,9]
 * ],
 *
 * 原地旋转输入矩阵，使其变为:
 * [
 *   [7,4,1],
 *   [8,5,2],
 *   [9,6,3]
 * ]
 * 示例 2:
 *
 * 给定 matrix =
 * [
 *   [ 5, 1, 9,11],
 *   [ 2, 4, 8,10],
 *   [13, 3, 6, 7],
 *   [15,14,12,16]
 * ],
 *
 * 原地旋转输入矩阵，使其变为:
 * [
 *   [15,13, 2, 5],
 *   [14, 3, 4, 1],
 *   [12, 6, 8, 9],
 *   [16, 7,10,11]
 * ]
 */
public class leecode_48 {
    public static void main(String[] args) {
        int[][] arr = {
                {5, 1, 9,11},
                {2, 4, 8,10},
                {13, 3, 6, 7},
                {15,14,12,16}
        };
        int[][] result = funcOne(arr);
        for (int i = 0;i<result.length;i++) {
            System.out.println(Arrays.toString(result[i]));
        }
    }
    private static int[][] funcOne(int[][] arr){
        //先转置
        for (int i=0;i<arr.length;i++){
            for (int j=i;j<arr.length;j++){
                int temp = arr[i][j];
                arr[i][j] = arr[j][i];
                arr[j][i] = temp;
            }
        }
        //先翻转 翻转到中间，否则会赚回来
        for (int i=0;i<arr.length;i++) {
            for (int j = 0; j < arr.length/2; j++) {
                int temp = arr[i][j];
                arr[i][j] = arr[i][arr.length-j-1];
                arr[i][arr.length-j-1] = temp;
            }
        }
        return arr;
    }
}
