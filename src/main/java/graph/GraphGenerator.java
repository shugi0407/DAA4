package graph;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GraphGenerator {

    static class Edge {
        int u, v, w;
        Edge(int u, int v, int w) { this.u = u; this.v = v; this.w = w; }
    }

    public static void main(String[] args) throws IOException {
        generateGraphs("data/");
    }

    public static void generateGraphs(String path) throws IOException {
        Random rand = new Random();

        // Categories
        int[][] configs = {
                {6, 12}, {8, 14}, {10, 16}, // Small
                {12, 24}, {15, 30}, {18, 35}, // Medium
                {25, 60}, {35, 80}, {45, 100} // Large
        };

        for (int i = 0; i < configs.length; i++) {
            int n = configs[i][0];
            int edgesCount = configs[i][1];
            boolean cyclic = (i % 2 == 0); // alternate cyclic/acyclic

            List<Edge> edges = new ArrayList<>();
            for (int j = 0; j < edgesCount; j++) {
                int u = rand.nextInt(n);
                int v = rand.nextInt(n);
                if (u == v) continue; // skip self-loops
                int w = 1 + rand.nextInt(10);
                edges.add(new Edge(u, v, w));

                // Optionally add a back edge to create a small cycle
                if (cyclic && rand.nextDouble() < 0.05) {
                    edges.add(new Edge(v, u, 1 + rand.nextInt(5)));
                }
            }

            JSONObject obj = new JSONObject();
            obj.put("directed", true);
            obj.put("n", n);

            JSONArray arr = new JSONArray();
            for (Edge e : edges) {
                JSONObject edge = new JSONObject();
                edge.put("u", e.u);
                edge.put("v", e.v);
                edge.put("w", e.w);
                arr.put(edge);
            }

            obj.put("edges", arr);
            obj.put("source", 0);
            obj.put("weight_model", "edge");

            String fileName;
            if (i < 3) fileName = "small_" + (i + 1);
            else if (i < 6) fileName = "medium_" + (i - 2);
            else fileName = "large_" + (i - 5);

            try (FileWriter file = new FileWriter(path + fileName + ".json")) {
                file.write(obj.toString(4));
            }

            System.out.printf("✅ Created %s.json — %d nodes, %d edges, %s%n",
                    fileName, n, edgesCount, cyclic ? "cyclic" : "DAG");
        }
    }
}
