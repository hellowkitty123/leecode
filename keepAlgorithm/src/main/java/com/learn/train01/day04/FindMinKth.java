package com.learn.train01.day04;

import java.util.Arrays;

/**
 * 找到第k小的数  笔试荷兰国旗问题， 面试bfprt
 */
public class FindMinKth {

    // 数组一定要copy
    public static int findMinKth(int[] aarr, int k){
        if (aarr == null || aarr.length == 0 || k <1){
            return -1;
        }
        int[] arr = copyArray(aarr);
        return bfprt(arr,0,arr.length-1,k-1);
    }
    // 假如排序的话，index就是第k小的数
    public static int bfprt(int[] arr, int L ,int R, int index){
        if (L==R){
            return arr[L];
        }
        int provit = getMedianOfMedians(arr,L,R);

        int[] result = partition(arr,L,R ,provit);
        if (result[0] <= index && result[1] >= index){
            return arr[result[0]];
        }else if (result[0] > index){
            return bfprt(arr,L , result[0]-1,index);
        }else{
            return bfprt(arr,result[1]+1 ,R ,index);
        }
    }
    // bug 1 pivot 数值类型 不是索引
    public static int[] partition(int[]arr,int L,int R,int provit){
        int less = L - 1;
        int move = L;
        int more = R + 1;

        while (move < more){
            if (arr[move] == provit){
                move++;
            }else if (arr[move] < provit){
                swap(arr,move++,++less);
            }else{
                swap(arr,move, --more);
            }
        }

        return new int[]{++less,--more};
    }

    private static void swap(int[]arr,int i ,int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }


    // 天选之子的位置
    public static int getMedianOfMedians(int[] arr,int L ,int R){
        int size =(R-L+1);
        int offset = size % 5 == 0 ? 0 : 1;
        int[] mArr = new int[size / 5 + offset];
        for (int i=0;i<mArr.length;i++){
            mArr[i] = getMedian(arr,L+i*5, Math.min(L + i * 5 + 4, R));
        }
        // bug 3 天选之子的范围是mArr 范围的
        return bfprt(mArr,0,mArr.length-1,mArr.length/2);
    }

    public static int getMedian(int[] arr, int L ,int R){
//        System.out.println("------------------------------ L " + L + " R "+ R);
        for (int i = L+1;i< R;i++){
            for (int j = i-1;j>=L;j--){
                if (arr[j] > arr[j+1]){
                    swap(arr,j,j+1);
                }
            }
        }
//        insertionSort(arr,L,R);
        return arr[L+(R-L)/2];
    }

    public static void insertionSort(int[] arr, int L, int R) {

        for (int i = L + 1; i <= R; i++) {
            System.out.println("------");
            for (int j = i - 1; j >= L && arr[j] > arr[j + 1]; j--) {
                swap(arr, j, j + 1);
            }
        }
    }


    // 改写快排，时间复杂度O(N)
    public static int minKth2(int[] array, int k) {
        int[] arr = copyArray(array);
        return process2(arr, 0, arr.length - 1, k - 1);
    }

    public static int[] copyArray(int[] arr) {
        int[] ans = new int[arr.length];
        for (int i = 0; i != ans.length; i++) {
            ans[i] = arr[i];
        }
        return ans;
    }

    // arr 第k小的数
    // process2(arr, 0, N-1, k-1)
    // arr[L..R]  范围上，如果排序的话(不是真的去排序)，找位于index的数
    // index [L..R]
    public static int process2(int[] arr, int L, int R, int index) {
        if (L == R) { // L = =R ==INDEX
            return arr[L];
        }
        // 不止一个数  L +  [0, R -L]
        int pivot = arr[L + (int) (Math.random() * (R - L + 1))];

        // range[0] range[1]
        //  L   ..... R     pivot
        //  0         1000     70...800
        int[] range = partition2(arr, L, R, pivot);
        if (index >= range[0] && index <= range[1]) {
            return arr[index];
        } else if (index < range[0]) {
            return process2(arr, L, range[0] - 1, index);
        } else {
            return process2(arr, range[1] + 1, R, index);
        }
    }


    public static int[] partition2(int[] arr, int L, int R, int pivot) {
        int less = L - 1;
        int more = R + 1;
        int cur = L;
        while (cur < more) {
            if (arr[cur] < pivot) {
                swap(arr, ++less, cur++);
            } else if (arr[cur] > pivot) {
                swap(arr, cur, --more);
            } else {
                cur++;
            }
        }
        return new int[] { less + 1, more - 1 };
    }



    // for test
    public static int[] generateRandomArray(int maxSize, int maxValue) {
        int[] arr = new int[(int) (Math.random() * maxSize) + 1];
//        int[] arr = new int[(int) (maxSize) + 1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * (maxValue + 1));
        }
        return arr;
    }

    public static void main(String[] args) {
        int testTime = 100000;
        int maxSize = 100;
        int maxValue = 100;
        System.out.println("test begin");
        for (int i = 0; i < testTime; i++) {
            int[] arr = generateRandomArray(maxSize, maxValue);
//            System.out.println(Arrays.toString(arr));
            int k = (int) (Math.random() * arr.length) + 1;
            int ans1 = findMinKth(arr, k);
            int ans2 = minKth2(arr, k);
            if (ans1 != ans2) {
                System.out.println("Oops!");
            }
        }
        System.out.println("test finish");
    }
}
