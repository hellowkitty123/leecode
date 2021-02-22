package com.learn.day08;

import com.mj.printer.BinaryTreeInfo;
import com.mj.printer.BinaryTrees;

public class MaxDistance {

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
        int height;
        int maxDistance;
        Info(int height,int maxDistance){
            this.height = height;
            this.maxDistance = maxDistance;
        }
    }
    // 返回整棵树的最大距离
    public static int getMaxDistance(Node head){
        if (head == null){
            return 0;
        }

        Info info = process(head);
        return info.maxDistance;
    }
    /**
     * 1、经过 X 顶点 （左子树 的高度 + 右子树的高度 + 1） -> 最大距离
     * 2、不经过 X 顶点 （左子树的 最大距离 ，右子树的最大距离）
     */
    public  static Info process(Node node){
        if (node == null){
            return  new Info(0,0);
        }

        Info infoL = process(node.left);
        Info infoR = process(node.right);

        int height = Math.max( infoL.height , infoR.height) + 1;

        int maxDistance = Math.max(
                Math.max( infoL.maxDistance, infoR.maxDistance), // 不过X定点
                infoL.height + infoR.height + 1   //过X定点
        );

        return  new Info(height,maxDistance);
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
        int testTimes = 20;
        for (int i = 0; i < testTimes; i++) {
            Node head = generateRandomBST(maxLevel, maxValue);
            int distance = getMaxDistance(head);

            BinaryTrees.println(head, BinaryTrees.PrintStyle.LEVEL_ORDER);
            System.out.println(distance);
//            if (getMaxDistance(head) != maxDistance2(head)) {
//                System.out.println("Oops!");
//            }
        }
        System.out.println("finish!");
    }
}
