package controllers;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

public class InterpolationAlgorithmsTest {

    Map<Double, Double> numberPairs = new LinkedHashMap<>();
    List<Double> xPoints;
    List<Double> yPoints;

    @Before
    public void initialize() {
        numberPairs.put(1.0, 2.0);
        numberPairs.put(-1.0, 3.0);
        numberPairs.put(-2.5, 3.1);

        xPoints = new LinkedList(numberPairs.keySet());
        yPoints = new LinkedList(numberPairs.values());
    }

    @Test
    public void testDividedDifferences() {

        InterpolationAlgorithms ipa = new InterpolationAlgorithms((LinkedHashMap<Double, Double>) numberPairs);

        List<Double> dividedDifferences = ipa.calculateDividedDifferenceTable(xPoints,yPoints);
        
        assertThat(dividedDifferences.size(),is(3));
        
        assertThat(dividedDifferences.get(0),is(2.0));
        assertThat(dividedDifferences.get(1),is(-0.5));
        
        
    }
    
    
    @Test
    public void TestLagrangeToString(){
        InterpolationAlgorithms ipa = new InterpolationAlgorithms((LinkedHashMap<Double, Double>) numberPairs);
        
        assertThat(ipa.lagrangeStringFunction(),is("(x+1.0)/(2.0)*(x+2.5)/(3.5)*(x-1.0)/(-2.0)*(x+2.5)/(1.5)*(x-1.0)/(-3.5)*(x+1.0)/(-1.5)*2.0*3.0*3.1"));
    }
    
    @Test
    public void TestNewtonToString(){
         InterpolationAlgorithms ipa = new InterpolationAlgorithms((LinkedHashMap<Double, Double>) numberPairs);
         
         assertThat(ipa.newtonStringFunction(), is("2.0+(x-1.0)*-0.5*(x-1.0)*(x+1.0)*-0.1238095238095238"));
    }
    

}
