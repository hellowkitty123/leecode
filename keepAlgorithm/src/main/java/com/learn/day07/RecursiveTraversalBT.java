package com.learn.day07;

import com.mj.printer.BinaryTreeInfo;
import com.mj.printer.BinaryTrees;

/*
先序中序后序遍历二叉树
 */
public class RecursiveTraversalBT {

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

    public static void pre(Node head){
        if (head == null){
            return;
        }

        System.out.println(head.value);

        pre(head.left);
        pre(head.right);
    }

    public static void main(String[] args) {
        Node head = new Node(1);
        head.left = new Node(2);
        head.right = new Node(3);
        head.left.left = new Node(4);
        head.left.right = new Node(5);
        head.right.left = new Node(6);
        head.right.right = new Node(7);
        BinaryTrees.println(head, BinaryTrees.PrintStyle.LEVEL_ORDER);
        pre(head);
    }
}
