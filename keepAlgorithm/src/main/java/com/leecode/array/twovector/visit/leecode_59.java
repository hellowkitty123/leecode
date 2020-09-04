package com.leecode.array.twovector.visit;

import java.util.Arrays;

/**
 * 59. 螺旋矩阵 II
 * 给定一个正整数 n，生成一个包含 1 到 n2 所有元素，且元素按顺时针顺序螺旋排列的正方形矩阵。
 *
 * 示例:
 *
 * 输入: 3
 * 输出:
 * [
 *  [ 1, 2, 3 ],
 *  [ 8, 9, 4 ],
 *  [ 7, 6, 5 ]
 * ]
 *
 */
public class leecode_59 {
    public static void main(String[] args) {
        int count = 5;
        int[][] arr = funcOne(count);
        for (int[] ints : arr) {
            System.out.println(Arrays.toString(ints));
        }
    }
    private static int[][] funcOne(int count){
        int[][] result = new int[count][count];
        int left = 0;
        int right = count-1;
        int up = 0;
        int down = count-1;
        int value = 1;
        while (true){
            for (int idx=left;idx<=right;idx++){
                result[up][idx]= value++;
            }
            if(++up>down){
                break;
            }
            for (int idx=up;idx<=down;idx++){
                result[idx][right] = value++;
            }
            if (--right<left){
                break;
            }
            for (int idx=right;idx>=left;idx--){
                result[down][idx] = value++;
            }
            if (--down<up){
                break;
            }
            for (int idx=down;idx>=up;idx--){
                result[idx][left] = value++;
            }
            if (++left>right){
                break;
            }
        }
        return result;
    }
}

