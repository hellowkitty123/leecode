package com.test.top;

import java.util.Stack;

public class code42_trap {

    public static int trap(int[] height) {
        return process(height);
    }

    public static int process(int[] height){
        if(height.length == 0){
            return 0;
        }

        int max = 0;
        Stack<Integer[]> stack = new Stack<>();
        for(int i = 0;i< height.length;i++){
            while(!stack.isEmpty() && stack.peek()[1] < height[i]){

                Integer[] cur = stack.pop();
                // bug 1 top字段是当前弹出的元素高度
                int top = (cur[1]);

                if(stack.isEmpty()){
                    break;
                }
                // 左右边界取比较矮的
                int temp  =  Math.min(stack.peek()[1],height[i]);
                int h = temp-top;
                // bug 2 还是需要多跑例子， 长度是 两边界 相减之后再减1
                int s = i - stack.peek()[0] - 1;
                max += h * s;
            }
            stack.add(new Integer[]{i,height[i]});
        }
        return max;
    }

    public static void main(String[] args) {
        int[] height = new int[]{0,1,0,2,1,0,1,3,2,1,2,1};
        int result = trap(height);
        System.out.println(result);
    }
}
