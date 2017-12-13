package controllers.logic;

public class Point {

    private double firstPoint;
    private double secondPoint;
    private double d1x = Double.NaN;
    private double d2x = Double.NaN;

    public Point(double firstPoint, double secondPoint) {
        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;
    }

    public double getFirstPoint() {
        return firstPoint;
    }

    public double getSecondPoint() {
        return secondPoint;
    }

    public double getD1x() {
        return d1x;
    }

    public double getD2x() {
        return d2x;
    }

    public void setD1x(double d1x) {
        this.d1x = d1x;
    }

    public void setD2x(double d2x) {
        this.d2x = d2x;
    }

}
