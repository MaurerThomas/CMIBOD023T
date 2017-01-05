package dbscan;

import java.util.List;

public class Dbscan {
    private static final int MIN_POINTS = 4;
    private static final double EPSILON = 1.00;

    public static void main(String[] args) {
        Dbscan dbscan = new Dbscan();
        dbscan.init();
    }

//    DBSCAN(D, eps, MinPts)
//    C = 0
//    for each unvisited point P in dataset D
//      mark P as visited
//      N = getNeighbors (P, eps)
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
//          N' = getNeighbors(P', eps)
//          if sizeof(N') >= MinPts
//              N = N joined with N
//        if P' is not yet member of any cluster
//          add P' to cluster C
//

    private void init() {
        startCluster();
    }

    private void startCluster() {
        int clusterIndex = 0;


    }

    private void expandCluster(Point point, List neighbors, Cluster cluster) {

    }

    private List getNeighbors(Point point) {

    }

    private enum status {
        VISITED, NOISE
    }


}
