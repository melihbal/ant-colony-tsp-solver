import java.util.ArrayList;

public class Edge {
    private ArrayList<Double> pheromoneLevel;
    private ArrayList<Double> distances;
    public Edge(ArrayList<Double> pheromoneLevel, ArrayList<Double> distances){
        this.distances = distances;
        this.pheromoneLevel = pheromoneLevel;
    }

    public ArrayList<Double> getPheromoneLevel() {
        return pheromoneLevel;
    }

    public ArrayList<Double> getDistances() {
        return distances;
    }

    public void setPheromoneLevel(ArrayList<Double> pheromoneLevel) {
        this.pheromoneLevel = pheromoneLevel;
    }

    public void setDistances(ArrayList<Double> distances) {
        this.distances = distances;
    }

    public void incrementAPheromone(double delta, int nextNode) {
        this.pheromoneLevel.set(nextNode, delta);
    }

    public void degradatePheromone(double degradationConstant){
        for (double elem : this.pheromoneLevel){
            elem = elem * degradationConstant;
        }
    }
}