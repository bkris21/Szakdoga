package controllers.logic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import java.util.LinkedList;
import java.util.List;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import net.objecthunter.exp4j.function.Function;
import org.apache.commons.lang.ArrayUtils;

public class InterpolationAlgorithms {

    private final List<Point> points;

    private Function function;
    private final List<Double> xPoints = new ArrayList<>();
    private final List<Double> yPoints = new ArrayList<>();
    private final List<Double> firstDerivatives = new ArrayList<>();
    private final List<Double> secondDerivatives = new ArrayList<>();
    List<Double> hermiteDividedDifferencesY = new LinkedList<>();
    List<Double> hermiteDividedDifferncesX = new LinkedList<>();

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

    public Function newtonInterpolation() {

        List<Double> dividedDifferences = calculateDividedDifferenceTable(xPoints, yPoints);

        function = new Function("Newton", 1) {
            @Override
            public double apply(double... doubles) {
                String func = newtonStringFunction();

                func = func.replace("x", "" + doubles[0]);

                Expression exp = new ExpressionBuilder(func).build();
                return exp.evaluate();

            }
        ;
        };
            
     return function;
    }

    public Function inverseInterpolation() {

        function = new Function("Inverse", 1) {
            @Override
            public double apply(double... doubles) {
                String func = inverseStringFunction();

                func = func.replace("x", "" + doubles[0]);

                Expression exp = new ExpressionBuilder(func).build();
                return exp.evaluate();
            }
        ;
        };
            
     return function;
    }

    public Function hermiteInterpolation() {

        function = new Function("Hermite", 1) {
            @Override
            public double apply(double... doubles) {
                String func = hermiteStringInterpolation();

                func = func.replace("x", "" + doubles[0]);

                Expression exp = new ExpressionBuilder(func).build();
                return exp.evaluate();
            }
        ;
        };
        
       
        
            
     return function;

    }

    public List<Function> splineInterpolation() {

        String[] functions = splineForFunction(splineStringInterPolation());

        List<Function> result = new ArrayList<>();

        for (String s : functions) {

            function = new Function("Spline") {

                @Override
                public double apply(double... doubles) {
                    String func = s.replace("x", "" + doubles[0]);
                    func = func.trim();
                    Expression exp = new ExpressionBuilder(func).build();
                    return exp.evaluate();
                }
            ;
            };
            result.add(function);
        }
        return result;
    }

    public String lagrangeStringFunction() {
        String result = "";

        for (int k = 0; k < xPoints.size(); k++) {
            for (int i = 0; i < xPoints.size(); i++) {
                if (i != k) {

                    result += "((x-" + round(xPoints.get(i), 3) + ")/(" + (round(xPoints.get(k), 3) - round(xPoints.get(i), 3)) + "))*";

                }
            }
            if (k == xPoints.size() - 1) {
                result += yPoints.get(k);
            } else {
                result += yPoints.get(k) + "+";
            }

        }

        result = result.replace("--", "+");

        return result;
    }

    public String newtonStringFunction() {

        List<Double> dividedDifferences = calculateDividedDifferenceTable(xPoints, yPoints);

        String s = newtonStyleStringInterpolation(xPoints, dividedDifferences);

        return s;
    }

    public String inverseStringFunction() {
        String result = "";
        List<Double> dividedDifferences = calculateDividedDifferenceTable(yPoints, xPoints);

        String s = newtonStyleStringInterpolation(yPoints, dividedDifferences);

        return s;
    }

    public String hermiteStringInterpolation() {

        calculateHermiteDividedDifferenceTable(hermiteDividedDifferncesX, hermiteDividedDifferencesY, points);

        String f = newtonStyleStringInterpolation(hermiteDividedDifferncesX, hermiteDividedDifferencesY);

        return f;
    }

    public String splineStringInterPolation() {

        String s = "";
        List<Point> pointsHelp = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            pointsHelp.add(new Point(points.get(i).getX(), points.get(i).getY()));
            pointsHelp.get(i).setD1x(points.get(i).getD1x());
        }

        int i = 0;

        while (Double.isNaN(points.get(i).getD1x())) {
            i++;

        }
        List<Point> splineIntervallPoints = new ArrayList<>();
        Double derivativeValue;
        String pI;

        for (int j = i; j > 0; j--) {
            splineIntervallPoints.clear();
            splineIntervallPoints.add(pointsHelp.get(j - 1));
            splineIntervallPoints.add(pointsHelp.get(j));

            uplodeHermiteLists(hermiteDividedDifferncesX, hermiteDividedDifferencesY, splineIntervallPoints);
            calculateHermiteDividedDifferenceTable(hermiteDividedDifferncesX, hermiteDividedDifferencesY, splineIntervallPoints);

            pI = newtonStyleStringInterpolation(hermiteDividedDifferncesX, hermiteDividedDifferencesY);

            s += pI + "   [" + pointsHelp.get(j - 1).getX() + "," + pointsHelp.get(j).getX() + "]" + "\n";

            String derivative = calculateDerivativeSb(pI);

            derivative = derivative.replace("x", "" + pointsHelp.get(j - 1).getX());

            Expression exp = new ExpressionBuilder(derivative).build();
            derivativeValue = exp.evaluate();

            pointsHelp.get(j - 1).setD1x(derivativeValue);

        }
        if (!s.equals("")) {
            s = sortSpline(s);
        }

