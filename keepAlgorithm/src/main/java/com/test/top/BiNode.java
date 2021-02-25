package com.test.top;

/**
 *
 * 面试题 17.12. BiNode
 * 二叉树数据结构TreeNode可用来表示单向链表（其中left置空，right为下一个链表节点）。实现一个方法，把二叉搜索树转换为单向链表，要求依然符合二叉搜索树的性质，转换操作应是原址的，也就是在原始的二叉搜索树上直接修改。
 *
 * 返回转换后的单向链表的头节点。
 *
 * 注意：本题相对原题稍作改动
 *示例：
 *
 * 输入： [4,2,5,1,3,null,6,0]
 * 输出： [0,null,1,null,2,null,3,null,4,null,5,null,6]
 * 提示：
 *
 * 节点数量不会超过 100000。
 */
public class BiNode {
    public static Node head = new Node(-1);
    public static Node pre = null;
    public static class Node{
        Node left ;
        Node right ;
        int value;
        Node(int value){
            this.value = value;
        }
    }
    // 中序遍历，
    public static Node BiNode(Node parent){
        if (parent == null){
            return null;
        }

        return process(parent);
    }


    public static Node process(Node parent){
        if (parent == null){
            return null;
        }
        process(parent.left);

        if (pre == null){
            pre = parent;
            head.right = pre;
        }else{
            pre.right = parent;
            pre = parent;
        }


        process(parent.right);
        return head;
    }


    public static void main(String[] args) {

    }
}
