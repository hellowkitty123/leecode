package com.learn.train01.day01;

/**
 * 马从（0，0） 点 到 x，y位置，走k步，总共有多少中方法数
 */
public class HorseJump {
    // 马来到 xy 位置 ，走了k步有多少种方法数
    public static int getHorsejumpCount(int x,int y ,int k){
        if (x <0 || y < 0 || k < 0){
            return 0;
        }

        return process(x,y,k);
    }

    public static int process(int x, int y, int k){
        //base case
        if(k == 0){
            return x == 0 && y == 0 ? 1 : 0;
        }

        if (x<0 || x>9 || y<0 || y>8){
            return 0;
        }

        return process(x+2,y-1,k-1)+
                process(x+2,y+1,k-1)+
                process(x-2,y-1,k-1)+
                process(x-2,y+1,k-1)+
                process(x+1,y-2,k-1)+
                process(x+1,y+2,k-1)+
                process(x-1,y-2,k-1)+
                process(x-1,y+2,k-1);
    }

    public static int getHorsejumpCount2(int x,int y ,int k){
        if (x < 0 || y < 0 || k < 0){
            return 0;
        }

        int[][][] dp = new int[10][9][k+1];
        dp[0][0][0] = 1;

        for (int step = 1;step<=k;step++) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 9; j++) {
                    dp[i][j][step] =
                            getValue(dp, x + 2, y - 1, k - 1) +
                            getValue(dp, x + 2, y + 1, k - 1) +
                            getValue(dp, x - 2, y - 1, k - 1) +
                            getValue(dp, x - 2, y + 1, k - 1) +
                            getValue(dp, x + 1, y - 2, k - 1) +
                            getValue(dp, x + 1, y + 2, k - 1) +
                            getValue(dp, x - 1, y - 2, k - 1) +
                            getValue(dp, x - 1, y + 2, k - 1);
                }
            }
        }

        return dp[x][y][k];
    }


    public static int getValue(int[][][] dp, int x,int y,int step){
        if (x<0 || x>9 || y<0 || y>8){
            return 0;
        }

        return dp[x][y][step];
    }

    public static void main(String[] args) {
        int x = 7;
        int y = 7;
        int step = 10;

        System.out.println(getHorsejumpCount(x, y, step));
        System.out.println(getHorsejumpCount2(x, y, step));

    }
}
