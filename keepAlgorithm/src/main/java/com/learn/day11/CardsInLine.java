package com.learn.day11;


// 范围上的尝试模型1
/**
 *
 * 给定一个整型数组arr，代表数值不同的纸牌排成一条线，
 * 玩家A和玩家B依次拿走每张纸牌，
 * 规定玩家A先拿，玩家B后拿，
 * 但是每个玩家每次只能拿走最左或最右的纸牌，
 * 玩家A和玩家B都绝顶聪明。请返回最后获胜者的分数。
 */
public class CardsInLine {

    public static int cardInLine(int[] arr){
        if (arr == null){
            return 0;
        }
        // 主函数 先手 和 后手 从L 到R范围上价值最大
        return Math.max(f(arr,0,arr.length-1),s(arr,0,arr.length-1));
    }
    // 先手视角
    public static int f(int[] arr, int L, int R){
        // base case 如果剩一张牌，而且此时我是先手，那么直接拿走
        if (L == R){
            return arr[L];
        }
        int resL = arr[L] + s(arr,L+1,R);
        int resR = arr[R] + s(arr,L ,R-1);
        // 先手拿走 L 、R 上价值最大的牌
        return Math.max(resL,resR);
    }
    // 后手视角，后手没法选，
    public static int s(int[] arr, int L , int R){
        //如果我是后手 之剩一张牌，先手拿走,后手没牌可拿
        if (L == R){
            return 0;
        }

        int resL = f(arr,L+1,R);
        int resR = f(arr,L ,R-1);
        // 对手一定选让我最差的
        return Math.min(resL,resR);
    }

    public static int cardInLine2(int[] arr){
        if (arr == null){
            return 0;
        }
        int n = arr.length;
        int[][] dpf = new int[n][n];
        int[][] dps = new int[n][n];

        for (int i=0;i<n;i++){
            dpf[i][i] = arr[i];
        }

        for(int i=1 ;i<n;i++){
            int L = 0;
            int R = i;
            while (L < n && R < n){

                int resL = arr[L] + s(arr,L+1,R);
                int resR = arr[R] + s(arr,L ,R-1);
                // 先手拿走 L 、R 上价值最大的牌
                dps[L][R] = Math.max(resL,resR);

                resL = dpf[L+1][R];
                resR = dpf[L][R-1];
                // 对手一定选让我最差的
                dpf[L][R] =  Math.min(resL,resR);


                L ++;
                R ++;
            }
        }


        // 主函数 先手 和 后手 从L 到R范围上价值最大
        return Math.max(dpf[0][arr.length-1],dps[0][arr.length-1]);
    }


    public static void main(String[] args) {
        int[] arr = { 4,7,9,5,19,29,80,4 };
        // A 4 9
        // B 7 5
        System.out.println(cardInLine(arr));
        System.out.println(cardInLine2(arr));
    }
}
