package interpolationapplication.coordinatesystem;

import controllers.logic.Point;
import java.util.ArrayList;
import java.util.List;
import net.objecthunter.exp4j.function.Function;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javafx.stage.Stage;
import javax.swing.RootPaneContainer;
import sun.awt.SunToolkit;

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
        rootPane.setPadding(new Insets(20));
        rootPane.setStyle("-fx-background-color: rgb(35, 39, 50);");

        Axis axes = new Axis(
                400, 300,
                -10, 10, 1,
                -10, 10, 1
        );

        int i = 0;
        for (Function func : functions) {
            plots.add(new Plot(func, intervals.get(i).getFirstPoint(), intervals.get(i).getSecondPoint(), 0.001, axes, Color.ORANGE.deriveColor(0, 1, 1, 0.9)));
            i++;
        }
        List<StackPane> panes = new ArrayList<>();

        for (int j = 0; j < plots.size(); j++) {
            panes.add(new StackPane(plots.get(j)));
        }

        for (StackPane pane : panes) {
            rootPane.getChildren().add(pane);
        }

        if (f2.getName().equals("default")) {
            plotOfF2 = new Plot(
                    f2,
                    intervals.get(0).getFirstPoint(), intervals.get(intervals.size() - 1).getSecondPoint(), 0.001,
                    axes,
                    Color.RED.deriveColor(0, 1, 1, 0.0)
            );

        } else {
            plotOfF2 = new Plot(
                    f2,
                    intervals.get(0).getFirstPoint(), intervals.get(intervals.size() - 1).getSecondPoint(), 0.001,
                    axes,
                    Color.RED.deriveColor(0, 1, 1, 0.3)
            );
        }

        StackPane layout2 = new StackPane(plotOfF2);

        stage.setTitle("Grafikus nézet");

        rootPane.getChildren().add(layout2);

        Scene scene = new Scene(rootPane, 430, 370);
        stage.setScene(scene);

        
        
       
       
         

          
          
        rootPane.setOnScroll(new EventHandler<ScrollEvent>() {

            @Override
            public void handle(ScrollEvent event) {
                event.consume();

                if (event.getDeltaY() == 0) {
                    return;
                }

                double scaleFactor;

                if (event.getDeltaY() > 0) {
                    scaleFactor = SCALE_DELTA;

                } else {
                    scaleFactor = 1 / SCALE_DELTA;

                }

                if (!(rootPane.getScaleX() * scaleFactor < 1) && !(rootPane.getScaleX() * scaleFactor > 8)
                        && rootPane.getLayoutX() + (rootPane.getWidth() / 2) * rootPane.getScaleX() * scaleFactor >= stage.getWidth() / 2
                        && rootPane.getLayoutX() - ((rootPane.getWidth() / 2) * rootPane.getScaleX()) * scaleFactor <= -stage.getWidth() /2
                        && rootPane.getLayoutY() + (rootPane.getHeight() / 2) * rootPane.getScaleY() * scaleFactor >= stage.getHeight() / 2
                        && rootPane.getLayoutY() - (rootPane.getHeight() / 2) * rootPane.getScaleY() * scaleFactor <= -stage.getHeight() / 2) {
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
                if (mouseEvent.getScreenX() + dragDelta.x + (rootPane.getWidth() / 2) * rootPane.getScaleX() >= stage.getWidth() / 2
                        && mouseEvent.getScreenX() + dragDelta.x - (rootPane.getWidth() / 2) * rootPane.getScaleX() <= -(stage.getWidth()/2 )
                        && mouseEvent.getScreenY() + dragDelta.y + (rootPane.getHeight() / 2) * rootPane.getScaleY() >= stage.getHeight() / 2
                        && mouseEvent.getScreenY() + dragDelta.y - (rootPane.getHeight() / 2) * rootPane.getScaleY() <= -stage.getHeight() / 2) {

                    rootPane.setLayoutX(mouseEvent.getScreenX() + dragDelta.x);
                    rootPane.setLayoutY(mouseEvent.getScreenY() + dragDelta.y);
                    
                    
                }

            }

        });

        stage.setResizable(false);

        stage.show();
    }

    class Delta {

        double x;
        double y;
    }

}
