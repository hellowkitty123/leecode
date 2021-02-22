package com.learn.day08;

import com.mj.printer.BinaryTreeInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * 给定一棵二叉树的头节点head，和另外两个节点a和b
 * 返回a和b的最低公共祖先
 */
public class LowestAncestor {
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

    /**
     * 暴力方法
     * 步骤1 遍历整个树，生成一张map表， key=节点，value = key的父
     * 步骤2 节点A从A往上遍历，加入到set中
     * 步骤4 节点B从B往上遍历，检查是否在set中，如果在就是答案
     * @param head
     * @return
     */
    public static Node lowestAncestor1(Node head, Node o1, Node o2) {
        if (head == null) {
            return null;
        }
        // key的父节点是value
        HashMap<Node, Node> parentMap = new HashMap<>();
        parentMap.put(head, null);
        fillParentMap(head, parentMap);
        HashSet<Node> o1Set = new HashSet<>();
        Node cur = o1;
        o1Set.add(cur);
        while (parentMap.get(cur) != null) {
            cur = parentMap.get(cur);
            o1Set.add(cur);
        }
        cur = o2;
        while (!o1Set.contains(cur)) {
            cur = parentMap.get(cur);
        }
        return cur;
    }

    public static void fillParentMap(Node head, HashMap<Node, Node> parentMap) {
        if (head.left != null) {
            parentMap.put(head.left, head);
            fillParentMap(head.left, parentMap);
        }
        if (head.right != null) {
            parentMap.put(head.right, head);
            fillParentMap(head.right, parentMap);
        }
    }

    public static class Info{
        Node ans ;
        boolean findO1;
        boolean findO2;

        Info(Node ans , boolean findO1 ,boolean findO2){
            this.ans = ans;
            this.findO1 = findO1;
            this.findO2 = findO2;

        }
    }
    /**
     * O1 和 O2 最初交汇点在哪？
     * 一棵树上第一次发现有O1 并且有 O2 说明交汇了
     * @param head
     * @return
     */
    public static Node getLowestAncestor2(Node head,Node O1,Node O2){
        if (head == null){
            return null;
        }

        Info info = process(head,O1,O2);
        return info.ans;
    }

    public static Info process(Node node,Node O1,Node O2){
        if (node == null){
            return new Info(null,false,false);
        }

        Info infoL = process(node.left,O1,O2);
        Info infoR = process(node.right,O1,O2);

        // O1和O2 最初的交汇点？
        // 1、在左子树上提前交汇
        // 2、在右子树上提前交汇
        // 3、没有在左子树上提前交汇，没有在右子树上提前交汇，但O1 和O2 已经全了
        // 4、O1 = O2 = X
        Node ans = null;
        boolean findO1 = node == O1 || infoL.findO1 || infoR.findO1;
        boolean findO2 = node == O2 || infoL.findO2 || infoR.findO2;
        // 提前在左子树上交汇
        if (infoL.ans != null){
            ans = infoL.ans;
        }
        // 提前在右子树上交汇
        if (infoR.ans != null){
            ans = infoR.ans;
        }
        // 没有提前交汇,O1 和 O2 已经找全了
        if (ans == null){
            ans = findO1 && findO2 ? node:null;
        }
        return new Info(ans,findO1,findO2);
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

    // for test
    public static Node pickRandomOne(Node head) {
        if (head == null) {
            return null;
        }
        ArrayList<Node> arr = new ArrayList<>();
        fillPrelist(head, arr);
        int randomIndex = (int) (Math.random() * arr.size());
        return arr.get(randomIndex);
    }

    // for test
    public static void fillPrelist(Node head, ArrayList<Node> arr) {
        if (head == null) {
            return;
        }
        arr.add(head);
        fillPrelist(head.left, arr);
        fillPrelist(head.right, arr);
    }

    public static void main(String[] args) {
        int maxLevel = 4;
        int maxValue = 100;
        int testTimes = 1000000;
        for (int i = 0; i < testTimes; i++) {
            Node head = generateRandomBST(maxLevel, maxValue);
            Node o1 = pickRandomOne(head);
            Node o2 = pickRandomOne(head);
            if (lowestAncestor1(head, o1, o2) != getLowestAncestor2(head, o1, o2)) {
                System.out.println("Oops!");
            }
        }
        System.out.println("finish!");
    }

}
