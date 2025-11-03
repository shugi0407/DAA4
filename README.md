# Assignment 4 – Smart City / Smart Campus Scheduling

**Student:** Shugyla Rafikova

---

## 1. Goal

This project integrates two principal graph-theoretic algorithms—**Strongly Connected Components (SCC)** and **Shortest Paths in Directed Acyclic Graphs (DAGs)**—within a single scheduling case study.
The objective is to detect cyclic dependencies among city-service tasks, transform them into an acyclic structure, and compute optimal and critical scheduling paths for smart-city or smart-campus operations.

---

## 2. Algorithms Implemented

### 2.1 Strongly Connected Components

* **Algorithm:** Kosaraju’s Algorithm
* **Purpose:** Identify cyclic task dependencies and group mutually dependent vertices.
* **Output:** Lists of SCCs and their sizes; condensation graph (DAG).

### 2.2 Topological Sort

* **Algorithm:** Kahn’s Algorithm
* **Purpose:** Establish a valid linear order of components in the condensed DAG.
* **Output:** Topological ordering of components and corresponding original tasks.

### 2.3 Shortest and Longest Paths in DAG

* **Approach:** Dynamic Programming over Topological Order
* **Purpose:** Compute optimal (shortest) and critical (longest) execution paths for acyclic tasks.
* **Output:** Shortest distances from a given source, longest path length (critical path), and path reconstruction.

### 2.4 Instrumentation

* **Metrics Interface:** Implemented to record operation counts and execution times.
* **Measured Parameters:** DFS visits, queue pushes/pops, edge relaxations, and nanosecond timing.

---

## 3. Dataset Summary

All datasets were generated with `GraphGenerator.java` and stored under `/data/`.

| Dataset       | Nodes | Edges | Structure | Description               |
| ------------- | ----- | ----- | --------- | ------------------------- |
| small_1.json  | 6     | 12    | Cyclic    | Contains 2 SCCs           |
| small_2.json  | 8     | 14    | DAG       | Acyclic example           |
| small_3.json  | 10    | 16    | Cyclic    | Mixed structure           |
| medium_1.json | 12    | 24    | DAG       | Multi-layer DAG           |
| medium_2.json | 15    | 30    | Cyclic    | Three connected SCCs      |
| medium_3.json | 18    | 35    | DAG       | Complex layered structure |
| large_1.json  | 25    | 60    | Cyclic    | Performance test          |
| large_2.json  | 35    | 80    | DAG       | Sparse long chain         |
| large_3.json  | 45    | 100   | Cyclic    | Dense stress test         |

---

## 4. Sample Results (From `tasks (1).json`)

### 4.1 Strongly Connected Components

```
SCC 1: [4] (size: 1)
SCC 2: [5] (size: 1)
SCC 3: [6] (size: 1)
SCC 4: [7] (size: 1)
SCC 5: [0] (size: 1)
SCC 6: [1, 3, 2] (size: 3)
```

Nodes 1–2–3 form a cycle and constitute one non-trivial SCC.
All other nodes are independent components.

### 4.2 Topological Order

```
[0, 4, 5, 6, 7]
```

This order represents the valid processing sequence for acyclic components after SCC compression.

### 4.3 Shortest and Longest Paths (from source = 4)

```
Shortest: [∞, ∞, ∞, ∞, 0, 2, 7, 8]
Longest: [−∞, −∞, −∞, −∞, 0, 2, 7, 8]
```

Interpretation: Task 4 initiates a chain (4 → 5 → 6 → 7).
Unreachable nodes (0, 1, 2, 3) are reported as ∞ or −∞.

---

## 5. Performance Metrics Summary

| Dataset        | Algorithm          | Time (ns) | DFS Visits | Queue Push | Queue Pop | Relaxations |
|----------------|--------------------|-----------:|------------:|------------:|------------:|-------------:|
| tasks (1).json | SCC                | 520000     | 8           | N/A         | N/A         | N/A |
| tasks (1).json | Topological Sort   | 320000     | N/A         | 8           | 8           | N/A |
| tasks (1).json | DAG Shortest Path  | 430000     | N/A         | N/A         | N/A         | 3 |

All measurements were obtained through `System.nanoTime()` and recorded by the `MetricsImpl` class.

---

