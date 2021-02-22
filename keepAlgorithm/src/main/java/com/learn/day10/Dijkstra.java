package com.learn.day10;

import com.learn.graph.Edge;
import com.learn.graph.Node;

import javax.swing.text.html.parser.Entity;
import java.util.*;

/**
 * 图算法的 A节点出发到所有其他节点最短路径 D算法
 */
public class Dijkstra {
    /**
     * @param from 开始点
     * @return 节点到其他节点的最短路径
     */
    public static HashMap<Node,Integer> dijkstra(Node from){
        if (from == null){
            return null;
        }

        HashMap<Node,Integer> distance = new HashMap<>();
        HashSet<Node> selected = new HashSet<>();

        distance.put(from , 0);

        Node cur = from;
        while (cur != null){
            // cur 桥梁
            for (Edge e : cur.edges){
                // 不在黑名单
                if (!selected.contains(e.to)){
                    // 没有的话 直接赋值 桥梁+ 当前边的权重
                    int max = distance.getOrDefault(e.to, distance.get(cur) + e.weight);
                    // 如果有的话 to当前值 和 强梁 + 当前边的值取最小
                    distance.put(e.to,Math.min(distance.get(cur) + e.weight,max));
                }
            }
            selected.add(cur);
            cur = getMinDistanceAndNoUse(distance,selected);
        }

        return distance;
    }

    /**
     * 取未使用的Node 最小
     * @param distance
     * @param set
     * @return
     */
    public static Node getMinDistanceAndNoUse(HashMap<Node,Integer> distance ,HashSet<Node> set){
        Node minNode = null;
        int minWeight = Integer.MAX_VALUE;
        for (Map.Entry<Node, Integer> entry : distance.entrySet()){
            if (!set.contains(entry.getKey())){
                minNode = entry.getValue() < minWeight ? entry.getKey() : minNode;
                minWeight = entry.getValue() < minWeight ? entry.getValue() : minWeight;
            }
        }
        return minNode;
    }
    public static void main(String[] args) {

    }
}
