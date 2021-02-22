package com.learn.day07;
import com.mj.printer.BinaryTreeInfo;
import com.mj.printer.BinaryTrees;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/*
二叉树哪一层节点数最多
1）其实就是宽度优先遍历，用队列
2）可以通过设置flag变量的方式，来发现某一层的结束（看题目）
 */
public class TreeMaxWidth {
    public static class Node implements BinaryTreeInfo{
        Node left ;
        Node right;
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
    // 树最宽层 节点数
    public static int treeMaxWidthUseMap(Node head){
        if (head == null){
            return 0;
        }
        int max = 0;
        int curLevelNum = 0;
        int curLevel = 1;
        Node curNode;
        Queue<Node> queue = new LinkedList<>();
        HashMap<Node,Integer> map = new HashMap<>();
        queue.add(head);
        map.put(head,curLevel);

        // 队列里还有值，最后一层curlevelnum 还没结算完
        while (!queue.isEmpty()){

            //弹出时加入他的左孩子和右孩子，map标记孩子所在的层
            curNode = queue.poll();
            int curNodeLevel = map.get(curNode); //弹出是直接获取当前节点，根据当前节点的层级，增加子节点的map

            if (curNode.left != null){
                queue.add(curNode.left);
                map.put(curNode.left,curNodeLevel+1);
            }

            if (curNode.right != null){
                queue.add(curNode.right);
                map.put(curNode.right,curNodeLevel+1);
            }
            // 当前节点的层级跟当前层相等 ， 层级node数加
            if (curLevel == curNodeLevel){
                curLevelNum++;
            }else{
//                System.out.println( " leve = "+ curLevel+ " curLevelNum = "+curLevelNum);
                max = Math.max(curLevelNum,max);
                curLevel = curNodeLevel;
                curLevelNum = 1;  //当前node是第二次的第一个节点，初始为1
            }
        }
        max = Math.max(curLevelNum,max);

        return max;
    }


    public static int treeMaxWidthNoMap(Node head){
        if (head == null){
            return 0;
        }
        int max = 0;
        Node curEnd;
        Node nextEnd = null;
        Node curNode;
        int curLevelNum = 1;

        Queue<Node> queue = new LinkedList<>();
        queue.add(head);
        curEnd = head;
        while (!queue.isEmpty()){
            curNode = queue.poll();
            if (curNode.left != null){
                queue.add(curNode.left);
                nextEnd = curNode.left;
            }
            if (curNode.right != null){
                queue.add(curNode.right);
                nextEnd = curNode.right;
            }

            // 当弹出元素跟当前层记录的最后一个元素一样，说明开始当前层结算
            if (curNode == curEnd){
                curEnd = nextEnd;
                max = Math.max(max,curLevelNum);
                curLevelNum = 1;  //下一层开始
            }else{
                curLevelNum ++;
            }
        }


        return max;
    }

    // for test
    public static Node generateRandomBST(int maxLevel, int maxValue) {
        return generate(1, maxLevel, maxValue);
    }

    // for test
    public static Node generate(int level, int maxLevel, int maxValue) {
        if (level > maxLevel || Math.random() > 0.6) {
            return null;
        }
        Node head = new Node((int) (Math.random() * maxValue));
        head.left = generate(level + 1, maxLevel, maxValue);
        head.right = generate(level + 1, maxLevel, maxValue);
        return head;
    }
    public static void main(String[] args) {
        int maxLevel = 4;
        int maxValue = 100;
        int testTimes = 200;
        for (int i = 0; i < testTimes; i++) {
            Node head = generateRandomBST(maxLevel, maxValue);
            BinaryTrees.println(head, BinaryTrees.PrintStyle.LEVEL_ORDER);
            int num = treeMaxWidthUseMap(head);
            int num2 = treeMaxWidthNoMap(head);
//            System.out.println(" --- num1 "+num);
//            System.out.println(" --- num2 "+num2);
//            System.out.println(" --------------");
            if (treeMaxWidthUseMap(head) != treeMaxWidthNoMap(head)) {
                System.out.println("Oops!");
            }
        }
        System.out.println("finish!");

    }

}
