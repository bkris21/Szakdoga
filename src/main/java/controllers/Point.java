
package controllers;

import java.util.Comparator;


 public  class Point implements Comparator<Point>{

        private double x;
        private double y;
        private double d1x;
        private double d2x;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
        

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
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

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Point other = (Point) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        return true;
    }

    

   
    

    @Override
    public int compare(Point o1, Point o2) {
        return (int)(o1.getX()-o2.getX());
    }

    
        
        
    }

