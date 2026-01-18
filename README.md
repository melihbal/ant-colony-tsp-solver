# Ant Colony Optimization (ACO) for Traveling Salesperson Problem

A Java-based implementation of the **Ant Colony Optimization** metaheuristic to solve the Traveling Salesperson Problem (TSP). This project visualizes the pheromone trails and optimal path finding process in real-time using `StdDraw`.

## 📌 Overview

This project tackles the classic NP-hard **Traveling Salesperson Problem**, where the goal is to find the shortest possible route that visits every city exactly once and returns to the origin.

It implements two distinct approaches for comparison:
1.  **Ant Colony Optimization (ACO):** A probabilistic technique simulating the behavior of ants finding paths to food. It uses pheromone deposition and evaporation to converge on an approximate optimal solution.
2.  **Brute-Force Search:** A deterministic approach that evaluates all possible permutations to find the mathematically perfect solution (feasible only for small $N$).

## ✨ Key Features

* **Heuristic & Exact Algorithms:** Switch between probabilistic ACO and deterministic Brute-Force methods.
* **Real-Time Visualization:** graphically renders cities, the "best so far" path, and pheromone intensity on edges.
* **Hyperparameter Tuning:** Configurable parameters for pheromone importance ($\alpha$), distance priority ($\beta$), evaporation rate, and ant population size.
* **Input Parsing:** Reads coordinate data from structured text files.

## 🛠️ Technology Stack

* **Language:** Java
* **Visualization:** `StdDraw` (Princeton Standard Library)
* **Concepts:** Graph Theory, Heuristics, Multi-Agent Systems, Object-Oriented Programming

## ⚙️ Configuration & Parameters

The algorithm's behavior is controlled by hyperparameters defined in `Main.java`:

| Parameter | Value | Description |
| :--- | :--- | :--- |
| **Alpha ($\alpha$)** | `0.8` | Importance of pheromone trail. |
| **Beta ($\beta$)** | `7.5` | Importance of heuristic information (distance). |
| **Evaporation** | `0.9` | Rate at which pheromones decay (prevents local optima). |
| **Ant Count** | `50` | Number of agents per iteration. |
| **Iterations** | `400` | Total simulation cycles. |

## 🚀 How to Run

### Prerequisites
* Java Development Kit (JDK) 8 or higher.
* `stdlib.jar` (standard library for `StdDraw`).

### Build & Execute
1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/yourusername/ant-colony-tsp-solver.git](https://github.com/yourusername/ant-colony-tsp-solver.git)
    cd ant-colony-tsp-solver
    ```

2.  **Compile the source code:**
    *(Ensure `stdlib.jar` is in your classpath)*
    ```bash
    javac -cp .:stdlib.jar src/*.java
    ```

3.  **Run the application:**
    ```bash
    java -cp .:src:stdlib.jar Main
    ```

*Note: The input file is currently set to `input05.txt` in `Main.java`. You can modify the source to point to `input01.txt` through `input04.txt` to test different graph topologies.*

## 📂 Input Format

Input files (e.g., `input05.txt`) should strictly follow this coordinate format:
```text
0.6550,0.5838
0.9338,0.4762
0.8000,0.4500
...
