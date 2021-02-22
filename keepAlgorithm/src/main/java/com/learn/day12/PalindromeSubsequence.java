package com.learn.day12;

/**
 * 最长公共子序列  （一样本做行一样本做列的对应模型）
 */
public class PalindromeSubsequence {

    /**
     *
     * @param str1
     * @param str2
     * @return
     */
    public static int lcsq(String str1,String str2){
        if (str1 == null || str2 ==null || str1.equals("") || str2.equals("")){
            return 0;
        }
        char[] str01 = str1.toCharArray();
        char[] str02 = str2.toCharArray();
        int len1 = str01.length;
        int len2 = str02.length;
        int[][] dp = new int[len1][len2];

        dp[0][0] = str01[0] == str02[0] ? 1 : 0;

        for (int i =1;i<len1;i++){
            dp[i][0] = Math.max(dp[i-1][0],str01[i] == str02[0] ? 1 : 0);
        }

        for (int i =1;i<len2;i++){
            dp[0][i] = Math.max(dp[0][i-1],str01[0] == str02[i] ? 1 : 0);
        }


        for (int i= 1;i<len1;i++){
            for (int j=1;j<len2;j++){
                dp[i][j] = Math.max(dp[i-1][j],dp[i][j-1]);

                if (str01[i] == str02[j]){
                    dp[i][j] = Math.max(dp[i][j],dp[i-1][j-1]+1);
                }
            }
        }

        return dp[len1-1][len2-1];
    }

    public static void main(String[] args) {

    }
}
