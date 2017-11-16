package interpolationapplication.coordinatesystem;

import net.objecthunter.exp4j.function.Function;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javafx.stage.Stage;


public class CoordinateSystem extends Application {

    Function f;
    double interval1;
    double interval2;
    
    public CoordinateSystem(Function f,double i1,double i2){
        this.f=f;
        interval1=i1;
        interval2=i2;
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        Axes axes = new Axes(
                400, 300,
                -10,10, 1,
                -10, 10, 1
        );

        Plot plot = new Plot(
                f,
                interval1,interval2, 0.001,
                axes
        );

        StackPane layout = new StackPane(
                plot
        );
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: rgb(35, 39, 50);");

        stage.setTitle("Grafikus nézet");
        stage.setScene(new Scene(layout, Color.rgb(35, 39, 50)));
        stage.show();
    }

}
