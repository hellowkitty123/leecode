package com.test.top;

/**
 * 538. 把二叉搜索树转换为累加树
 * 给出二叉 搜索 树的根节点，该树的节点值各不相同，请你将其转换为累加树（Greater Sum Tree），使每个节点 node 的新值等于原树中大于或等于 node.val 的值之和。
 *
 * 提醒一下，二叉搜索树满足下列约束条件：
 *
 * 节点的左子树仅包含键 小于 节点键的节点。
 * 节点的右子树仅包含键 大于 节点键的节点。
 * 左右子树也必须是二叉搜索树。
 *
 */
public class code538_convertBST {
    public static int sum =0;
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    public static TreeNode convertBST(TreeNode root) {
        if (root == null){
            return null;
        }
        process(root);
        return root;
    }

    public static void process(TreeNode node){
        if (node == null){
            return ;
        }

        process(node.right);

        sum += node.val;
        node.val = sum;

        process(node.left);
    }

    public static void main(String[] args) {

    }
}
