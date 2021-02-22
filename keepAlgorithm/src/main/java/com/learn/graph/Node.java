package com.learn.graph;

import java.util.ArrayList;

public class Node {
    public int in;  //入度
    public int out; //出度
    public int value;
    public ArrayList<Node> nexts; //邻居表法
    public ArrayList<Edge> edges;
    public Node(int value){
        this.value = value;
    }
}
