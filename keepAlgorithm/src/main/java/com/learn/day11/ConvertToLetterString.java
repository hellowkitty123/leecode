package com.learn.day11;

/** facebook 面试题
 * 规定1和A对应、2和B对应、3和C对应...
 * 那么一个数字字符串比如"111”就可以转化为:
 * "AAA"、"KA"和"AK"
 * 给定一个只有数字字符组成的字符串str，返回有多少种转化结果
 */
public class ConvertToLetterString {


    // 从左往右的尝试模型
    public static int converToLetter(String str){
        if (str.equals("")) {
            return 0;
        }
        char[] str2  = str.toCharArray();

        return process(str2,0);
    }

    public static int process(char[] str,int i){
        // base case i来到最后一位位置，返回1
        if (i == str.length){
            return 1;
        }
        // 规定1和A对应、2和B对应、3和C对应
        // 开头是0 无法组成 组合
        if (str[i] == '0'){
            return 0;
        }
        if (str[i] == '1'){
            int res = process(str,i+1);
            if (i+1 <str.length){
                res += process(str,i+2);
            }
            return res;
        }

        if (str[i] == '2'){
            int res = process(str,i+1);
            if (i+1 < str.length && str[i+1] >= '0' && str[i+1] <= '6' ){
                res += process(str,i+2);
                return res;
            }
        }
        // 如果 i位置是 ‘3’
        return process(str,i+1);
    }

    public static int converToLetter2(String str2){
        if (str2.equals("")) {
            return 0;
        }
        char[] str  = str2.toCharArray();
        int len = str.length;
        int[] dp = new int[len+1];
        dp[len] = 1;

        for (int i = len-1;i >=0;i--){
            if (str[i] == '0'){
                dp[i] =  0;
            }
            if (str[i] == '1'){
                int res = dp[i+1];
                if (i+1 <str.length){
                    res += dp[i+2];
                }
                dp[i] = res;
            }

            if (str[i] == '2'){
                int res = dp[i+1];
                if (i+1 < str.length && str[i+1] >= '0' && str[i+1] <= '6' ){
                    res += dp[i+2];
                    dp[i] =  res;
                }
            }
        }

        return dp[0];
    }




    public static void main(String[] args) {
        System.out.println(converToLetter("11111"));
        System.out.println(converToLetter2("11111"));
    }
}
