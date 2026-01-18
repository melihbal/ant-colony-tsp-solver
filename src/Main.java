import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

/**
 * Solves the Traveling Salesperson Problem (TSP) using either:
 * 1. Brute Force (Exact solution, slow for large N)
 * 2. Ant Colony Optimization (Probabilistic heuristic, fast)
 */
public class Main {
    public static void main(String[] args) {
        // Hyperparameters for Ant Colony
        int iterationCount = 400;       // Total simulation rounds
        int antCount = 50;              // Ants per iteration
        double degradationFactor = 0.9; // Pheromone decay rate (0.9 = 90% remains)
        double alpha = 0.8;             // Importance of Pheromones
        double beta = 7.5;              // Importance of Distance (Visibility)
        double initialPheromoneIntensity = 0.11;
        double q = 0.001;               // Pheromone deposit constant

        // Parse Command Line Arguments

        // Input Filename
        String filename = (args.length > 0) ? args[0] : "input05.txt";

        // Visualization Toggle, Default: true
        boolean pheromoneDisplay = true;
        if (args.length > 1) {
            pheromoneDisplay = Boolean.parseBoolean(args[1]);
        }

        // Method Selection, 1 = Brute Force, 2 = Ant Colony
        int chosenMethod = 2;
        if (args.length > 2) {
            chosenMethod = Integer.parseInt(args[2]);
        }

        double startingTime = System.currentTimeMillis();

        // Validate method and display compatibility
        if (chosenMethod != 1 && chosenMethod != 2){
            System.out.println("You didn't choose a valid method.");
        } else if (chosenMethod == 1 && pheromoneDisplay) {
            System.out.println("Error: You can't use Brute Force with Pheromone Display. Run with 'false'.");
        } else {
            // Input Parsing
            ArrayList<ArrayList<Double>> coordinates = new ArrayList<>();

            try (Scanner sc = new Scanner(new FileInputStream(filename))) {
                while (sc.hasNextLine()) {
                    ArrayList<Double> coordinatesTemp = new ArrayList<>();
                    String line = sc.nextLine();
                    String[] coordinateSubList = line.split(",");
                    coordinatesTemp.add(Double.parseDouble(coordinateSubList[0]));
                    coordinatesTemp.add(Double.parseDouble(coordinateSubList[1]));
                    coordinates.add(coordinatesTemp);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }

            int nodeNum = coordinates.size();
            Edge[] edgeList = new Edge[nodeNum];

            // Variables to store final results
            double distanceToPrint = 0;
            Integer[] pathToPrint = new Integer[nodeNum + 1];

            // Method 1: Brute Force (Permutations)
            double bestDist = Double.MAX_VALUE;
            Integer[] bestRoute = null;

            if (chosenMethod == 1) {
                // Initialize array for permutation (0 is fixed start, permute 1..N)
                Integer[] firstRoute = new Integer[coordinates.size() - 1];
                for (int l = 1; l < coordinates.size(); l++) {
                    firstRoute[l - 1] = l;
                }

                // Generating permutations
                int n = firstRoute.length;
                Integer[] inds = new Integer[n];
                Arrays.fill(inds, 0);

                int i = 0;
                while (i < n) {
                    if (inds[i] < i) {
                        if (i % 2 == 0) {
                            swap(firstRoute, 0, i);
                        } else {
                            swap(firstRoute, inds[i], i);
                        }

                        // Construct full path: Start -> Permutation -> End
                        Integer[] adjustedRoute = new Integer[firstRoute.length + 2];
                        System.arraycopy(firstRoute, 0, adjustedRoute, 1, firstRoute.length);
                        adjustedRoute[0] = 0;
                        adjustedRoute[adjustedRoute.length - 1] = 0;

                        // Calculate total distance for this route
                        double totalDist = 0;
                        for (int p = 0; p < adjustedRoute.length - 1; p++) {
                            int ind1 = adjustedRoute[p];
                            int ind2 = adjustedRoute[p + 1];
                            totalDist += distance(coordinates.get(ind1).get(0), coordinates.get(ind2).get(0), coordinates.get(ind1).get(1), coordinates.get(ind2).get(1));
                        }

                        if (totalDist < bestDist) {
                            bestDist = totalDist;
                            bestRoute = adjustedRoute;
                        }

                        inds[i]++;
                        i = 0;
                    } else {
                        inds[i] = 0;
                        i++;
                    }
                }
                distanceToPrint = bestDist;

                // Ant Colony Optimization
            } else if (chosenMethod == 2) {

                // Initialize edges with default pheromone levels
                ArrayList<Double> initialPheromoneList = new ArrayList<>();
                for (int i = 0; i < nodeNum; i++) {
                    initialPheromoneList.add(initialPheromoneIntensity);
                }

                // Precompute distances between all nodes
                for (int index = 0; index < nodeNum; index++) {
                    ArrayList<Double> distanceFromObject = new ArrayList<>();
                    for (ArrayList<Double> pairs : coordinates) {
                        double dist = 0;
                        dist += distance(pairs.get(0), coordinates.get(index).get(0), pairs.get(1), coordinates.get(index).get(1));
                        distanceFromObject.add(dist);
                    }
                    edgeList[index] = new Edge(initialPheromoneList, distanceFromObject);
                }

                ArrayList<Double> bestDistancesOfIterations = new ArrayList<>();
                Double overallBestDistance = Double.MAX_VALUE;
                ArrayList<Integer> overallBestPath = new ArrayList<>();

                // Main Simulation Loop
                for (int y = 0; y < iterationCount; y++) {

                    ArrayList<Integer> bestPathForIteration = new ArrayList<>();
                    double bestDistanceOfIteration = Double.MAX_VALUE;

                    // Run each ant
                    for (int p = 0; p < antCount; p++) {
                        Random random = new Random();
                        int currentIndex = random.nextInt(coordinates.size());
                        int startingIndex = currentIndex;

                        ArrayList<Integer> unvisitedNodes = new ArrayList<>();
                        for (int i = 0; i < nodeNum; i++) {
                            unvisitedNodes.add(i);
                        }

                        double totalDistanceOfAnAnt = 0.0;
                        ArrayList<Integer> nodeVisitPath = new ArrayList<>();
                        nodeVisitPath.add(currentIndex);

                        // Ant builds a path node by node
                        while (unvisitedNodes.size() > 1) {
                            int oldIndex = currentIndex;
                            unvisitedNodes.remove(Integer.valueOf(currentIndex));

                            if (unvisitedNodes.size() != 1) {
                                ArrayList<Double> currentDistances = edgeList[currentIndex].getDistances();
                                ArrayList<Double> currentPheromones = edgeList[currentIndex].getPheromoneLevel();

                                // Calculate transition probabilities: (Pheromone^alpha) / (Distance^beta)
                                Double[] edgeValueArr = new Double[nodeNum];
                                for (int elem : unvisitedNodes) {
                                    double edgeValue = Math.pow(currentPheromones.get(elem), alpha) / Math.pow(currentDistances.get(elem), beta);
                                    edgeValueArr[elem] = edgeValue;
                                }

                                double totalEdgeValues = 0;
                                for (int i = 0; i < nodeNum; i++) {
                                    if (edgeValueArr[i] != null) totalEdgeValues += edgeValueArr[i];
                                }

                                Double[] probabilities = new Double[nodeNum];
                                for (int j = 0; j < nodeNum; j++) {
                                    if (edgeValueArr[j] != null) probabilities[j] = edgeValueArr[j] / totalEdgeValues;
                                }

                                // Roulette Wheel Selection
                                double randomDouble = new Random().nextDouble();
                                double counter = 0.0;
                                for (int i = 0; i < nodeNum; i++) {
                                    if (probabilities[i] == null) continue;

                                    counter += probabilities[i];
                                    if (counter >= randomDouble) {
                                        currentIndex = i;
                                        break;
                                    }
                                }
                            } else {
                                currentIndex = unvisitedNodes.get(0); // Only one choice left
                            }
                            nodeVisitPath.add(currentIndex);
                            totalDistanceOfAnAnt += edgeList[oldIndex].getDistances().get(currentIndex);
                        }

                        // Return to start
                        nodeVisitPath.add(startingIndex);
                        totalDistanceOfAnAnt += edgeList[currentIndex].getDistances().get(startingIndex);

                        // Pheromone Update
                        // Add pheromone inversely proportional to total distance (shorter path = more pheromone)
                        double delta = q / totalDistanceOfAnAnt;
                        for (int i = 0; i < nodeVisitPath.size() - 1; i++) {
                            int lastNode = nodeVisitPath.get(i);
                            int nextNode = nodeVisitPath.get(i + 1);

                            // Update both directions
                            updatePheromone(edgeList, lastNode, nextNode, delta);
                            updatePheromone(edgeList, nextNode, lastNode, delta);
                        }

                        // Track best path in this batch
                        if (bestDistanceOfIteration > totalDistanceOfAnAnt) {
                            bestDistanceOfIteration = totalDistanceOfAnAnt;
                            bestPathForIteration = nodeVisitPath;
                        }
                    }

                    // Track global best
                    if (overallBestDistance > bestDistanceOfIteration) {
                        overallBestDistance = bestDistanceOfIteration;
                        overallBestPath = bestPathForIteration;
                    }
                    distanceToPrint = overallBestDistance;
                    bestDistancesOfIterations.add(bestDistanceOfIteration);

                    // Pheromone Evaporation
                    // Decay all pheromones
                    for (Edge elem : edgeList) {
                        elem.degradatePheromone(degradationFactor);
                    }
                }

                // Normalize path to start at 0 for consistency
                int posOfZero = overallBestPath.indexOf(0);
                List<Integer> temporaryList = overallBestPath.subList(posOfZero, overallBestPath.size() - 1);
                List<Integer> temporaryList2 = overallBestPath.subList(0, posOfZero + 1);

                ArrayList<Integer> finalPath = new ArrayList<>();
                finalPath.addAll(temporaryList);
                finalPath.addAll(temporaryList2);

                bestRoute = finalPath.toArray(new Integer[0]);
            }

            // Visualization (StdDraw)
            StdDraw.setCanvasSize(800, 800);
            StdDraw.setXscale(0, 1);
            StdDraw.setYscale(0, 1);
            StdDraw.enableDoubleBuffering();
            StdDraw.setPenRadius(0.005);

            // Draw Best Path (if pheromones off)
            if (!pheromoneDisplay) {
                StdDraw.setPenColor(Color.BLACK);
                for (int t = 0; t < coordinates.size(); t++) {
                    StdDraw.line(coordinates.get(bestRoute[t]).get(0), coordinates.get(bestRoute[t]).get(1), coordinates.get(bestRoute[t + 1]).get(0), coordinates.get(bestRoute[t + 1]).get(1));
                }
            }

            // Draw Pheromone Intensities
            if (pheromoneDisplay) {
                for (int j = 0; j < edgeList.length; j++) {
                    ArrayList<Double> pheromoneList = edgeList[j].getPheromoneLevel();
                    for (int i = 0; i < pheromoneList.size(); i++) {
                        double x0 = coordinates.get(j).get(0);
                        double y0 = coordinates.get(j).get(1);
                        double x1 = coordinates.get(i).get(0);
                        double y1 = coordinates.get(i).get(1);

                        // Thickness based on pheromone strength
                        StdDraw.setPenRadius(Math.pow(pheromoneList.get(i), 1.5) * 0.0008);
                        StdDraw.line(x0, y0, x1, y1);
                    }
                }
            }

            // Draw Cities
            for (int in = 0; in < coordinates.size(); in++) {
                ArrayList<Double> location = coordinates.get(in);
                StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
                StdDraw.filledCircle(location.get(0), location.get(1), 0.013);
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setPenRadius(0.01);
                StdDraw.text(location.get(0), location.get(1), Integer.toString(in + 1));
            }

            double endingTime = System.currentTimeMillis();
            StdDraw.show();

            // Console Output
            String usedMethod = (chosenMethod == 1) ? "Brute-Force Method" : "Ant Colony Method";
            for (int i = 0; i < bestRoute.length; i++){
                pathToPrint[i] = bestRoute[i] + 1; // 1-based indexing for display
            }

            System.out.print("Method: ");
            System.out.println(usedMethod);
            System.out.print("Shortest Distance: ");
            System.out.println(distanceToPrint);
            System.out.print("Shortest Path: ");
            System.out.println(Arrays.toString(pathToPrint));
            System.out.print("Time: ");
            System.out.print((endingTime - startingTime) * 0.001);
            System.out.println(" seconds.");
        }
    }

    // Helper: Updates pheromone level for a specific edge
    private static void updatePheromone(Edge[] edgeList, int from, int to, double delta) {
        ArrayList<Double> levels = new ArrayList<>(edgeList[from].getPheromoneLevel());
        double currentVal = levels.get(to);
        levels.set(to, currentVal + delta);
        edgeList[from].setPheromoneLevel(levels);
    }

    private static double distance(double x1, double x2, double y1, double y2){
        return Math.sqrt(Math.pow((x1-x2),2) + Math.pow((y1-y2),2));
    }

    private static void swap(Integer[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}