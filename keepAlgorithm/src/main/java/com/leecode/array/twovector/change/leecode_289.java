package com.leecode.array.twovector.change;

import java.util.Arrays;

/**
 *
 * 289. 生命游戏
 * 根据 百度百科 ，生命游戏，简称为生命，是英国数学家约翰·何顿·康威在 1970 年发明的细胞自动机。
 *
 * 给定一个包含 m × n 个格子的面板，每一个格子都可以看成是一个细胞。每个细胞都具有一个初始状态：1 即为活细胞（live），或 0 即为死细胞（dead）。每个细胞与其八个相邻位置（水平，垂直，对角线）的细胞都遵循以下四条生存定律：
 *
 * 如果活细胞周围八个位置的活细胞数少于两个，则该位置活细胞死亡；
 * 如果活细胞周围八个位置有两个或三个活细胞，则该位置活细胞仍然存活；
 * 如果活细胞周围八个位置有超过三个活细胞，则该位置活细胞死亡；
 * 如果死细胞周围正好有三个活细胞，则该位置死细胞复活；
 * 根据当前状态，写一个函数来计算面板上所有细胞的下一个（一次更新后的）状态。下一个状态是通过将上述规则同时应用于当前状态下的每个细胞所形成的，其中细胞的出生和死亡是同时发生的。
 *
 *  
 *
 * 示例：
 *
 * 输入：
 * [
 *   [0,1,0],
 *   [0,0,1],
 *   [1,1,1],
 *   [0,0,0]
 * ]
 * 输出：
 * [
 *   [0,0,0],
 *   [1,0,1],
 *   [0,1,1],
 *   [0,1,0]
 * ]
 *  
 *
 * 进阶：
 *
 * 你可以使用原地算法解决本题吗？请注意，面板上所有格子需要同时被更新：你不能先更新某些格子，然后使用它们的更新后的值再更新其他格子。
 * 本题中，我们使用二维数组来表示面板。原则上，面板是无限的，但当活细胞侵占了面板边界时会造成问题。你将如何解决这些问题？
 *
 *
 */
public class leecode_289 {
    public static void main(String[] args) {
        int[][] arr = {
                {0,1,0},    //{5, 0, 9,0}
                {0,0,1},   //{0, 0, 13,15}
                {1,1,1},
                {0,0,0}
        };
        int[][] result = funcOne(arr);
        for (int i = 0;i<result.length;i++) {
            System.out.println(Arrays.toString(result[i]));
        }
    }
    private static int[][] funcOne(int[][] arr){
        for (int i=0;i<arr.length;i++){
            for (int j=0;j<arr[0].length;j++){
                int temp = 0;
                for (int m=-1;m<=1;m++){
                    for (int n=-1;n<=1;n++){
                        if (i+m >=0 && i+m<arr.length && j+n>=0 && j+n<arr[0].length && (Math.abs(arr[i+m][j+n]) == 1)){
                            temp = temp + 1;
                        }
                    }
                }
                // 规则 1 或规则 3
                if ((arr[i][j] == 1) && (temp < 2 || temp > 3)) {
                    // -1 代表这个细胞过去是活的现在死了
                    arr[i][j] = -1;
                }
                // 规则 4
                if (arr[i][j] == 0 && temp == 3) {
                    // 2 代表这个细胞过去是死的现在活了
                    arr[i][j] = 2;
                }
            }
        }
        // 遍历 board 得到一次更新后的状态
        for (int row = 0; row < arr.length; row++) {
            for (int col = 0; col < arr[0].length; col++) {
                if (arr[row][col] > 0) {
                    arr[row][col] = 1;
                } else {
                    arr[row][col] = 0;
                }
            }
        }
        return arr;
    }
}
