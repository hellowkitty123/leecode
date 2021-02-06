package com.learn.day01;

public class BSExist {
    public static boolean BSExist(int[] sortArr,int num){
        if (sortArr ==  null ){
            return false;
        }
        int L = 0;
        int R = sortArr.length;
        while (L < R){
            //位运算括起来
            int mid =  L + ((R-L) >> 1);
            if (sortArr[mid] > num){
                R = mid - 1;
            }else if (sortArr[mid] < num){
                L = mid + 1;
            }else{
                return true;
            }
            System.out.println("L : "+ L + " R : "+R+" mid : "+mid);
        }
        return false;
    }
    // 小于等于最左的位置
    public static int BSNearLeft(int[] sortArr ,int num){
        if (sortArr ==  null ){
            return -1;
        }
        int L = 0;
        int R = sortArr.length;
        int index = -1;
        while (L <= R){
            //位运算括起来
            int mid =  L + ((R-L) >> 1);
            System.out.println("L : "+ L + " R : "+R+" mid : "+mid);
            if (sortArr[mid] > num){

                R = mid - 1;
            }else if (sortArr[mid] < num){
                L = mid + 1;
            }else{
                R = mid - 1;
                index = mid;
            }

        }
        return index;
    }
    public static void main(String[] args) {
        int[] sortArr = {1, 1, 1, 2, 3, 4, 4, 5, 6, 6, 7, 8, 32, 77};
        boolean result = BSExist(sortArr,50);
//        System.out.println(result);
        int index = BSNearLeft(sortArr,1);
        System.out.println(index);
    }
}
