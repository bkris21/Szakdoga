package interpolationapplication.coordinatesystem;

import controllers.Point;
import java.util.ArrayList;
import java.util.List;
import net.objecthunter.exp4j.function.Function;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javafx.stage.Stage;

public class CoordinateSystem extends Application {

    Function f2 = new Function("default") {
        @Override
        public double apply(double... doubles) {
            return 0;
        }
    };

    List<Function> functions = new ArrayList<>();
    List<Plot> plots = new ArrayList<>();
    Plot plotOfF2;
    List<Point> intervals = new ArrayList<>();
    final double SCALE_DELTA = 1.1;
    private final Delta dragDelta = new Delta();

    public CoordinateSystem(List<Function> functions, List<Point> intervals) {
        this.functions = functions;
        this.intervals = intervals;
    }

    public CoordinateSystem(List<Function> functions, Function f2, List<Point> intervals) {
        this.functions = functions;
        this.intervals = intervals;
        this.f2 = f2;

    }

    @Override
    public void start(Stage stage) throws Exception {
       
        StackPane rootPane = new StackPane();
        rootPane.setStyle("-fx-background-color: rgb(35, 39, 50);");

        Axes axes = new Axes(
                400, 300,
                -10, 10, 1,
                -10, 10, 1
        );

        int i = 0;
        for (Function func : functions) {
            plots.add(new Plot(func, intervals.get(i).getX(), intervals.get(i).getY(), 0.001, axes, Color.ORANGE.deriveColor(0, 1, 1, 0.8)));
            i++;
        }
        System.out.println(plots.size());
        for (Plot p : plots) {
            StackPane layout = new StackPane(p);
            layout.setPadding(new Insets(20));
            layout.setStyle("-fx-background-color: rgb(35, 39, 50);");
            rootPane.getChildren().add(layout);
        }

        if (f2.getName() == "default") {
            plotOfF2 = new Plot(
                    f2,
                    intervals.get(0).getX(), intervals.get(intervals.size() - 1).getY(), 0.001,
                    axes,
                    Color.RED.deriveColor(0, 1, 1, 0.0)
            );

        } else {
            plotOfF2 = new Plot(
                    f2,
                    intervals.get(0).getX(), intervals.get(intervals.size() - 1).getY(), 0.001,
                    axes,
                    Color.RED.deriveColor(0, 1, 1, 0.4)
            );
        }


        Scene scene = new Scene(rootPane);

       
        StackPane layout2 = new StackPane(plotOfF2);

        
        layout2.setPadding(new Insets(10));
      
        layout2.setStyle("-fx-background-color: rgba(0, 100, 100, 0.1); -fx-background-radius: 0;");

        stage.setTitle("Grafikus nézet");

       
        rootPane.getChildren().add(layout2);

        stage.setScene(scene);

        rootPane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                event.consume();

                if (event.getDeltaY() == 0) {
                    return;
                }

                double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA : 1 / SCALE_DELTA;

                if (!(rootPane.getScaleX() * scaleFactor < 1) && !(rootPane.getScaleX() * scaleFactor > 11)) {
                    rootPane.setScaleX(rootPane.getScaleX() * scaleFactor);
                    rootPane.setScaleY(rootPane.getScaleY() * scaleFactor);
                }

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
                double newX = mouseEvent.getScreenX() + dragDelta.x;

                rootPane.setLayoutX(mouseEvent.getScreenX() + dragDelta.x);
                rootPane.setLayoutY(mouseEvent.getScreenY() + dragDelta.y);
                System.out.println((mouseEvent.getScreenX() + dragDelta.x) + " " + (mouseEvent.getScreenX() + dragDelta.y));
            }

        });

        stage.show();
    }

    class Delta {

        double x;
        double y;
    }

}
