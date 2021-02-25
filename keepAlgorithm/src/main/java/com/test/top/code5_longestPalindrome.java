package com.test.top;

public class code5_longestPalindrome {


    public static String longestPalindrome(String s) {
        String str = mancher(s);
        return str;
    }

    public static String mancher(String str){
        if(str.length() == 1){
            return str;
        }
        String newStr = mancharString(str);
        char[] arr = newStr.toCharArray();
        int C = -1; //回文中心位置
        int R = -1; //最长回文最右的位置的下一个位置
        int result = 0; //答案所在的match中的位置
        int[] match = new int[arr.length];
        for(int i=0;i<match.length;i++){
            match[i] = i < R ? Math.min(match[2*C - i],R-i) : 1;

            while(i+match[i] < match.length && i-match[i] >=0){
                if(arr[i+match[i]] == arr[i-match[i]]){
                    match[i]++;
                }else{
                    break;
                }
            }
            if(match[i]+i > C){
                C = i;
                R = match[i]+i;
            }
            result = i;
        }

        return str.substring(result/2 - match[result]/2 ,result/2 + match[result]/2);
    }

    public static String mancharString(String str){
        char[] s = str.toCharArray();
        char[] newS = new char[s.length * 2 + 1];

        for(int i =0;i<s.length;i++){
            newS[i*2] = '#';
            newS[i*2+1] = s[i];
        }
        newS[newS.length-1] = '#';
        return String.copyValueOf(newS);
    }

    public static void main(String[] args) {
        int mid1 = 2; //上中位数
        int mid2 = 3; //上中位数
        double sum  = (double) mid1 + (double) mid2;
        System.out.println(sum/2);
    }
}
