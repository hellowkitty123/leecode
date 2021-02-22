package com.learn.train01.day06;

/**
 * morris 遍历树 流程
 * 0 当前节点cur ， 一开始cur来到整棵树的头结点
 * 1） cur 无左树， cur = cur.right
 * 2)  cur 有左树，找到作数最右的节点，mostright
 *      1- mostright 的右指针指向null的， mostright.right = cur ,cur = cur.left
 *      2- mostright 的右指针指向cur mostright.right = null cur = cur.right
 *      3- cur = null 停
 */
public class MorrisTraversal {
    public static class Node {
        public int value;
        Node left;
        Node right;

        public Node(int data) {
            this.value = data;
        }
    }

    /**
     *
     * @param head
     * @return
     */
    public static void morris(Node head){
        if (head == null){
            return;
        }
        Node cur = head;
        Node mostRight;

        while (cur != null){
            mostRight = cur.left;
            // 左孩子不等于null ， 找左树最右节点
            if (mostRight != null){
                while (mostRight.right != null && mostRight.right != cur){
                    mostRight = mostRight.right;
                }
                // mostRight == null 第一次来到， 向左移动
                if (mostRight.right == null){
                    mostRight.right = cur ;
                    // ... 先序遍历
//                    System.out.println(cur.value);
                    cur = cur.left;
                    continue;
                    //mostRight == cur 第二次来到， 向右移动
                }else{
                    // ... 后续遍历 第二次来到翻转边界打印
                    // printEdge(cur.left);
                    mostRight.right = null;
                }
            }
            // ... 中序遍历
            // System.out.println(cur.value);
            // 左孩子等于空， 向右移动


            // ... 后续遍历 最右的边界
            // printEdge(cur.left);
            cur = cur.right;
        }

    }

    public static boolean isBST(Node head){
        if (head == null){
            return true;
        }
        Node cur = head;
        Node mostRight;
        Integer pre = null;
        while (cur != null){
            mostRight = cur.left;
            // 左孩子不等于null ， 找左树最右节点
            if (mostRight != null){
                while (mostRight.right != null && mostRight.right != cur){
                    mostRight = mostRight.right;
                }
                // mostRight == null 第一次来到， 向左移动
                if (mostRight.right == null){
                    mostRight.right = cur ;
                    // ... 先序遍历
//                    System.out.println(cur.value);
                    cur = cur.left;
                    continue;
                    //mostRight == cur 第二次来到， 向右移动
                }else{
                    // ... 后续遍历 第二次来到翻转边界打印
                    // printEdge(cur.left);
                    mostRight.right = null;
                }
            }
            // ... 中序遍历
            // System.out.println(cur.value);
            // 左孩子等于空， 向右移动
            if (pre != null && pre >= cur.value){
                return false;
            }
            pre = cur.value;
            cur = cur.right;
        }
        return true;
    }


    public static void main(String[] args) {
        Node head = new Node(4);
        head.left = new Node(2);
        head.right = new Node(6);
        head.left.left = new Node(1);
        head.left.right = new Node(3);
        head.right.left = new Node(5);
        head.right.right = new Node(7);
//        printTree(head);
//        morrisIn(head);
//        morrisPre(head);
//        morrisPos(head);
//        printTree(head);

    }
}
