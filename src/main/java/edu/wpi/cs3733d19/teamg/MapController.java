package edu.wpi.cs3733d19.teamg;

import com.jfoenix.controls.JFXTabPane;
import edu.wpi.cs3733d19.teamg.models.Node;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Transform;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;

public class MapController {

    private double paneWidth = 5000; //this is where the pane width should go
    private double paneHeight = 3400; //this is where the pane height should go

    private PannableCanvas canvas;
    private NodeGestures nodeGestures;

    private SequentialTransition sq1 = new SequentialTransition();
    private SequentialTransition sq2 = new SequentialTransition();
    private SequentialTransition sq3 = new SequentialTransition();
    private SequentialTransition sq4 = new SequentialTransition();
    private SequentialTransition sqG = new SequentialTransition();
    private SequentialTransition sqF1 = new SequentialTransition();
    private SequentialTransition sqF2 = new SequentialTransition();


    private String floor = "3";
    double linePos = 0;

    @FXML
    public ImageView imageViewF2;
    public ImageView imageViewF1;
    public ImageView imageViewF3;
    public ImageView imageViewF4;
    public ImageView imageViewGf;
    public ImageView imageViewLf1;
    public ImageView imageViewLf2;
    public AnchorPane anchorPaneF1;
    public AnchorPane anchorPaneF2;
    public AnchorPane anchorPaneF3;
    public AnchorPane anchorPaneF4;
    public AnchorPane anchorPaneGf;
    public AnchorPane anchorPaneLf1;
    public AnchorPane anchorPaneLf2;
    public Tab floor4Tab;
    public Tab floor3Tab;
    public Tab floor2Tab;
    public Tab floor1Tab;
    public Tab groundFloorTab;
    public Tab lowerFloor1Tab;
    public Tab lowerFloor2Tab;
    public AnchorPane anchorPane;
    public JFXTabPane tabPane;

    @FXML
    private void initialize() {
        KioskApplication.setMapController(this);

        KioskApplication.setFloor("3");
        setOpenTab();

        setGraphics();
    }

    /**
     * Sets all graphics on map.
     */
    public void setGraphics() {
        imageViewF1.setImage(KioskApplication.getFirstFloorImage());
        imageViewF2.setImage(KioskApplication.getSecondFloorImage());
        imageViewF3.setImage(KioskApplication.getThirdFloorImage());
        imageViewF4.setImage(KioskApplication.getFourthFloorImage());
        imageViewGf.setImage(KioskApplication.getGroundFloorImage());
        imageViewLf1.setImage(KioskApplication.getLowerLevelOneImage());
        imageViewLf2.setImage(KioskApplication.getLowerLevelTwoImage());
        bindImages(imageViewF1, anchorPaneF1);
        bindImages(imageViewF2, anchorPaneF2);
        bindImages(imageViewF3, anchorPaneF3);
        bindImages(imageViewF4, anchorPaneF4);
        bindImages(imageViewGf, anchorPaneGf);
        bindImages(imageViewLf1, anchorPaneLf1);
        bindImages(imageViewLf2, anchorPaneLf2);

        canvas = new PannableCanvas();
        canvas.setTranslateX(100);
        canvas.setTranslateY(100);
        nodeGestures = new NodeGestures(canvas);

        nodeGestures.zoom(anchorPaneF1);
        nodeGestures.zoom(anchorPaneF2);
        nodeGestures.zoom(anchorPaneF3);
        nodeGestures.zoom(anchorPaneF4);
        nodeGestures.zoom(anchorPaneGf);
        nodeGestures.zoom(anchorPaneLf1);
        nodeGestures.zoom(anchorPaneLf2);

        anchorPaneF1.addEventFilter(MouseEvent.MOUSE_PRESSED,
                nodeGestures.getOnMousePressedEventHandler());
        anchorPaneF1.addEventFilter(MouseEvent.MOUSE_DRAGGED,
                nodeGestures.getOnMouseDraggedEventHandler());


        anchorPaneF2.addEventFilter(MouseEvent.MOUSE_PRESSED,
                nodeGestures.getOnMousePressedEventHandler());
        anchorPaneF2.addEventFilter(MouseEvent.MOUSE_DRAGGED,
                nodeGestures.getOnMouseDraggedEventHandler());

        anchorPaneF3.addEventFilter(MouseEvent.MOUSE_PRESSED,
                nodeGestures.getOnMousePressedEventHandler());
        anchorPaneF3.addEventFilter(MouseEvent.MOUSE_DRAGGED,
                nodeGestures.getOnMouseDraggedEventHandler());

        anchorPaneF4.addEventFilter(MouseEvent.MOUSE_PRESSED,
                nodeGestures.getOnMousePressedEventHandler());
        anchorPaneF4.addEventFilter(MouseEvent.MOUSE_DRAGGED,
                nodeGestures.getOnMouseDraggedEventHandler());

        anchorPaneGf.addEventFilter(MouseEvent.MOUSE_PRESSED,
                nodeGestures.getOnMousePressedEventHandler());
        anchorPaneGf.addEventFilter(MouseEvent.MOUSE_DRAGGED,
                nodeGestures.getOnMouseDraggedEventHandler());

        anchorPaneLf1.addEventFilter(MouseEvent.MOUSE_PRESSED,
                nodeGestures.getOnMousePressedEventHandler());
        anchorPaneLf1.addEventFilter(MouseEvent.MOUSE_DRAGGED,
                nodeGestures.getOnMouseDraggedEventHandler());

        anchorPaneLf2.addEventFilter(MouseEvent.MOUSE_PRESSED,
                nodeGestures.getOnMousePressedEventHandler());
        anchorPaneLf2.addEventFilter(MouseEvent.MOUSE_DRAGGED,
                nodeGestures.getOnMouseDraggedEventHandler());

        anchorPaneF1.addEventFilter(MouseEvent.MOUSE_PRESSED,
                nodeGestures.getOnMousePressedEventHandler());
        anchorPaneF1.addEventFilter(MouseEvent.MOUSE_DRAGGED,
                nodeGestures.getOnMouseDraggedEventHandler());

        setTabGraphics(floor1Tab, KioskApplication.getOneIcon(), 40, 50);

        setTabGraphics(floor2Tab, KioskApplication.getTwoIcon(), 40, 50);

        setTabGraphics(floor3Tab, KioskApplication.getThreeIcon(), 40, 50);

        setTabGraphics(floor4Tab, KioskApplication.getFourIcon(), 40, 50);

        setTabGraphics(groundFloorTab, KioskApplication.getGroundIcon(), 35, 50);

        setTabGraphics(lowerFloor1Tab, KioskApplication.getLf1Icon(), 50, 50);

        setTabGraphics(lowerFloor2Tab, KioskApplication.getLf2Icon(), 50, 50);

        sq1.setCycleCount(Animation.INDEFINITE);
        sq2.setCycleCount(Animation.INDEFINITE);
        sq3.setCycleCount(Animation.INDEFINITE);
        sq4.setCycleCount(Animation.INDEFINITE);
        sqG.setCycleCount(Animation.INDEFINITE);
        sqF1.setCycleCount(Animation.INDEFINITE);
        sqF2.setCycleCount(Animation.INDEFINITE);
    }

