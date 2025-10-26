package edu.assignment3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.*;
import java.io.PrintWriter;
import java.io.FileWriter;

public class IO {
    private static final Gson G = new GsonBuilder().setPrettyPrinting().create();

    public static class InputGraph {
        public int id;
        public List<String> nodes;
        public List<Map<String, Object>> edges;
    }

    public static Map<String,Object> readInput(PathWrapper p) throws IOException {
        String s = Files.readString(p.path);
        Type t = new TypeToken<Map<String, List<InputGraph>>>(){}.getType();
        Map<String, List<InputGraph>> map = G.fromJson(s, t);
        Map<String,Object> out = new HashMap<>();
        List<InputGraph> gs = map.get("graphs");
        List<Graph> graphs = new ArrayList<>();
        for (InputGraph ig: gs){
            Graph g = new Graph(ig.nodes);
            for (Map<String,Object> e: ig.edges){
                String from = (String)e.get("from");
                String to = (String)e.get("to");
                double w = ((Number)e.get("weight")).doubleValue();
                g.addEdge(from, to, w);
            }
            graphs.add(g);
        }
        out.put("graphs", graphs);
        out.put("meta", gs);
        return out;
    }

    public static void writeOutput(List<Map<String,Object>> results, File out) throws IOException {
        Map<String,Object> wrapper = new HashMap<>();
        wrapper.put("results", results);
        try (Writer w = new FileWriter(out)){
            G.toJson(wrapper, w);
        }
    }

    public static void writeCsvOutput(List<Map<String,Object>> results, File out) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(out))) {

            pw.println("graph_id,vertices,edges,total_cost,prim_time_ms,prim_ops,kruskal_time_ms,kruskal_ops_comps,kruskal_ops_unions");

            for (Map<String,Object> res : results) {
                Object graphId = res.get("graph_id");

                @SuppressWarnings("unchecked")
                Map<String,Object> stats = (Map<String,Object>) res.get("input_stats");
                Object vertices = stats.get("vertices");
                Object edges = stats.get("edges");

                @SuppressWarnings("unchecked")
                Map<String,Object> prim = (Map<String,Object>) res.get("prim");
                Object totalCost = prim.get("total_cost");
                Object primTime = prim.get("execution_time_ms");

                @SuppressWarnings("unchecked")
                Map<String,Object> primOpsMap = (Map<String,Object>) prim.get("operations_count");
                Object primOps = primOpsMap.get("comparisons");

                @SuppressWarnings("unchecked")
                Map<String,Object> kruskal = (Map<String,Object>) res.get("kruskal");
                Object kruskalTime = kruskal.get("execution_time_ms");

                @SuppressWarnings("unchecked")
                Map<String,Object> kruskalOpsMap = (Map<String,Object>) kruskal.get("operations_count");
                Object kruskalOpsComps = kruskalOpsMap.get("comparisons");
                Object kruskalOpsUnions = kruskalOpsMap.get("unions");

                String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s",
                        graphId, vertices, edges, totalCost, primTime, primOps,
                        kruskalTime, kruskalOpsComps, kruskalOpsUnions
                );

                pw.println(line);
            }
        }
    }

    public static class PathWrapper {
        public java.nio.file.Path path;
        public PathWrapper(java.nio.file.Path p){ this.path = p; }
    }
}