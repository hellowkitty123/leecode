package com.mianshi.tengxun;

import java.util.Arrays;

/**
 * 一个机器人位于一个 m x n 网格的左上角 （起始点在下图中标记为 “Start” ）。
 *
 * 机器人每次只能向下或者向右移动一步。机器人试图达到网格的右下角（在下图中标记为 “Finish” ）。
 *
 * 问总共有多少条不同的路径？
 *
 * 示例 2：
 * 输入：m = 3, n = 2
 * 输出：3
 * 解释：
 * 从左上角开始，总共有 3 条路径可以到达右下角。
 * 1. 向右 -> 向下 -> 向下
 * 2. 向下 -> 向下 -> 向右
 * 3. 向下 -> 向右 -> 向下
 *
 * 示例 3：
 * 输入：m = 7, n = 3
 * 输出：28
 *
 * 示例 4：
 * 输入：m = 3, n = 3
 * 输出：6
 */
public class uniquePaths {


    public static int uniquePathsuniquePaths(int m, int n) {
        if (m == 1){
            return 1;
        }
        return process(m-1,m+n-2);

    }

    public static int process(int x,int step){

        if (step == 0){
            return x == 0 ? 1:0;
        }

        int up = process(x-1,step-1);
        int down = process(x,step-1);
        return up+ down;
    }
    public static int countWays2( int x,int step){

        int[][] dp = new int[x+1][step+1];

        dp[0][0] = 1;
        // dp[x][0] = 0

        for (int s=1;s<=step ;s++){
            for(int xx = 0; xx<=x;xx++){
                // bug 越界的情况，如果越界判定方法数为0
                dp[xx][s] =  (xx-1 <0 ? 0 : dp[xx-1][s-1]) + dp[xx][s-1];
            }
        }
        for (int[] e : dp){
            System.out.println(Arrays.toString(e));
        }
        return dp[x][step];


    }

    public static int countWays(int x, int y){
        if(x<=1 && y<=1)
			return 0;
		else if(x == 1 || y == 1)
			return 1;
		else
			return countWays(x-1,y)+countWays(x,y-1);
    }



    public static void main(String[] args) {
        int m = 3;
        int n = 3;
        int result = uniquePathsuniquePaths(m,n);
        int result2 = countWays(m,n);
        int result3 = countWays2(m-1,m+n-2);
        System.out.println(result);
        System.out.println(result2);
        System.out.println(result3);
    }
}
