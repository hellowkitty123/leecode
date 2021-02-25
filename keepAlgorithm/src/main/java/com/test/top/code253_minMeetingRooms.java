package com.test.top;


import java.security.PublicKey;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 253. 会议室 II
 * 给你一个会议时间安排的数组 intervals ，每个会议时间都会包括开始和结束的时间 intervals[i] = [starti, endi] ，为避免会议冲突，同时要考虑充分利用会议室资源，请你计算至少需要多少间会议室，才能满足这些会议安排。
 *
 * 示例 1：
 * 输入：intervals = [[0,30],[5,10],[15,20]]
 * 输出：2
 *
 * 示例 2：
 * 输入：intervals = [[7,10],[2,4]]
 * 输出：1
 */
public class code253_minMeetingRooms {

    public static  class Program{
        public int start;
        public int end;
        public Program(int start ,int end){
            this.start = start;
            this.end = end;
        }
    }


    public static class Compara1 implements Comparator<Program>{

        @Override
        public int compare(Program o1, Program o2) {
            return o1.start - o2.start;
        }
    }

    public static class Compara2 implements Comparator<Integer>{
        @Override
        public int compare(Integer o1,Integer o2){
            return o1 - o2;
        }
    }

    public static int minMeetingRooms(int[][] intervals){
        if (intervals == null || intervals.length == 0){
            return 0;
        }
        // 所有的回忆
        PriorityQueue<Program> heapProgram = new PriorityQueue<>(new Compara1());

        for (int[] pro : intervals){
            heapProgram.add(new Program(pro[0],pro[1]));
        }
        // 回忆最后结束时间
        PriorityQueue<Integer> heapRoom = new PriorityQueue<>(new Compara2());

        while (!heapProgram.isEmpty()){

            Program pro = heapProgram.poll();
            if (heapRoom.isEmpty() || heapRoom.peek() > pro.start){
                heapRoom.add(pro.end);
                continue;
            }

            heapRoom.poll();
            heapRoom.add(pro.end);

        }


        return heapRoom.size();
    }

    public static void main(String[] args) {
        int[][] intervals= new int[][]{{0,30},{5,10},{15,20}};
        intervals= new int[][]{{7,10},{2,4}};
        intervals= new int[][]{{6,15},{13,20},{6,17}};

        int result = minMeetingRooms(intervals);
        System.out.println(result);
    }
}