    /**
     * bind Images method to bind the height and width of an image to its respective pane.
     *
     * @param imageView  imageView to be bound.
     * @param anchorPane pane to bind imageView to.
     */
    private void bindImages(ImageView imageView, AnchorPane anchorPane) {
        imageView.setPreserveRatio(false);
        imageView.fitWidthProperty().bind(anchorPane.widthProperty());
        imageView.fitHeightProperty().bind(anchorPane.heightProperty());
    }

    /**
     * Sets the tab graphics.
     *
     * @param tab    the tab to set graphics
     * @param img    the image to set
     * @param height the height of the image
     * @param width  the width of the image
     */
    public void setTabGraphics(Tab tab, Image img, int height, int width) {
        ImageView imgview = new ImageView();
        imgview.setImage(img);
        imgview.setFitHeight(height);
        imgview.setFitWidth(width);
        tab.setGraphic(imgview);
    }

    @FXML
    private void floorHandler(Event event) {
        if (event.getSource() == floor1Tab) {
            KioskApplication.setFloor("1");
        } else if (event.getSource() == floor2Tab) {
            KioskApplication.setFloor("2");
        } else if (event.getSource() == floor3Tab) {
            KioskApplication.setFloor("3");
        } else if (event.getSource() == floor4Tab) {
            KioskApplication.setFloor("4");
        } else if (event.getSource() == groundFloorTab) {
            KioskApplication.setFloor("G");
        } else if (event.getSource() == lowerFloor1Tab) {
            KioskApplication.setFloor("L1");
        } else if (event.getSource() == lowerFloor2Tab) {
            KioskApplication.setFloor("L2");
        }
        if (KioskApplication.getController() != null) {

            KioskApplication.getController().setFloor();
        }
    }


