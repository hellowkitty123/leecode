package com.learn.day11;

public class Hanoi {
    /**
     * 汉诺塔问题 解决步骤
     * 1、n-1 层 从from 移动到 other 腾路
     * 2、n   层 从from 移动到 to
     * 3、n-1 层 从other移动到 to
     * @param N
     */
    public static void hanoi(int N ){
        if (N == 1){
            System.out.println("Move 1 from left to right");
            return;
        }
        func(N,"left","right","mid");
    }

    public static void func(int N,String from,String to,String other){
        if (N == 1){
            System.out.println("Move 1 from "+from+" to "+to);
            return;
        }
        func(N-1,from,other,to);
        System.out.println("Move "+ N + " from " + from + " to "+ to  );
        func(N-1,other,to,from);
    }


    public static void main(String[] args) {
        int n =3;
        hanoi(n);

    }
}
