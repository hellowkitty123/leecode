package com.leecode.array.twovector.suffix;

import java.util.Arrays;

/**
 * 304. 二维区域和检索 - 矩阵不可变
 * 给定一个二维矩阵，计算其子矩形范围内元素的总和，该子矩阵的左上角为 (row1, col1) ，右下角为 (row2, col2)。
 *
 *
 * 上图子矩阵左上角 (row1, col1) = (2, 1) ，右下角(row2, col2) = (4, 3)，该子矩形内元素的总和为 8。
 *
 * 示例:
 *
 * 给定 matrix = [
 *   [3, 0, 1, 4, 2],
 *   [5, 6, 3, 2, 1],
 *   [1, 2, 0, 1, 5],
 *   [4, 1, 0, 1, 7],
 *   [1, 0, 3, 0, 5]
 * ]
 *
 * sumRegion(2, 1, 4, 3) -> 8
 * sumRegion(1, 1, 2, 2) -> 11
 * sumRegion(1, 2, 2, 4) -> 12
 * 说明:
 *
 * 你可以假设矩阵不可变。
 * 会多次调用 sumRegion 方法。
 * 你可以假设 row1 ≤ row2 且 col1 ≤ col2
 */
public class leecode_304 {
    private static int[][] sum ;
    public static void main(String[] args) {
        int[][] arr = {
                {3, 0, 1, 4, 2},
                {5, 6, 3, 2, 1},
                {1, 2, 0, 1, 5},
                {4, 1, 0, 1, 7},
                {1, 0, 3, 0, 5}
        };
        sum = preCompute(arr);
        for (int i=0;i<sum.length;i++){
            System.out.println(Arrays.toString(sum[i]));

        }
        int result = funcOne(2,1,4 ,3);
        System.out.println(result);
    }

    private static int[][] preCompute(int[][] arr) {
        int[][] sum = new int[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                if (i==0 && j==0){
                    sum[0][0] = arr[0][0];
                }else {
                    sum[i][j] = (i-1>=0?sum[i-1][j]:0) + (j-1>=0?sum[i][j-1]:0) - (j-1>=0 && i-1>=0?sum[i-1][j-1]:0) + arr[i][j];
                }
            }
        }
        return sum;
    }

    private static int funcOne(int starty,int startx, int endy ,int endx) {
        return sum[endx][endy] - sum[startx][endy] - sum[endx][starty] + sum[startx][starty];
    }
}
