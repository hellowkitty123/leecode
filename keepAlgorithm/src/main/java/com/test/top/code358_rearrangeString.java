package com.test.top;

import java.lang.reflect.Array;
import java.util.*;

/**
 * 358. K 距离间隔重排字符串
 * 给你一个非空的字符串 s 和一个整数 k，你要将这个字符串中的字母进行重新排列，使得重排后的字符串中相同字母的位置间隔距离至少为 k。
 *
 * 所有输入的字符串都由小写字母组成，如果找不到距离至少为 k 的重排结果，请返回一个空字符串 ""。
 *
 * 示例 1：
 * 输入: s = "aabbcc", k = 3
 * 输出: "abcabc"
 * 解释: 相同的字母在新的字符串中间隔至少 3 个单位距离。
 *
 * 示例 2:
 * 输入: s = "aaabc", k = 3
 * 输出: ""
 * 解释: 没有办法找到可能的重排结果。
 *
 * 示例 3:
 * 输入: s = "aaadbbcc", k = 2
 * 输出: "abacabcd"
 * 解释: 相同的字母在新的字符串中间隔至少 2 个单位距离。
 */
public class code358_rearrangeString {

    public static class Word{
        String value;
        int count;
        public Word(String value,int count){
            this.value = value;
            this.count = count;
        }
    }

    public static class Compare implements Comparator<Word>{
        @Override
        public int compare(Word o1,Word o2){
            return o2.count - o1.count;
        }
    }

    public static String rearrangeString(String s ,int k){
        if (s == null ){
            return "";
        }
        if (k == 0 ){
            return s;
        }
        char[] cs = s.toCharArray();
        HashMap<String,Integer> map = new HashMap<>();
        for (char s0 :cs){
            String temps = String.valueOf(s0);
            if (map.containsKey(temps)){
                map.put(String.valueOf(s0),map.get(temps)+1);
            }else{
                map.put(String.valueOf(s0),1);
            }
        }

        PriorityQueue<Word> heap = new PriorityQueue<>(new Compare());

        for (String key : map.keySet()){
            heap.add(new Word(key,map.get(key)));
        }
        if (cs.length == 1){
            return s;
        }
        if (heap.size() < k){
            return "";
        }


        Queue<Word> queue = new LinkedList<>();
        StringBuilder result = new StringBuilder();
        while (!heap.isEmpty()){
            Word w = heap.poll();
            result.append(w.value);
            w.count--;
            queue.add(w);

            if (queue.size() == k){
                w = queue.poll();
                if (w.count > 0){
                    // bug 1 放回heap时候，多次poll
                    heap.add(w);
                }
            }
            System.out.println(result);

        }
        if (cs.length != result.length()){
            return "";
        }
        return result.toString() ;
    }

    public static void main(String[] args) {
        String s = "aabbcc";
        String result = rearrangeString(s ,2);
        System.out.println(result);
    }

}
