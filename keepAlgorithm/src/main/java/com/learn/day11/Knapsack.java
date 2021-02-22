package com.learn.day11;


// 从左往右的尝试模型2
/**
 * 经典背包问题 暴力求解
 * 给定两个长度都为N的数组weights和values，
 * weights[i]和values[i]分别代表 i号物品的重量和价值。
 * 给定一个正数bag，表示一个载重bag的袋子，
 * 你装的物品不能超过这个重量。
 * 返回你能装下最多的价值是多少?
 */
// 暴力递归改动态规划是否有重复过程
public class Knapsack {


    public static int knapsack(int[] w ,int[] v ,int bag){
        if (bag == 0){
            return 0;
        }

        return process(w,v,0,bag);
    }

    /**
     * 	只剩下rest的空间了，
     * 	index...货物自由选择，但是剩余空间不要小于0
     * 	返回 index...货物能够获得的最大价值
     * @param w     每个货物重量
     * @param v     每个货物价值
     * @param index 当前来的货物的位置
     * @param rest  剩余可利用背包空间
     * @return      货物最高价值
     */
    public static int process(int[] w,int[] v,int index, int rest){
        // base case1 index。。货物自由选择，空间已经满了
        if (rest <0){
            return -1;
        }
        // base case  空间没满，但是货物已经用完
        if (index == w.length){
            return 0;
        }
        // 不要 index 位置的货物
        int res = process(w,v,index+1,rest);
        // 要了 index 位置货物，背包空间减少
        int p = -1;
        // 如果 要了 index 货物之后 剩余空间变成负数，当前可能性无效
        int res2 = process(w,v,index+1,rest-w[index]);
        if (res2 != -1){
            p = res2 + v[index];
        }
        return Math.max(res,p);
    }


    public static int dpWay(int[] w ,int[] v ,int bag){
        if (bag == 0){
            return 0;
        }
        int len = w.length;
        // n 位置 对应下标 长度n+1, base case bag 能到bag下标
        int[][] dp = new int[len+1][bag+1];

        // 默认 index = bag位置的值 为0
        for (int index = len-1;index>=0;index--){
            for (int rest = 0;rest <= bag;rest++){
                // 不要 index 位置的货物
                int res = dp[index+1][rest];
                // 要了 index 位置货物，背包空间减少
                int p = -1;
                // 如果 要了 index 货物之后 剩余空间变成负数，当前可能性无效
                if (rest-w[index] >= 0){
                    p = dp[index+1][rest-w[index]] + v[index];
                }
                dp[index][rest] = Math.max(res,p);
            }
        }
        return dp[0][bag];
    }

    public static void main(String[] args) {
        int[] weights = { 3, 2, 4, 7 };
        int[] values = { 5, 6, 3, 19 };
        int bag = 11;
        System.out.println(knapsack(weights, values, bag));
        System.out.println(dpWay(weights, values, bag));
    }
}
