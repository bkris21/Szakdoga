
package controllers.logic;

import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class InterpolationAlgorithmsTest {
    
  
   InterpolationAlgorithms ia;
   List<Point> points = new ArrayList<>();
           
    @Before
    public void setUp() {
        points.add(new Point(1,2));
        points.add(new Point(Math.PI,Math.PI/2));
        points.add(new Point(Math.PI*2,0));
        ia = new InterpolationAlgorithms(points);
    }
 
    @Test
    public void lagrangeStringInterpolationTest(){
        assertThat(ia.lagrangeStringInterpolation(),is("((x-3.142)/(-2.142))*((x-6.283)/(-5.283))*2.0+((x-1.0)/(2.142))*((x-6.283)/(-3.142))*1.571+((x-1.0)/(5.283))*((x-3.142)/(3.142))*0.0"));
    }

    @Test
    public void newtoneStringInterpolationTest(){
         assertThat(ia.newtonStringInterpolation(),is("2.0+((x-1.0))*-0.2+((x-1.0))*((x-3.142))*-0.057"));
    }
    
    @Test
    public void inverseStringInterpolationTest(){
         assertThat(ia.inverseStringInterpolation(),is("1.0+((x-2.0))*-4.99+((x-2.0))*((x-1.571))*-1.495"));
    }
    
    @Test
    public void hermiteStringInterpolationTest(){
        points.get(0).setD1x(Math.E);
        points.get(2).setD1x(3);
        points.get(2).setD2x(4);
        
         assertThat(ia.hermiteStringInterpolation(),is("2.0+((x-1.0))*2.718+((x-1.0)^2)*-1.363+((x-1.0)^2)*((x-3.142))*0.247+((x-1.0)^2)*((x-3.142))*((x-6.283))*-0.005+((x-1.0)^2)*((x-3.142))*((x-6.283)^2)*0.003"));
    }
    
     @Test
    public void splieStringInterpolationTest(){
        points.get(0).setD1x(Math.E);   
        
         assertThat(ia.splineStringInterPolation(),is("2.0+((x-1.0))*2.718+((x-1.0)^2)*-1.363   [1.0,3.142]\n1.571+((x-3.142))*-3.12+((x-3.142)^2)*0.834   [3.142,6.283]\n"));
    }
}
