package edu.assignment3;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String input = args.length>0 ? args[0] : "input.json";
        String output = args.length>1 ? args[1] : "output.json";
        java.nio.file.Path inputPath = Paths.get(input).toAbsolutePath();
        IO.PathWrapper pw = new IO.PathWrapper(inputPath);
        Map<String,Object> loaded = IO.readInput(pw);
        @SuppressWarnings("unchecked")
        List<Graph> graphs = (List<Graph>)loaded.get("graphs");

        @SuppressWarnings("unchecked")
        List<IO.InputGraph> meta = (List<IO.InputGraph>)loaded.get("meta");

        List<Map<String,Object>> results = new ArrayList<>();
        for (int i=0;i<graphs.size();i++){
            Graph g = graphs.get(i);

            IO.InputGraph gm = meta.get(i);

            Map<String,Object> res = new LinkedHashMap<>();

            res.put("graph_id", gm.id);

            Map<String,Object> inputStats = new LinkedHashMap<>();
            inputStats.put("vertices", g.numVertices());
            inputStats.put("edges", g.numEdges());
            res.put("input_stats", inputStats);

            Prim.Result p = Prim.run(g, null);
            Kruskal.Result k = Kruskal.run(g);

            Map<String,Object> primMap = new LinkedHashMap<>();
            primMap.put("mst_edges", edgeListToMaps(p.mstEdges));
            primMap.put("total_cost", p.totalCost);
            primMap.put("operations_count", Map.of("comparisons", p.comparisons));
            primMap.put("execution_time_ms", p.timeMs);
            res.put("prim", primMap);

            Map<String,Object> kruskalMap = new LinkedHashMap<>();
            kruskalMap.put("mst_edges", edgeListToMaps(k.mstEdges));
            kruskalMap.put("total_cost", k.totalCost);
            kruskalMap.put("operations_count", Map.of("comparisons", k.comparisons, "unions", k.unions));
            kruskalMap.put("execution_time_ms", k.timeMs);
            res.put("kruskal", kruskalMap);

            results.add(res);
        }
        IO.writeOutput(results, new File(output));
        System.out.println("Wrote output to " + output);

        String csvOutput = "results_summary.csv";
        IO.writeCsvOutput(results, new File(csvOutput));
        System.out.println("Wrote CSV summary to " + csvOutput);
    }

    private static List<Map<String,Object>> edgeListToMaps(List<Edge> edges){
        List<Map<String,Object>> out = new ArrayList<>();
        for (Edge e: edges){
            Map<String,Object> m = new LinkedHashMap<>();
            m.put("from", e.u);
            m.put("to", e.v);
            m.put("weight", e.w);
            out.add(m);
        }
        return out;
    }
}