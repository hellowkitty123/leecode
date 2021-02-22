package com.learn.day10;

import com.learn.graph.Edge;
import com.learn.graph.Graph;
import com.learn.graph.Node;

import java.util.ArrayList;


/**
 * weigth 边的权值 ， from 从某个节点，  to 到某个节点
 * [
 *      [weight, from , to]
 *      [weight, from , to]
 * ]
 */
public class GraphGenerator {
    /**
     * 图描述转换
     * @param matrix
     * @return
     */
    public static Graph createGraph(int[][] matrix){
        if (matrix == null){
            return null;
        }
        Graph graph = new Graph();

        for (int i =0; i<matrix.length;i++){
            int weigth = matrix[i][0];
            int from = matrix[i][1];
            int to = matrix[i][2];
            if (!graph.nodes.containsKey(from)){
                graph.nodes.put(from,new Node(from));
            }

            if (!graph.nodes.containsKey(to)){
                graph.nodes.put(to,new Node(to));
            }

            Node fromNode = graph.nodes.get(from);
            Node toNode = graph.nodes.get(to);
            Edge edge = new Edge(weigth,fromNode,toNode);

            graph.edges.add(edge);
            fromNode.nexts.add(toNode);
            fromNode.out++;
            fromNode.edges.add(edge);
            toNode.edges.add(edge);
            toNode.in++;
        }
        return graph;
    }
}
