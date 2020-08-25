package com.leecode.array.twovector;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *119. 杨辉三角 II
 * 难度:简单
 * 给定一个非负索引 k，其中 k ≤ 33，返回杨辉三角的第 k 行。

 * 在杨辉三角中，每个数是它左上方和右上方的数的和。
 *
 * 示例:
 *
 * 输入: 3
 * 输出: [1,3,3,1]
 * 进阶：
 *
 * 你可以优化你的算法到 O(k) 空间复杂度吗？
 */
public class leecode_119 {

    public static void main(String[] args) {
        int numRows = 10;
        ArrayList<Integer> yanghui = funcOne(numRows);
        System.out.println(Arrays.toString(yanghui.toArray()));
    }
    private static ArrayList<Integer> funcOne(int numRows){
        ArrayList<Integer> yanghui = new ArrayList<Integer>();
        for (int i=0;i<numRows;i++){
            if (i == 0) {
                yanghui.add(1);
            }else{
                for (int j=i;j>=0;j--) {
                    //   4  3  4
                    int size = yanghui.size();
                    ArrayList<Integer> precell = yanghui;
                    int left = j - 1 >=0 ? precell.get(j - 1):0;
                    int right = size <= j ? 0 : precell.get(j);
                    if (j == i){
                        precell.add(left + right);
                    }else{
                        precell.set(j,left + right);
                    }
                }
            }
        }
        return yanghui;
    }
}