        for (int j = i; j < pointsHelp.size() - 1; j++) {
            splineIntervallPoints.clear();
            splineIntervallPoints.add(pointsHelp.get(j));
            splineIntervallPoints.add(pointsHelp.get(j + 1));

            uplodeHermiteLists(hermiteDividedDifferncesX, hermiteDividedDifferencesY, splineIntervallPoints);
            calculateHermiteDividedDifferenceTable(hermiteDividedDifferncesX, hermiteDividedDifferencesY, splineIntervallPoints);

            pI = newtonStyleStringInterpolation(hermiteDividedDifferncesX, hermiteDividedDifferencesY);

            s += pI + "   [" + pointsHelp.get(j).getX() + "," + pointsHelp.get(j + 1).getX() + "]" + "\n";

            String derivative = calculateDerivativeSa(pI);
            derivative = derivative.replace("x", "" + pointsHelp.get(j + 1).getX());

            Expression exp = new ExpressionBuilder(derivative).build();
            derivativeValue = exp.evaluate();

            pointsHelp.get(j + 1).setD1x(derivativeValue);

        }

        return s;
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

    public void calculateHermiteDividedDifferenceTable(List<Double> hermiteDividedDifferncesX, List<Double> hermiteDividedDifferencesY, List<Point> points) {

        uplodeHermiteLists(hermiteDividedDifferncesX, hermiteDividedDifferencesY, points);

        for (int i = 1; i < hermiteDividedDifferencesY.size(); i++) {
            for (int j = hermiteDividedDifferencesY.size() - 1; j > 0; j--) {
                if (j - i >= 0) {
                    if (i == 1 && (hermiteDividedDifferncesX.get(j) - hermiteDividedDifferncesX.get(j - i)) == 0) {
                        for (Point p1 : points) {
                            if (p1.getX() == hermiteDividedDifferncesX.get(j)) {
                                hermiteDividedDifferencesY.set(j, p1.getD1x());
                                break;
                            }
                        }
                    } else if (i == 2 && (hermiteDividedDifferncesX.get(j) - hermiteDividedDifferncesX.get(j - i)) == 0) {
                        for (Point p1 : points) {
                            if (p1.getX() == hermiteDividedDifferncesX.get(j)) {
                                hermiteDividedDifferencesY.set(j, p1.getD2x() / 2);
                                break;
                            }
                        }
                    } else {

                        hermiteDividedDifferencesY.set(j, (hermiteDividedDifferencesY.get(j) - hermiteDividedDifferencesY.get(j - 1)) / (hermiteDividedDifferncesX.get(j) - hermiteDividedDifferncesX.get(j - i)));
                    }
                }
            }
        }

    }

    public String newtonStyleStringInterpolation(List<Double> xs, List<Double> ys) {

        String result = "";

        if (ys.size() > 1) {
            result += round(ys.get(0), 3) + "+";
        } else {
            result += round(ys.get(0), 3);
        }

        int db = 1;
        for (int k = 1; k < xs.size(); k++) {
            for (int i = 0; i < k; i++) {
                if (xs.get(i).equals(xs.get(i + 1)) && i + 1 != k) {
                    db++;
                } else {
                    result += "((x-" + round(xs.get(i), 3) + ")^" + db + ")*";
                    db = 1;
                }

            }
            if (k == xs.size() - 1) {
                result += round(ys.get(k), 3);
            } else {
                result += round(ys.get(k), 3) + "+";
            }
        }

        result = result.replace("-+", "-");
        result = result.replace("^1", "");

        return result;
    }

    private void uplodeHermiteLists(List<Double> hermiteDividedDifferncesX, List<Double> hermiteDividedDifferencesY, List<Point> points) {
        hermiteDividedDifferencesY.clear();
        hermiteDividedDifferncesX.clear();

        for (Point p : points) {
            hermiteDividedDifferncesX.add(p.getX());
            hermiteDividedDifferencesY.add(p.getY());
            if (!Double.isNaN(p.getD1x())) {
                hermiteDividedDifferncesX.add(p.getX());
                hermiteDividedDifferencesY.add(p.getY());
            }

            if (!Double.isNaN(p.getD2x())) {

                hermiteDividedDifferncesX.add(p.getX());
                hermiteDividedDifferencesY.add(p.getY());
            }

        }
    }

    private String calculateDerivativeSa(String f) {
        String fdx = "";

        String[] s = f.split("[+]");

        String[] firstDegree = s[1].split("[*]");
        String[] secondDegree = s[2].split("[*]");

        String[] summa = secondDegree[0].split("\\^");

        fdx += firstDegree[1] + "+" + secondDegree[1] + "*2*" + summa[0];
        fdx = fdx.replace("((", "(");

        return fdx;
    }

    private String calculateDerivativeSb(String f) {
        String fdx = "";

        String[] s = f.split("[+]");
        String[] firstDegree = s[1].split("[*]");
        String[] secondDegree = s[2].split("[*]");

        fdx += firstDegree[1] + "+(" + secondDegree[0] + "+" + secondDegree[1] + ")*" + secondDegree[2];

        fdx = fdx.replace("^1", "");

        return fdx;
    }

    public double round(double value, int places) {

        BigDecimal num = new BigDecimal(value);
        num = num.setScale(places, RoundingMode.HALF_UP);
        return num.doubleValue();

    }

    private String sortSpline(String spline) {

        String s = "";
        String[] split = spline.split("\\n");

        for (int i = split.length - 1; i >= 0; i--) {

            s += split[i] + "\n";
        }

        return s;

    }

    private String[] splineForFunction(String s) {
        String[] result = s.split("\\n");


        

        for (int i = 0; i < result.length; i++) {

            int j = 0;
            String help = "";
            while (result[i].charAt(j) != ' ') {
                help += result[i].charAt(j);
                j++;
            }

           result[i] = help;

        }

        return result;
    }

}
