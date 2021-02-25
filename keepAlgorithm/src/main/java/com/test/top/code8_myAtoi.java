package com.test.top;

public class code8_myAtoi {
    public  static int myAtoi(String s) {
        if(s.length() == 0){
            return 0;
        }
        char[] cs = s.toCharArray();
        String result = "";
        int index = 0;
        while(cs[index] == ' '){
            index++;
        }
        if(isFu(cs[index])){
            result += String.valueOf(cs[index]);
            index++;
        }


        StringBuilder number = new StringBuilder();
        while(index <cs.length && isNumber(cs[index])){
            number.append(String.valueOf(cs[index]));
            index++;
        }
        number = new StringBuilder(validNumber(number.toString()));
        if(number.toString().equals("")){
            return 0;
        }
        result += number;
        return Integer.parseInt(result);

    }

    public  static boolean isNumber(char c){
        return c == '0' || c == '1' || c == '2' || c == '3' || c == '4' ||
                c == '5' || c == '6' || c == '7' || c == '8' || c == '9';
    }

    public  static boolean isFu(char c){
        return c == '+' || c =='-';
    }

    public  static String validNumber(String str){
        String base = "2147483648";
        char[] cArr = str.toCharArray();
        if(str.length() > base.length()){
            return base;
        }else if (str.length() < base.length()){
            return str;
        }
        char[] baseArr = str.toCharArray();
        for(int i=0;i< cArr.length;i++){
            if ((int) cArr[i] > (int) baseArr[i]){
                return base;
            }
        }
        return str;
    }

    public static void main(String[] args) {
        String s = "21474836460";
        int a = myAtoi(s);
        System.out.println(a);
    }
}
