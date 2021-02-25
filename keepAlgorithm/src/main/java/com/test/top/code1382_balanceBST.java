package com.test.top;


import com.mj.printer.BinaryTreeInfo;
import com.mj.printer.BinaryTrees;

import java.util.ArrayList;

public class code1382_balanceBST {

    public static class TreeNode implements BinaryTreeInfo {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }

        @Override
        public Object root() {
            return ((TreeNode)this);
        }

        @Override
        public Object left(Object node) {
            return ((TreeNode)node).left;
        }

        @Override
        public Object right(Object node) {
            return ((TreeNode)node).right;
        }

        @Override
        public Object string(Object node) {
            return ((TreeNode)node).val;
        }
    }
    public static TreeNode balanceBST(TreeNode root){

        ArrayList<TreeNode> queue = new ArrayList<>();
        process(root,queue);
        return build(queue, 0, queue.size()-1);
    }

    public static void process(TreeNode root,ArrayList<TreeNode> queue){
        if(root == null){
            return ;
        }
        process(root.left, queue);

        queue.add(root);

        process(root.right, queue);


    }

    public static TreeNode build(ArrayList<TreeNode> queue,int L ,int R){
        // base case 是递归结束的条件，不一定是在头部，有这样的题可以先手动推一个例子，模拟过程
        // bug 1 当 L == mid 时候，左递归 L < R
        // if(L == R){
        //     return queue.get(L);
        // }
        int mid = (L+R)/2;
        TreeNode root = queue.get(mid);
        System.out.println("L = "+L + " mid = "+ mid + " R = "+ R);
        if(L<mid){
            root.left = build(queue, L, mid - 1);
        }else{
            root.left = null;
        }
        if(R>mid){
            root.right = build(queue, mid+1, R);
        }else{
            root.right = null;
        }
        return root;
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.right = new TreeNode(2);
        root.right.right = new TreeNode(3);
        root.right.right.right = new TreeNode(4);
        BinaryTrees.println(root, BinaryTrees.PrintStyle.LEVEL_ORDER);
        TreeNode node = balanceBST(root);
        BinaryTrees.println(node, BinaryTrees.PrintStyle.LEVEL_ORDER);

    }
}
