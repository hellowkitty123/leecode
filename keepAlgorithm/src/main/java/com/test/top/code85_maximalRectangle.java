package com.test.top;

import java.util.Stack;

public class code85_maximalRectangle {

    public static int maximalRectangle(char[][] matrix) {
        if(matrix.length == 0){
            return 0;
        }

        // for(int i =0;i< matrix.length;i++){
        //     System.out.println(Arrays.toString(matrix[i]));
        // }
        // System.out.println("-------------");
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[] height = new int[cols];
        int result = 0;
        for(int row = 0;row < rows ;row++){
            for(int col = 0;col < cols;col++){
                height[col] = matrix[row][col] == '1' ? height[col] + 1 : 0;
            }
            result = Math.max(trap(height), result);
        }

        return result;
    }

    public static int trap(int[] height){

        // System.out.println(Arrays.toString(height));
        int max = 0;
        Stack<Integer> stack = new Stack<>();
        for(int i=0;i<height.length;i++){

            while(!stack.isEmpty() && height[i] <= height[stack.peek()]){
                // j 左边距离我最近的，我扩不到的位置
                // k 右边距离我最近的，我扩不到的位置
                int k = stack.pop();
                int j = stack.isEmpty() ? -1: stack.peek();
                int airs = ((i-1)-(j+1)+1) * height[k];
                max = Math.max(max,airs);
            }
            stack.add(i);
        }
        while(!stack.isEmpty()){

            int k = stack.pop();
            int j = stack.isEmpty() ? -1: stack.peek();
            int airs = ( (height.length-1) - (j+1)+1) * height[k];
            max = Math.max(max,airs);
        }

        return max;
    }
    public static void main(String[] args) {
        char[][] matrix = new char[][]{
                {'1','0','1','0','0'},
                {'1','0','1','1','1'},
                {'1','1','1','1','1'},
                {'1','0','0','1','0'}};
        int result = maximalRectangle(matrix);
        System.out.println(result);
    }
}
