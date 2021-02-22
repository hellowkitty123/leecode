package com.learn.day08;

import com.learn.day07.RecursiveTraversalBT;
import com.mj.printer.BinaryTreeInfo;
import com.mj.printer.BinaryTrees;

// 给定一棵二叉树的头节点head，返回这颗二叉树是不是平衡二叉树
public class IsBalanced {

    public static class Node implements BinaryTreeInfo {
        Node left;
        Node right;
        int value;
        Node(int value){
            this.value = value;
        }
        @Override
        public Object root() {
            return this;
        }

        @Override
        public Object left(Object node) {
            return ((Node)node).left;
        }

        @Override
        public Object right(Object node) {
            return  ((Node)node).right;
        }

        @Override
        public Object string(Object node) {
            return  ((Node)node).value;
        }
    }

    public static class Info{
        boolean isbalanced;
        int height;
        Info(int height, boolean isbalanced){
            this.height = height;
            this.isbalanced = isbalanced;
        }
    }


    public static boolean isBalancedTree(Node head){
        if (head == null){
            return true;
        }
        Info info = process(head);
        return info.isbalanced;
    }

    public static Info process(Node node){
        // base case
        if (node == null){
            return new Info(0,true);
        }

        Info infoL = process(node.left);
        Info infoR = process(node.right);

        int height = Math.max(infoL.height,infoR.height) + 1;

        if (infoL.isbalanced && infoR.isbalanced && Math.abs(infoL.height -infoR.height) <= 1){
            return new Info(height,true);
        }

        return new Info(height,false);
    }


    // for test
    public static Node generateRandomBST(int maxLevel, int maxValue) {
        return generate(1, maxLevel, maxValue);
    }

    // for test
    public static Node generate(int level, int maxLevel, int maxValue) {
        if (level > maxLevel || Math.random() < 0.6) {
            return null;
        }
        Node head = new Node((int) (Math.random() * maxValue));
        head.left = generate(level + 1, maxLevel, maxValue);
        head.right = generate(level + 1, maxLevel, maxValue);
        return head;
    }

    public static boolean isBalanced1(Node head) {
        boolean[] ans = new boolean[1];
        ans[0] = true;
        process1(head, ans);
        return ans[0];
    }

    public static int process1(Node head, boolean[] ans) {
        if (!ans[0] || head == null) {
            return -1;
        }
        int leftHeight = process1(head.left, ans);
        int rightHeight = process1(head.right, ans);
        if (Math.abs(leftHeight - rightHeight) > 1) {
            ans[0] = false;
        }
        return Math.max(leftHeight, rightHeight) + 1;
    }

    public static void main(String[] args) {
        int maxLevel = 5;
        int maxValue = 100;
        int testTimes = 20;
        for (int i = 0; i < testTimes; i++) {
            Node head = generateRandomBST(maxLevel, maxValue);
            BinaryTrees.println(head, BinaryTrees.PrintStyle.LEVEL_ORDER);
            if (isBalanced1(head) != isBalancedTree(head)) {
                System.out.println("Oops!");
            }
        }
        System.out.println("finish!");
    }
}
