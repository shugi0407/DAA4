package graph.scc;

import java.util.*;
import utils.Metrics;   // add this import

public class SCCFinder {
    private int n;
    private List<List<Integer>> graph;
    private List<List<Integer>> reversed;
    private boolean[] visited;
    private Stack<Integer> stack;
    private List<List<Integer>> sccList;
    private Metrics metrics; // store reference

    // ✅ Updated constructor: accepts both n and metrics
    public SCCFinder(int n, Metrics metrics) {
        this.n = n;
        this.metrics = metrics;
        graph = new ArrayList<>();
        reversed = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
            reversed.add(new ArrayList<>());
        }
    }

    public void addEdge(int u, int v) {
        graph.get(u).add(v);
        reversed.get(v).add(u);
    }

    public List<List<Integer>> findSCCs() {
        visited = new boolean[n];
        stack = new Stack<>();

        for (int i = 0; i < n; i++) {
            if (!visited[i])
                dfs1(i);
        }

        Arrays.fill(visited, false);
        sccList = new ArrayList<>();

        while (!stack.isEmpty()) {
            int node = stack.pop();
            if (!visited[node]) {
                List<Integer> component = new ArrayList<>();
                dfs2(node, component);
                sccList.add(component);
            }
        }

        return sccList;
    }

    private void dfs1(int v) {
        visited[v] = true;
        metrics.increment("DFS_Visits"); // count visits
        for (int next : graph.get(v)) {
            if (!visited[next]) dfs1(next);
        }
        stack.push(v);
    }

    private void dfs2(int v, List<Integer> component) {
        visited[v] = true;
        metrics.increment("DFS_Visits");
        component.add(v);
        for (int next : reversed.get(v)) {
            if (!visited[next]) dfs2(next, component);
        }
    }

    public void printSCCs() {
        int index = 1;
        for (List<Integer> scc : sccList) {
            System.out.println("SCC " + index++ + ": " + scc + " (size: " + scc.size() + ")");
        }
    }
}
