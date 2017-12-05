package interpolationapplication.coordinatesystem;

import net.objecthunter.exp4j.function.Function;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import javafx.stage.Stage;

public class CoordinateSystem extends Application {

    Function f;
    Function f2 = new Function("default") {
        @Override
        public double apply(double... doubles) {
            return 0;
        }
    };
    Plot plot2;
    Plot plot1;
    double interval1;
    double interval2;
    final double SCALE_DELTA = 1.1;
    private final Delta dragDelta = new Delta();

    public CoordinateSystem(Function f, double i1, double i2) {
        this.f = f;
        interval1 = i1;
        interval2 = i2;
    }

    public CoordinateSystem(Function f, Function f2, double i1, double i2) {
        this.f = f;
        this.f2 = f2;
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

        plot1 = new Plot(
                f,
                interval1, interval2, 0.001,
                axes,
                Color.ORANGE.deriveColor(0, 1, 1, 0.8)
        );
        if (f2.getName() == "default") {
            plot2 = new Plot(
                    f2,
                    interval1, interval2, 0.001,
                    axes,
                    Color.RED.deriveColor(0, 1, 1, 0.0)
            );
        } else {
            plot2 = new Plot(
                    f2,
                    interval1, interval2, 0.001,
                    axes,
                    Color.RED.deriveColor(0, 1, 1, 0.4)
            );
        }

      StackPane rootPane = new StackPane();
        rootPane.setStyle("-fx-background-color: rgb(35, 39, 50);");

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
