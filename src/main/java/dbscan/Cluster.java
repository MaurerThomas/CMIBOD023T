package dbscan;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
    private List<Point> points;
    private int id;

    public Cluster(int id) {
        this.points = new ArrayList<>();
        this.id = id;
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public void clearPoints() {
        this.points.clear();
    }

    public int getId() {
        return id;
    }

    public void plotCluster() {
        System.out.println("[Cluster: " + id + "]");
        System.out.println("[Points: \n");
        for (Point p : points) {
            System.out.println(p);
        }
        System.out.println("]");
    }
}
