package com.learn.train01.day01;

import java.util.LinkedList;

/**
 * 滑动窗口、首尾指针法， 问题和范围建立单调性
 * 给定一个整型数组arr，和一个整数num
 * 某个arr中的子数组sub，如果想达标，必须满足：
 * sub中最大值 – sub中最小值 <= num，
 * 返回arr中达标子数组的数量
 */
public class AllLessNumSubArray {
    // 子数组是连续的，而且具有单调性
    // i... 到最大窗口位置，  i... 所有子数组都满足条件
    public static int allLessNumSumArray(int[]arr ,int num){
        if (arr == null || arr.length ==0 || num <= 0){
            return 0;
        }

        int len = arr.length;

        LinkedList<Integer> maxList = new LinkedList<>();
        LinkedList<Integer> minList = new LinkedList<>();
        // [0 , 0)
        int L = 0;
        int R = 0;
        int sum = 0;
        while (L < len) {


            while (L < len && R < len) {
                while (!maxList.isEmpty() && arr[maxList.peekLast()] <= arr[R]) {
                    maxList.pollLast();
                }
                maxList.add(R);
                while (!minList.isEmpty() && arr[minList.peekLast()] >= arr[R]) {
                    minList.pollLast();
                }
                minList.add(R);

                if (L == R || arr[maxList.peekFirst()] - arr[minList.peekFirst()] <= num) {
                    System.out.println("--- maxList " + arr[maxList.peekFirst()] + " ----minList " + arr[minList.peekFirst()]);
                    R++;
                }
                if (arr[maxList.peekFirst()] - arr[minList.peekFirst()] > num){
                    break;
                }
//                R++;
            }

            sum += R - L;

            // 当前循环结束，后面是状态移动
            if (maxList.peekFirst() == L) {
                maxList.pollFirst();
            }

            if (minList.peekFirst() == L) {
                minList.pollFirst();
            }
            L++;

        }
        return sum;
    }

    // for test
    public static int[] getRandomArray(int len) {
        if (len < 0) {
            return null;
        }
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * 10);
        }
        return arr;
    }

    // for test
    public static void printArray(int[] arr) {
        if (arr != null) {
            for (int i = 0; i < arr.length; i++) {
                System.out.print(arr[i] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int[] arr = getRandomArray(30);
        arr = new int[]{2, 6, 0, 9, 6, 3, 6, 8, 5, 0, 1, 5, 5, 3, 2, 3, 8, 0, 8, 5, 9, 6, 6, 3, 8, 6, 0, 6, 5, 9 };
        int num = 5;
        printArray(arr);
        System.out.println(allLessNumSumArray(arr, num));

    }

}
