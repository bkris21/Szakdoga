package controllers;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import net.objecthunter.exp4j.function.Function;

public class InterpolationAlgorithms {

    private List<Point> points;
    private Function function;
    private List<Double> xPoints = new ArrayList<>();
    private List<Double> yPoints = new ArrayList<>();
    private List<Double> firstDerivatives = new ArrayList<>();
    private List<Double> secondDerivatives = new ArrayList<>();

    public InterpolationAlgorithms(List<Point> points) {
        this.points = points;
        for (Point p : points) {
            xPoints.add(p.getX());
            yPoints.add(p.getY());
            firstDerivatives.add(p.getD1x());
            secondDerivatives.add(p.getD2x());
        }
    }

    public Function lagrangeInterpolation() {

        function = new Function("lagrange", 1) {
            @Override
            public double apply(double... doubles) {
                String func = lagrangeStringFunction();

                func = func.replace("x", "" + doubles[0]);

                Expression exp = new ExpressionBuilder(func).build();
                return exp.evaluate();

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
                    
                        result += "((x-" + round(xPoints.get(i), 3) + ")/(" + (round(xPoints.get(k), 3) - round(xPoints.get(i), 3)) + "))*";
                   
                }
                
            }
            if(k==xPoints.size()-1){
                result +=  yPoints.get(k);  
            }else{
                result +=  yPoints.get(k)+"+";
            }
          
        }

        result = result.replace("--", "+");

        return result;
    }

    public Function newtonInterpolation() {

        List<Double> dividedDifferences = calculateDividedDifferenceTable(xPoints, yPoints);

        function = new Function("Newton", 1) {
            @Override
            public double apply(double... doubles) {
                    String func =newtonStringFunction();

                func = func.replace("x", "" + doubles[0]);

                Expression exp = new ExpressionBuilder(func).build();
                return exp.evaluate();
              
            }
        ;
        };
            
     return function;
    }

    public String newtonStringFunction() {
        String result = "";
        List<Double> dividedDifferences = calculateDividedDifferenceTable(xPoints, yPoints);
        if (dividedDifferences.size() > 1) {
            result += round(dividedDifferences.get(0), 3) + "+";
        } else {
            result += round(dividedDifferences.get(0), 3);
        }

        for (int k = 1; k < xPoints.size(); k++) {
            Double mult = 1.0;
            for (int i = 0; i < k; i++) {
                result += "(x-" + round(xPoints.get(i), 3) + ")*";
            }
            if (k == xPoints.size() - 1) {
                result += round(dividedDifferences.get(k), 3);
            } else {
                result += round(dividedDifferences.get(k), 3) + "+";
            }
        }

        result = result.replace("--", "+");

        return result;
    }

    public Function inverseInterpolation() {
        List<Double> dividedDifferences = calculateDividedDifferenceTable(yPoints, xPoints);

        function = new Function("Inverse", 1) {
            @Override
            public double apply(double... doubles) {
                     String func =inverseStringFunction();

                func = func.replace("x", "" + doubles[0]);

                Expression exp = new ExpressionBuilder(func).build();
                return exp.evaluate();
            }
        ;
        };
            
     return function;
    }
    
    public String inverseStringFunction() {
        String result = "";
        List<Double> dividedDifferences = calculateDividedDifferenceTable(yPoints, xPoints);
        if (dividedDifferences.size() > 1) {
            result += round(dividedDifferences.get(0), 3) + "+";
        } else {
            result += round(dividedDifferences.get(0), 3);
        }

        for (int k = 1; k < yPoints.size(); k++) {
            Double mult = 1.0;
            for (int i = 0; i < k; i++) {
                result += "(x-" + round(yPoints.get(i), 3) + ")*";
            }
            if (k == yPoints.size() - 1) {
                result += round(dividedDifferences.get(k), 3);
            } else {
                result += round(dividedDifferences.get(k), 3) + "+";
            }
        }

        result = result.replace("--", "+");

        return result;
    }


    public List<Double> calculateDividedDifferenceTable(List<Double> xPoints, List<Double> yPoints) {

        List<Double> dividedDifferences = new LinkedList(yPoints);

        for (int i = 1; i < xPoints.size(); i++) {
            for (int j = xPoints.size() - 1; j > 0; j--) {
                if (j - i >= 0) {

                    dividedDifferences.set(j, (dividedDifferences.get(j) - dividedDifferences.get(j - 1)) / (xPoints.get(j) - xPoints.get(j - i)));
                }
            }
        }

        return dividedDifferences;
    }

    public double round(double value, int places) {

        BigDecimal num = new BigDecimal(value);
        num = num.setScale(places, RoundingMode.HALF_UP);
        return num.doubleValue();
    }

}
