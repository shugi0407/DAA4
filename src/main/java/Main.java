import graph.scc.SCCFinder;
import graph.topo.TopologicalSort;
import graph.dagsp.DAGShortestPath;
import utils.Metrics;
import utils.MetricsImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.json.*;   // Make sure org.json:json:20240303 is added as a dependency

public class Main {
    public static void main(String[] args) throws IOException {
        // === Load Graph from JSON file ===
        String filePath = "data/tasks (1).json"; // you can change this to any dataset (small_1.json, etc.)
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        JSONObject obj = new JSONObject(content);

        int n = obj.getInt("n");
        int source = obj.getInt("source");
        JSONArray edges = obj.getJSONArray("edges");

        // === Create Metrics ===
        Metrics metrics = new MetricsImpl();

        // === 1️⃣ SCC Detection ===
        System.out.println("==== STRONGLY CONNECTED COMPONENTS (SCC) ====");
        SCCFinder sccFinder = new SCCFinder(n, metrics);
        for (int i = 0; i < edges.length(); i++) {
            JSONObject e = edges.getJSONObject(i);
            sccFinder.addEdge(e.getInt("u"), e.getInt("v"));
        }

        metrics.start();
        var sccs = sccFinder.findSCCs();
        metrics.stop();

        sccFinder.printSCCs();
        System.out.println("\n--- SCC Metrics ---");
        System.out.println(metrics);

        // === 2️⃣ Topological Sort ===
        System.out.println("\n==== TOPOLOGICAL SORT ====");
        metrics.reset();
        TopologicalSort topo = new TopologicalSort(n, metrics);
        for (int i = 0; i < edges.length(); i++) {
            JSONObject e = edges.getJSONObject(i);
            topo.addEdge(e.getInt("u"), e.getInt("v"));
        }

        metrics.start();
        var topoOrder = topo.sort();
        metrics.stop();

        System.out.println("Topological Order: " + topoOrder);
        System.out.println("\n--- TopoSort Metrics ---");
        System.out.println(metrics);

        // === 3️⃣ Shortest & Longest Path in DAG ===
        System.out.println("\n==== DAG SHORTEST & LONGEST PATH ====");
        metrics.reset();
        DAGShortestPath dag = new DAGShortestPath(n, metrics);
        for (int i = 0; i < edges.length(); i++) {
            JSONObject e = edges.getJSONObject(i);
            dag.addEdge(e.getInt("u"), e.getInt("v"), e.getInt("w"));
        }

        metrics.start();
        var shortest = dag.shortestPath(source, topoOrder);
        var longest = dag.longestPath(source, topoOrder);
        metrics.stop();

        System.out.println("Shortest Distances from " + source + ": " + Arrays.toString(shortest));
        System.out.println("Longest Distances (Critical Path) from " + source + ": " + Arrays.toString(longest));
        System.out.println("\n--- DAG Path Metrics ---");
        System.out.println(metrics);

        // === Summary ===
        System.out.println("\n==== SUMMARY ====");
        System.out.println("Total SCCs: " + sccs.size());
        System.out.println("Topological Order Length: " + topoOrder.size());
        System.out.println("Source Node: " + source);
        System.out.println("Graph File: " + filePath);
    }
}
