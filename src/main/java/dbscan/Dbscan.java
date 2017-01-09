package dbscan;

import java.util.ArrayList;
import java.util.List;

public class Dbscan {
    private static final int MIN_POINTS = 2;
    private static final double EPSILON = 10.00;
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
        points.add(new Point(0, 0));
        points.add(new Point(5, 1));
        points.add(new Point(3, 2));
        points.add(new Point(10, 0));
        points.add(new Point(12, 3));
        points.add(new Point(30, 15));
        points.add(new Point(25, 20));
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

        for (Point neighbourPoint : neighbours) {
            if (!neighbourPoint.isVisited()) {
                neighbourPoint.setVisited(true);
                List<Point> newNeighbourPoints = getNeighbours(neighbourPoint);
                if (newNeighbourPoints.size() >= MIN_POINTS) {
                    neighbours.addAll(newNeighbourPoints);
                }
            }
            if (!pointAlreadyAssignedToCluster(neighbourPoint)) {
                cluster.addPoint(neighbourPoint);
            }
        }

    }

    private boolean pointAlreadyAssignedToCluster(Point point) {
        for (Cluster cluster : clusters) {
            if (cluster.getPoints().contains(point)) {
                return true;
            }
        }

        return false;
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

}
