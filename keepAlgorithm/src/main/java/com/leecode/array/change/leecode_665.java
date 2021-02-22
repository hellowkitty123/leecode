package com.leecode.array.change;

/**
 * 665. 非递减数列 难度：简单
 * 给你一个长度为 n 的整数数组，请你判断在 最多 改变 1 个元素的情况下，该数组能否变成一个非递减数列。
 *
 * 我们是这样定义一个非递减数列的： 对于数组中所有的 i (0 <= i <= n-2)，总满足 nums[i] <= nums[i + 1]。
 *
 *  
 *
 * 示例 1:
 *[1,2,6,4,5]
 *[1,2,1,4,5]
 *[1,2,3,2,5]
 *[1,2,3,4,3]
 * 输入: nums = [4,2,3]
 * 输出: true
 * 解释: 你可以通过把第一个4变成1来使得它成为一个非递减数列。
 * 示例 2:
 *
 * 输入: nums = [4,2,1]
 * 输出: false
 * 解释: 你不能在只改变一个元素的情况下将其变为非递减数列。
 *  
 *
 * 说明：
 *
 * 1 <= n <= 10 ^ 4
 * - 10 ^ 5 <= nums[i] <= 10 ^ 5
 */
public class leecode_665 {
    public static void main(String[] args) {
        int[] arr = {1,2,6,1,3};
        boolean ismatch = funcTwo(arr);
        System.out.println(ismatch);
    }
    //方法不对
    public static boolean funcOne(int[] arr){
        int count = 0;
        for (int i=0;i<arr.length;i++){
            if (count >1) break;
            if (i+1 >= arr.length) break;
            if (i == 0){
                if (arr[i]>arr[i+1]){
                    count++;
                    arr[i]=arr[i+1]-1;
                }
            }else{
                if ((arr[i-1]>=arr[i] && arr[i]<=arr[i+1])|| ( arr[i-1]<arr[i] && arr[i]<=arr[i+1])){
                    if (arr[i-1]<arr[i]-1){
                        count++;
                        arr[i] = arr[i-1]+1;
                    }else{
                        return false;
                    }
                }
            }
        }
        return count ==1;
    }

    private static boolean funcTwo(int[] arr){
        int count = 0;
        for (int i=1;i<arr.length;i++){
            if (arr[i]-arr[i-1]<0){
                count++;
                if (count >1) return false ;
                if (i-2>=0 && arr[i]-arr[i-2] <0) {
                    arr[i]=arr[i-1];
                }else{
                    arr[i-1]=arr[i];
                }
                System.out.println(count);
            }
        }
        return count == 1;
    }
}
