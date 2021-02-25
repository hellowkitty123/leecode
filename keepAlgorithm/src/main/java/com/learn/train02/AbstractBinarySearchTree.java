package com.learn.train02;

public class AbstractBinarySearchTree {

    public static class TreeNode{
        TreeNode left;
        TreeNode right;
        TreeNode parent;
        int val;
        TreeNode(int val){
            this.val = val;
        }
    }



    public static class BinarySearchTree{
        TreeNode root;
        int size;
        BinarySearchTree(){
            this.root = null;
            size = 0;
        }

        public TreeNode search(int val){
            if (this.root == null){
                return null;
            }
            TreeNode currentNode = this.root;
            while (!(currentNode == null)){
                if (currentNode.val > val){
                    currentNode = currentNode.left;
                }else if(currentNode.val < val){
                    currentNode = currentNode.right;
                }else {
                    return currentNode;
                }
            }
            return null;
        }

        /**
         * 经典二叉搜索树，插入
         *
         * 当前节点 > 头结点 往右划
         * 当前节点 < 头结点 往左划
         * 划不动了，加到头结点下
         * @param val
         * @return
         */
        public TreeNode insert(int val){


            TreeNode newTree = new TreeNode(val);
            if (this.root == null){
                this.root = newTree;
                return this.root;
            }

            TreeNode currentNode = this.root;
            TreeNode insertParentNode = null;
            while (currentNode!= null){
                insertParentNode = currentNode;
                if (currentNode.val > val){
                    currentNode = currentNode.left;
                }else if(currentNode.val < val){
                    currentNode = currentNode.right;
                }
            }

            if (insertParentNode.val > val){
                insertParentNode.left = newTree;
            }else {
                insertParentNode.right = newTree;
            }
            newTree.parent = insertParentNode;
            size++;
            return newTree;
        }

        /**
         *
         * @param val
         * @return
         */
        public TreeNode delete(int val){

            if (this.root == null){
                return  null;
            }else{
                TreeNode node = search(val);
                if (node != null){
                    return delete(node);
                }
            }
            return null;
        }

        private TreeNode delete(TreeNode node){
            TreeNode result = null;
            if (node != null){
                    // 只有左孩子
                if (node.right == null && node.left != null){
                    result = translate(node , node.left);
                    // 只有右孩子
                }else if(node.left == null && node.right != null){
                    result = translate(node , node.right);
                    // 两边都有
                }else{
                    // 找后继节点，替换头结点， 后继节点父亲做孩子 = 后继节点的有孩子
                    // 头结点替换成 后继节点
                    TreeNode successer = getMinTreeNode(node.right);
                    // 如果 头结点有孩子没有做孩子， 返回的后继就是自己，此时删除头结点，不需要调整右子树的状态
                    if (successer != node){
                        translate(successer,successer.right);
                        successer.right = node.right;
                        successer.right.parent =successer;
                    }
                    translate(node,successer);
                    successer.left = node.left;
                    successer.left.parent = successer;
                    result = successer;
                }
            }

            return result;
        }

        private TreeNode translate(TreeNode repalceToNode,TreeNode newNode){
            if (repalceToNode.parent == null){
                this.root = newNode;
                return this.root;
            }
            if (repalceToNode.parent.left == repalceToNode){
                repalceToNode.parent.left = newNode;
            }else if(repalceToNode.parent.right == repalceToNode){
                repalceToNode.parent.right = newNode;
            }
            if (newNode != null ){
                newNode.parent = repalceToNode.parent;
            }
            return newNode;
        }

        private TreeNode getMinTreeNode(TreeNode parent){
            TreeNode currentNode = parent;
            while (currentNode.right!= null){
                currentNode = currentNode.right;
            }
            return currentNode;
        }

        private TreeNode getMaxTreeNode(TreeNode parent){
            TreeNode currentNode = parent;
            while (currentNode.left!= null){
                currentNode = currentNode.left;
            }
            return currentNode;
        }
    }

    public static void main(String[] args) {

    }
}
