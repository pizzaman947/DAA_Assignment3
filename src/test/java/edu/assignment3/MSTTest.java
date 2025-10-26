package edu.assignment3;

import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MSTTest {

    private Graph sampleGraph(){
        Graph g = new Graph(Arrays.asList("A","B","C","D","E"));
        g.addEdge("A","B",4);
        g.addEdge("A","C",3);
        g.addEdge("B","C",2);
        g.addEdge("B","D",5);
        g.addEdge("C","D",7);
        g.addEdge("C","E",8);
        g.addEdge("D","E",6);
        return g;
    }

    private Graph smallGraph(){
        Graph g = new Graph(Arrays.asList("A","B","C","D"));
        g.addEdge("A","B",1);
        g.addEdge("A","C",4);
        g.addEdge("B","C",2);
        g.addEdge("C","D",3);
        g.addEdge("B","D",5);
        return g;
    }

    private Graph disconnectedGraph(){
        Graph g = new Graph(Arrays.asList("A","B","C","D"));
        g.addEdge("A","B",1);
        g.addEdge("C","D",2);
        return g;
    }

    @Test
    public void primKruskalEqualCost(){
        Graph g = sampleGraph();
        Prim.Result p = Prim.run(g, null);
        Kruskal.Result k = Kruskal.run(g);
        assertEquals(p.totalCost, k.totalCost, 1e-9);
    }

    @Test
    public void edgeCountIsVminus1(){
        Graph g = sampleGraph();
        Prim.Result p = Prim.run(g, null);
        Kruskal.Result k = Kruskal.run(g);
        assertEquals(g.numVertices()-1, p.mstEdges.size());
        assertEquals(g.numVertices()-1, k.mstEdges.size());
    }

    @Test
    public void mstIsAcyclic(){
        Graph g = sampleGraph();
        Prim.Result p = Prim.run(g, null);
        assertFalse(hasCycle(p.mstEdges, g.getNodes()));

        Kruskal.Result k = Kruskal.run(g);
        assertFalse(hasCycle(k.mstEdges, g.getNodes()));
    }

    @Test
    public void mstConnectsAllVertices(){
        Graph g = sampleGraph();
        Prim.Result p = Prim.run(g, null);
        assertTrue(isConnected(p.mstEdges, g.getNodes()));

        Kruskal.Result k = Kruskal.run(g);
        assertTrue(isConnected(k.mstEdges, g.getNodes()));
    }

    @Test
    public void disconnectedGraphHandled(){
        Graph g = disconnectedGraph();

        Prim.Result p = Prim.run(g, "A");
        assertTrue(p.mstEdges.size() < g.numVertices() - 1);
        assertEquals(1, p.mstEdges.size());

        Kruskal.Result k = Kruskal.run(g);
        assertTrue(k.mstEdges.size() < g.numVertices() - 1);
        assertEquals(2, k.mstEdges.size());
    }

    @Test
    public void executionTimeNonNegative(){
        Graph g = sampleGraph();
        Prim.Result p = Prim.run(g, null);
        Kruskal.Result k = Kruskal.run(g);
        assertTrue(p.timeMs >= 0);
        assertTrue(k.timeMs >= 0);
    }

    @Test
    public void operationCountsNonNegative(){
        Graph g = sampleGraph();
        Prim.Result p = Prim.run(g, null);
        Kruskal.Result k = Kruskal.run(g);
        assertTrue(p.comparisons >= 0);
        assertTrue(k.comparisons >= 0);
        assertTrue(k.unions >= 0);
    }

    @Test
    public void resultsReproducible(){
        Graph g = sampleGraph();
        Prim.Result p1 = Prim.run(g, null);
        Prim.Result p2 = Prim.run(g, null);
        assertEquals(p1.totalCost, p2.totalCost, 1e-9);
        assertEquals(p1.mstEdges.size(), p2.mstEdges.size());

        Kruskal.Result k1 = Kruskal.run(g);
        Kruskal.Result k2 = Kruskal.run(g);
        assertEquals(k1.totalCost, k2.totalCost, 1e-9);
        assertEquals(k1.mstEdges.size(), k2.mstEdges.size());
    }

    @Test
    public void smallGraphTest(){
        Graph g = smallGraph();
        Prim.Result p = Prim.run(g, null);
        Kruskal.Result k = Kruskal.run(g);

        assertEquals(6.0, p.totalCost, 1e-9);
        assertEquals(6.0, k.totalCost, 1e-9);
        assertEquals(3, p.mstEdges.size());
        assertEquals(3, k.mstEdges.size());
    }

    @Test
    public void emptyGraphHandled(){
        Graph g = new Graph();
        Prim.Result p = Prim.run(g, null);
        Kruskal.Result k = Kruskal.run(g);

        assertEquals(0, p.mstEdges.size());
        assertEquals(0, k.mstEdges.size());
        assertEquals(0.0, p.totalCost, 1e-9);
        assertEquals(0.0, k.totalCost, 1e-9);
    }

    @Test
    public void singleVertexGraph(){
        Graph g = new Graph(Arrays.asList("A"));
        Prim.Result p = Prim.run(g, null);
        Kruskal.Result k = Kruskal.run(g);

        assertEquals(0, p.mstEdges.size());
        assertEquals(0, k.mstEdges.size());
    }

    private boolean hasCycle(List<Edge> edges, Set<String> nodes){
        Map<String, String> parent = new HashMap<>();
        for (String n : nodes) {
            parent.put(n, n);
        }

        for (Edge e : edges){
            String rootU = find(parent, e.u);
            String rootV = find(parent, e.v);

            if (rootU.equals(rootV)) {
                return true;
            }
            parent.put(rootU, rootV);
        }
        return false;
    }

    private String find(Map<String, String> parent, String node){
        if (!parent.get(node).equals(node)) {
            parent.put(node, find(parent, parent.get(node)));
        }
        return parent.get(node);
    }

    private boolean isConnected(List<Edge> edges, Set<String> nodes){
        if (nodes.isEmpty()) return true;
        if (edges.isEmpty()) return nodes.size() == 1;

        Map<String, List<String>> adj = new HashMap<>();
        for (String n : nodes) {
            adj.put(n, new ArrayList<>());
        }

        for (Edge e : edges){
            adj.get(e.u).add(e.v);
            adj.get(e.v).add(e.u);
        }

        Set<String> visited = new HashSet<>();
        String start = nodes.iterator().next();
        dfs(start, adj, visited);

        return visited.size() == nodes.size();
    }

    private void dfs(String node, Map<String, List<String>> adj, Set<String> visited){
        visited.add(node);
        for (String neighbor : adj.get(node)){
            if (!visited.contains(neighbor)){
                dfs(neighbor, adj, visited);
            }
        }
    }
}