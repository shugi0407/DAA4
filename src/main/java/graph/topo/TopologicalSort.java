package graph.topo;

import java.util.*;
import utils.Metrics;   // ✅ import metrics interface

public class TopologicalSort {
    private final int n;
    private final List<List<Integer>> graph;
    private final Metrics metrics;  // ✅ store metrics reference

    // ✅ Updated constructor
    public TopologicalSort(int n, Metrics metrics) {
        this.n = n;
        this.metrics = metrics;
        graph = new ArrayList<>();
        for (int i = 0; i < n; i++)
            graph.add(new ArrayList<>());
    }

    public void addEdge(int u, int v) {
        graph.get(u).add(v);
    }

    public List<Integer> sort() {
        metrics.start();
        int[] indegree = new int[n];
        for (List<Integer> edges : graph)
            for (int v : edges) indegree[v]++;

        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                q.add(i);
                metrics.increment("Queue_Push");
            }
        }

        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            metrics.increment("Queue_Pop");
            order.add(u);

            for (int v : graph.get(u)) {
                indegree[v]--;
                if (indegree[v] == 0) {
                    q.add(v);
                    metrics.increment("Queue_Push");
                }
            }
        }

        metrics.stop();
        return order;
    }
}