    /**
     * Draws a line on the Map.
     *
     * @param x1        starting x coordinate of line.
     * @param y1        starting y coordinate of line.
     * @param x2        ending x coordinate of line.
     * @param y2        ending y coordinate of line.
     * @param floorName floorText on which to draw line.
     */
    void drawLine(double x1, double y1, double x2, double y2, String floorName) {
        Line line = LineBuilder.create().startX(x1)
                .startY(y1)
                .endX(x2)
                .endY(y2)
                .stroke(Color.color(0, 0.16, 0.33))
                .strokeWidth(5.0f)
                .build();

        line.setStrokeWidth(4);


        if (floorName.equals("1")) {
            anchorPaneF1.getChildren().addAll(line);
        } else if (floorName.equals("2")) {
            anchorPaneF2.getChildren().addAll(line);
        } else if (floorName.equals("3")) {
            anchorPaneF3.getChildren().add(line);
        } else if (floorName.equals("4")) {
            anchorPaneF4.getChildren().add(line);
        } else if (floorName.equals("G")) {
            anchorPaneGf.getChildren().add(line);
        } else if (floorName.equals("L1")) {
            anchorPaneLf1.getChildren().add(line);
        } else if (floorName.equals("L2")) {
            anchorPaneLf2.getChildren().add(line);
        }
    }

    private void drawCircle(double xval, double yval, Node node, List<Node> path) {
        Circle cir = new Circle(xval, yval, 8);
        cir.setFill(Color.RED);
        cir.setOnMouseEntered(e ->
                makeCallout(xval, yval, node, false, path));
        cir.setOnMouseExited(e ->
                removeCallout(node));
        if (node.getFloor().equals("1")) {
            anchorPaneF1.getChildren().add(cir);
        } else if (node.getFloor().equals("2")) {
            anchorPaneF2.getChildren().add(cir);
        } else if (node.getFloor().equals("3")) {
            anchorPaneF3.getChildren().add(cir);
        } else if (node.getFloor().equals("4")) {
            anchorPaneF4.getChildren().add(cir);
        } else if (node.getFloor().equals("G")) {
            anchorPaneGf.getChildren().add(cir);
        } else if (node.getFloor().equals("L1")) {
            anchorPaneLf1.getChildren().add(cir);
        } else if (node.getFloor().equals("L2")) {
            anchorPaneLf2.getChildren().add(cir);
        }
    }

    private void drawStartCircle(double xval, double yval, Node node, List<Node> path) {
        Circle cir = new Circle(xval, yval, 8);
        cir.setFill(Color.LIMEGREEN);
        cir.setOnMouseEntered(e ->
                makeCallout(xval, yval, node, true, path));
        cir.setOnMouseExited(e ->
                removeCallout(node));
        if (node.getFloor().equals("1")) {
            anchorPaneF1.getChildren().add(cir);
        } else if (node.getFloor().equals("2")) {
            anchorPaneF2.getChildren().add(cir);
        } else if (node.getFloor().equals("3")) {
            anchorPaneF3.getChildren().add(cir);
        } else if (node.getFloor().equals("4")) {
            anchorPaneF4.getChildren().add(cir);
        } else if (node.getFloor().equals("G")) {
            anchorPaneGf.getChildren().add(cir);
        } else if (node.getFloor().equals("L1")) {
            anchorPaneLf1.getChildren().add(cir);
        } else if (node.getFloor().equals("L2")) {
            anchorPaneLf2.getChildren().add(cir);
        }

    }

    public void makeCallout(double x1, double y1, Node node, boolean start, List<Node> path) {
        animateCallout(x1, y1, node, start, path);

    }

    /**
     * Removes the callout from a specific node.
     * @param node The node from which to remove the callout
     */
    public void removeCallout(Node node) {
        if (node.getFloor().equals("1")) {
            anchorPaneF1.getChildren().remove(anchorPaneF1
                    .getChildren().size() - 3, anchorPaneF1.getChildren().size());
        } else if (node.getFloor().equals("2")) {
            anchorPaneF2.getChildren().remove(anchorPaneF2
                    .getChildren().size() - 3, anchorPaneF2.getChildren().size());
        } else if (node.getFloor().equals("3")) {
            anchorPaneF3.getChildren().remove(anchorPaneF3
                    .getChildren().size() - 3, anchorPaneF3.getChildren().size());
        } else if (node.getFloor().equals("4")) {
            anchorPaneF4.getChildren().remove(anchorPaneF4
                    .getChildren().size() - 3, anchorPaneF4.getChildren().size());
        } else if (node.getFloor().equals("G")) {
            anchorPaneGf.getChildren().remove(anchorPaneGf
                    .getChildren().size() - 3, anchorPaneGf.getChildren().size());
        } else if (node.getFloor().equals("L1")) {
            anchorPaneLf1.getChildren().remove(anchorPaneLf1
                    .getChildren().size() - 3, anchorPaneLf1.getChildren().size());
        } else if (node.getFloor().equals("L2")) {
            anchorPaneLf2.getChildren().remove(anchorPaneLf2
                    .getChildren().size() - 3, anchorPaneLf2.getChildren().size());
        }

    }

