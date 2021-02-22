package com.learn.train01.day04;

/**
 * 假设字符串str长度为N，字符串match长度为M，M <= N
 *
 * 想确定str中是否有某个子串是等于match的。
 *
 * 时间复杂度O(N)
 */
public class KMP {

    /**
     * 字符串 s 中有 s子串
     * @param s 源字符
     * @param m 匹配字符
     * @return 第一个匹配成功的位置，没有返回-1
     */
    public static int getIndexOf(String s,String m){
        if (s == null || m == null || m.length() < 1 || s.length() < m.length()){
            return -1;
        }
        char[] str = s.toCharArray();
        char[] match = m.toCharArray();
//        int sl = str.length;
//        int ml = match.length;
        // 初始化源串、匹配串的起始位置
        int x = 0;
        int y = 0;

        int[] next = getNextArray(m);

        // x 不能越界 并且 match也不能越界
        while (x < str.length && y < match.length) {
            // 1) x、y位置字符匹配上了，x、y一起移动
            if (str[x] == match[y]) {
                x++;
                y++;
                // 2）y来到起始位置还没匹配上，x位置前进1
            } else if (next[y] == -1) {
                x++;
                // 3) y没有退回到起始位置，还有希望，
            } else {
                y = next[y];
            }
        }
        //y已经来到终止为止 ，匹配位置为x来到的位置前移动
        return y == m.length() ? x - y : -1;
    }

    /**
     * match 加速数组
     * @param m 匹配字符串
     * @return int[] 每个位置指标 前后缀最大长度
     */
    public static int[] getNextArray(String m){
        //数学归纳法 0,1,2 位置都知道， [...., i-1 , i]

        if (m.length() == 1){
            return new int[] {-1};
        }
        char[] match = m.toCharArray();
        int[] next = new int[m.length()];
        next[0] = -1;
        next[1] = 0 ;
        int cn = 0; // i 位置指标，前后缀字符串相等的总长度
        int i = 2;  // 跳转位置

        while (i< next.length){
            // 对当前i位置匹配，最终跳出来了
            if (match[cn] == match[i-1]){
                next[i++] = ++cn;
                // 还可以继续跳
            }else if (cn > 0){
                cn = next[cn];
                //已经不能跳了 ，没有匹配上
            }else{
                next[i++] = 0;
            }
        }


        return next;
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
        int matchSize = 5;
        int testTimes = 5000000;
        System.out.println("test begin");
        for (int i = 0; i < testTimes; i++) {
            String str = getRandomString(possibilities, strSize);
            String match = getRandomString(possibilities, matchSize);
            if (getIndexOf(str, match) != str.indexOf(match)) {
                System.out.println("Oops!");
            }
        }
        System.out.println("test finish");
    }

}
