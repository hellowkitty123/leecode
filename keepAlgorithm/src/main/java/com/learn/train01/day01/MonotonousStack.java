package com.learn.train01.day01;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class MonotonousStack {

    public static int[][] getNearLessNoRepeat(int[]arr){
        if (arr == null || arr.length == 0){
            return null;
        }
//        System.out.println(Arrays.toString(arr));
        int n = arr.length;
        int[][] result = new int[n][2];

        Stack<Integer> stack = new Stack<>();


        for (int i=0;i<n;i++){
            int cur = arr[i];

            // 弹出是记录答案
            while (!stack.isEmpty() && arr[stack.peek()] > cur){
                int pos = stack.pop();
                result[pos][1] = i;
                result[pos][0] = stack.isEmpty() ? -1 : stack.peek();
            }
            stack.add(i);
        }

        while (!stack.isEmpty()){
            int pos = stack.pop();
            result[pos][1] = -1;
            result[pos][0] = stack.isEmpty()?-1:stack.peek();
        }
        return  result;
    }

    public static int[][] getNearLess(int[]arr){

        if (arr == null || arr.length == 0){
            return null;
        }
//        System.out.println(Arrays.toString(arr));
        int n = arr.length;
        int[][] result = new int[n][2];

        Stack<ArrayList<Integer>> stack = new Stack<>();


        for (int i=0;i<n;i++){
            int cur = arr[i];

            // 弹出是记录答案
            while (!stack.isEmpty() && arr[stack.peek().get(0)] > cur){
                ArrayList<Integer> posList = stack.pop();
                for (Integer pos : posList){
                    result[pos][1] = i;
                    result[pos][0] = stack.isEmpty() ? -1 : stack.peek().get(stack.peek().size() - 1);
                }
            }
            if (!stack.isEmpty() && arr[stack.peek().get(0)] == arr[i]){
                stack.peek().add(i);
            }else{
                ArrayList<Integer> list = new ArrayList<>();
                list.add(i);
                stack.add(list);
            }
        }

        while (!stack.isEmpty()){

            ArrayList<Integer> posList = stack.pop();
            for (Integer pos : posList){
                result[pos][1] = -1;
                result[pos][0] = stack.isEmpty()?-1:stack.peek().get(stack.peek().size() - 1);
            }
        }
        return  result;
    }


    // for test
    public static int[] getRandomArrayNoRepeat(int size) {
        int[] arr = new int[(int) (Math.random() * size) + 1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
        for (int i = 0; i < arr.length; i++) {
            int swapIndex = (int) (Math.random() * arr.length);
            int tmp = arr[swapIndex];
            arr[swapIndex] = arr[i];
            arr[i] = tmp;
        }
        return arr;
    }

    // for test
    public static int[] getRandomArray(int size, int max) {
        int[] arr = new int[(int) (Math.random() * size) + 1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * max) - (int) (Math.random() * max);
        }
        return arr;
    }

    // for test
    public static int[][] rightWay(int[] arr) {
        int[][] res = new int[arr.length][2];
        for (int i = 0; i < arr.length; i++) {
            int leftLessIndex = -1;
            int rightLessIndex = -1;
            int cur = i - 1;
            while (cur >= 0) {
                if (arr[cur] < arr[i]) {
                    leftLessIndex = cur;
                    break;
                }
                cur--;
            }
            cur = i + 1;
            while (cur < arr.length) {
                if (arr[cur] < arr[i]) {
                    rightLessIndex = cur;
                    break;
                }
                cur++;
            }
            res[i][0] = leftLessIndex;
            res[i][1] = rightLessIndex;
        }
        return res;
    }

    // for test
    public static boolean isEqual(int[][] res1, int[][] res2) {
        if (res1.length != res2.length) {
            return false;
        }
        for (int i = 0; i < res1.length; i++) {
            if (res1[i][0] != res2[i][0] || res1[i][1] != res2[i][1]) {
                return false;
            }
        }

        return true;
    }

    // for test
    public static void printArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int size = 10;
        int max = 20;
        int testTimes = 10000;
        for (int i = 0; i < testTimes; i++) {
            int[] arr1 = getRandomArrayNoRepeat(size);
            arr1 = new int[]{1, 2, 7, 0, 8, 5, 3, 6, 4};
            int[] arr2 = getRandomArray(size, max);
            if (!isEqual(getNearLessNoRepeat(arr1), rightWay(arr1))) {
                System.out.println("Oops!");
                printArray(arr1);
                break;
            }
            if (!isEqual(getNearLess(arr2), rightWay(arr2))) {
                System.out.println("Oops!");
                printArray(arr2);
                break;
            }
        }
    }
}
