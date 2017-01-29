package dbscan;

import java.util.List;

public class Point {
    private static EuclideanDistance euclideanDistance = new EuclideanDistance();
    private boolean visited = false;
    private int clusterIndex;
    private Cluster cluster;
    private List<Double> properties;

    public Point(List<Double> properties) {
        this.properties = properties;
    }

    public double distance(Point otherPoint) {
        // return euclidiean distance
        return euclideanDistance.calculate(this, otherPoint);
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public List<Double> getProperties() {
        return properties;
    }

    public int getClusterIndex() {
        return clusterIndex;
    }

    public void setClusterIndex(int clusterIndex) {
        this.clusterIndex = clusterIndex;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }


}
