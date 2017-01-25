package dbscan;

import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.ScatterMultiColor;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import util.DataReader;

import java.util.ArrayList;
import java.util.List;

public class Dbscan {
    private static final int MIN_POINTS = 10;
    private static final double EPSILON = 0.01;
    private List<Point> points = new ArrayList<>();
    private List<Cluster> clusters = new ArrayList<>();
    private DataReader dataReader = new DataReader();

    public static void main(String[] args) throws Exception {
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

    private void init() throws Exception {
        points = dataReader.parseStars();

        startCluster();
        draw();
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

    private void draw() throws Exception {
        Coord3d[] coord3dsPoints = new Coord3d[points.size()];
        //Color[] colors = new Color[points.size()];
        //float a;
        for (int i = 0; i < coord3dsPoints.length; i++) {
            Point star = points.get(i);
            coord3dsPoints[i] = new Coord3d(star.getProperties().get(0), star.getProperties().get(1), star.getProperties().get(2));
            //a = 0.25f;
            //colors[i] = new Color(star.getProperties().get(0),star.getProperties().get(1), star.getProperties().get(2), a);
        }
        //Scatter scatter = new Scatter(coord3dsPoints, colors);

        ScatterMultiColor scatter = new ScatterMultiColor(coord3dsPoints, new ColorMapper(new ColorMapRainbow(), -0.5f, 0.5f));
        AnalysisLauncher.open(new AbstractAnalysis() {
            @Override
            public void init() throws Exception {
                chart = AWTChartComponentFactory.chart(Quality.Fastest);
                chart.getScene().add(scatter);
            }
        });
    }

}
