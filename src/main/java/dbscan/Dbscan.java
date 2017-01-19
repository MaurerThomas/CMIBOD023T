package dbscan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dbscan {
    private static final int MIN_POINTS = 4;
    private static final double EPSILON = 7.00;
    private List<Point> points = new ArrayList<>();
    private List<Cluster> clusters = new ArrayList<>();

    public static void main(String[] args) {
        Dbscan dbscan = new Dbscan();
        dbscan.init();
    }

//    DBSCAN(D, eps, MinPts)
//    C = 0
//    for each unvisited point P in dataset D
//      mark P as visited
//      N = getNeighbours (P, eps)
//      if sizeof(N) < MinPts
//          mark P as NOISE
//      else
//          C = next cluster
//          expandCluster(P, N, C, eps, MinPts)
//
//    expandCluster(P, N, C, eps, MinPts)
//    add P to cluster C
//    for each point P' in N
//        if P' is not visited
//          mark P' as visited
//          N' = getNeighbours(P', eps)
//          if sizeof(N') >= MinPts
//              N = N joined with N
//        if P' is not yet member of any cluster
//          add P' to cluster C
//

    private void init() {
        points.add(new Point(Arrays.asList(0d, 0d, 0d)));
        points.add(new Point(Arrays.asList(5d, 1d, 0d)));
        points.add(new Point(Arrays.asList(3d, 2d, 0d)));
        points.add(new Point(Arrays.asList(10d, 0d, 0d)));
        points.add(new Point(Arrays.asList(12d, 3d, 0d)));
        points.add(new Point(Arrays.asList(30d, 15d, 0d)));
        points.add(new Point(Arrays.asList(25d, 20d, 0d)));
        points.add(new Point(Arrays.asList(250d, 200d, 0d)));
        points.add(new Point(Arrays.asList(249d, 200d, 0d)));
        points.add(new Point(Arrays.asList(248d, 201d, 0d)));
        points.add(new Point(Arrays.asList(247d, 202d, 0d)));
        startCluster();
        clusters.forEach(Cluster::plotCluster);
    }

    private void startCluster() {
        int clusterIndex = 0;

        for (Point point : points) {
            if (!point.isVisited()) {
                point.setVisited(true);
                List<Point> neighbours = getNeighbours(point);
                if (neighbours.size() < MIN_POINTS) {
                    point.setVisited(false);
                } else {
                    Cluster cluster = new Cluster(clusterIndex);
                    expandCluster(point, neighbours, cluster);
                    clusters.add(cluster);
                    clusterIndex++;
                }
            }
        }
    }

    private void expandCluster(Point point, List<Point> neighbours, Cluster cluster) {
        cluster.addPoint(point);

        for (int i = 0; i < neighbours.size(); i++) {
            Point p = neighbours.get(i);

            if (!p.isVisited()) {
                p.setVisited(true);
                List<Point> newNeighbourPoints = getNeighbours(p);
                if (newNeighbourPoints.size() >= MIN_POINTS) {
                    neighbours.addAll(newNeighbourPoints);
                }
            }
            if (!cluster.getPoints().contains(p)) {
                cluster.addPoint(p);
            }
        }

    }

    private List<Point> getNeighbours(Point dataPoint) {
        List<Point> neighbours = new ArrayList();

        for (Point point : points) {
            double distance = dataPoint.distance(point);
            if (distance <= EPSILON) {
                neighbours.add(point);
            }
        }
        return neighbours;
    }

    private boolean pointAlreadyAssignedToCluster(Point point) {
        for (Cluster cluster : clusters) {
            if (cluster.getPoints().contains(point)) {
                return true;
            }
        }

        return false;
    }
}
