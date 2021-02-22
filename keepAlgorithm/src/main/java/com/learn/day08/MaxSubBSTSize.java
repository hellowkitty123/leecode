package com.learn.day08;

import com.mj.printer.BinaryTreeInfo;
import com.mj.printer.BinaryTrees;

/**
 * 给定一棵二叉树的头节点head，
 * 返回这颗二叉树中最大的二叉搜索子树的头节点
 */
public class MaxSubBSTSize {

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

    public static class Info {
        boolean isBST;
        int maxValue;
        int minValue;
        int BSTSize;
        Info(boolean isBST,int maxValue, int minValue , int BSTSize){
            this.isBST = isBST;
            this.maxValue = maxValue;
            this.minValue = minValue;
            this.BSTSize = BSTSize;
        }
    }
    /**
     *   1、跟 X 有关
     *   2、跟 X 无关
     *      条件 1 左子树 、右子树  最大BST子树大小，
     *      条件 2 左子树的最大值，右子树的最小值
     *      条件 3 左子树、右子树最大size的BST
     */
    public static int getMaxSubBSTSize(Node head){
        if (head == null){
            return 0;
        }
        Info info = process(head);
        return info.BSTSize;
    }


    public static Info process(Node node){
        if (node == null){
            return null;
        }
        Info infoL = process(node.left);
        Info infoR = process(node.right);
        // node为头的树必包含 node，初始化
        int maxValue = node.value;
        int minValue = node.value;


        // 取左右子树+node 最大值和最小值
        if (infoL != null){
            minValue = Math.min(minValue,infoL.minValue);
            maxValue = Math.max(maxValue,infoL.maxValue);
        }

        if (infoR != null){
            minValue = Math.min(minValue,infoR.minValue);
            maxValue = Math.max(maxValue,infoR.maxValue);
        }

        boolean isBST = false;

        int BSTSize = 0;
        // 这里计算 情况 1 与X 无关
        if (infoL != null){
            BSTSize = infoL.BSTSize;
        }

        if (infoR != null){
            BSTSize = Math.max(BSTSize,infoR.BSTSize);
        }

        // 这里计算 情况 2 与X 有关
        if (
                (infoL == null || infoL.isBST)  // 左孩子是二叉树
                &&
                (infoR == null || infoR.isBST)  // 右孩子是二叉树
                &&
                (infoL == null || infoL.maxValue < node.value)
                &&
                (infoR == null || infoR.minValue > node.value)
        ){
            isBST = true;
            BSTSize = (infoL == null ? 0 : infoL.BSTSize)
                        +
                      (infoR == null ? 0 : infoR.BSTSize)
                        + 1;
        }

        return  new Info(isBST,maxValue,minValue,BSTSize);
    }
    // for test
    public static Node generateRandomBST(int maxLevel, int maxValue) {
        return generate(1, maxLevel, maxValue);
    }

    // for test
    public static Node generate(int level, int maxLevel, int maxValue) {
        if (level > maxLevel || Math.random() < 0.5) {
            return null;
        }
        Node head = new Node((int) (Math.random() * maxValue));
        head.left = generate(level + 1, maxLevel, maxValue);
        head.right = generate(level + 1, maxLevel, maxValue);
        return head;
    }

    public static void main(String[] args) {
        int maxLevel = 4;
        int maxValue = 100;
        int testTimes = 10;
        for (int i = 0; i < testTimes; i++) {
            Node head = generateRandomBST(maxLevel, maxValue);
            BinaryTrees.println(head, BinaryTrees.PrintStyle.LEVEL_ORDER);
            int size = getMaxSubBSTSize(head);
            System.out.println(size);
//            if (maxSubBSTSize1(head) != maxSubBSTSize2(head)) {
//                System.out.println("Oops!");
//            }
        }
        System.out.println("finish!");
    }
}