    /**
     * Draw a path on the map.
     *
     * @param path Path to draw - list of nodes.
     */
    void drawPath(List<Node> path) {
        KioskApplication.getMapController().clearMap();
        double originalWidth = 5000; //original width of image
        double originalHeight = 3400; //original height of image
        paneWidth = anchorPaneF3.getWidth();
        paneHeight = anchorPaneF3.getHeight();
        //the ratio we want to scale the x cord to (1.0 right now)
        double widthRatio = (paneWidth / originalWidth);
        //the ratio we want to scale the y cord to (1.0 right now)
        double heightRatio = (paneHeight / originalHeight);

        double x1;
        double x2;
        double y1;
        double y2;
        x1 = (path.get(0).getXCoord() * widthRatio);
        y1 = (path.get(0).getYCoord() * heightRatio);

        for (int i = 0; i < path.size() - 1; i++) {
            x1 = (path.get(i).getXCoord() * widthRatio);
            x2 = (path.get(i + 1).getXCoord() * widthRatio);
            y1 = (path.get(i).getYCoord() * heightRatio);
            y2 = (path.get(i + 1).getYCoord() * heightRatio);

            if ((path.get(i).getFloor().equals(path.get(i + 1).getFloor()))) {
                drawLine(x1, y1, x2, y2, path.get(i).getFloor());
            }


        }
        for (int i = 0; i < path.size() - 1; i++) {
            x1 = (path.get(i).getXCoord() * widthRatio);
            x2 = (path.get(i + 1).getXCoord() * widthRatio);
            y1 = (path.get(i).getYCoord() * heightRatio);
            y2 = (path.get(i + 1).getYCoord() * heightRatio);
            if ((path.get(i).getFloor().equals(path.get(i + 1).getFloor()))) {
                addToSq(x1, y1, x2, y2, path.get(i));
            }
            if (!(path.get(i).getFloor().equals(path.get(i + 1).getFloor()))) {
                if (path.get(i).getFloor().equals(path.get(i + 1).getFloor())) {
                    drawLine(x1, y1, x2, y2, path.get(i).getFloor());
                }

            }
        }


        x1 = (path.get(0).getXCoord() * widthRatio);
        y1 = (path.get(0).getYCoord() * heightRatio);
        x2 = (path.get(path.size() - 1).getXCoord() * widthRatio);
        y2 = (path.get(path.size() - 1).getYCoord() * heightRatio);
        drawCircle(x2, y2, path.get(path.size() - 1), path);

        drawStartCircle(x1, y1, path.get(0), path);
        sq1.stop();
        sq1.play();
        sq2.stop();
        sq2.play();
        sq3.stop();
        sq3.play();
        sq4.stop();
        sq4.play();
        sqG.stop();
        sqG.play();
        sqF1.stop();
        sqF1.play();
        sqF2.stop();
        sqF2.play();


    }

    /**
     * Change the tab/floorText of the map to a desired position.
     */
    void setOpenTab() {
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        String floorName = KioskApplication.getFloor();
        if (floorName.equals("1")) {
            selectionModel.select(floor1Tab);
        } else if (floorName.equals("2")) {
            selectionModel.select(floor2Tab);
        } else if (floorName.equals("3")) {
            selectionModel.select(floor3Tab);
        } else if (floorName.equals("4")) {
            selectionModel.select(floor4Tab);
        } else if (floorName.equals("G")) {
            selectionModel.select(groundFloorTab);
        } else if (floorName.equals("L1")) {
            selectionModel.select(lowerFloor1Tab);
        } else if (floorName.equals("L2")) {
            selectionModel.select(lowerFloor2Tab);
        }
    }


    double getMapWidth() {
        return anchorPaneF3.getWidth();
    }

    double getMapHeight() {
        return anchorPaneF3.getHeight();
    }


    /**
     * Method to draw a circle with an ID on the map.
     *
     * @param xval      x coordinate of circle.
     * @param yval      y coordinate of circle.
     * @param floorName floorText on which to draw circle.
     * @param color     color of circle.
     * @param id        id of circle.
     */
    public void drawCircleIdHandler(double xval, double yval, String floorName,
                                    Color color, String id, EventHandler<MouseEvent> clickHandler,
                                    EventHandler<MouseEvent> dragHandler,
                                    EventHandler<MouseEvent> releaseHandler) {
        Circle cir = new Circle(xval, yval, 4);
        cir.setFill(color);
        cir.setId(id);
        cir.setOnMousePressed(clickHandler);
        cir.setOnMouseDragged(dragHandler);
        cir.setOnMouseReleased(releaseHandler);
        if (floorName.equals("1")) {
            anchorPaneF1.getChildren().add(cir);
        } else if (floorName.equals("2")) {
            anchorPaneF2.getChildren().add(cir);
        } else if (floorName.equals("3")) {
            anchorPaneF3.getChildren().add(cir);
        } else if (floorName.equals("4")) {
            anchorPaneF4.getChildren().add(cir);
        } else if (floorName.equals("G")) {
            anchorPaneGf.getChildren().add(cir);
        } else if (floorName.equals("L1")) {
            anchorPaneLf1.getChildren().add(cir);
        } else if (floorName.equals("L2")) {
            anchorPaneLf2.getChildren().add(cir);
        }
    }

