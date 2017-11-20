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
    double interval1;
    double interval2;
    final double SCALE_DELTA = 1.1;
    private final Delta dragDelta = new Delta();

    public CoordinateSystem(Function f, double i1, double i2) {
        this.f = f;
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

        Plot plot = new Plot(
                f,
                interval1, interval2, 0.001,
                axes
        );

        StackPane layout = new StackPane(plot);

        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: rgb(35, 39, 50);");

        stage.setTitle("Grafikus nézet");
        stage.setScene(new Scene(layout, Color.rgb(35, 39, 50)));

        layout.setOnScroll(new EventHandler<ScrollEvent>() {
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

                layout.setScaleX(layout.getScaleX() * scaleFactor);
                layout.setScaleY(layout.getScaleY() * scaleFactor);
            }
        });

        layout.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                dragDelta.x = layout.getLayoutX() - mouseEvent.getScreenX();
                dragDelta.y = layout.getLayoutY() - mouseEvent.getScreenY();
            }
        });

        layout.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                layout.setLayoutX(mouseEvent.getScreenX() + dragDelta.x);
                layout.setLayoutY(mouseEvent.getScreenY() + dragDelta.y);
            }

        });

        stage.show();
    }

    class Delta {

        double x;
        double y;
    }

}
