package com.test.top;

public class code1214_twoSumBSTs {

     public static class TreeNode {
          int val;
          TreeNode left;
          TreeNode right;
          TreeNode() {}
          TreeNode(int val) { this.val = val; }
          TreeNode(int val, TreeNode left, TreeNode right) {
              this.val = val;
              this.left = left;
              this.right = right;
          }
      }

    public static boolean twoSumBSTs(TreeNode root1, TreeNode root2, int target){
        if (root1  == null ){
            return false;
        }
        return search(root2,target - root1.val) ||
                twoSumBSTs(root1.left,root2,target) ||
                twoSumBSTs(root1.right,root2,target);
    }

    private static boolean search(TreeNode root,int target){
         TreeNode currentNode = root;
         boolean find = false;
         while (currentNode != null){
             if (currentNode.val > target){
                 currentNode = currentNode.left;
             }else if (currentNode.val < target){
                 currentNode= currentNode.right;
             }else {
                 find = true;
                 break;
             }
         }
         return find;
    }



    public static void main(String[] args) {

    }
}
