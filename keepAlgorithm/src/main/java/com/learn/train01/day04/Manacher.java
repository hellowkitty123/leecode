package com.learn.train01.day04;

/**
 * 假设字符串str长度为N，想返回最长回文子串的长度
 *
 * 时间复杂度O(N)
 */
public class Manacher {



    /**
     * 1 i 在 R 外 ，中心向两边暴力扩
     * 2 i 在 R 内
     *      1） i 的右边界在R内
     *      2） i 的右边界在R外
     *      3） i 的右边界在R上，压线
     */
    public static int manacher(String s ){
        if (s == null || s.length() ==0){
            return 0;
        }
        String s1 = manacherString(s);
        char[] chars = s1.toCharArray();
        int[] pArr = new int[chars.length];
        int C = -1;
        int R = -1;
        int max = Integer.MIN_VALUE;

        for (int i=0;i<pArr.length;i++){
            // 不用验证的区域大小 ，
            pArr[i] =  i < R ? Math.min(pArr[C - (i - C)] , R-i) : 1;

            while (i+ pArr[i] < pArr.length && i - pArr[i] > -1){
                if (chars[i+pArr[i]] == chars[i-pArr[i]]){
                    pArr[i] ++;
                }else{
                    break;
                }
            }

            if (i+pArr[i] > R){
                C = i;
                R = i + pArr[i];
            }
            max = Math.max(max,pArr[i]);
        }

        return max -1;
    }

    public static String manacherString(String s){

        char[] s1 = s.toCharArray();

        char[] s2 = new char[s1.length * 2 + 1];
        int index = 0;
        for (int i = 0;i<s2.length;i++){
            s2[i] = i%2 == 0 ? '#' : s1[index++];
        }

        return String.valueOf(s2);
    }

    // for test
    public static int right(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] str = manacherString(s).toCharArray();
        int max = 0;
        for (int i = 0; i < str.length; i++) {
            int L = i - 1;
            int R = i + 1;
            while (L >= 0 && R < str.length && str[L] == str[R]) {
                L--;
                R++;
            }
            max = Math.max(max, R - L - 1);
        }
        return max / 2;
    }

    // for test
    public static String getRandomString(int possibilities, int size) {
        char[] ans = new char[(int) (Math.random() * size) + 1];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (char) ((int) (Math.random() * possibilities) + 'a');
        }
        return String.valueOf(ans);
    }

    public static void main(String[] args) {
        int possibilities = 5;
        int strSize = 20;
        int testTimes = 5000000;
        System.out.println("test begin");
        for (int i = 0; i < testTimes; i++) {
            String str = getRandomString(possibilities, strSize);
            if (manacher(str) != right(str)) {
                System.out.println("-----------------Oops!");
                int i1 = manacher(str);
                int i2 = right(str);
                System.out.println(i1);
                System.out.println(i2);
                break;
            }
        }
        System.out.println("test finish");
    }
}
