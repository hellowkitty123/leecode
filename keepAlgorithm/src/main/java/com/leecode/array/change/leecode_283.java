package com.leecode.array.change;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 283. 移动零 难度：简单
 * 给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
 *
 * 示例:
 *
 * 输入: [0,1,0,3,12]
 * 输出: [1,3,12,0,0]
 * 说明:
 *
 * 必须在原数组上操作，不能拷贝额外的数组。
 * 尽量减少操作次数。
 *
 */
public class leecode_283 {
    public static void main(String[] args) {
        int[] arr = {0,1,0,3,12};
        arr = funcOne(arr);
        System.out.println(Arrays.toString(arr));
    }
    private static int[] funcOne(int[] arr){
        int j = 0;
        for (int i=0; i<arr.length;i++){
            if (arr[i] != 0 ){
                arr[j++]=arr[i];
                arr[i]=0;
                System.out.println(j);
            }
            System.out.println(Arrays.toString(arr));

        }
        return arr;
    }
}
