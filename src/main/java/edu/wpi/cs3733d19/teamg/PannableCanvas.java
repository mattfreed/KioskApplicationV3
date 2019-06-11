package edu.wpi.cs3733d19.teamg;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

class PannableCanvas extends Pane {

    private DoubleProperty myScale = new SimpleDoubleProperty(1.0);

    /**
    * Constructor for PanableCanvas.
     */
    PannableCanvas() {

        setPrefSize(600, 600);
        setStyle("-fx-background-color: lightgrey; -fx-border-color: blue;");

        // add scale transform
        scaleXProperty().bind(myScale);
        scaleYProperty().bind(myScale);

        addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            System.out.println(
                    "canvas event: " + (((event.getSceneX()
                            - getBoundsInParent().getMinX()) / getScale())
                            + ", scale: " + getScale())
            );
            System.out.println("canvas bounds: " + getBoundsInParent());
        });

    }

    /**
     * Add a grid to the canvas, send it to back.
     */
    public void addGrid() {

        double width = getBoundsInLocal().getWidth();
        double height = getBoundsInLocal().getHeight();

        // add grid
        Canvas grid = new Canvas(width, height);

        // don't catch mouse events
        grid.setMouseTransparent(true);

        GraphicsContext gc = grid.getGraphicsContext2D();

        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1);

        // draw grid lines
        double offset = 50;
        for (double i = offset; i < width; i += offset) {
            // vertical
            gc.strokeLine(i, 0, i, height);
            // horizontal
            gc.strokeLine(0, i, width, i);
        }

        getChildren().add(grid);

        grid.toBack();
    }

    double getScale() {
        return myScale.get();
    }
}
