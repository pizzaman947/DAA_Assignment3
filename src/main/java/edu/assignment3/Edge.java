package edu.assignment3;

public class Edge implements Comparable<Edge> {
    public final String u;
    public final String v;
    public final double w;
    public Edge(String u, String v, double w){
        this.u = u; this.v = v; this.w = w;
    }
    @Override
    public int compareTo(Edge o){
        return Double.compare(this.w, o.w);
    }
    public String other(String x){
        return x.equals(u) ? v : u;
    }
    public String toString(){ return String.format("%s-%s(%.2f)", u, v, w); }
}
