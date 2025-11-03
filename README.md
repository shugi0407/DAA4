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

| Dataset | Algorithm | Time (ns) | DFS Visits | Queue Push | Queue Pop | Relaxations |
|----------|------------|-----------|-------------|-------------|-------------|
| tasks (1).json | SCC | 520,000 | 8 | – | – | – |
| tasks (1).json | Topological Sort | 320,000 | – | 8 | 8 | – |
| tasks (1).json | DAG Shortest Path | 430,000 | – | – | – | 3 |

All measurements were obtained through `System.nanoTime()` and recorded by the `MetricsImpl` class.

---

## 6. Analysis

**SCC (Kosaraju):** Execution time increases linearly with graph size and edge density. Cyclic graphs require two complete DFS passes, but performance remains efficient for graphs under 50 nodes.

**Topological Sort (Kahn):** Complexity is O(V + E). Queue operations increase proportionally to density but do not significantly affect runtime.

**DAG Shortest Path:** Efficiency depends on the number of edge relaxations. The algorithm operates in linear time on acyclic graphs and is highly scalable.

**Graph Structure Impact:** Denser graphs exhibit higher operation counts and longer execution times. Sparse graphs complete quickly with fewer relaxations.

**Practical Implication:** For real-world task planning, cycle detection (SCC) should precede any topological or path analysis. The combined approach ensures efficient and safe scheduling without logical deadlocks.

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

