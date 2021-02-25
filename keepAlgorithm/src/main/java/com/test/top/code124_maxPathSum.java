package com.test.top;

/**
 * 路径 被定义为一条从树中任意节点出发，沿父节点-子节点连接，达到任意节点的序列。同一个节点在一条路径序列中 至多出现一次 。该路径 至少包含一个 节点，且不一定经过根节点。
 *
 * 路径和 是路径中各节点值的总和。
 *
 * 给你一个二叉树的根节点 root ，返回其 最大路径和 。
 */
public class code124_maxPathSum {
    public static int max = Integer.MIN_VALUE;
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
    public static int maxPathSum(TreeNode root) {
        if (root.left == null && root.right == null){
            return root.val;
        }

        Info info = process(root);
        return info.allTreeMaxSum;
    }

    public static class Info{

        int allTreeMaxSum; //整棵树最大路径和
        int fromHeadMaxSum; //从头出发最大路径和

        Info(int allTreeMaxSum,int fromHeadMaxSum){
            this.allTreeMaxSum = allTreeMaxSum;
            this.fromHeadMaxSum = fromHeadMaxSum;
        }
    }
    // 1、跟x无关， 1）left.allTreeMaxSum 2) right.allTreeMaxSum
    // 2、跟x有关， 3) left.fromHead + head 4) right.fromHead + head 5) head
    //            6) left.fromHead + right.fromHead + head
    public static Info process(TreeNode root){
        if (root == null){
            return null;
        }

        Info leftInfo = process(root.left);
        Info rightInfo = process(root.right);

        int p1 = leftInfo != null ? leftInfo.allTreeMaxSum : Integer.MIN_VALUE;
        int p2 = rightInfo != null ? rightInfo.allTreeMaxSum : Integer.MIN_VALUE;
        int p3 = leftInfo != null ? (leftInfo.fromHeadMaxSum + root.val) : Integer.MIN_VALUE;
        int p4 = rightInfo != null ? (rightInfo.fromHeadMaxSum + root.val) : Integer.MIN_VALUE;
        int p5 = root.val;
        int p6 = (leftInfo != null  && rightInfo != null) ? (leftInfo.fromHeadMaxSum + rightInfo.fromHeadMaxSum + root.val) : Integer.MIN_VALUE;

        int allTreeMaxSum = Math.max(Math.max(Math.max(p1,p2),p3),Math.max(Math.max(p4,p5),p6));
        int fromHeadMaxSum = Math.max(Math.max(p3,p4),p5);

        return new Info(allTreeMaxSum,fromHeadMaxSum);
    }


    public static void main(String[] args) {
        TreeNode root = new TreeNode(-2);
        root.right = new TreeNode(-1);
        int result  = maxPathSum(root);
        System.out.println(result);
    }
}
