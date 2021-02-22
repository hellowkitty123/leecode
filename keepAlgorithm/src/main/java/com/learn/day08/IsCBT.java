package com.learn.day08;

import com.mj.printer.BinaryTreeInfo;
import com.mj.printer.BinaryTrees;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 判断完全二叉树的标准
 * 1、宽度优先遍历
 *      条件1： 任何节点有右无左
 *      条件2： 一旦遇到左右孩子不双全后续遇到的左右节点都是叶子（没有左右孩子）
 * 2、二叉树递归套路
 *      可能情况：
 *      1、是满二叉树
 *      2、是完全二叉树，左子树是完全二叉树 ，右子树是满二叉树， 左子树高度 = 右子树高度+1
 *      3、是完全二叉树，左子树是满二叉树，右子树是满二叉树，左子树高度 = 右子树高度 + 1
 *      4、是完全二叉树，左子树是满二叉树，右子树是完全二叉树，左子树高度 = 右子树高度
 */
public class IsCBT {

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
        boolean isFull;
        boolean isCBT;
        int height;
        Info(boolean isFull, boolean isCBT , int height){
            this.isFull = isFull;
            this.isCBT = isCBT;
            this.height = height;
        }
    }


    public static boolean isCBT(Node head){

        if (head == null) {
            return true;
        }

        Info info = process(head);

        return info.isCBT;
    }

    public static Info process(Node node){
        if (node == null){
            return new Info(true,true,0);
        }

        Info infoL = process(node.left);
        Info infoR = process(node.right);

        int height = Math.max(infoL.height,infoR.height) + 1;


        boolean isFull = false;
        boolean isCBT = false;

        if (infoL.isCBT == infoR.isCBT){
            // 1、左子树是满二叉树，右子树是满二叉树 3、
            if (infoL.isFull && infoR.isFull && (infoL.height == infoR.height)){
                isCBT = true;
                isFull = true;
            }
            if (infoL.isFull && infoR.isFull && (infoL.height == infoR.height + 1)){
                isCBT = true;
            }
            if (infoL.isCBT && infoR.isFull &&infoL.height == infoR.height + 1){
                isCBT = true;
            }
            if (infoL.isFull &&infoR.isCBT && infoL.height == infoR.height){
                isCBT = true;
            }
        }

        return new Info(isFull,isCBT,height);
    }

    // 宽度优先遍历
    public static boolean isCBT2(Node head){
        Queue<Node> queue = new LinkedList<>();
        boolean isLead = false;
        if (head == null ){
            return true;
        }
        Node node = head;
        queue.add(node);
        while (!queue.isEmpty()){
            node = queue.poll();
            // 有右无左
            if (node.left == null && node.right != null){
                return false;
            }


            if (isLead && node.left != null){
                return false;
            }


            if (node.left == null || node.right == null){
                isLead = true;
            }

            if (node.left != null){
                queue.add(node.left);
            }

            if (node.right != null){
                queue.add(node.right);
            }

        }



        return true;
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
        int maxLevel = 5;
        int maxValue = 100;
        int testTimes = 200000;
        for (int i = 0; i < testTimes; i++) {
            Node head = generateRandomBST(maxLevel, maxValue);

            if (isCBT(head) != isCBT2(head)) {
                System.out.println("Oops!");
                System.out.println("--------------------");
                BinaryTrees.println(head, BinaryTrees.PrintStyle.LEVEL_ORDER);
                System.out.println(isCBT(head));
                System.out.println(isCBT2(head));
            }
        }
        System.out.println("finish!");
    }
}
