package com.learn.day09;

import java.util.Comparator;
import java.util.PriorityQueue;

public class LessMoneySplitGold {

    /**
     * 哈夫曼树
     *
     * 一块金条切成两半，是需要花费和长度数值一样的铜板的。
     * 比如长度为20的金条，不管怎么切，都要花费20个铜板。 一群人想整分整块金条，怎么分最省铜板?
     *
     * 例如,给定数组{10,20,30}，代表一共三个人，整块金条长度为60，金条要分成10，20，30三个部分。
     *
     * 如果先把长度60的金条分成10和50，花费60; 再把长度50的金条分成20和30，花费50;一共花费110铜板。
     * 但如果先把长度60的金条分成30和30，花费60;再把长度30金条分成10和20， 花费30;一共花费90铜板。
     * 输入一个数组，返回分割的最小代价。
     *
     */

    public static class MyComparetor implements Comparator<Integer>{

        @Override
        public int compare(Integer o1, Integer o2) {
            return o1 - o2;
        }
    }


    public static int getLessMoneySplitGold(int[] arr){

        // 代价低的先合并，总代价最低
        PriorityQueue<Integer> stack = new PriorityQueue<>(new MyComparetor());

        for (int a : arr){
            stack.add(a);
        }
        int sum = 0;
        while (stack.size() > 1){
            int cur = stack.poll() + stack.poll();
            sum += cur;
            stack.add(cur);
        }
        return sum;
    }

    public static void main(String[] args) {
        int[] arr = new int[]{ 1 ,2 ,4, 5,7,8};
        int a = getLessMoneySplitGold(arr);
        System.out.println(a);
    }
}
