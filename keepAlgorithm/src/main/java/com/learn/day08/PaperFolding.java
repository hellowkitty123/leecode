package com.learn.day08;

public class PaperFolding {

    public static void printAllFolds(int N){
        if (N < 1){
            return;
        }
        //从折一次
        process(1,N,true);
    }

    public static void process(int i ,int N, boolean isDown){
        if (i > N){
            return;
        }

        process(i+1,N,true);
        System.out.println(isDown ? "凹" : "凸");
        process(i+1,N,false);
    }



    public static void main(String[] args) {
        printAllFolds(3);
    }

}
