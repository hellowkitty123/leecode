package com.test.top;

import java.util.*;

/**
 * 159. 至多包含两个不同字符的最长子串
 * 给定一个字符串 s ，找出 至多 包含两个不同字符的最长子串 t ，并返回该子串的长度。
 *
 * 示例 1:
 * 输入: "eceba"
 * 输出: 3
 * 解释: t 是 "ece"，长度为3。
 *
 * 示例 2:
 * 输入: "ccaabbb"
 * 输出: 5
 * 解释: t 是 "aabbb"，长度为5。
 */
public class code159_lengthOfLongestSubstringTwoDistinct {
    public static class Tuple{
        String key;
        Integer value;
        Tuple(String key,Integer value){
            this.key= key;
            this.value = value;
        }
    }

    public static int getMapCount(HashMap<String,Integer> map){
        return map.keySet().size();
    }
    public static Tuple getAndDropMapMin(HashMap<String,Integer> map){
        if (map.size() <1){
            return null;
        }

        int min = Integer.MAX_VALUE;
        Tuple tu = null;
        for (String key: map.keySet()){
            if (min > map.get(key)){
                tu = new Tuple(key,map.get(key));
                // bug 1 没有更新最小值
                min = map.get(key);
            }
        }
        map.remove(tu.key);
        return tu;
    }
    public static int lengthOfLongestSubstringTwoDistinct(String s) {
        if (s == null ){
            return 0;
        }
        char[] cs = s.toCharArray();
        if (cs.length <=2){
            return cs.length;
        }

        HashMap<String,Integer> map = new HashMap<>();

        int max = 0;
        int L = 0;
        int R = 0; // [R,L]
        while (R < cs.length){
            if (map.containsKey(String.valueOf(cs[R])) || map.size() < 2){

                map.put(String.valueOf(cs[R]),R++);
                max = Math.max(max,R-L);

            }else if(getMapCount(map) >= 2){
                Tuple tu = getAndDropMapMin(map);
                L = tu.value + 1;
            }

        }
        return max;
    }

    public static void main(String[] args) {
        String s = "bacc";
        int num = lengthOfLongestSubstringTwoDistinct(s);
        System.out.println(num);
    }

}