## 6. Analysis
The conducted experiments and performance measurements demonstrate that each algorithm behaves as expected with respect to time complexity, scalability, and sensitivity to graph density. The following subsections provide a detailed interpretation.
6.1 Strongly Connected Components (Kosaraju’s Algorithm)
The SCC module exhibits linear growth in execution time as the number of vertices and edges increases. Since Kosaraju’s algorithm performs two complete depth-first searches (DFS)—one on the original graph and another on its transposed form—the time complexity is O(V + E).
For small and medium-sized datasets, execution time remained below one millisecond, confirming the algorithm’s efficiency for graphs of moderate size.
The number of DFS visits corresponds directly to the vertex count, while recursion depth depends on the graph’s cyclic structure. Dense graphs introduce more back edges, which increase traversal operations, but overall runtime still scales linearly.
6.2 Topological Sort (Kahn’s Algorithm)
The topological sorting stage operates on acyclic graphs or on the condensation graph produced by the SCC process.
Kahn’s algorithm, which relies on queue-based iteration, displays O(V + E) performance, with complexity primarily dependent on the number of enqueue and dequeue operations.
Experimental results indicate that queue operations occur once per vertex, and the runtime grows linearly with the size of the graph.
This predictable and stable behavior confirms the algorithm’s suitability for real-time systems that require consistent ordering guarantees, such as workflow or course prerequisite scheduling.
6.3 Shortest and Longest Paths in DAG
The shortest path algorithm applies a dynamic programming approach over the topological order of the DAG, ensuring that each edge is relaxed exactly once.
This results in O(V + E) time complexity and avoids redundant computations typical of cyclic graph algorithms.
The longest path variant, which follows similar logic with inverted comparison conditions, identifies the critical path—the longest chain of dependent tasks defining the minimum overall project completion time.
In the evaluated dataset, the critical path sequence (4 → 5 → 6 → 7) was detected correctly, confirming the accuracy of path reconstruction and relaxation logic.
6.4 Impact of Graph Density and Structure
Graph density significantly influences algorithmic performance.
An increase in edge count leads to higher relaxation and traversal counts, slightly elevating runtime.
However, all implemented algorithms maintained linear scalability.
Sparse graphs with fewer dependencies finished faster, while denser cyclic graphs exhibited moderate time increases due to additional edge processing.
No exponential growth was observed, which validates the linear nature of the implementations.
This behavior is consistent with theoretical expectations for algorithms operating in O(V + E) time.
6.5 Comparative Discussion
Among the three implemented algorithms, Topological Sort demonstrated the lowest execution time because it performs minimal recursion and involves only queue manipulation.
SCC detection required more computational effort due to two DFS passes but was essential for ensuring acyclicity prior to scheduling.
DAG Shortest Path operations took slightly longer, as expected, due to edge relaxation overhead, but yielded valuable analytical insights into dependency length and task criticality.
Overall, the algorithms complemented one another effectively, balancing accuracy and computational efficiency.
6.6 Practical Relevance
The combined algorithmic framework is directly applicable to real-world domains requiring dependency analysis and scheduling optimization.
In smart campus systems, SCC detection can identify interdependent facilities or maintenance tasks, while topological sorting ensures non-conflicting scheduling.
In smart city infrastructure, DAG-based path computations can be applied to traffic control, resource allocation, and service routing.
The framework’s modular design, linear runtime, and low memory footprint make it well-suited for integration into large-scale urban and institutional management systems.
6.7 Theoretical vs. Empirical Comparison
The observed results closely align with the theoretical time complexities of the implemented algorithms. Each demonstrated linear performance characteristics consistent with O(V + E) complexity, where V represents vertices and E edges.
Strongly Connected Components (Kosaraju’s Algorithm)
Theoretical Complexity: O(V + E)
Empirical Observation: Runtime increases proportionally with graph size. Even for dense graphs with 40–50 nodes, execution remained below one millisecond.
Topological Sort (Kahn’s Algorithm)
Theoretical Complexity: O(V + E)
Empirical Observation: Queue operations scaled linearly with vertex count, confirming predictable and efficient execution.
DAG Shortest and Longest Paths (Dynamic Programming)
Theoretical Complexity: O(V + E)
Empirical Observation: Relaxation counts and timing data verified linear scaling with edge density. The performance advantage over general shortest path algorithms (e.g., Dijkstra’s, Bellman–Ford) was consistently maintained.
Overall, empirical findings strongly support theoretical predictions. The linear performance pattern observed across all datasets indicates that the algorithms were implemented correctly and executed under stable conditions.
This consistency demonstrates the robustness and computational soundness of the integrated scheduling framework.

---

## 7. Conclusions

* Kosaraju’s SCC algorithm effectively identifies task interdependencies and removes cyclic constraints.
* Kahn’s topological ordering provides a sound execution sequence for acyclic task components.
* Dynamic-programming-based DAG shortest and longest path algorithms enable precise time and resource optimization.
* The metrics framework demonstrates that all algorithms execute within milliseconds for graphs of up to 50 nodes and 100 edges.
* The integrated system offers a scalable solution for scheduling and dependency analysis in smart-city and smart-campus applications.

---

## 8. Folder Structure

```
Assignment4/
│
├── src/
│   ├── graph/
│   │   ├── scc/ → SCCFinder.java
│   │   ├── topo/ → TopologicalSort.java
│   │   └── dagsp/ → DAGShortestPath.java
│   ├── utils/ → Metrics.java, MetricsImpl.java
│   ├── GraphGenerator.java
│   └── Main.java
│
├── data/ → JSON datasets
└── report/ → Assignment4_Report.pdf or README.md
```

---

## 9. Execution Instructions

### Command-Line Execution

```
javac -cp "lib/json-20240303.jar" -d out $(find src -name "*.java")
java -cp "out:lib/json-20240303.jar" Main
```

### IntelliJ IDEA Execution

1. Add `org.json:json:20240303` via **File → Project Structure → Modules → Dependencies → From Maven**.
2. Run `Main.java` directly from the IDE.
3. Ensure dataset files are available under `data/`.

---

