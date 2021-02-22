package com.learn.day12;


/**
 *  arr中都是正数且无重复值，返回组成aim的方法数
 */
public class CoinsWay {

    public static int coinsWay(int[] arr,int aim){
        if (arr == null || arr.length == 0 || aim < 0){
            return  0;
        }

        return process(arr,0,aim);

    }


    // 使用[index...] index 及其往后的数字，搞定剩余rest 数值，所有的方法数
    public static int process(int[] arr,int index,int rest){
//        if (rest < 0){
//            return 0;
//        }
        // 左右数字已用完，如果此时rest刚好为0，说明搞定了，如果不是 0，说明搞不定
        if (index == arr.length){
            return rest == 0 ? 1 : 0;
        }
        int result = 0;
        for (int zhang=0;zhang * arr[index]<=rest;zhang++){
            result += process(arr,index+1,rest- zhang * arr[index]);
        }
        return result;
    }


    public static int  coinsWay2(int[] arr,int aim){
        if (arr == null || arr.length == 0 || aim < 0){
            return  0;
        }
        int n = arr.length;
        int[][] dp = new int[n+1][aim+1];

        dp[n][0] = 1;
        for (int index = n-1 ;index >= 0;index--){
            for (int rest = 0;rest <= aim;rest++){

                int result = 0;
//                for (int zhang=0;zhang * arr[index]<rest;zhang++){
//                    result += dp[index+1][rest- zhang * arr[index]];
//                }


                result = dp[index+1][rest];
                //当前行左边位置如果存在，就累加
                if (rest - arr[index] >= 0){
                    result += dp[index][rest - arr[index]];
                }
                dp[index][rest] = result;

            }
        }

        return dp[0][aim];
    }


    public static void main(String[] args) {
        int[] arr = { 5, 10,50,100 };
        int sum = 1000;
        System.out.println(coinsWay(arr, sum));
        System.out.println(coinsWay2(arr, sum));
    }
}
