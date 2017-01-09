package dbscan;

public class EuclideanDistance {

    public double calculate(Point point, Point otherPoint) {
        double sum = 0;
        double pointX = point.getX();
        double pointY = point.getY();
        double otherPointX = otherPoint.getX();
        double otherPointY = otherPoint.getY();

        double d = pointX - otherPointX;
        sum += d * d;
        d = pointY - otherPointY;
        sum += d * d;

//        for (int i = 0; i < purchases.size() && i < centroidPurchases.size(); i++) {
//            double d = purchases.get(i) - centroidPurchases.get(i);
//            sum += d * d;
//        }
        return Math.sqrt(sum);
    }


}
