package dbscan;

public class Point {
    private static EuclideanDistance euclideanDistance = new EuclideanDistance();
    private boolean visited = false;
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
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

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

}
