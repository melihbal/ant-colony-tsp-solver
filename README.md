# Ant Colony Optimization (ACO) for Traveling Salesperson Problem

A Java-based implementation of the **Ant Colony Optimization** metaheuristic to solve the Traveling Salesperson Problem (TSP). This project visualizes the pheromone trails and optimal path finding process in real-time using `StdDraw`.

## Overview

This project tackles the classic NP-hard **Traveling Salesperson Problem**, where the goal is to find the shortest possible route that visits every city exactly once and returns to the origin.

It implements two distinct approaches for comparison:
1.  **Ant Colony Optimization:** A probabilistic technique simulating the behavior of ants finding paths to food. It uses pheromone deposition and evaporation to converge on an approximate optimal solution.
2.  **Brute-Force Search:** A deterministic approach that evaluates all possible permutations to find the mathematically perfect solution (feasible only for small $N$).

## Key Features

* **Heuristic & Exact Algorithms:** Switch between probabilistic ACO and deterministic Brute-Force methods via command line.
* **Real-Time Visualization:** Graphically renders cities, the "best so far" path, and pheromone intensity on edges.
* **Dynamic Input:** Load different graph topologies (text files) without recompiling.
* **Hyperparameter Tuning:** Configurable parameters for pheromone importance ($\alpha$), distance priority ($\beta$), evaporation rate, and ant population size.

## Technology Stack

* **Language:** Java
* **Visualization:** `StdDraw` (Princeton Standard Library)
* **Concepts:** Graph Theory, Heuristics, Multi-Agent Systems, Object-Oriented Programming

## How to Run

### Prerequisites
* Java Development Kit (JDK) 8 or higher.
* `stdlib.jar` (Required for `StdDraw` visualization). Ensure this file is in your project root.

### 1. Compile
```bash
javac -cp .:src:stdlib.jar src/*.java
```
*(Note: On Windows, use `;` instead of `:`).*

### 2. Run with Different Input Files
You can run the program on different graph topologies by specifying the input file as the first argument.

**Syntax:**
```bash
java -cp .:src:stdlib.jar Main [filename] [showPheromones] [methodID]
```

*(Note: Input files are located in the inputs/ directory, but you can reference them directly by name (e.g., input01.txt) when running the program.)*

**Examples:**

* **Run on default file (`input05.txt`):**
    ```bash
    java -cp .:src:stdlib.jar Main
    ```

* **Run on specific files:**
    ```bash
    java -cp .:src:stdlib.jar Main input01.txt
    ```
    ```bash
    java -cp .:src:stdlib.jar Main input04.txt
    ```

* **Disable Pheromone Rendering (Faster Performance):**
    ```bash
    java -cp .:src:stdlib.jar Main input03.txt false
    ```

* **Run Brute Force Method (Exact Solution):**
    *Note: Pheromone display must be `false` for Brute Force as utilizes a different approach.*
    ```bash
    java -cp .:src:stdlib.jar Main input02.txt false 1
    ```

## Configuration

The algorithm's hyperparameters are defined in `Main.java`:

| Parameter | Value | Description |
| :--- | :--- | :--- |
| **Alpha ($\alpha$)** | `0.8` | Importance of pheromone trail. |
| **Beta ($\beta$)** | `7.5` | Importance of heuristic information (distance). |
| **Evaporation** | `0.9` | Rate at which pheromones decay. |
| **Ant Count** | `50` | Number of agents per iteration. |
| **Iterations** | `400` | Total simulation cycles. |

## Input Format

Input files (e.g., `input05.txt`) must follow this coordinate format (normalized 0.0 to 1.0):
```text
0.6550,0.5838
0.9338,0.4762
0.8000,0.4500
...
```
Each line represents the `(x, y)` coordinates of a city.
