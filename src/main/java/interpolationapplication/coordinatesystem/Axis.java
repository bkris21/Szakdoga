package interpolationapplication.coordinatesystem;

import javafx.beans.binding.Bindings;
import javafx.geometry.Side;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.Pane;


public class Axis extends Pane {

    private NumberAxis axisX;
    private NumberAxis axisY;

    public Axis(int width, int height, double minX, double maxX, double unitX,double minY, double maxY, double unitY) {
        setMinSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
        setPrefSize(width, height);
        setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);

        axisX = new NumberAxis(minX, maxX, unitX);
        axisX.setSide(Side.BOTTOM);
        axisX.setMinorTickVisible(false);
        axisX.setPrefWidth(width);
        axisX.setLayoutY(height / 2);

        axisY = new NumberAxis(minY, maxY, unitY);
        axisY.setSide(Side.LEFT);
        axisY.setMinorTickVisible(false);
        axisY.setPrefHeight(height);
        axisY.layoutXProperty().bind(Bindings.subtract((width / 2) + 1,
                        axisY.widthProperty()
                )
        );

        getChildren().setAll(axisX, axisY);
    }

    public NumberAxis getAxisX() {
        return axisX;
    }

    public NumberAxis getAxisY() {
        return axisY;
    }
}
