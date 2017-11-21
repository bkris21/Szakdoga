package interpolationapplication.coordinatesystem;

import net.objecthunter.exp4j.function.Function;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javafx.stage.Stage;

public class CoordinateSystem extends Application {

    Function f;
   Function f2=new Function("defoult") {
       @Override
       public double apply(double... doubles) {
         return 0;
       }
   };
    double interval1;
    double interval2;
    final double SCALE_DELTA = 1.1;
    private final Delta dragDelta = new Delta();

    public CoordinateSystem(Function f, double i1, double i2) {
        this.f = f;
        interval1 = i1;
        interval2 = i2;
    }
     public CoordinateSystem(Function f,Function f2, double i1, double i2) {
        this.f = f;
        this.f2=f2;
        interval1 = i1;
        interval2 = i2;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Axes axes = new Axes(
                400, 300,
                -10, 10, 1,
                -10, 10, 1
        );
      
        Plot plot1 = new Plot(
                f,
                interval1, interval2, 0.001,
                axes,
                Color.ORANGE.deriveColor(0, 1, 1, 0.6)
        );
        Plot plot2 = new Plot(
                f2,
                interval1, interval2, 0.001,
                axes,
                Color.RED.deriveColor(0, 1, 1, 0.2)
        );

        StackPane rootPane = new StackPane();
        Scene scene = new Scene(rootPane);

        StackPane layout = new StackPane(plot1);
        StackPane layout2 = new StackPane(plot2);

        layout.setPadding(new Insets(20));
        layout2.setPadding(new Insets(10));
        layout.setStyle("-fx-background-color: rgb(35, 39, 50);");
        layout2.setStyle("-fx-background-color: rgba(0, 100, 100, 0.1); -fx-background-radius: 0;");

        stage.setTitle("Grafikus nézet");

        
       
        rootPane.getChildren().add(layout);
         rootPane.getChildren().add(layout2);

        stage.setScene(scene);

        rootPane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                event.consume();

                if (event.getDeltaY() == 0) {
                    return;
                }

                double scaleFactor
                        = (event.getDeltaY() > 0)
                        ? SCALE_DELTA
                        : 1 / SCALE_DELTA;

                rootPane.setScaleX(rootPane.getScaleX() * scaleFactor);
                rootPane.setScaleY(rootPane.getScaleY() * scaleFactor);
            }
        });

        rootPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                dragDelta.x = rootPane.getLayoutX() - mouseEvent.getScreenX();
                dragDelta.y = rootPane.getLayoutY() - mouseEvent.getScreenY();
            }
        });

        rootPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                rootPane.setLayoutX(mouseEvent.getScreenX() + dragDelta.x);
                rootPane.setLayoutY(mouseEvent.getScreenY() + dragDelta.y);
            }

        });

        stage.show();
    }

    class Delta {

        double x;
        double y;
    }

}
