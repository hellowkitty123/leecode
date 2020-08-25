package com.leecode.array.twovector;

import java.util.ArrayList;

/**
 * 419. 甲板上的战舰
 * 给定一个二维的甲板， 请计算其中有多少艘战舰。 战舰用 'X'表示，空位用 '.'表示。 你需要遵守以下规则：
 *
 * 给你一个有效的甲板，仅由战舰或者空位组成。
 * 战舰只能水平或者垂直放置。换句话说,战舰只能由 1xN (1 行, N 列)组成，或者 Nx1 (N 行, 1 列)组成，其中N可以是任意大小。
 * 两艘战舰之间至少有一个水平或垂直的空位分隔 - 即没有相邻的战舰。
 * 示例 :
 *
 * X..X
 * ...X
 * ...X
 * 在上面的甲板中有2艘战舰。
 *
 * 无效样例 :
 *
 * ...X
 * XXXX
 * ...X
 * 你不会收到这样的无效甲板 - 因为战舰之间至少会有一个空位将它们分开。
 *
 * 进阶:
 *
 * 你可以用一次扫描算法，只使用O(1)额外空间，并且不修改甲板的值来解决这个问题吗？
 */
public class leecode_419 {

    public static void main(String[] args) {
        String[][] arr =  { {"X",".",".","X"},
                            {".","X",".","X"},
                            {".","X",".","X"},
                            {"X",".",".","X"},
                         } ;
        int num = funcOne(arr);
        System.out.println(num);
    }
    private static int  funcOne(String[][] arr){
        int num = 0;
        for (int col=0;col<arr.length;col++){
            for (int row=0;row<arr[0].length;row++){
                if (arr[col][row].equals(".")) continue;
                if (col>0 && arr[col - 1][row].equals("X")) continue;
                if (row>0 && arr[col][row - 1].equals("X"))continue;
                num ++;
            }
        }
        return num;
    }
}