    /**
     * Draw all nodes on the screen to select start and end destinations
     *
     * @param xval         x coordinate of circle.
     * @param yval         y coordinate of circle.
     * @param node         floorText on which to draw circle.
     * @param clickHandler color of circle.
     */
    public void drawCircleForPathfindClicking(double xval, double yval,
                                              Node node, EventHandler<MouseEvent> clickHandler) {

        Circle cir = new Circle(xval, yval, 6);

        cir.setFill(Color.BLACK);
        cir.setId(node.getNodeId());
        cir.setOnMousePressed(clickHandler);
        if (node.getFloor().equals("1")) {
            anchorPaneF1.getChildren().add(cir);
        } else if (node.getFloor().equals("2")) {
            anchorPaneF2.getChildren().add(cir);
        } else if (node.getFloor().equals("3")) {
            anchorPaneF3.getChildren().add(cir);
        } else if (node.getFloor().equals("4")) {
            anchorPaneF4.getChildren().add(cir);
        } else if (node.getFloor().equals("G")) {
            anchorPaneGf.getChildren().add(cir);
        } else if (node.getFloor().equals("L1")) {
            anchorPaneLf1.getChildren().add(cir);
        } else if (node.getFloor().equals("L2")) {
            anchorPaneLf2.getChildren().add(cir);
        }
    }

    /**
     * Draws a line on the Map.
     *
     * @param x1        starting x coordinate of line.
     * @param y1        starting y coordinate of line.
     * @param x2        ending x coordinate of line.
     * @param y2        ending y coordinate of line.
     * @param floorName floorText on which to draw line.
     */
    void drawLineEditMap(double x1, double y1, double x2, double y2, String floorName) {
        Line line = LineBuilder.create().startX(x1)
                .startY(y1)
                .endX(x2)
                .endY(y2)
                .stroke(Color.color(0, 0, 0))
                .strokeWidth(2.0f)
                .build();

        if (floorName.equals("1")) {
            anchorPaneF1.getChildren().add(line);
        } else if (floorName.equals("2")) {
            anchorPaneF2.getChildren().add(line);
        } else if (floorName.equals("3")) {
            anchorPaneF3.getChildren().add(line);
        } else if (floorName.equals("4")) {
            anchorPaneF4.getChildren().add(line);
        } else if (floorName.equals("G")) {
            anchorPaneGf.getChildren().add(line);
        } else if (floorName.equals("L1")) {
            anchorPaneLf1.getChildren().add(line);
        } else if (floorName.equals("L2")) {
            anchorPaneLf2.getChildren().add(line);
        }
    }

    /**
     * Method to animate callouts.
     *
     * @param xval  x coordinate of node
     * @param yval  y coordinate of node
     * @param node  node to make callout for
     * @param start whether it is a start or a dest
     */
    public void animateCallout(double xval, double yval, Node node,
                               boolean start, List<Node> path) {

        double x1;
        double x2;
        double y1;
        int dir;
        if (node.getFloor().equals("4")) {
            if (node.getXCoord() > 1818) {
                x1 = xval - 25;
                x2 = xval - 75;
                dir = 1;
            } else {

                x1 = xval + 25;
                x2 = xval + 75;
                dir = 0;
            }


            if (node.getYCoord() > 1800) {
                yval -= 8;
                y1 = yval - 50;
            } else {
                yval += 8;
                y1 = yval + 50;
            }
        } else {
            if (node.getXCoord() > 2500) {
                x1 = xval - 25;
                x2 = xval - 75;
                dir = 1;
            } else {

                x1 = xval + 25;
                x2 = xval + 75;
                dir = 0;
            }


            if (node.getYCoord() > 1700) {
                yval -= 8;
                y1 = yval - 50;
            } else {
                yval += 8;
                y1 = yval + 50;
            }
        }
        Line line = new Line(xval, yval, xval, yval);
        line.setStrokeWidth(3);

        Line line2 = new Line(x1, y1, x1, y1);
        line2.setStrokeWidth(3);

        if (start) {
            line2.setStroke(Color.LIMEGREEN);
            line.setStroke(Color.LIMEGREEN);
        } else {
            line2.setStroke(Color.RED);
            line.setStroke(Color.RED);
        }

        HBox mainTitle = new HBox();
        mainTitle.setBackground(
                new Background(
                        new BackgroundFill(Color.WHITE,
                                new CornerRadii(5),
                                new Insets(0)))
        );
        mainTitle.setLayoutX(xval + 300);
        mainTitle.setLayoutY(yval + 100);
        mainTitle.setStyle("-fx-border-style: solid inside;"
                + "-fx-border-width: 2;"
                + "-fx-border-radius: 5;" + "-fx-border-color: black;");
        // Main title text
        Text mainTitleText;
        if (node == path.get(path.size() - 1)) {
            mainTitleText = new Text("Final Destination: " + node.getShortName());
        } else if (node == path.get(0)) {
            mainTitleText = new Text("Starting Location: " + node.getShortName());
        } else {
            mainTitleText = new Text("Location: " + node.getShortName());
        }


        HBox.setMargin(mainTitleText, new Insets(8, 8, 0, 8));
        mainTitleText.setFont(Font.font(9));
        mainTitle.getChildren().add(mainTitleText);

        SequentialTransition calloutAnimation = new SequentialTransition();
        calloutAnimation.getChildren().add(buildBeginLeaderLineAnim(line, x1, y1));
        calloutAnimation.getChildren().add(buildBeginLeaderLineAnim(line2, x2, y1));
        calloutAnimation.getChildren().add(buildMainTitleAnim(mainTitle, x2, y1, dir));
        line.setVisible(false);
        line2.setVisible(false);
        mainTitle.setVisible(false);
        if (node.getFloor().equals("1")) {
            anchorPaneF1.getChildren().addAll(line, line2, mainTitle);
        } else if (node.getFloor().equals("2")) {
            anchorPaneF2.getChildren().addAll(line, line2, mainTitle);
        } else if (node.getFloor().equals("3")) {
            anchorPaneF3.getChildren().addAll(line, line2, mainTitle);
        } else if (node.getFloor().equals("4")) {
            anchorPaneF4.getChildren().addAll(line, line2, mainTitle);
        } else if (node.getFloor().equals("G")) {
            anchorPaneGf.getChildren().addAll(line, line2, mainTitle);
        } else if (node.getFloor().equals("L1")) {
            anchorPaneLf1.getChildren().addAll(line, line2, mainTitle);
        } else if (node.getFloor().equals("L2")) {
            anchorPaneLf2.getChildren().addAll(line, line2, mainTitle);
        }
        calloutAnimation.stop();
        calloutAnimation.play();


    }

