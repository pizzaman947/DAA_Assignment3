package edu.assignment3;

import java.util.*;

public class Graph {
    private final Set<String> nodes = new LinkedHashSet<>();
    private final List<Edge> edges = new ArrayList<>();
    private final Map<String, List<Edge>> adj = new HashMap<>();

    public Graph() {}

    public Graph(Collection<String> initialNodes) {
        for (String n : initialNodes) addNode(n);
    }

    public void addNode(String n){
        nodes.add(n);
        adj.putIfAbsent(n, new ArrayList<>());
    }

    public void addEdge(String u, String v, double w){
        addNode(u); addNode(v);
        Edge e = new Edge(u, v, w);
        edges.add(e);
        adj.get(u).add(e);
        adj.get(v).add(e);
    }

    public Set<String> getNodes(){ return Collections.unmodifiableSet(nodes); }
    public List<Edge> getEdges(){ return Collections.unmodifiableList(edges); }
    public Map<String, List<Edge>> getAdj(){ return Collections.unmodifiableMap(adj); }
    public int numVertices(){ return nodes.size(); }
    public int numEdges(){ return edges.size(); }
}
