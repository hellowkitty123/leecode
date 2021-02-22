package com.learn.day10;

import com.learn.graph.Node;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;

public class GraphBST {
    public static void graphBST(Node node){
        if (node == null){
            return;
        }
        Queue<Node> queue = new LinkedList<>();
        HashSet<Node> set = new LinkedHashSet<>();
        queue.add(node);
        set.add(node);
        while (!queue.isEmpty()){
            node = queue.poll();
            System.out.println(node.value);
            for (Node next : node.nexts){
                if (!set.contains(next)){
                    queue.add(next);
                    set.add(node);
                }
            }
        }
    }

    public static void main(String[] args) {

    }

}
