package com.learn.day08;

import com.learn.day07.TreeMaxWidth;
import com.mj.printer.BinaryTreeInfo;
import com.mj.printer.BinaryTrees;

/*
   找二叉树上任意节点的后继节点
 */
public class SuccessorNode {

    public static class Node implements BinaryTreeInfo {
        Node left;
        Node right;
        Node parent;
        int value ;
        Node(int value){
            this.value = value;
        }

        @Override
        public Object root() {
            return (Node)this;
        }

        @Override
        public Object left(Object node) {
            return ((Node)node).left;
        }

        @Override
        public Object right(Object node) {
            return ((Node)node).right;
        }

        @Override
        public Object string(Object node) {
            return ((Node)node).value;
        }
    }

    // 获得该节点的后继节点
    public static Node getSuccessorNode(Node node){
        if (node == null){
            return null;
        }

        // 如果有孩子存在，寻找右孩子的最左孩子
        if (node.right != null){
             return getMaxLeftChild(node.right);
        }

        // 父亲为后继
        if (node.parent != null){
            return getParentRightLeft(node);
        }
        return null;
    }

    public static Node getParentRightLeft(Node node){

        if (node.parent.right != node){
            return node.parent;
        }
        while (node.parent != null && node.parent.right == node){
            node = node.parent;
        }

        if (node.parent == null){
            return null;
        }

        if (node.parent.left == node){
            return node.parent;
        }

        return null;
    }

    public static Node getMaxLeftChild(Node node){

        if (node.left == null){
            return node;
        }
        while (node.left != null){
            node = node.left;
        }

        return node;
    }


    public static void main(String[] args) {
        Node head = new Node(6);
        head.parent = null;
        head.left = new Node(3);
        head.left.parent = head;
        head.left.left = new Node(1);
        head.left.left.parent = head.left;
        head.left.left.right = new Node(2);
        head.left.left.right.parent = head.left.left;
        head.left.right = new Node(4);
        head.left.right.parent = head.left;
        head.left.right.right = new Node(5);
        head.left.right.right.parent = head.left.right;
        head.right = new Node(9);
        head.right.parent = head;
        head.right.left = new Node(8);
        head.right.left.parent = head.right;
        head.right.left.left = new Node(7);
        head.right.left.left.parent = head.right.left;
        head.right.right = new Node(10);
        head.right.right.parent = head.right;

        BinaryTrees.println(head, BinaryTrees.PrintStyle.LEVEL_ORDER);


        Node test = head.left.left;
        System.out.println(test.value + " next: " + getSuccessorNode(test).value);
        test = head.left.left.right;
        System.out.println(test.value + " next: " + getSuccessorNode(test).value);
        test = head.left;
        System.out.println(test.value + " next: " + getSuccessorNode(test).value);
        test = head.left.right;
        System.out.println(test.value + " next: " + getSuccessorNode(test).value);
        test = head.left.right.right;
        System.out.println(test.value + " next: " + getSuccessorNode(test).value);
        test = head;
        System.out.println(test.value + " next: " + getSuccessorNode(test).value);
        test = head.right.left.left;
        System.out.println(test.value + " next: " + getSuccessorNode(test).value);
        test = head.right.left;
        System.out.println(test.value + " next: " + getSuccessorNode(test).value);
        test = head.right;
        System.out.println(test.value + " next: " + getSuccessorNode(test).value);
        test = head.right.right; // 10's next is null
        System.out.println(test.value + " next: " + getSuccessorNode(test));
    }
}
