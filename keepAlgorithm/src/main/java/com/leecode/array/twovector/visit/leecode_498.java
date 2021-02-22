package com.leecode.array.twovector.visit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * 498. 对角线遍历
 * 给定一个含有 M x N 个元素的矩阵（M 行，N 列），请以对角线遍历的顺序返回这个矩阵中的所有元素，对角线遍历如下图所示。
 *
 *  
 *
 * 示例:
 *
 * 输入:
 * [
 *  [ 1, 2, 3 ],
 *  [ 4, 5, 6 ],
 *  [ 7, 8, 9 ]
 * ]
 *
 *
 * 输出:  [1,2,4,7,5,3,6,8,9]
 *   [1, 2, 3, 4],
 *   [5, 6, 7, 8],
 *   [9,10,11,12]
 * 解释:
 *
 *   [图]
 *
 * 说明:
 *
 * 给定矩阵中的元素总数不会超过 100000 。
 *
 */
public class leecode_498 {
    public static void main(String[] args) {

        int[][] arr = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9,10,11,12}
        };

//        List<Integer> result = funcOne(arr);
//        System.out.println(result.toString());

        int[] result2 = funcTwo(arr);
        System.out.println(Arrays.toString(result2));
    }

    // 问题像复杂了（失败）
    private static List<Integer> funcOne(int[][] arr){
        int total = arr.length + arr[0].length;
        int count = arr.length * arr[0].length;
        List<Integer> result = new ArrayList<Integer>(count);

        int col = 0;
        int row = 1;
        int[][] direction = {{1,-1},{1,0},{-1,1},{0,1}};
        result.add(arr[0][0]);
        for (int i=0;i<total;i++){
            //第一个方向

            if (col <arr.length-1){

                col+=direction[1][0];
                row+=direction[1][1];
                result.add(arr[col][row]) ;

                for (int idx = row;idx >0;idx --){
                    col+=direction[0][0];
                    row+=direction[0][1];

                    result.add(arr[col][row]) ;
                }

            }
            if (row <arr[0].length-1){
                for (int idx = row;idx >0;idx --){
                    col+=direction[2][0];
                    row+=direction[2][1];
                    result.add(arr[col][row]) ;
                }
                for (int idx = row;idx >0;idx --){
                    col+=direction[3][0];
                    row+=direction[3][1];
                    result.add(arr[col][row]) ;
                }
            }
        }
        return result;

    }
    private static int[] funcTwo(int[][] arr){
        int col = arr.length;
        int row = arr[0].length;
        int[] result = new int[row*col];
        List<Integer> temp = new ArrayList<Integer>();
        int k = 0;
        for (int d=0;d<col+row-1;d++){
            temp.clear();
            int r = d<row?0:d-row+1;
            int c = d<row?d:row-1;
            while (r<col && c > -1){

                temp.add(arr[r][c]);
                ++r;
                --c;

            }
            if (d%2 == 0){
                Collections.reverse(temp);
            }
            for (Integer integer : temp) {
                System.out.println(Arrays.toString(result));
                result[k++] = integer;
            }
        }
        return result;
    }

}
