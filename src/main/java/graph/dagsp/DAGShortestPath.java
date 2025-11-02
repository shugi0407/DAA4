package graph.dagsp;

import java.util.*;
import utils.Metrics;   // ✅ import metrics interface

public class DAGShortestPath {
    private final int n;
    private final List<List<Edge>> graph;
    private final Metrics metrics; // ✅ store metrics reference

    static class Edge {
        int to, weight;
        Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    // ✅ Updated constructor
    public DAGShortestPath(int n, Metrics metrics) {
        this.n = n;
        this.metrics = metrics;
        graph = new ArrayList<>();
        for (int i = 0; i < n; i++)
            graph.add(new ArrayList<>());
    }

    public void addEdge(int u, int v, int w) {
        graph.get(u).add(new Edge(v, w));
    }

    // --- Shortest Path in DAG ---
    public int[] shortestPath(int src, List<Integer> topoOrder) {
        metrics.start();
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;

        for (int u : topoOrder) {
            if (dist[u] != Integer.MAX_VALUE) {
                for (Edge e : graph.get(u)) {
                    metrics.increment("Relaxations");
                    if (dist[e.to] > dist[u] + e.weight)
                        dist[e.to] = dist[u] + e.weight;
                }
            }
        }

        metrics.stop();
        return dist;
    }

    // --- Longest Path in DAG ---
    public int[] longestPath(int src, List<Integer> topoOrder) {
        metrics.start();
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MIN_VALUE);
        dist[src] = 0;

        for (int u : topoOrder) {
            if (dist[u] != Integer.MIN_VALUE) {
                for (Edge e : graph.get(u)) {
                    metrics.increment("Relaxations");
                    if (dist[e.to] < dist[u] + e.weight)
                        dist[e.to] = dist[u] + e.weight;
                }
            }
        }

        metrics.stop();
        return dist;
    }
}
