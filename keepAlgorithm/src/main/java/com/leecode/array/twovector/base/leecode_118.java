package com.leecode.array.twovector.base;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 118. 杨辉三角
 * 给定一个非负整数 numRows，生成杨辉三角的前 numRows 行。

 * 在杨辉三角中，每个数是它左上方和右上方的数的和。
 *
 * 示例:
 *
 * 输入: 5
 * 输出:
 * [
 *      [1],
 *     [1,1],
 *    [1,2,1],
 *   [1,3,3,1],
 *  [1,4,6,4,1]
 * ]
 *
 */
public class leecode_118 {

    public static void main(String[] args) {
        int numRows = 5;
        ArrayList<ArrayList<Integer>> yanghui = funcOne(numRows);
        for (ArrayList<Integer> cell : yanghui){


            System.out.println(Arrays.toString(cell.toArray()));
        }
    }
    private static ArrayList<ArrayList<Integer>> funcOne(int numRows){
        ArrayList<ArrayList<Integer>> yanghui = new ArrayList<ArrayList<Integer>>();
        for (int i=0;i<numRows;i++){
            ArrayList<Integer> cell = new ArrayList<Integer>();
            if (i == 0) {
                cell.add(1);
            }else{
                for (int j=0;j<i+1;j++) {
                    //   4  3  4
                    int size = yanghui.get(i - 1).size();
                    ArrayList<Integer> precell = yanghui.get(i - 1);
                    int left = j - 1 >=0 ? precell.get(j - 1):0;
                    int right = size <= j ? 0 : precell.get(j);

                    cell.add(left + right);
                }
            }
            yanghui.add(cell);
        }
        return yanghui;
    }
}
