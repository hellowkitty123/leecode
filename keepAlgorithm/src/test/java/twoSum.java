
import java.util.HashMap;
import java.util.HashSet;

class twoSum {
    public static int lengthOfLongestSubstring(String s) {
        char[] str = s.toCharArray();
        if(str.length < 2){
            return str.length;
        }
        if(str.length == 2){
            return (str[0]-'a') == (str[1]-'a') ? 1:2;
        }
        int L = 0;
        int R = 0;
        int max = 0;
        HashMap<Integer,Integer> hash = new HashMap<>();

        while (R<str.length){
            // [L...R-1] 跟 R有重复字符
            if (hash.containsKey(str[R]-'a') && hash.get(str[R]-'a') >= L ){

                L = hash.get(str[R]-'a')+1;
            }
            hash.put(str[R]-'a',R);

            max = Math.max(max,R-L+1);
            System.out.println(" L : "+ L + " R : "+ R);
            R++;
        }
        return max;
    }

    public static void main(String[] args) {
//        int[] arr = new int[]{2,7,11,15};
//        String s = "abcabcbb";
//        String s = "abba";
        String s = "abcabcbb";
//        String s = "cdd";
        int result = lengthOfLongestSubstring(s);
        System.out.println(result);
    }
}