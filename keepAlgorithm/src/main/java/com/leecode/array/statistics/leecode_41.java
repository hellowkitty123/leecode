package com.leecode.array.statistics;

import static com.sun.tools.javac.jvm.ByteCodes.swap;

/**
 * 41. 缺失的第一个正数 难度：困难
 * 给你一个未排序的整数数组，请你找出其中没有出现的最小的正整数。
 *
 *  
 *
 * 示例 1:
 *
 * 输入: [1,2,0]
 * 输出: 3
 * 示例 2:
 *
 * 输入: [3,4,-1,1]
 * 输出: 2
 * 示例 3:
 *
 * 输入: [7,8,9,11,12]
 * 输出: 1
 *  
 *
 * 提示：
 *
 * 你的算法的时间复杂度应为O(n)，并且只能使用常数级别的额外空间。
 *
 *
 * 排序法	平均时间	最差情形	    稳定度	     额外空间	备注
 * 冒泡	    O(n2)	    O(n2)	稳定	         O(1)	    n小时较好
 * 选择	    O(n2)	    O(n2)	不稳定	     O(1)	    n小时较好
 * 插入	    O(n2)	    O(n2)	稳定	         O(1)	    大部分已排序时较好
 * 基数	    O(logRB)	O(logRB)稳定	         O(n)
 * B是真数(0-9)，
 * R是基数(个十百)
 * Shell    O(nlogn)	O(ns)   1<s<2不稳定	 O(1)	    s是所选分组
 * 快速	    O(nlogn)	O(n2)	不稳定	     O(nlogn)	n大时较好
 * 归并	    O(nlogn)	O(nlogn)稳定	         O(1)	    n大时较好
 * 堆	    O(nlogn)	O(nlogn)不稳定	     O(1)	    n大时较好
 */



public class leecode_41 {
    public static void main(String[] args) {
//        int[] arr = {3,4,-1,1};
                int[] arr = {7,8,9,11,12};
        int pos =  funOne(arr);
        System.out.println(pos);
    }
    private static int funOne(int[] arr){
        int tmp = 0;
        for (int i=0;i<arr.length;i++){
            while (arr[i]>0 && arr[i]<arr.length && arr[arr[i]-1]!= arr[i]){
                tmp= arr[arr[i]-1];
                arr[arr[i]-1]=arr[i];
                arr[i] = tmp;
            }
        }
        for (int i=0;i<arr.length;i++){
            if (arr[i]!=i+1){
                return i+1;
            }
        }
        return arr.length+1;
    }
}
