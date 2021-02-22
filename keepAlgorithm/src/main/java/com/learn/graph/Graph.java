package com.learn.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Graph {
    public HashMap<Integer,Node> nodes;
    public HashSet<Edge> edges;
    public Graph(){
        this.nodes = new HashMap<>();
        this.edges = new HashSet<>();
    }
}
