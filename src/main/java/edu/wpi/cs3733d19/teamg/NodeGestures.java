package edu.wpi.cs3733d19.teamg;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

/**
 * Listeners for making the nodes draggable via left mouse button. Considers if parent is zoomed.
 */
class NodeGestures {

    private double mouseAnchorX;
    private double mouseAnchorY;

    double prevMouseAnchorX;
    double prevMouseAnchorY;

    private double translateAnchorX;
    private double translateAnchorY;
    private double totalTranslateY;
    private double totalTranslateX;

    private PannableCanvas canvas;

    NodeGestures(PannableCanvas canvas) {
        this.canvas = canvas;
        mouseAnchorX = 0;
        mouseAnchorY = 0;
    }

    public double getTotalTranslateY() {
        return totalTranslateY;
    }

    public double getTotalTranslateX() {
        return totalTranslateX;
    }

    EventHandler<MouseEvent> getOnMousePressedEventHandler() {
        return onMousePressedEventHandler;
    }

    EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
        return onMouseDraggedEventHandler;
    }


    private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

        public void handle(MouseEvent event) {
            if (KioskApplication.getMapEnabled()) {

                // left mouse button => dragging
                if (!event.isPrimaryButtonDown()) {
                    return;
                }

                mouseAnchorX = event.getSceneX();
                mouseAnchorY = event.getSceneY();

                Node node = (Node) event.getSource();

                translateAnchorX = node.getTranslateX();
                translateAnchorY = node.getTranslateY();
            }
        }

    };

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            if (KioskApplication.getMapEnabled()) {

                // left mouse button => dragging
                if (!event.isPrimaryButtonDown()) {
                    return;
                }

                double scale = canvas.getScale();

                Node node = (Node) event.getSource();

                node.setTranslateX(translateAnchorX + ((event.getSceneX() - mouseAnchorX) / scale));
                node.setTranslateY(translateAnchorY + ((event.getSceneY() - mouseAnchorY) / scale));

                totalTranslateX = node.getTranslateX();
                totalTranslateY = node.getTranslateY();

                event.consume();
            }
        }
    };

    /**
     * Zoom in the map.
     * @param pane Pane to zoom
     */
    void zoom(Pane pane) {
        pane.setOnScroll(
                new EventHandler<ScrollEvent>() {
                    @Override
                    public void handle(ScrollEvent event) {
                        if (KioskApplication.getMapEnabled()) {

                            double zoomFactor = 1.05;
                            double deltaY = event.getDeltaY();

                            if (deltaY < 0) {
                                zoomFactor = 0.95;
                            }


                            if ((pane.getScaleY() >= 1 && pane.getScaleX() >= 1)
                                    && (pane.getScaleX() <= 2 && pane.getScaleY() <= 2)) {
                                pane.setScaleX(pane.getScaleX() * zoomFactor);
                                pane.setScaleY(pane.getScaleY() * zoomFactor);

                                event.consume();
                            } else if (!(pane.getScaleY() >= 1 && pane.getScaleX() >= 1)) {
                                pane.setScaleX(1);
                                pane.setScaleY(1);
                            } else {
                                pane.setScaleX(2.0);
                                pane.setScaleY(2.0);
                            }
                        }
                    }
                });
    }

    public double getTranslateAnchorX() {
        return translateAnchorX;
    }

    public double getTranslateAnchorY() {
        return translateAnchorY;
    }

}
