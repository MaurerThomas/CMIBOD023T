package dbscan;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
    private final int id;
    private List<Point> points;

    public Cluster(int id) {
        this.points = new ArrayList<>();
        this.id = id;
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public int getId() {
        return id;
    }

    public void plotCluster() {
        System.out.println("Cluster: " + id + "\t-\t" + "Amount of Points: " + points.size());
        //System.out.println("[Points:");
        //points.forEach(System.out::println);
        //System.out.println("]  \n");
    }
}
