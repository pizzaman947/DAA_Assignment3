# Assignment 3: Optimization of a City Transportation Network (MST)

**Author:** Shomanov Rakhat
**Group:** SE-2436  

---

## Table of Contents
1. [Project Overview](#project-overview)
2. [Implementation](#implementation)
3. [Testing Strategy](#testing-strategy)
4. [Results Summary](#results-summary)
5. [Algorithm Comparison](#algorithm-comparison)
6. [Theoretical vs Practical Analysis](#theoretical-vs-practical-analysis)
7. [Conclusions](#conclusions)

---

## Project Overview

This project implements and analyzes **Prim's** and **Kruskal's** algorithms for finding Minimum Spanning Trees (MST) in weighted undirected graphs. The goal is to optimize a city's transportation network by determining the minimum set of roads that connect all districts with the lowest possible construction cost.

### Key Features
- Custom `Graph` and `Edge` data structures (Java OOP)
- JSON-based input/output handling
- Automated JUnit testing suite
- Performance benchmarking with operation counting
- CSV export for result analysis

---

## Implementation

### Project Structure
```
src/main/java/edu/assignment3/
├── Graph.java           # Graph data structure
├── Edge.java            # Edge representation
├── Prim.java            # Prim's algorithm implementation
├── Kruskal.java         # Kruskal's algorithm with Union-Find
├── IO.java              # JSON/CSV input-output utilities
└── Main.java            # Main execution entry point

src/test/java/edu/assignment3/
└── MSTTest.java         # Comprehensive test suite
```

### Algorithm Implementations

#### Prim's Algorithm
- **Data Structure:** Priority Queue (min-heap) for edge selection
- **Strategy:** Grows MST incrementally from a starting vertex
- **Time Complexity:** O(E log V) with binary heap
- **Operation Counting:** Tracks comparisons during edge selection

#### Kruskal's Algorithm
- **Data Structure:** Disjoint Set Union (DSU) with path compression and union by rank
- **Strategy:** Sorts all edges and adds minimum edges that don't create cycles
- **Time Complexity:** O(E log E) for sorting + O(E α(V)) for union-find
- **Operation Counting:** Tracks comparisons and union operations

---

## Testing Strategy

### Test Datasets

Three categories of graphs were generated to evaluate correctness and performance:

#### Small Graphs (4-6 vertices)
- **Purpose:** Verify correctness, easy debugging
- **Examples:**
    - Graph 10: 4 vertices, 5 edges
    - Graph 11: 6 vertices, 8 edges

#### Medium Graphs (10-15 vertices)
- **Purpose:** Observe performance on moderately sized networks
- **Examples:**
    - Graph 3: 15 vertices, 20 edges (sparse)
    - Graph 4: 12 vertices, 52 edges (dense)
    - Graph 5: 10 vertices, 7 edges (disconnected components)

#### Large Graphs (20-30+ vertices)
- **Purpose:** Test scalability and efficiency differences
- **Examples:**
    - Graph 6: 30 vertices, 46 edges (sparse ring with shortcuts)
    - Graph 7: 20 vertices, 162 edges (highly dense network)

### Automated Tests (JUnit)

The test suite validates:

✅ **Correctness Tests:**
- MST total cost is identical for both algorithms
- Number of edges equals V-1 (for connected graphs)
- MST contains no cycles (acyclic property)
- MST connects all vertices (single connected component)
- Disconnected graphs are handled gracefully

✅ **Performance Tests:**
- Execution time is non-negative
- Operation counts are consistent
- Results are reproducible

---

## Results Summary

### Small Graphs

| Graph ID | V | E | Total Cost | Prim Time (ms) | Prim Ops | Kruskal Time (ms) | Kruskal Ops | Winner |
|----------|---|---|------------|----------------|----------|-------------------|-------------|--------|
| 10 | 4 | 5 | 12.0 | 0.0040 | 3 | 0.0027 | 3/3 | **Kruskal** |
| 11 | 6 | 8 | 17.0 | 0.0011 | 6 | 0.0020 | 6/5 | **Prim** |

### Medium Graphs

| Graph ID | V | E | Total Cost | Prim Time (ms) | Prim Ops | Kruskal Time (ms) | Kruskal Ops | Winner |
|----------|---|---|------------|----------------|----------|-------------------|-------------|--------|
| 3 | 15 | 20 | 77.0 | 0.0072 | 14 | 0.0049 | 14/14 | **Kruskal** |
| 4 | 12 | 52 | 26.0 | 0.0065 | 18 | 0.0049 | 19/11 | **Kruskal** |
| 5 | 10 | 7 | 7.0* | 0.0006 | 3 | 0.0014 | 7/6 | **Prim** |

*Graph 5 is disconnected (4 components), so MST is actually a forest.

### Large Graphs

| Graph ID | V | E | Total Cost | Prim Time (ms) | Prim Ops | Kruskal Time (ms) | Kruskal Ops | Winner |
|----------|---|---|------------|----------------|----------|-------------------|-------------|--------|
| 6 | 30 | 46 | 335.0 | 0.0124 | 29 | 0.0147 | 29/29 | **Prim** |
| 7 | 20 | 162 | 146.0 | 0.0148 | 128 | 0.0399 | 129/19 | **Prim** |

---

## Algorithm Comparison

### Theoretical Analysis

| Aspect | Prim's Algorithm | Kruskal's Algorithm |
|--------|------------------|---------------------|
| **Time Complexity** | O(E log V) with binary heap | O(E log E) ≈ O(E log V) |
| **Space Complexity** | O(V) for visited set + priority queue | O(V) for DSU + O(E) for sorted edges |
| **Best Use Case** | Dense graphs (E ≈ V²) | Sparse graphs (E ≈ V) |
| **Edge Processing** | Considers all edges from visited vertices | Processes all edges globally in sorted order |
| **Data Structure** | Priority Queue | Union-Find (DSU) |

### Practical Observations

#### 1. Graph Density Impact

**Sparse Graphs (E/V < 3):**
- Kruskal performs better on small-medium sparse graphs (Graphs 3, 4, 10)
- Fewer edges to sort means efficient sorting phase
- DSU operations are fast with path compression

**Dense Graphs (E/V > 8):**
- Prim performs significantly better (Graph 7: 0.0148 ms vs 0.0399 ms)
- Priority queue efficiently handles many edges from each vertex
- Kruskal suffers from expensive sorting of many edges

#### 2. Graph Size Impact

For **small graphs** (V ≤ 6):
- Performance differences are negligible (microsecond range)
- Kruskal slightly faster due to simpler logic

For **medium graphs** (10 ≤ V ≤ 15):
- Kruskal shows advantage on sparse topologies
- Prim catches up on denser structures

For **large graphs** (V ≥ 20):
- Prim dominates on dense graphs (Graph 7)
- Performance gap widens with increasing edge count

#### 3. Operation Count Analysis

**Prim's Comparisons:**
- Correlates with MST edges + rejected edges
- Graph 7: 128 comparisons for 19 MST edges (6.7x overhead)

**Kruskal's Operations:**
- Comparisons ≈ total edges (processes all edges)
- Unions = V-1 (exactly the MST edges)
- Graph 7: 129 comparisons, but 19 unions (efficient filtering)

---

## Theoretical vs Practical Analysis

### Theory Predicts

1. **Prim's algorithm** should excel when E >> V (dense graphs) because:
    - Priority queue operations: O(log V) per edge
    - Total: O(E log V)

2. **Kruskal's algorithm** should excel when E ≈ V (sparse graphs) because:
    - Sorting dominates: O(E log E)
    - When E is small, sorting is cheap

### Practice Confirms

✅ **Graph 7 (Dense: 162 edges, 20 vertices):**
- Theory: Prim better
- Practice: Prim 0.0148 ms vs Kruskal 0.0399 ms (2.7x faster) ✅

✅ **Graph 3 (Sparse: 20 edges, 15 vertices):**
- Theory: Kruskal better
- Practice: Kruskal 0.0049 ms vs Prim 0.0072 ms (1.5x faster) ✅

### Surprising Results

⚠️ **Graph 6 (30 vertices, 46 edges):**
- Expected: Kruskal advantage (sparse)
- Actual: Prim slightly faster (0.0124 ms vs 0.0147 ms)
- **Reason:** Graph structure (ring with shortcuts) favors Prim's incremental growth

### Benchmark Methodology

To achieve accurate timing for microsecond-scale operations:
- **Warmup:** 10 iterations to allow JVM optimization
- **Measurement:** Average of 1000 iterations
- **Precision:** Double-precision milliseconds (e.g., 0.0124 ms)

This microbenchmarking approach is standard for Java performance testing.

---

## Conclusions

### When to Use Prim's Algorithm

✅ **Recommended for:**
- Dense graphs (many edges between vertices)
- Graphs where E/V ratio > 5
- Transportation networks with high connectivity
- Real-time systems where predictable priority queue performance matters

**Example:** Urban subway systems with many interconnected stations.

### When to Use Kruskal's Algorithm

✅ **Recommended for:**
- Sparse graphs (few edges relative to vertices)
- Graphs where E/V ratio < 3
- Distributed systems (edges can be processed independently)
- When edge list is already sorted or partially sorted

**Example:** Rural road networks connecting scattered villages.

### Practical Recommendations for City Transportation

For the **city transportation network** optimization problem:

1. **Small cities (< 10 districts):** Either algorithm works; choose Kruskal for simplicity
2. **Medium cities (10-20 districts):** Analyze density:
    - High connectivity → Prim
    - Low connectivity → Kruskal
3. **Large cities (20+ districts):**
    - Dense urban areas → **Prim's algorithm**
    - Suburban/rural sprawl → **Kruskal's algorithm**

### Implementation Quality Factors

Beyond algorithmic choice, performance depends on:
- **Data structures:** Union-Find with path compression is crucial for Kruskal
- **Priority queue:** Binary heap vs Fibonacci heap trade-offs for Prim
- **Language/JVM:** Java's JIT compilation affects small-graph performance
- **Memory locality:** Matters more for large-scale graphs



## Running the Project

### Build and Run
```bash
# Compile the project
mvn clean compile

# Run tests
mvn test

# Execute with default input
mvn exec:java

# Execute with custom input
mvn exec:java -Dexec.args="path/to/input.json output.json"
```

### Output Files
- `output.json` - Detailed results with MST edges for each graph
- `results_summary.csv` - Tabular comparison for analysis



---

