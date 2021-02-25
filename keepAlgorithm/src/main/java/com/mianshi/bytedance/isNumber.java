package com.mianshi.bytedance;

/**
 * 有效数字（按顺序）可以分成以下几个部分：
 *
 * 一个 小数 或者 整数
 * （可选）一个 'e' 或 'E' ，后面跟着一个 整数
 * 小数（按顺序）可以分成以下几个部分：
 *
 * （可选）一个符号字符（'+' 或 '-'）
 * 下述格式之一：
 * 至少一位数字，后面跟着一个点 '.'
 * 至少一位数字，后面跟着一个点 '.' ，后面再跟着至少一位数字
 * 一个点 '.' ，后面跟着至少一位数字
 * 整数（按顺序）可以分成以下几个部分：
 *
 * （可选）一个符号字符（'+' 或 '-'）
 * 至少一位数字
 * 部分有效数字列举如下：
 *
 * ["2", "0089", "-0.1", "+3.14", "4.", "-.9", "2e10", "-90E3", "3e+7", "+6e-1", "53.5e93", "-123.456e789"]
 * 部分无效数字列举如下：
 *
 * ["abc", "1a", "1e", "e3", "99e2.5", "--6", "-+3", "95a54e53"]
 * 给你一个字符串 s ，如果 s 是一个 有效数字 ，请返回 true 。
 *
 * 示例 1：
 *
 * 输入：s = "0"
 * 输出：true
 * 示例 2：
 *
 * 输入：s = "e"
 * 输出：false
 * 示例 3：
 *
 * 输入：s = "."
 * 输出：false
 * 示例 4：
 *
 * 输入：s = ".1"
 * 输出：true
 *
 * 提示：
 *
 * 1 <= s.length <= 20
 * s 仅含英文字母（大写和小写），数字（0-9），加号 '+' ，减号 '-' ，或者点 '.' 。
 */
public class isNumber {

    public static boolean isNumber0(String s){
        char[] chars = s.toCharArray();

        if (chars[0] != '+' && chars[0] != '-' && !validNumber(chars[0])  && chars[0]!='.'){
            return false;
        }
        for (int i=0;i<chars.length;i++){
            char c = chars[i];
            if (!valid(c)){
                return false;
            }
            chars[i] = c == 'E' ? 'e' : c;
        }
        s = String.copyValueOf(chars);
        String[] arr = s.split("e");
        System.out.println(s);
        if (arr.length > 2 || s.indexOf("e") == s.length()-1 ||s.indexOf("e") == 0 ){
            return false;
        }
        boolean isOk = isFloat(arr[0]) || isInt(arr[0]);

        if (arr.length == 2){
            isOk =  isOk && isInt(arr[1]);
        }
        return isOk;
    }
    public static boolean isInt(String s){
        char[] cs = s.toCharArray();
        if (cs[0] == '+' || cs[0] == '-'){
            if (cs.length == 1){
                return false;
            }
            cs[0] = '0';
        }
        for (char c : cs ){
            if (!validNumber(c)){
                return false;
            }
        }

        return true;
    }

    public static boolean isFloat(String s){
        char[] cs = s.toCharArray();

        if (cs[0] == '+' || cs[0] == '-'){
            if (cs.length == 1){
                return false;
            }
            cs[0] = '0';
        }
        s = String.copyValueOf(cs);

        String[] arr = s.split(".");


        if (!s.contains(".") || arr.length > 2 || s .equals(".") ){
            return false;
        }
        int count = 0;
        while (s.contains(".")){
            s=s.substring(s.indexOf(".")+1);
            ++count;
        }
        if (count > 1){
            return false;
        }
        for (int i=0;i<arr.length;i++){
            char[] aa = arr[i].toCharArray();

            for (char a : aa){
                if (!validNumber(a)){
                    return false;
                }
            }
        }


        return true;
    }

    public static boolean valid(char c){
        return validNumber(c) || c == '+' || c == '-' || c == 'e' || c == 'E' || c == '.';
    }
    public static boolean validNumber(char c){
        return  c == '0' || c == '1' ||c == '2' ||c == '3' ||c == '4' ||c == '5' ||
                c == '6' || c == '7' ||c == '8' ||c == '9' ;
    }

    public static void main(String[] args) {
        String s = "..";
        boolean isNumer = isNumber0(s);
        System.out.println(isNumer);
    }
}
