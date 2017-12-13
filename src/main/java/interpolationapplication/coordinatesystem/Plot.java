
package interpolationapplication.coordinatesystem;

import net.objecthunter.exp4j.function.Function;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;


public  class Plot extends Pane {

        public Plot(
                Function f,
                double xMin, double xMax, double xInc,
                Axis axes,Color color
        ) {
            Path path = new Path();
            path.setStroke(color);
            path.setStrokeWidth(2);

            path.setClip(
                    new Rectangle(
                            0, 0,
                            axes.getPrefWidth(),
                            axes.getPrefHeight()
                    )
            );

            double x = xMin;
            double y = f.apply(x);

            path.getElements().add(
                    new MoveTo(
                            mapX(x, axes), mapY(y, axes)
                    )
            );

            x += xInc;
            while (x < xMax) {
                y = f.apply(x);

                path.getElements().add(
                        new LineTo(
                                mapX(x, axes), mapY(y, axes)
                        )
                );

                x += xInc;
            }

            setMinSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
            setPrefSize(axes.getPrefWidth(), axes.getPrefHeight());
            setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);

            getChildren().setAll(axes, path);
        }

        private double mapX(double x, Axis axes) {
            double tx = axes.getPrefWidth() / 2;
            double sx = axes.getPrefWidth()
                    / (axes.getAxisX().getUpperBound()
                    - axes.getAxisX().getLowerBound());

            return x * sx + tx;
        }

        private double mapY(double y, Axis axes) {
            double ty = axes.getPrefHeight() / 2;
            double sy = axes.getPrefHeight()
                    / (axes.getAxisY().getUpperBound()
                    - axes.getAxisY().getLowerBound());

            return -y * sy + ty;
        }
    }

