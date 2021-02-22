package com.learn.day10;

import com.learn.graph.Edge;
import com.learn.graph.Node;

import java.util.*;

public class Prim {

    public static class Comparetor implements Comparator<Edge>{

        @Override
        public int compare(Edge o1, Edge o2) {
            return o2.weight - o1.weight;
        }
    }

    /**
     * 最小生成树P算法
     * @param graph
     */
    public static Set<Node> getPrim(Node graph){

        // 解锁出来的node进小根堆
        HashSet<Node> nodes = new LinkedHashSet<>();
//        // 已经使用过的边
//        HashSet<Edge> edges = new LinkedHashSet<>();
        //结果集
        Set<Node> result = new HashSet<>();

        // 解锁出来的边进小根堆
        PriorityQueue<Edge> heap = new PriorityQueue<>(new Comparetor());

        for (Node node : graph.nexts){
            if (!nodes.contains(node)){
                // 头节点加入node 解锁set中
                nodes.add(node);
                for (Edge edge : node.edges){
                    heap.add(edge);
                }

                while (!heap.isEmpty()){
                    Edge edge = heap.poll();
                    Node toNode = edge.to;
                    if (!nodes.contains(toNode)){
                        nodes.add(toNode);
                        result.add(toNode);
                        for (Edge e : toNode.edges){
                            heap.add(e);
                        }
                    }
                }
            }
        }
        return result;
    }


    public static void main(String[] args) {

    }
}
