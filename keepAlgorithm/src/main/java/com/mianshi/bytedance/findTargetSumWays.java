package com.mianshi.bytedance;

/**
 * 给定一个非负整数数组，a1, a2, ..., an, 和一个目标数，S。现在你有两个符号 + 和 -。对于数组中的任意一个整数，你都可以从 + 或 -中选择一个符号添加在前面。
 *
 * 返回可以使最终数组和为目标数 S 的所有添加符号的方法数。
 *
 * 示例：
 *
 * 输入：nums: [1, 1, 1, 1, 1], S: 3
 * 输出：5
 * 解释：
 *
 * -1+1+1+1+1 = 3
 * +1-1+1+1+1 = 3
 * +1+1-1+1+1 = 3
 * +1+1+1-1+1 = 3
 * +1+1+1+1-1 = 3
 *
 * 一共有5种方法让最终目标和为3。
 *
 * 提示：
 *
 * 数组非空，且长度不会超过 20 。
 * 初始的数组的和不会超过 1000 。
 * 保证返回的最终结果能被 32 位整数存下。
 */
public class findTargetSumWays {

    public static int findTargetSumWays0(int[] nums, int S) {
        return process(nums,0,S) ;
    }
    public static int process(int[] nums,int index , int rest){
        // 所有数字必须用
        if (index == nums.length){
            return rest == 0 ? 1 : 0;
        }

        return  process(nums,index+1,rest - nums[index]) +
                process(nums,index+1,rest + nums[index]);
    }

    public static void main(String[] args) {
        int[] arr = new int[]{1, 1, 1, 1, 1};
        int S = 3;
        int result = findTargetSumWays0(arr,S);
        System.out.println(result);
    }
}
