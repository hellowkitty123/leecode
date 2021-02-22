package com.learn.day08;


import com.mj.printer.BinaryTreeInfo;
import com.mj.printer.BinaryTrees;

/**
 * 满二叉树的性质   L 高度 N 节点个数  ， (2 << L) -1 = N
 */
public class IsFull {
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
        int nodes;
        Info(int height,int nodes){
            this.height = height ;
            this.nodes = nodes;
        }
    }


    public static boolean getIsFull(Node head){
        Info info = process(head);
        return (1 << info.height) -1 == info.nodes ;
    }


    public  static Info process(Node node){
        if (node == null){
            return new Info(0,0);
        }

        Info infoL = process(node.left);
        Info infoR = process(node.right);

        int height = Math.max(infoL.height ,infoR.height) + 1;
        int nodes = infoL.nodes + infoR.nodes + 1;
        return new Info(height,nodes);
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
            BinaryTrees.println(head, BinaryTrees.PrintStyle.LEVEL_ORDER);
            boolean isfull = getIsFull(head);
            System.out.println(isfull);
//            if (isFull1(head) != isFull2(head)) {
//                System.out.println("Oops!");
//            }
        }
        System.out.println("finish!");
    }
}
