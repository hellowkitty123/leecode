package com.learn.day12;

/** 机器人走路 动态规划，记忆化搜索
 * 假设有排成一行的N个位置，记为1~N，N 一定大于或等于 2
 * 开始时机器人在其中的M位置上(M 一定是 1~N 中的一个)
 * 如果机器人来到1位置，那么下一步只能往右来到2位置；
 * 如果机器人来到N位置，那么下一步只能往左来到 N-1 位置；
 * 如果机器人来到中间位置，那么下一步可以往左走或者往右走；
 * 规定机器人必须走 K 步，最终能来到P位置(P也是1~N中的一个)的方法有多少种
 * 给定四个参数 N、M、K、P，返回方法数。
 */
public class RobotWalk {

    /**
     * 机器人走格子
     * @param n  一共几个格子
     * @param f  从第几个格子出发
     * @param t  到第几个格子
     * @param k  走几步
     * @return   方法数个数
     */
    public  static  int robotWalk(int n,int f,int k,int t){
        if(n <= 1 || k < 1 || f <= 0 || t <= 0 || f > n || t >n){
            return 0;
        }

        return walk(n,f,t,k);
    }

    public static int walk(int n ,int t , int index, int rest){
        if (rest == 0){
            return index == t ? 1 : 0;
        }

        if (index == 1){
            return walk(n, t, index+1,rest - 1);
        }

        if (index == n){
            return  walk(n,t , index-1,rest-1);
        }

        return walk(n,t,index-1,rest-1) + walk(n,t, index+1,rest-1);
    }

    //该函数的含义：只能在1~N这些位置上移动，当前在cur位置，走完rest步之后，停在P位置的方法数作为返回值返回
    public  static  int robotWalk2(int n,int f,int k,int t){
        if(n <= 1 || k < 1 || f <= 0 || t <= 0 || f > n || t >n){
            return 0;
        }
        int[][] dp = new int[k+1][n+1];
        dp[0][t] = 1; // 来到t位置的basecase
        for (int step = 1;step < k+1;step++){
            for (int pos = 1;pos < n+1;pos ++){
                if (pos == 1){
                    dp[step][pos] = dp[step-1][pos+1];
                }else if (pos == n){
                    dp[step][pos] = dp[step-1][pos-1];
                }else {
                    dp[step][pos] = dp[step-1][pos-1]+dp[step-1][pos+1];
                }

            }
        }
        return dp[k][f];
    }



    public static void main(String[] args) {
        System.out.println(robotWalk(7, 4, 9, 5));
        System.out.println(robotWalk2(7, 4, 9, 5));
    }
}
