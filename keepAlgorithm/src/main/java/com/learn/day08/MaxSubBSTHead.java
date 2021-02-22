package com.learn.day08;

import com.mj.printer.BinaryTreeInfo;

/**
 * 给定一棵二叉树的头节点head，
 * 返回这颗二叉树中最大的二叉搜索子树的头节点
 */
public class MaxSubBSTHead {

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
        Node BSTHead;
        int maxValue;
        int minValue;
        int BSTSize;
        Info(Node BSTHead,int maxValue, int minValue , int BSTSize){
            this.BSTHead = BSTHead;
            this.maxValue = maxValue;
            this.minValue = minValue;
            this.BSTSize = BSTSize;
        }
    }
    /**
     *   1、跟 X 无关
     *   2、跟 X 有关
     *      条件 1 左子树 、右子树  最大BST子树大小，
     *      条件 2 左子树的最大值，右子树的最小值
     *      条件 3 左子树、右子树最大size的BST
     */
    public static Node getMaxSubBSThead(Node head){
        if (head == null){
            return null;
        }
        Info info = process(head);
        return info.BSTHead;
    }


    public static Info process(Node node){
        if (node == null){
            return null;
        }
        Info infoL = process(node.left);
        Info infoR = process(node.right);


        int maxValue = node.value;
        int minValue = node.value;

        // 假定跟X 无关时候
        Node BSTHead = null;
        int BSTSize = 0;

        // 与有关、无关都算法一致
        if (infoL != null){
            maxValue = Math.max(maxValue,infoL.maxValue);
            minValue = Math.min(minValue,infoR.minValue);
            BSTHead = infoL.BSTSize > BSTSize ? infoL.BSTHead : BSTHead;
            BSTSize = Math.max(infoL.BSTSize, BSTSize);
        }

        if (infoR != null){
            maxValue = Math.max(maxValue,infoR.maxValue);
            minValue = Math.min(minValue,infoR.minValue);
            BSTHead = infoR.BSTSize > BSTSize ? infoR.BSTHead : BSTHead;
            BSTSize = Math.max(infoR.BSTSize, BSTSize);
        }

        // 与X 有关的情况
        // 条件1 左子树是BST，
        // 条件2 右子树是BST
        // 条件3 左子树最大值小于头结点，
        // 条件4 右子树最小值大于头结点
        if (
            (infoL == null || infoL.BSTHead == node.left)
            &&
            (infoR == null || infoR.BSTHead == node.right)
            &&
            (infoL == null || infoL.maxValue < node.value)
            &&
            (infoR == null || infoR.minValue > node.value)
        ){
            BSTHead = node;
            BSTSize = (infoL != null ? infoL.BSTSize : 0)
                    +
                    (infoR != null ? infoR.BSTSize : 0)
                    + 1 ;
        }

        return new Info(BSTHead,maxValue,minValue,BSTSize);
    }

    public static void main(String[] args) {

    }
}
