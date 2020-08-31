package com.leecode.array.twovector.change;

import java.util.Arrays;

/**
 *
 * 73. 矩阵置零
 * 给定一个 m x n 的矩阵，如果一个元素为 0，则将其所在行和列的所有元素都设为 0。请使用原地算法。
 *
 * 示例 1:
 *
 * 输入:
 * [
 *   [1,1,1],
 *   [1,0,1],
 *   [1,1,1]
 * ]
 * 输出:
 * [
 *   [1,0,1],
 *   [0,0,0],
 *   [1,0,1]
 * ]
 * 示例 2:
 *
 * 输入:
 * [
 *   [0,1,2,0],
 *   [3,4,5,2],
 *   [1,3,1,5]
 * ]
 * 输出:
 * [
 *   [0,0,0,0],
 *   [0,4,5,0],
 *   [0,3,1,0]
 * ]
 * 进阶:
 *
 * 一个直接的解决方案是使用  O(mn) 的额外空间，但这并不是一个好的解决方案。
 * 一个简单的改进方案是使用 O(m + n) 的额外空间，但这仍然不是最好的解决方案。
 * 你能想出一个常数空间的解决方案吗？
 *
 */
public class leecode_73 {
    public static void main(String[] args) {
        int[][] arr = {
                {5, 1, 9,1,1},    //{5, 0, 9,0}
                {2, 1, 8,10,1},   //{0, 0, 13,15}
                {13, 3, 6, 7,1},
                {15,14,12,0,1}
        };
        int[][] result = funcOne(arr);
        for (int i = 0;i<result.length;i++) {
            System.out.println(Arrays.toString(result[i]));
        }
    }
    private static int[][] funcOne(int[][] arr){
        int[] row = new int[arr.length];
        for (int j=0;j<arr.length;j++){
            row[j] =1;
        }
        int[] col = new int[arr[0].length];
        for (int j=0;j<arr[0].length;j++){
            col[j] =1;
        }
        for (int i=0;i<arr.length;i++){
            for (int j=0;j<arr[0].length;j++){
                row[i]=row[i]*arr[i][j];

            }
        }

        for(int i=0;i<arr[0].length;i++){
            for (int j=0;j<arr.length;j++){
                col[i]=col[i]*arr[j][i];
            }
        }

        for (int i=0;i<arr.length;i++){
            for (int j=0;j<arr[0].length;j++){
                if (row[i]==0 || col[j]==0){
                    arr[i][j] = 0;
                }
            }
        }
        return arr;
    }
}
