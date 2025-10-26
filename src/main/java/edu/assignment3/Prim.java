package edu.assignment3;

import java.util.*;

public class Prim {
    public static class Result {
        public List<Edge> mstEdges = new ArrayList<>();
        public double totalCost = 0.0;
        public double timeMs = 0.0;
        public long comparisons = 0;
    }

    public static Result run(Graph g, String startNode){
        int iterations = 1000;

        for(int i = 0; i < 10; i++){
            runOnce(g, startNode);
        }

        Result finalRes = runOnce(g, startNode);

        long t0 = System.nanoTime();
        for(int i = 0; i < iterations; i++){
            runOnce(g, startNode);
        }
        long t1 = System.nanoTime();

        finalRes.timeMs = (t1 - t0) / 1_000_000.0 / iterations;

        return finalRes;
    }

    private static Result runOnce(Graph g, String startNode){
        Result res = new Result();

        if (g.numVertices() == 0) return res;
        Set<String> inMst = new HashSet<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingDouble(e -> e.w));

        if (startNode == null) {
            startNode = g.getNodes().iterator().next();
        }

        inMst.add(startNode);
        List<Edge> adj0 = g.getAdj().getOrDefault(startNode, Collections.emptyList());
        for (Edge e : adj0) pq.add(e);

        while (!pq.isEmpty() && inMst.size() < g.numVertices()){
            Edge e = pq.poll();
            res.comparisons++;
            String next = inMst.contains(e.u) ? e.v : e.u;
            if (inMst.contains(next)) continue;
            res.mstEdges.add(e);
            res.totalCost += e.w;
            inMst.add(next);
            for (Edge ne : g.getAdj().getOrDefault(next, Collections.emptyList())){
                if (!inMst.contains(ne.other(next))) pq.add(ne);
            }
        }
        return res;
    }
}