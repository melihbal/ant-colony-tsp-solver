# Ant Colony Optimization (ACO) for Traveling Salesperson Problem

A Java-based implementation of the **Ant Colony Optimization** metaheuristic to solve the Traveling Salesperson Problem (TSP). This project visualizes the pheromone trails and optimal path finding process in real-time using `StdDraw`.

## 📌 Overview

This project tackles the classic NP-hard **Traveling Salesperson Problem**, where the goal is to find the shortest possible route that visits every city exactly once and returns to the origin.

It implements two distinct approaches for comparison:
1.  **Ant Colony Optimization (ACO):** A probabilistic technique simulating the behavior of ants finding paths to food. It uses pheromone deposition and evaporation to converge on an approximate optimal solution.
2.  **Brute-Force Search:** A deterministic approach that evaluates all possible permutations to find the mathematically perfect solution (feasible only for small $N$).

## ✨ Key Features

* **Heuristic & Exact Algorithms:** Switch between probabilistic ACO and deterministic Brute-Force methods via command line.
* **Real-Time Visualization:** Graphically renders cities, the "best so far" path, and pheromone intensity on edges.
* **Dynamic Input:** Load different graph topologies (text files) without recompiling.
* **Hyperparameter Tuning:** Configurable parameters for pheromone importance ($\alpha$), distance priority ($\beta$), evaporation rate, and ant population size.

## 🛠️ Technology Stack

* **Language:** Java
* **Visualization:** `StdDraw` (Princeton Standard Library)
* **Concepts:** Graph Theory, Heuristics, Multi-Agent Systems, Object-Oriented Programming

## 🚀 How to Run

### Prerequisites
* Java Development Kit (JDK) 8 or higher.
* `stdlib.jar` (Required for `StdDraw` visualization).

### 1. Compile
Ensure `stdlib.jar` is in your root directory or classpath.
```bash
javac -cp .:src:stdlib.jar src/*.java
