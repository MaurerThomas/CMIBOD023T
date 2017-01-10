package dbscan;

import java.util.List;

public class EuclideanDistance {

    public double calculate(Point point, Point otherPoint) {
        double sum = 0;

        List<Double> pointProperties = point.getProperties();
        List<Double> otherPointProperties = otherPoint.getProperties();

        for (int i = 0; i < pointProperties.size() && i < otherPointProperties.size(); i++) {
            double d = pointProperties.get(i) - otherPointProperties.get(i);
            sum += d * d;
        }
        return Math.sqrt(sum);
    }
}
