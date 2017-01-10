package dbscan;

import java.util.List;

public class Point {
    private static EuclideanDistance euclideanDistance = new EuclideanDistance();
    private boolean visited = false;

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
}