    private void addToSq(double x1, double y1, double x2, double y2, Node node) {
        ImageView iv1 = new ImageView();
        iv1.setImage(KioskApplication.getDuck());
        iv1.setFitWidth(32);
        iv1.setFitHeight(32);
        iv1.setX(x1 - 16);
        iv1.setY(y1 - 16);
        iv1.setVisible(false);
        if (node.getFloor().equals("1")) {
            sq1.getChildren().addAll(buildHeadAnim(iv1, x2, y2));
        } else if (node.getFloor().equals("2")) {
            sq2.getChildren().addAll(buildHeadAnim(iv1, x2, y2));
        } else if (node.getFloor().equals("3")) {
            sq3.getChildren().addAll(buildHeadAnim(iv1, x2, y2));
        } else if (node.getFloor().equals("4")) {
            sq4.getChildren().addAll(buildHeadAnim(iv1, x2, y2));
        } else if (node.getFloor().equals("G")) {
            sqG.getChildren().addAll(buildHeadAnim(iv1, x2, y2));
        } else if (node.getFloor().equals("L1")) {
            sqF1.getChildren().addAll(buildHeadAnim(iv1, x2, y2));
        } else if (node.getFloor().equals("L2")) {
            sqF2.getChildren().addAll(buildHeadAnim(iv1, x2, y2));
        }

        if (node.getFloor().equals("1")) {
            anchorPaneF1.getChildren().addAll(iv1);
        } else if (node.getFloor().equals("2")) {
            anchorPaneF2.getChildren().addAll(iv1);
        } else if (node.getFloor().equals("3")) {
            anchorPaneF3.getChildren().addAll(iv1);
        } else if (node.getFloor().equals("4")) {
            anchorPaneF4.getChildren().addAll(iv1);
        } else if (node.getFloor().equals("G")) {
            anchorPaneGf.getChildren().addAll(iv1);
        } else if (node.getFloor().equals("L1")) {
            anchorPaneLf1.getChildren().addAll(iv1);
        } else if (node.getFloor().equals("L2")) {
            anchorPaneLf2.getChildren().addAll(iv1);
        }
    }


    protected Animation buildHeadAnim(ImageView iv1, double x1, double y1) {
        ImageView ivHead = iv1;
        int millis = 1000;
        double distance = Math.sqrt(Math.pow(((x1 - 16) - ivHead.getX()), 2)
                + Math.pow(((y1 - 16) - ivHead.getY()), 2));
        double ratio = distance / millis;
        millis *= ratio;
        millis *= 20;
        return new Timeline(
                new KeyFrame(Duration.millis(1),
                        new KeyValue(ivHead.visibleProperty(), true)), // show
                new KeyFrame(Duration.millis(millis),
                        new KeyValue(ivHead.xProperty(), x1 - 16),
                        new KeyValue(ivHead.yProperty(), y1 - 16),
                        new KeyValue(ivHead.visibleProperty(), false))// max value
        );

    }

