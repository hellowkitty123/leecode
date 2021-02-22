package com.leecode.array.visit;


import java.util.*;

/**
 * 414. 第三大的数 难度：简单
 * 给定一个非空数组，返回此数组中第三大的数。如果不存在，则返回数组中最大的数。要求算法时间复杂度必须是O(n)。
 *
 * 示例 1:
 *
 * 输入: [3, 2, 1]
 *
 * 输出: 1
 *
 * 解释: 第三大的数是 1.
 * 示例 2:
 *
 * 输入: [1, 2]
 *
 * 输出: 2
 *
 * 解释: 第三大的数不存在, 所以返回最大的数 2 .
 * 示例 3:
 *
 * 输入: [2, 2, 3, 1]
 *
 * 输出: 1
 *
 * 解释: 注意，要求返回第三大的数，是指第三大且唯一出现的数。
 * 存在两个值为2的数，它们都排第二。
 *
 */
public class leecode_414 {
    private static long MIN =  Long.MIN_VALUE;
    public static void main(String[] args) {
        int[] arr = { 0,2, 2,5};
        List<Integer> list = funcOne(arr);
        int result = list.size() == 3 ? list.get(2):list.get(0);
//        System.out.println(result);

        List<Long> list2 = funcTwo(arr);
//        System.out.println(list2);
        result = list2.size() == 3 ? list.get(2):list.get(0);
//        System.out.println(result);

        list = funcThree(arr);
        System.out.println(list);
    }

    private static List<Integer> funcOne(int[] arr){
        ArrayList<Integer> list = new ArrayList<Integer>();

        for (int i :arr){
            if(list.size() == 0){
                list.add(i);
            }else{
                if(list.size() == 1){
                    if(list.get(0) > i){
                        list.add(i);
                    }else if (list.get(0) < i) {
                        list.add(list.get(0));
                        list.set(0,i);
                    }
                }else if(list.size() == 2) {
                    if (list.get(1) > i){
                        list.add(i);
                    }else if(list.get(0)>i && list.get(1)<i){
                        list.add(list.get(1));
                        list.set(1,i);

                    }else if(list.get(0)<i){
                        list.add(list.get(1));
                        list.set(1,list.get(0));
                        list.set(0,i);
                    }
                }else if(list.size() == 3) {
                    if (list.get(2) > i){
                        System.out.println("");
                    }else if(list.get(1)>i && list.get(2)<i){
                        list.set(2,i);
                    }else if(list.get(0)>i && list.get(1)<i){
                        list.set(2,list.get(1));
                        list.set(1,i);
                    }else if(list.get(0)<i){
                        list.set(2,list.get(1));
                        list.set(1,list.get(0));
                        list.set(0,i);
                    }
                }
            }
//            System.out.println(list.toString());
        }
        return list;
    }

    private static List<Long> funcTwo(int[] arr){
        if(arr == null || arr.length == 0) throw new RuntimeException("nums is null or length of 0");
        long one = arr[0];
        long two = MIN;
        long  three = MIN;
        for (int i:arr){
            if (one == i || two == i || three == i) continue;
            if(one <i){
                three = two;
                two = one ;
                one = i;
            }else if (one >i &&two <i ){
                three = two;
                two = i;

            }else if (two >i && three <i ){
                three = i;
            }
        }
        ArrayList<Long> list = new ArrayList<Long>();
        list.add(one);
        list.add(two);
        list.add(three);
        return list;
    }

    private static ArrayList<Integer> funcThree(int[] arr){
        TreeSet<Integer> set = new TreeSet<Integer>();
        if (arr == null || arr.length == 0) throw new RuntimeException("nums is null or length of 0");
        for (int i:arr ){
            set.add(i);
            if (set.size() > 3){
                set.remove(set.first());
            }
        }
        ArrayList<Integer> list = new ArrayList<Integer>();
        Iterator<Integer> iterator = set.iterator();
        while (iterator.hasNext()){
            System.out.println(
                    set.iterator().hasNext()
            );
            list.add(iterator.next());
        }
        return list;
    }
}
