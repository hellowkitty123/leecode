package com.mianshi.bytedance;

import java.security.PublicKey;

public class insertIntoBST {

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

     public static class Info{
         TreeNode maxright;
         TreeNode maxleft;
         Info(TreeNode maxleft ,TreeNode maxright){
             this.maxleft = maxleft;
             this.maxright = maxright;
         }
     }

    public static TreeNode insertIntoBST0(TreeNode root,int val){

         TreeNode max = new TreeNode(val);
         while (root != null){

             Info info = getMax(root);

             max = max.val > root.val ? max : root;
             max = max.val > info.maxright.val ? max : info.maxright;


         }


        return null;
    }

    public static Info getMax(TreeNode root){
        TreeNode maxright = getMaxRight(root.left);
        TreeNode maxleft = getMaxRight(root.right);
        return new Info(maxleft, maxright);
    }

    public static TreeNode getMaxRight(TreeNode root){
         TreeNode tempNode = root.left;
         if (tempNode == null){
             return null;
         }
         if (tempNode.right == null){
             return tempNode.right;
         }
         while (tempNode.right != null){
             tempNode  = tempNode.right;
         }
         return tempNode;
    }



    public static TreeNode getMaxLeft(TreeNode root){
        TreeNode tempNode = root.right;
        if (tempNode == null){
            return null;
        }
        if (tempNode.left == null){
            return tempNode.left;
        }
        while (tempNode.left != null){
            tempNode  = tempNode.left;
        }
        return tempNode;
    }

    public static void main(String[] args) {

    }

}
