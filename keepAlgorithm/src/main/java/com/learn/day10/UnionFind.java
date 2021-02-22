package com.learn.day10;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class UnionFind {
    public static class Node{
        int value;
        Node(int value){
            this.value = value;
        }
    }

    public static class UnionFind2{
        HashSet<Node> nodeSet ;
        HashMap<Node,Node> parentMap;
        HashMap<Node,Integer> sizeMap;
        UnionFind2(Node[] arr){
            nodeSet = new HashSet<>();   //注册 节点
            parentMap = new HashMap<>(); //父节点指针
            sizeMap = new HashMap<>();   //并查集的子节点个数，只有顶部节点有值
            for (Node node : arr){
                nodeSet.add(node);
                parentMap.put(node,node);
                sizeMap.put(node,1);
            }
        }

        public boolean isSameSet(Node node1,Node node2){
            if (!(nodeSet.contains(node1) && nodeSet.contains(node2))){
                return false;
            }
            Node parent1 = findFather(node1);
            Node parent2 = findFather(node2);
            return parent2 != null && parent1 == parent2;
        }

        public void union(Node node1, Node node2){
            if (!nodeSet.contains(node1)){
                nodeSet.add(node1);
                parentMap.put(node1,node1);
                sizeMap.put(node1,1);
            }
            if (!nodeSet.contains(node2)){
                nodeSet.add(node2);
                parentMap.put(node2,node2);
                sizeMap.put(node2,1);
            }

            Node parent1 = findFather(node1);
            Node parent2 = findFather(node2);
            if ( parent1 != parent2) {
                if (sizeMap.get(node1) > sizeMap.get(node2)){
                    parentMap.put(parent2,parent1);
                    sizeMap.put(parent2,sizeMap.get(parent1) + sizeMap.get(parent2));
                    sizeMap.remove(parent2);
                }else {
                    parentMap.put(parent1,parent2);
                    sizeMap.put(parent1,sizeMap.get(parent1) + sizeMap.get(parent2));
                    sizeMap.remove(parent1);
                }
            }
        }
        // 通过当前节点找父节点, 重要优化， findFather时将并查集扁平化
        private Node findFather(Node node){
            if (node == null || !nodeSet.contains(node)){
                return null;
            }
            Node cur = node;
            Queue<Node> queue = new LinkedList<>();

            while (parentMap.get(cur) != cur){
                queue.add(cur);
                cur = parentMap.get(cur);
            }

            while (!queue.isEmpty()){
                parentMap.put(queue.poll(),cur);
            }
            return cur;
        }

    }


    public static void main(String[] args) {

    }
}
