package controllers;


import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import net.objecthunter.exp4j.function.Function;

public class InterpolationAlgorithms {

    private LinkedHashMap<Double, Double> points;
    private Function function;
    private List<Double> xPoints;
    private List<Double> yPoints;

    public InterpolationAlgorithms(LinkedHashMap<Double, Double> points) {
        this.points = points;
        xPoints = new LinkedList(points.keySet());
        yPoints = new LinkedList(points.values());
    }

    public Function lagrangeInterpolation() {

        function = new Function("lagrange", 1) {
            @Override
            public double apply(double... doubles) {
                Double sum = 0.0;
                for (int k = 0; k < xPoints.size(); k++) {
                    Double mult = 1.0;
                    for (int i = 0; i < xPoints.size(); i++) {
                        if (i != k) {
                            mult *= (doubles[0] - xPoints.get(i)) / (xPoints.get(k) - xPoints.get(i));
                        }
                    }
                    sum += mult * yPoints.get(k);
                }
                return sum;
            }
        ;
        };
            
     return function;

    }

    public String lagrangeStringFunction() {
        String result = "";

        for (int k = 0; k < xPoints.size(); k++) {
            for (int i = 0; i < xPoints.size(); i++) {
                if (i != k) {
                    if (result.equals("")) {
                        result += "(x-" + xPoints.get(i) + ")/(" + (xPoints.get(k) - xPoints.get(i)) + ")";
                    } else {
                        result += "*(x-" + xPoints.get(i) + ")/(" + (xPoints.get(k) - xPoints.get(i)) + ")";
                    }
                }
            }

        }
        for (Double y : yPoints) {
            if (result.equals("")) {
                result +=y;
            } else {
                result += "*" + y;
            }
        }

        return result;
    }

    public Function newtonInterpolation() {

        List<Double> dividedDifferences = calculateDividedDifferenceTable(xPoints,yPoints);

        function = new Function("Newton", 1) {
            @Override
            public double apply(double... doubles) {

                Double sum = dividedDifferences.get(0);
                for (int k = 1; k < xPoints.size(); k++) {
                    Double mult = 1.0;
                    for (int i = 0; i < k; i++) {
                        mult *= (doubles[0] - xPoints.get(i));
                    }
                    sum += mult * dividedDifferences.get(k);
                }
                return sum;
            }
        ;
        };
            
     return function;
    }

    public String newtonStringFunction() {

        List<Double> dividedDifferences = calculateDividedDifferenceTable(xPoints,yPoints);
        String result = dividedDifferences.get(0) + "+";

        for (int k = 1; k < xPoints.size(); k++) {
            Double mult = 1.0;
            for (int i = 0; i < k; i++) {
                result += "(x-" + xPoints.get(i) + ")*";
            }
            if (k == xPoints.size() - 1) {
                result += dividedDifferences.get(k);
            } else {
                result += dividedDifferences.get(k) + "*";
            }
        }

        return result;
    }
    
    
    
    public Function inverseInterpolation(){
        List<Double> dividedDifferences = calculateDividedDifferenceTable(yPoints,xPoints);

        function = new Function("Inverse", 1) {
            @Override
            public double apply(double... doubles) {

                Double sum = dividedDifferences.get(0);
                for (int k = 1; k < xPoints.size(); k++) {
                    Double mult = 1.0;
                    for (int i = 0; i < k; i++) {
                        mult *= (doubles[0] - xPoints.get(i));
                    }
                    sum += mult * dividedDifferences.get(k);
                }
                return sum;
            }
        ;
        };
            
     return function;
    }

    private List<Double> calculateDividedDifferenceTable(List<Double> xPoints,List<Double> yPoints) {

        List<Double> dividedDifferences = new LinkedList(yPoints);

        for (int i = 1; i < xPoints.size(); i++) {
            for (int j = xPoints.size() - 1; j > 0; j--) {
                if (j - i >= 0) {

                    dividedDifferences.set(j, (dividedDifferences.get(j) - dividedDifferences.get(j - 1)) / (xPoints.get(j) - xPoints.get(j - i)));
                }
            }
        }

        for (Double d : dividedDifferences) {
            System.out.println(d);
        }
        return dividedDifferences;
    }

}
