package com.learn.day09;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 输入: 正数数组costs、正数数组profits、正数K、正数M
 * costs[i]表示i号项目的花费
 * profits[i]表示i号项目在扣除花费之后还能挣到的钱(利润)
 * K表示你只能串行的最多做k个项目
 * M表示你初始的资金
 * 说明: 每做完一个项目，马上获得的收益，可以支持你去做下一个项目。不能并行的做项目。
 * 输出：你最后获得的最大钱数。
 */
// 思路， 未解锁的项目放到 小根堆里， 解锁项目放大根堆
public class IPO {

    public static class Item{
        int cost ;
        int profit ;
        Item(int cost,int profit){
            this.cost = cost;
            this.profit = profit;
        }
    }


    public static class smallComparetor implements Comparator<Item>{

        @Override
        public int compare(Item o1, Item o2) {
            return o2.profit - o1.profit;
        }
    }

    public static class bigComparetor implements Comparator<Item>{

        @Override
        public int compare(Item o1, Item o2) {
            return o1.cost - o2.cost;
        }
    }
    public static int getIPO(int[] costs,int[] profits,int k,int m){
        if (costs ==null || profits == null || costs.length != profits.length){
            return m;
        }
        PriorityQueue<Item> remain = new PriorityQueue<>(new smallComparetor());
        PriorityQueue<Item> canUse = new PriorityQueue<>(new bigComparetor());


        for (int i =0; i < costs.length ; i++){
            remain.add(new Item(costs[i],profits[i]));
        }

        for (int i= 0;i<k;i++){
            // 1、还有未解锁项目，
            // 2、小根堆堆顶的项目花费能cover住
            while (!remain.isEmpty() && remain.peek().cost <= m){
                canUse.add(remain.poll());
            }
            if (canUse.isEmpty()){
                return m;
            }
            m += canUse.poll().profit;
        }

        return m;
    }
    public static void main(String[] args) {

    }

}
