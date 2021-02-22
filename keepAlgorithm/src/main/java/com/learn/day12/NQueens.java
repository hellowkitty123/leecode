package com.learn.day12;

public class NQueens {

    public static int getNQueens(int n){
        if (n <= 1){
            return 0;
        }
        int[] arr = new int[n];
        return process(0,arr,arr.length);
    }

    public static int process(int index,int[] arr,int n){
        if (index == n){
            return 1;
        }

        int res = 0;
        for (int k=0;k<n;k++){
            if (isValid(arr,index,k)){
                arr[index] = k;
                res += process(index+1,arr,n);
            }
        }

        return res;
    }

    public static int process1(int i, int[] record, int n) {
        if (i == n) { // 终止行
            return 1;
        }
        // 没有到终止位置，还有皇后要摆
        int res = 0;
        for (int j = 0; j < n; j++) { // 当前行在i行，尝试i行所有的列  -> j
            // 当前i行的皇后，放在j列，会不会和之前(0..i-1)的皇后，不共行共列或者共斜线，
            // 如果是，认为有效
            // 如果不是，认为无效
            if (isValid(record, i, j)) {
                record[i] = j;
                res += process1(i + 1, record, n);
            }
        }
        return res;
    }

    public static boolean isValid(int[] record, int i, int j) {
        for (int k = 0; k < i; k++) { // 之前的某个k行的皇后
            // k, record[k]   i, j
            if (j == record[k] || Math.abs(record[k] - j) == Math.abs(i - k)) {
                return false;
            }
        }
        return true;
    }

//    public static boolean isvald(int index,int[] arr,int i){
//        for (int k=0;k < index;k++){
//            if (arr[k] == i || Math.abs(k - index) == Math.abs(arr[k] - i)){
//                return false;
//            }
//        }
//
//        return true;
//    }

    public static void main(String[] args) {
        System.out.println(getNQueens(8));
    }
}
