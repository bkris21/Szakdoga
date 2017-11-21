package controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

public class InterpolationAlgorithmsTest {

   List<Point> numberPairs = new ArrayList<>();
    List<Double> xPoints = new ArrayList<>();
    List<Double> yPoints = new ArrayList<>();

    @Before
    public void initialize() {
        numberPairs.add(new Point(1.0,2.0));
        numberPairs.add(new Point(-1.0,3.0));
        numberPairs.add(new Point(-2.5,3.1));
        
        for(Point p : numberPairs){
        xPoints.add(p.getX());
        yPoints.add(p.getY());
        }
    }

    @Test
    public void testDividedDifferences() {

        InterpolationAlgorithms ipa = new InterpolationAlgorithms((List<Point>) numberPairs);

        List<Double> dividedDifferences = ipa.calculateDividedDifferenceTable(xPoints,yPoints);
        
        assertThat(dividedDifferences.size(),is(3));
        
        assertThat(dividedDifferences.get(0),is(2.0));
        assertThat(dividedDifferences.get(1),is(-0.5));
        
        
    }
    
    
    @Test
    public void TestLagrangeToString(){
        InterpolationAlgorithms ipa = new InterpolationAlgorithms((List<Point>) numberPairs);
        
        assertThat(ipa.lagrangeStringFunction(),is("(x+1.0)/(2.0)*(x+2.5)/(3.5)*(x-1.0)/(-2.0)*(x+2.5)/(1.5)*(x-1.0)/(-3.5)*(x+1.0)/(-1.5)*2.0*3.0*3.1"));
    }
    
    @Test
    public void TestNewtonToString(){
         InterpolationAlgorithms ipa = new InterpolationAlgorithms((List<Point>) numberPairs);
         
         assertThat(ipa.newtonStringFunction(), is("2.0+(x-1.0)*-0.5*(x-1.0)*(x+1.0)*-0.1238095238095238"));
    }
    

}
