package com.test.top;

public class code32_longestValidParentheses {

    public static int longestValidParentheses(String s) {
        if(s.equals("") || s.length() == 1){
            return 0;
        }

        char[] cs = s.toCharArray();
        int n = cs.length;
        int[] dp = new int[n];
        dp[1] = cs[0] =='(' && cs[1] ==')' ? 2 : 0;
        int result = dp[1];
        for(int j=2;j<n;j++){

            int p1 =  (cs[j-1] =='(' && cs[j] ==')' ? dp[j-2] + 2 : 0);
            //之前的  "()(())"
            int pre = j-dp[j-1]-2 >=0 ? dp[j-dp[j-1]-2]:0;
            int p2 =   (j-dp[j-1]-1 >= 0 && cs[j-dp[j-1]-1] =='(' && cs[j] ==')' ? pre + dp[j-1] + 2 : 0);
            dp[j] = Math.max( p1, p2);
            result = Math.max(dp[j],result);
        }

        // System.out.println(Arrays.toString(dp));


        return result;
    }

    public static void main(String[] args) {
        String s = "()(())";
        int result = longestValidParentheses(s);
        System.out.println(result);
    }
}
