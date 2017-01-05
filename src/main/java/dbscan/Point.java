package dbscan;

public class Point {
    private boolean visited = false;

    public double distance(Point point, Point otherPoint) {
        // return euclidiean distance
        return 0.1;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

}
