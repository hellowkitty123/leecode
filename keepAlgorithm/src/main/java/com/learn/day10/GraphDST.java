package com.learn.day10;

import com.learn.graph.Node;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Stack;

public class GraphDST {
    public static void graphDST(Node node){
        if (node == null){
            return;
        }
        HashSet<Node> set = new LinkedHashSet<>();
        Stack<Node> stack = new Stack<>();          // stack
        stack.add(node);
        set.add(node);
        System.out.println(node.value);
        while (!stack.isEmpty()){
            node = stack.pop();
            for (Node next : node.nexts){
                if (!set.contains(next)){
                    System.out.println(next.value);
                    set.add(next);
                    stack.add(node);
                    stack.add(next);
                    break;
                }
            }
        }
    }
    public static void main(String[] args) {

    }
}