    private Animation buildBeginLeaderLineAnim(Line firstLeaderLine, double xval, double yval) {
        return new Timeline(
                new KeyFrame(Duration.millis(1),
                        new KeyValue(firstLeaderLine.visibleProperty(), true)), // show
                new KeyFrame(Duration.millis(250),
                        new KeyValue(firstLeaderLine.endXProperty(), xval),
                        new KeyValue(firstLeaderLine.endYProperty(), yval)
                )
        );
    }

    private Animation buildMainTitleAnim(HBox mainTitleBackground,
                                         double xval, double yval, int dir) {

        // main title box
        // Calculate main title width and height upfront
        Rectangle2D mainTitleBounds = getBoundsUpfront(mainTitleBackground);

        double mainTitleWidth = mainTitleBounds.getWidth();
        double mainTitleHeight = mainTitleBounds.getHeight();

        // Position mainTitleText background beside the end part of the leader line.


        // Viewport to make main title appear to scroll
        Rectangle mainTitleViewPort = new Rectangle();
        mainTitleViewPort.setWidth(0);
        mainTitleViewPort.setHeight(mainTitleHeight);
        mainTitleBackground.setClip(mainTitleViewPort);
        mainTitleBackground.setLayoutX(xval);
        mainTitleBackground.setLayoutY(yval - (mainTitleHeight / 2));

        // Animate main title from end point to the left.
        // animate layout x and width
        if (dir == 0) {
            return new Timeline(
                    new KeyFrame(Duration.millis(1),
                            new KeyValue(mainTitleBackground.visibleProperty(), true),
                            new KeyValue(mainTitleBackground.layoutXProperty(), xval)
                    ), // show
                    new KeyFrame(Duration.millis(500),
                            new KeyValue(mainTitleBackground.layoutXProperty(), xval),
                            new KeyValue(mainTitleViewPort.widthProperty(), mainTitleWidth)
                    )
            );
        } else {
            return new Timeline(
                    new KeyFrame(Duration.millis(1),
                            new KeyValue(mainTitleBackground.visibleProperty(), true),
                            new KeyValue(mainTitleBackground.layoutXProperty(), xval)
                    ), // show
                    new KeyFrame(Duration.millis(500),
                            new KeyValue(mainTitleBackground.layoutXProperty(),
                                    xval - mainTitleWidth),
                            new KeyValue(mainTitleViewPort.widthProperty(), mainTitleWidth)
                    )
            );
        }
    }


    private Rectangle2D getBoundsUpfront(Region node) {
        // Calculate main title width and height
        Group titleRoot = new Group();
        new Scene(titleRoot);
        titleRoot.getChildren().add(node);
        titleRoot.applyCss();
        titleRoot.layout();
        return new Rectangle2D(0, 0, node.getWidth(), node.getHeight());
    }

    /**
     * Method to clear all drawn items on the map.
     */
    void clearMap() {
        anchorPaneF1.getChildren().remove(1, anchorPaneF1.getChildren().size());
        anchorPaneF2.getChildren().remove(1, anchorPaneF2.getChildren().size());
        anchorPaneF3.getChildren().remove(1, anchorPaneF3.getChildren().size());
        anchorPaneF4.getChildren().remove(1, anchorPaneF4.getChildren().size());
        anchorPaneGf.getChildren().remove(1, anchorPaneGf.getChildren().size());
        anchorPaneLf1.getChildren().remove(1, anchorPaneLf1.getChildren().size());
        anchorPaneLf2.getChildren().remove(1, anchorPaneLf2.getChildren().size());
    }

    /**
     * receiving clicking on map.
     *
     * @param event on click
     */
    @FXML
    public void recieveMapSelection(MouseEvent event) {
        if (KioskApplication.getController() != null
                && KioskApplication.getController() instanceof EditNodesController) {
            EditNodesController controller = (EditNodesController) KioskApplication.getController();
            controller.recieveMapSelection(event);
        }
    }

    /**
     * Changing the floor tab.
     *
     * @return Transform
     */
    Transform getPaneTransform() {
        if (KioskApplication.getFloor().equals("1")) {
            return anchorPaneF1.getLocalToParentTransform();
        } else if (KioskApplication.getFloor().equals("2")) {
            return anchorPaneF2.getLocalToParentTransform();
        } else if (KioskApplication.getFloor().equals("3")) {
            return anchorPaneF3.getLocalToParentTransform();
        } else if (KioskApplication.getFloor().equals("4")) {
            return anchorPaneF4.getLocalToParentTransform();
        } else if (KioskApplication.getFloor().equals("G")) {
            return anchorPaneGf.getLocalToParentTransform();
        } else if (KioskApplication.getFloor().equals("L1")) {
            return anchorPaneLf1.getLocalToParentTransform();
        } else {
            return anchorPaneLf2.getLocalToParentTransform();
        }
    }

