package com.leecode.array.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 697. 数组的度 难度：简单
 * 给定一个非空且只包含非负数的整数数组 nums, 数组的度的定义是指数组里任一元素出现频数的最大值。
 *
 * 你的任务是找到与 nums 拥有相同大小的度的最短连续子数组，返回其长度。
 *
 * 示例 1:
 *
 * 输入: [1, 2, 2, 3, 1]
 * 输出: 2
 * 解释:
 * 输入数组的度是2，因为元素1和2的出现频数最大，均为2.
 * 连续子数组里面拥有相同度的有如下所示:
 * [1, 2, 2, 3, 1], [1, 2, 2, 3], [2, 2, 3, 1], [1, 2, 2], [2, 2, 3], [2, 2]
 * 最短连续子数组[2, 2]的长度为2，所以返回2.
 * 示例 2:
 *
 * 输入: [1,2,2,3,1,4,2]
 * 输出: 6
 * 注意:
 *
 * nums.length 在1到50,000区间范围内。
 * nums[i] 是一个在0到49,999范围内的整数。
 */
public class leecode_697 {
    public static void main(String[] args) {
//        int[] arr = {1,2,2,3,1,4,2};
        int[] arr = {1, 2, 2, 3, 1};
        int min = funcOne(arr);
        System.out.println(min);
    }
    private static int funcOne(int[] arr){
        Map<Integer,ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>();
        for (int i=0;i<arr.length ; i++){
            if (map.get(arr[i]) == null){
                ArrayList<Integer> rst = new ArrayList<Integer>();
                rst.add(i);
                rst.add(50000);
                rst.add(1);
                map.put(arr[i],rst);
            }else{
                map.get(arr[i]).set(1,i);
                map.get(arr[i]).set(2,map.get(arr[i]).get(2)+1);
            }
            System.out.println(map);
        }
        int min = 50000;
        int degree = -1;
        for(ArrayList<Integer> value: map.values()){
            degree = Math.max(degree,value.get(2));
        }

        for (Integer key: map.keySet()){
            if (degree == map.get(key).get(2)){
                min = Math.min(min,map.get(key).get(1) -map.get(key).get(0)+1);
            }
        }
        return min;
    }
}
