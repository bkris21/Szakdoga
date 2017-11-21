
package controllers;


 public  class Point {

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
        
        
        
    }

