package edu.assignment3;

import java.util.*;

public class Kruskal {
    public static class Result {
        public List<Edge> mstEdges = new ArrayList<>();
        public double totalCost = 0.0;
        public double timeMs = 0.0;
        public long comparisons = 0;
        public long unions = 0;
    }

    static class DSU {
        private final Map<String, String> parent = new HashMap<>();
        private final Map<String, Integer> rank = new HashMap<>();
        public DSU(Collection<String> nodes){
            for (String n: nodes){ parent.put(n,n); rank.put(n,0); }
        }
        public String find(String x){
            String p = parent.get(x);
            if (!p.equals(x)){
                String r = find(p);
                parent.put(x, r);
                return r;
            }
            return p;
        }
        public boolean union(String a, String b){
            String ra = find(a), rb = find(b);
            if (ra.equals(rb)) return false;
            int raRank = rank.get(ra), rbRank = rank.get(rb);
            if (raRank < rbRank) parent.put(ra, rb);
            else if (raRank > rbRank) parent.put(rb, ra);
            else { parent.put(rb, ra); rank.put(ra, raRank+1); }
            return true;
        }
    }

    public static Result run(Graph g){
        int iterations = 1000;

        for(int i = 0; i < 10; i++){
            runOnce(g);
        }

        Result finalRes = runOnce(g);

        long t0 = System.nanoTime();
        for(int i = 0; i < iterations; i++){
            runOnce(g);
        }
        long t1 = System.nanoTime();

        finalRes.timeMs = (t1 - t0) / 1_000_000.0 / iterations;

        return finalRes;
    }

    private static Result runOnce(Graph g){
        Result res = new Result();

        List<Edge> edges = new ArrayList<>(g.getEdges());
        edges.sort(Comparator.comparingDouble(e -> e.w));
        DSU dsu = new DSU(g.getNodes());
        for (Edge e : edges){
            res.comparisons++;
            if (dsu.union(e.u, e.v)){
                res.unions++;
                res.mstEdges.add(e);
                res.totalCost += e.w;
            }
            if (res.mstEdges.size() == g.numVertices()-1) break;
        }
        return res;
    }
}