    /**
     * Disables the side tabs.
     */
    public void disableTabs() {
        floor1Tab.setDisable(true);
        anchorPaneF1.setDisable(false);
        floor2Tab.setDisable(true);
        anchorPaneF2.setDisable(false);
        floor3Tab.setDisable(true);
        anchorPaneF3.setDisable(false);
        floor4Tab.setDisable(true);
        anchorPaneF4.setDisable(false);
        groundFloorTab.setDisable(true);
        anchorPaneGf.setDisable(false);
        lowerFloor1Tab.setDisable(true);
        anchorPaneLf1.setDisable(false);
        lowerFloor2Tab.setDisable(true);
        anchorPaneLf2.setDisable(false);
    }

    /**
     * Disables the side tabs.
     */
    public void removeTabImage() {
        floor1Tab.setGraphic(null);
        floor2Tab.setGraphic(null);
        floor3Tab.setGraphic(null);
        floor4Tab.setGraphic(null);
        groundFloorTab.setGraphic(null);
        lowerFloor1Tab.setGraphic(null);
        lowerFloor2Tab.setGraphic(null);
    }

    /**
     * Enables the side tabs.
     */
    public void enableTabs() {
        floor1Tab.setDisable(false);
        floor2Tab.setDisable(false);
        floor3Tab.setDisable(false);
        floor4Tab.setDisable(false);
        groundFloorTab.setDisable(false);
        lowerFloor1Tab.setDisable(false);
        lowerFloor2Tab.setDisable(false);
    }

    /**
     * Resets zoom and pan of map screen to default.
     */
    public void resetMap() {
        String floorName = KioskApplication.getFloor();
        if (floorName.equals("1")) {
            anchorPaneF1.setScaleX(1);
            anchorPaneF1.setScaleY(1);
            anchorPaneF1.setTranslateX(0);
            anchorPaneF1.setTranslateY(0);
        } else if (floorName.equals("2")) {
            anchorPaneF2.setScaleX(1);
            anchorPaneF2.setScaleY(1);
            anchorPaneF2.setTranslateX(0);
            anchorPaneF2.setTranslateY(0);
        } else if (floorName.equals("3")) {
            anchorPaneF3.setScaleX(1);
            anchorPaneF3.setScaleY(1);
            anchorPaneF3.setTranslateX(0);
            anchorPaneF3.setTranslateY(0);
        } else if (floorName.equals("4")) {
            anchorPaneF4.setScaleX(1);
            anchorPaneF4.setScaleY(1);
            anchorPaneF4.setTranslateX(0);
            anchorPaneF4.setTranslateY(0);
        } else if (floorName.equals("G")) {
            anchorPaneGf.setScaleX(1);
            anchorPaneGf.setScaleY(1);
            anchorPaneGf.setTranslateX(0);
            anchorPaneGf.setTranslateY(0);
        } else if (floorName.equals("L1")) {
            anchorPaneLf1.setScaleX(1);
            anchorPaneLf1.setScaleY(1);
            anchorPaneLf1.setTranslateX(0);
            anchorPaneLf1.setTranslateY(0);
        } else if (floorName.equals("L2")) {
            anchorPaneLf2.setScaleX(1);
            anchorPaneLf2.setScaleY(1);
            anchorPaneLf2.setTranslateX(0);
            anchorPaneLf2.setTranslateY(0);
        }
    }

    /**
     * Changes color of circles of map based on if cooresponding node type matches filter.
     *
     * @param filter The nodetype to search by.
     */
    public void colorNodeByFilter(String filter) {
        List<AnchorPane> allFloors =
                Arrays.asList(anchorPaneF4, anchorPaneF3, anchorPaneF2, anchorPaneF1, anchorPaneGf,
                        anchorPaneLf1, anchorPaneLf2);
        for (int j = 0; j < allFloors.size(); j++) {
            AnchorPane currentFloor = allFloors.get(j);
            for (int i = 0; i < currentFloor.getChildren().size(); i++) {
                if (currentFloor.getChildren().get(i) instanceof Circle) {
                    Circle current = (Circle) currentFloor.getChildren().get(i);
                    Node currentNode = findNodeWithId(current.getId());
                    if (currentNode.getNodeType().equals(filter)) {
                        current.setFill(Paint.valueOf("#FFDF00"));
                        current.setStroke(Color.BLACK);
                    }
                }
            }
        }
    }


    private Node findNodeWithId(String idNode) {
        Node myNode = null;
        for (Node numNode : Node.getAll(KioskApplication.getEntityManager())) {
            if (idNode.equals(numNode.getNodeId())) {
                myNode = numNode;
                break;
            }
        }
        return myNode;
    }

}
