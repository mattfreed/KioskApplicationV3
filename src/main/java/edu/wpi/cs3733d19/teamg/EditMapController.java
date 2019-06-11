package edu.wpi.cs3733d19.teamg;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import edu.wpi.cs3733d19.teamg.models.Edge;
import edu.wpi.cs3733d19.teamg.models.Node;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;
import javafx.scene.text.Font;
import javafx.scene.transform.Transform;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

public class EditMapController extends Controller {

    List<Node> nodes = new ArrayList<>();

    private double paneWidth; //this is where the pane width should go
    private double paneHeight; //this is where the pane height should go

    private PannableCanvas canvas;
    private NodeGestures nodeGestures;

    Circle tempCircle = null;

    double selectedNodeSceneX;
    double selectedNodeSceneY;

    JFXTextField xcordText = new JFXTextField();
    JFXTextField ycordText = new JFXTextField();
    JFXTextField floorText = new JFXTextField();
    JFXTextField buildingText = new JFXTextField();
    JFXTextField nodeTypeText = new JFXTextField();
    JFXTextField longNameText = new JFXTextField();
    JFXTextField shortNameText = new JFXTextField();


    @FXML
    public JFXToggleButton newNodeToggle;
    public AnchorPane mainAnchorPane;
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
    public JFXTabPane tabPane;
    public JFXButton cancelActionButton;
    public Label instructionsLabel;


    private Node selectedNode;
    private Node toSelectedNode;

    private boolean addNodeFromMap = false;
    private boolean selectNodeFromMap = true;
    private boolean editingEdges = false;

    private double originalSceneX;
    private double originalSceneY;
    private double originalTranslateX;
    private double originalTranslateY;

    private boolean wasDragged = false;

    private AnchorPane anchorPaneCurrent = anchorPaneF3;


    @FXML
    private void initialize() {

        clearMap();

        cancelActionButton.setDisable(true);
        nodes = Node.getAll(KioskApplication.getEntityManager());

        KioskApplication.setController(this);

        KioskApplication.setMapEnabled(true);

        KioskApplication.setFloor("3");

        setOpenTab();

        setGraphics();

        instructionsLabel.setText("Use toggle to add new node or select a node for options.");
        instructionsLabel.setWrapText(true);

        paneWidth = KioskApplication.getPrimaryScene().getWidth();
        paneHeight = KioskApplication.getPrimaryScene().getHeight();

        drawonMap(KioskApplication.getPrimaryScene().getWidth() - 50,
                KioskApplication.getPrimaryScene().getHeight() - 50);
    }

    //Graphics Functions ***********************************************************************
    private void setGraphics() {
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
    }

    private void setTabGraphics(Tab tab, Image img, int height, int width) {
        ImageView imgview = new ImageView();
        imgview.setImage(img);
        imgview.setFitHeight(height);
        imgview.setFitWidth(width);
        tab.setGraphic(imgview);
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

    //Tab Pane Functions ***********************************************************************
    @FXML
    private void floorHandler(Event event) {
        if (event.getSource() == floor1Tab) {
            KioskApplication.setFloor("1");
            anchorPaneCurrent = anchorPaneF1;
        } else if (event.getSource() == floor2Tab) {
            KioskApplication.setFloor("2");
            anchorPaneCurrent = anchorPaneF2;
        } else if (event.getSource() == floor3Tab) {
            KioskApplication.setFloor("3");
            anchorPaneCurrent = anchorPaneF3;
        } else if (event.getSource() == floor4Tab) {
            KioskApplication.setFloor("4");
            anchorPaneCurrent = anchorPaneF4;
        } else if (event.getSource() == groundFloorTab) {
            KioskApplication.setFloor("G");
            anchorPaneCurrent = anchorPaneGf;
        } else if (event.getSource() == lowerFloor1Tab) {
            KioskApplication.setFloor("L1");
            anchorPaneCurrent = anchorPaneLf1;
        } else if (event.getSource() == lowerFloor2Tab) {
            KioskApplication.setFloor("L2");
            anchorPaneCurrent = anchorPaneLf2;
        }

        if (KioskApplication.getController() != null) {
            KioskApplication.getController().setFloor();
        }
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

    /**
     * Resets zoom and pan of map screen to default.
     */
    public void resetMap() {
        cancelAction(null);
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

    //Search Functions ***********************************************************************
    private Node findNodeWithId(String id) {
        Node myNode = null;
        for (Node n : Node.getAll(KioskApplication.getEntityManager())) {
            if (id.equals(n.getNodeId())) {
                myNode = n;
                break;
            }
        }
        return myNode;
    }

    private Circle findCircleWithId(String id) {
        Circle myCircle = null;
        for (javafx.scene.Node n : anchorPaneF1.getChildren()) {
            if (id.equals(n.getId())) {
                myCircle = (Circle) n;
                break;
            }
        }
        for (javafx.scene.Node n : anchorPaneF2.getChildren()) {
            if (id.equals(n.getId())) {
                myCircle = (Circle) n;
                break;
            }
        }
        for (javafx.scene.Node n : anchorPaneF3.getChildren()) {
            if (id.equals(n.getId())) {
                myCircle = (Circle) n;
                break;
            }
        }
        for (javafx.scene.Node n : anchorPaneF4.getChildren()) {
            if (id.equals(n.getId())) {
                myCircle = (Circle) n;
                break;
            }
        }
        for (javafx.scene.Node n : anchorPaneGf.getChildren()) {
            if (id.equals(n.getId())) {
                myCircle = (Circle) n;
                break;
            }
        }
        for (javafx.scene.Node n : anchorPaneLf1.getChildren()) {
            if (id.equals(n.getId())) {
                myCircle = (Circle) n;
                break;
            }
        }
        for (javafx.scene.Node n : anchorPaneLf2.getChildren()) {
            if (id.equals(n.getId())) {
                myCircle = (Circle) n;
                break;
            }
        }
        return myCircle;
    }

    //Download Function ***********************************************************************
    @FXML
    private void downloadMap(ActionEvent actionEvent) {
        //Download Nodes
        BufferedWriter writerNodes;
        try {
            writerNodes = new BufferedWriter(new FileWriter("NewNodes.csv"));
            writerNodes.write("nodeID,xcoord,ycoord,floorText,buildingText,"
                    + "nodeTypeText,longNameText,shortNameText\n");
            for (Node n : Node.getAll(KioskApplication.getEntityManager())) {
                writerNodes.write(n.getNodeId() + "," + n.getXCoord() + "," + n.getYCoord() + ","
                        + n.getFloor() + "," + n.getBuilding() + "," + n.getNodeType() + ","
                        + n.getLongName().trim() + "," + n.getShortName() + "\n");
            }
            writerNodes.close();
        } catch (IOException exception) {
            System.out.println("Could not write to file.");
            exception.printStackTrace();
        }
        //Download Edges
        List<Node> graph = Node.getAll(KioskApplication.getEntityManager());
        BufferedWriter writerEdges;
        try {
            writerEdges = new BufferedWriter(new FileWriter("NewEdges.csv"));
            writerEdges.write("edgeID,startNode,endNode\n");
            for (int i = 0; i < graph.size(); i++) {
                for (int j = i + 1; j < graph.size(); j++) {
                    Node n1 = graph.get(i);
                    Node n2 = graph.get(j);
                    if (n1.getNeighbors().contains(n2)) {
                        writerEdges.write(n1.getNodeId() + "_" + n2.getNodeId() + ","
                                + n1.getNodeId() + "," + n2.getNodeId() + "\n");
                    }
                }
            }
            writerEdges.close();
        } catch (IOException exception) {
            System.out.println("Could not write to file.");
            exception.printStackTrace();
        }
    }

    //Draw on Map Functions ***********************************************************************

    /**
     * Draws all nodes and edges on the map.
     */
    private void drawonMap(double paneWidth, double paneHeight) {
        double originalHeight = 3400;
        double originalWidth = 5000;

        double heightRatio = (paneHeight / originalHeight);
        double widthRatio = (paneWidth / originalWidth);

        List<Node> nodes = Node.getAll(KioskApplication.getEntityManager());
        for (int i = 0; i < nodes.size(); i++) {
            Node current = nodes.get(i);
            for (int j = 0; j < current.getNeighbors().size(); j++) {
                Node neighbor = current.getNeighbors().get(j);
                if (neighbor.getFloor().equals(current.getFloor())) {
                    drawLineEditMap(current.getXCoord() * widthRatio,
                            current.getYCoord() * heightRatio,
                            neighbor.getXCoord() * widthRatio,
                            neighbor.getYCoord() * heightRatio,
                            current.getFloor());
                }
            }
        }

        for (int i = 0; i < nodes.size(); i++) {
            Node current = nodes.get(i);
            boolean crossFloor = false;
            for (int j = 0; j < current.getNeighbors().size(); j++) {
                Node neighbor = current.getNeighbors().get(j);
                if (!neighbor.getFloor().equals(current.getFloor())) {
                    crossFloor = true;
                }
            }
            drawCircleIdHandler(current.getXCoord() * widthRatio,
                    current.getYCoord() * heightRatio, current.getFloor(),
                    crossFloor ? Color.GOLD : Color.BLACK,
                    current.getNodeId(), this::selectedFromMap,
                    this::draggedOnMap, this::releasedFromDrag);
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

        //Line line = new Line(x1, y1, x2, y2);
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
        //anchorPaneF3.getChildren().add(line);
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
        cir.setStroke(Color.BLACK);
        //
        //        cir.setOnMouseDragged(this::draggedOnMap);
        //        cir.setOnMouseReleased(this::releasedFromDrag);
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

    private int sceneXtoMapX(double sceneX) {

        paneWidth = anchorPaneCurrent.getWidth();

        Transform transform = anchorPaneCurrent.getLocalToParentTransform();

        double xtrans = transform.getTx();

        return (int) ((sceneX - 50 - xtrans) * (1 / transform.getMxx()));
    }

    private int sceneYtoMapY(double sceneY) {

        paneHeight = anchorPaneCurrent.getHeight();

        Transform transform = anchorPaneCurrent.getLocalToParentTransform();

        double ytrans = transform.getTy();

        return (int) ((sceneY - 50 - ytrans) * (1 / transform.getMyy()));

    }

    //Add Node Functions ***********************************************************************

    /**
     * Method to enable the map and draw the nodes on map.
     *
     * @param actionEvent toggle switch event to indicate that nodes should be drawn.
     */
    public void mapEnabler(ActionEvent actionEvent) {
        addNodeFromMap = !addNodeFromMap;
        if (addNodeFromMap) {
            KioskApplication.setMapEnabled(false);
            selectNodeFromMap = false;
            cancelActionButton.setDisable(false);
            instructionsLabel.setText("Click on the map to place a node.");
        }
        if (!addNodeFromMap) {
            KioskApplication.setMapEnabled(true);
            selectNodeFromMap = true;
            cancelActionButton.setDisable(true);
            instructionsLabel.setText("Use toggle to add new node or select a node for options.");

        }

    }

    /**
     * Creates a new node.
     *
     * @param actionEvent the triggering event
     */
    @FXML
    public void addNewNode(ActionEvent actionEvent) {
        selectedNode.setXCoord(Integer.parseInt(xcordText.getText()));
        selectedNode.setYCoord(Integer.parseInt(ycordText.getText()));
        selectedNode.setFloor(floorText.getText());
        selectedNode.setBuilding(buildingText.getText());
        selectedNode.setLongName(longNameText.getText());
        selectedNode.setShortName(shortNameText.getText());
        KioskApplication.getEntityManager().getTransaction().begin();
        KioskApplication.getEntityManager().persist(selectedNode);
        KioskApplication.getEntityManager().getTransaction().commit();

        cancelAction(null);

        xcordText.setText("");
        ycordText.setText("");
        floorText.setText("");
        buildingText.setText("");
        nodeTypeText.setText("");
        shortNameText.setText("");
        longNameText.setText("");
    }

    /**
     * getCoordinatesHandler adds new node with coordinates selected from map.
     *
     * @param mouseEvent mouseEvent with coordinates of selection.
     */
    @FXML
    private void getCoordinatesHandler(MouseEvent mouseEvent) {
        if (addNodeFromMap) {

            newNodeToggle.setDisable(true);
            addNodeFromMap = false;

            paneWidth = anchorPaneCurrent.getWidth();
            paneHeight = anchorPaneCurrent.getHeight();
            double originalHeight = 3400;
            double originalWidth = 5000;
            double heightRatio = (originalHeight / paneHeight);
            double widthRatio = (originalWidth / paneWidth);

            double xcoord = sceneXtoMapX(mouseEvent.getSceneX());
            double ycoord = sceneYtoMapY(mouseEvent.getSceneY());

            selectedNode = new Node(null, (int) (xcoord * widthRatio), (int) (ycoord * heightRatio),
                    KioskApplication.getFloor(), "", "", "", "");

            drawCircleIdHandler(xcoord,
                    ycoord, KioskApplication.getFloor(),
                    Color.BLACK, selectedNode.getNodeId(),
                    this::selectedFromMap, this::draggedOnMap, this::releasedFromDrag);

            selectNodeFromMap = false;

            drawNewNodePane(mouseEvent.getSceneX(), mouseEvent.getSceneY());

        }
    }

    //Select Node Functions ***********************************************************************
    @FXML
    private void selectedFromMap(MouseEvent mouseEvent) {
        Circle circle = (Circle) mouseEvent.getSource();
        System.out.println(circle.getId());
        if (selectNodeFromMap) {
            KioskApplication.setMapEnabled(false);
            cancelActionButton.setDisable(false);
            newNodeToggle.setDisable(true);
            selectNodeFromMap = false;


            tempCircle = circle;

            tempCircle.setFill(Paint.valueOf("#008F00"));

            selectedNode = findNodeWithId(circle.getId());

            Transform transform = anchorPaneCurrent.getLocalToParentTransform();

            selectedNodeSceneX = mouseEvent.getSceneX();
            selectedNodeSceneY = mouseEvent.getSceneY();

            drawOptionsPane(selectedNodeSceneX, selectedNodeSceneY);


            originalSceneX = mouseEvent.getSceneX();
            originalSceneY = mouseEvent.getSceneY();
            originalTranslateX = ((Circle) (mouseEvent.getSource())).getTranslateX();
            originalTranslateY = ((Circle) (mouseEvent.getSource())).getTranslateY();
        }

        if (editingEdges) {
            toSelectedNode = findNodeWithId(circle.getId());
            addNewEdge();
        }
    }

    //Drag Node Functions ***********************************************************************
    @FXML
    private void draggedOnMap(MouseEvent mouseEvent) {
        KioskApplication.setMapEnabled(false);
        wasDragged = true;

        Transform transform = anchorPaneCurrent.getLocalToParentTransform();

        double offsetX = mouseEvent.getSceneX() - originalSceneX;
        double offsetY = mouseEvent.getSceneY() - originalSceneY;
        double newTranslateX = originalTranslateX + (offsetX);
        double newTranslateY = originalTranslateY + (offsetY);


        tempCircle.setTranslateX(newTranslateX / transform.getMxx());
        tempCircle.setTranslateY(newTranslateY / transform.getMyy());

    }

    @FXML
    private void releasedFromDrag(MouseEvent mouseEvent) {
        if (wasDragged) {
            System.out.println("was released");
            paneWidth = anchorPaneCurrent.getWidth();
            paneHeight = anchorPaneCurrent.getHeight();
            double originalHeight = 3400;
            double originalWidth = 5000;
            double heightRatio = (originalHeight / paneHeight);
            double widthRatio = (originalWidth / paneWidth);

            selectedNode.setXCoord((int) (sceneXtoMapX(mouseEvent.getSceneX()) * widthRatio));
            selectedNode.setYCoord((int) (sceneYtoMapY(mouseEvent.getSceneY()) * heightRatio));

            KioskApplication.getEntityManager().getTransaction().begin();
            KioskApplication.getEntityManager().merge(selectedNode);
            KioskApplication.getEntityManager().getTransaction().commit();

            cancelAction(null);

        }
    }

    @FXML
    private void dragNode(ActionEvent event) {
        instructionsLabel.setText("Drag node to new location on map.");

        anchorPaneCurrent.getChildren().remove(anchorPaneCurrent.getChildren().size() - 1);
        tempCircle.setOnMouseDragged(this::draggedOnMap);
        tempCircle.setOnMouseReleased(this::releasedFromDrag);
    }

    //Delete Node Functions ***********************************************************************
    @FXML
    private void deleteNode(ActionEvent event) {
        EntityManager manager = KioskApplication.getEntityManager();
        manager.getTransaction().begin();
        for (Node neighbor : selectedNode.getNeighbors()) {
            selectedNode.removeNeighbor(neighbor);
            neighbor.removeNeighbor(selectedNode);
            manager.merge(selectedNode);
            manager.merge(neighbor);
            manager.remove(Edge.getEdge(selectedNode, neighbor, manager));
            manager.remove(Edge.getEdge(neighbor, selectedNode, manager));
        }
        manager.remove(selectedNode);
        manager.getTransaction().commit();

        cancelAction(null);
    }

    //Edit Node Functions ***********************************************************************
    @FXML
    private void editNode(ActionEvent event) {
        selectedNode.setXCoord(Integer.parseInt(xcordText.getText()));
        selectedNode.setYCoord(Integer.parseInt(ycordText.getText()));
        selectedNode.setFloor(floorText.getText());
        selectedNode.setBuilding(buildingText.getText());
        selectedNode.setLongName(longNameText.getText());
        selectedNode.setShortName(shortNameText.getText());
        KioskApplication.getEntityManager().getTransaction().begin();
        KioskApplication.getEntityManager().merge(selectedNode);
        KioskApplication.getEntityManager().getTransaction().commit();

        cancelAction(null);

        xcordText.setText("");
        ycordText.setText("");
        floorText.setText("");
        buildingText.setText("");
        nodeTypeText.setText("");
        shortNameText.setText("");
        longNameText.setText("");

    }

    //Edit Edge Functions ***********************************************************************
    @FXML
    private void editEdge(ActionEvent actionEvent) {
        instructionsLabel.setText("Click on a red node to delete an existing edge,"
                + " click on a black node to add a new edge.");
        editingEdges = true;
        KioskApplication.setMapEnabled(true);
        List<Node> neighbors = selectedNode.getNeighbors();
        anchorPaneCurrent.getChildren().remove(anchorPaneCurrent.getChildren().size() - 1);
        for (int i = 0; i < neighbors.size(); i++) {
            Circle nodeCircle = findCircleWithId(neighbors.get(i).getNodeId());
            nodeCircle.setFill(Paint.valueOf("#FF2600"));
            nodeCircle.setOnMousePressed(this::deleteEdge);
        }
    }

    private void deleteEdge(MouseEvent event) {
        toSelectedNode = findNodeWithId(((Circle) event.getSource()).getId());

        EntityManager em = KioskApplication.getEntityManager();
        em.getTransaction().begin();
        selectedNode.removeNeighbor(toSelectedNode);
        toSelectedNode.removeNeighbor(selectedNode);
        em.merge(toSelectedNode);
        em.merge(selectedNode);
        em.remove(Edge.getEdge(selectedNode, toSelectedNode, em));
        em.remove(Edge.getEdge(toSelectedNode, selectedNode, em));
        em.getTransaction().commit();

        cancelAction(null);
    }

    private void addNewEdge() {
        if (selectedNode != null && toSelectedNode != null && selectedNode != toSelectedNode) {
            if (!selectedNode.getNeighbors().contains(toSelectedNode)) {
                EntityManager em = KioskApplication.getEntityManager();
                em.getTransaction().begin();
                Edge e1 = new Edge(selectedNode, toSelectedNode, 1);
                Edge e2 = new Edge(toSelectedNode, selectedNode, 1);
                em.persist(e1);
                em.persist(e2);
                em.merge(selectedNode);
                em.merge(toSelectedNode);
                em.getTransaction().commit();
            }
        }

        cancelAction(null);
    }

    //Draw Panel Functions ***********************************************************************
    private void drawNewNodePane(double sceneX, double sceneY) {

        xcordText.setText("");
        ycordText.setText("");
        floorText.setText("");
        buildingText.setText("");
        nodeTypeText.setText("");
        shortNameText.setText("");
        longNameText.setText("");

        instructionsLabel.setText("Enter information for new node, then click 'Add Node'.");

        Transform transform = anchorPaneCurrent.getLocalToParentTransform();
        double fontsize = 12 * (1 / transform.getMyy());

        //Setup Finish Button
        JFXButton button = new JFXButton();
        double buttonWidth = 150 * (1 / transform.getMxx());
        double buttonHeight = 30 * (1 / transform.getMyy());
        button.setMaxWidth(buttonWidth);
        button.setMinWidth(buttonWidth);
        button.setMaxHeight(buttonHeight);
        button.setMinHeight(buttonHeight);
        button.setBackground(new Background(
                new BackgroundFill(Color.rgb(1, 41, 83),
                        CornerRadii.EMPTY, Insets.EMPTY)));
        button.setTextFill(Paint.valueOf("#FFFFFF"));
        button.setOnAction(this::addNewNode);
        button.setText("Add Node");
        button.setFont(Font.font(fontsize));

        //Create VBox and add items to it
        VBox newBox = new VBox();
        newBox.getChildren().add(xcordText);
        newBox.getChildren().add(ycordText);
        newBox.getChildren().add(floorText);
        newBox.getChildren().add(buildingText);
        newBox.getChildren().add(nodeTypeText);
        newBox.getChildren().add(longNameText);
        newBox.getChildren().add(shortNameText);
        newBox.getChildren().add(button);
        newBox.setAlignment(Pos.CENTER);

        double textBoxWidth = 150 * (1 / transform.getMxx());
        double textBoxHeight = 20 * (1 / transform.getMyy());

        //Setup xcord TextBox
        xcordText.setPromptText("X Coordinate");
        xcordText.setMaxWidth(textBoxWidth);
        xcordText.setMinWidth(textBoxWidth);
        xcordText.setMaxHeight(textBoxHeight);
        xcordText.setMinHeight(textBoxHeight);
        xcordText.setFont(Font.font(fontsize));
        xcordText.setText(Integer.toString(selectedNode.getXCoord()));


        //Setup ycord TextBox
        ycordText.setPromptText("Y Coordinate");
        ycordText.setMaxWidth(textBoxWidth);
        ycordText.setMinWidth(textBoxWidth);
        ycordText.setMaxHeight(textBoxHeight);
        ycordText.setMinHeight(textBoxHeight);
        ycordText.setFont(Font.font(fontsize));
        ycordText.setText(Integer.toString(selectedNode.getYCoord()));


        //Setup floor TextBox
        floorText.setPromptText("Floor");
        floorText.setMaxWidth(textBoxWidth);
        floorText.setMinWidth(textBoxWidth);
        floorText.setMaxHeight(textBoxHeight);
        floorText.setMinHeight(textBoxHeight);
        floorText.setFont(Font.font(fontsize));
        floorText.setText(selectedNode.getFloor());


        //Setup building textbox
        buildingText.setPromptText("Building");
        buildingText.setMaxWidth(textBoxWidth);
        buildingText.setMinWidth(textBoxWidth);
        buildingText.setMaxHeight(textBoxHeight);
        buildingText.setMinHeight(textBoxHeight);
        buildingText.setFont(Font.font(fontsize));

        //Setup nodeType textbox
        nodeTypeText.setPromptText("Node Type");
        nodeTypeText.setMaxWidth(textBoxWidth);
        nodeTypeText.setMinWidth(textBoxWidth);
        nodeTypeText.setMaxHeight(textBoxHeight);
        nodeTypeText.setMinHeight(textBoxHeight);
        nodeTypeText.setFont(Font.font(fontsize));

        //Setup shortName textbox
        shortNameText.setPromptText("Short Name");
        shortNameText.setMaxWidth(textBoxWidth);
        shortNameText.setMinWidth(textBoxWidth);
        shortNameText.setMaxHeight(textBoxHeight);
        shortNameText.setMinHeight(textBoxHeight);
        shortNameText.setFont(Font.font(fontsize));

        //Setup longName textbox
        longNameText.setPromptText("Long Name");
        longNameText.setMaxWidth(textBoxWidth);
        longNameText.setMinWidth(textBoxWidth);
        longNameText.setMaxHeight(textBoxHeight);
        longNameText.setMinHeight(textBoxHeight);
        longNameText.setFont(Font.font(fontsize));

        //Create AnchorPane and bind Vbox
        AnchorPane editPane = new AnchorPane();
        newBox.prefWidthProperty().bind(editPane.widthProperty());
        newBox.prefHeightProperty().bind(editPane.heightProperty());
        newBox.setAlignment(Pos.CENTER);
        newBox.setSpacing(5 * (1 / transform.getMyy()));

        //Add VBox to AnchorPane
        editPane.getChildren().add(newBox);

        //Setup AnchorPane Properties
        editPane.setBackground(new Background(
                new BackgroundFill(Color.rgb(255, 255, 255),
                        CornerRadii.EMPTY, Insets.EMPTY)));
        editPane.setBorder(new Border(
                new BorderStroke(Paint.valueOf("#102953"),
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        double editPaneWidth = 200 * (1 / transform.getMxx());
        double editPaneHeight = 250 * (1 / transform.getMyy());
        editPane.setMaxWidth(editPaneWidth);
        editPane.setPrefWidth(editPaneWidth);
        editPane.setMinWidth(editPaneWidth);
        editPane.setMinHeight(editPaneHeight);
        editPane.setPrefHeight(editPaneHeight);
        editPane.setMaxHeight(editPaneHeight);

        //Place AnchorPane in Map
        double[] placement = placeAnchorPane(200, 250, sceneX,
                sceneY, editPaneWidth, editPaneHeight);

        editPane.setLayoutX(placement[0]);
        editPane.setLayoutY(placement[1]);

        anchorPaneCurrent.getChildren().add(editPane);
    }

    private void drawOptionsPane(double sceneX, double sceneY) {
        instructionsLabel.setText("Select an option for map editing.");

        Transform transform = anchorPaneCurrent.getLocalToParentTransform();
        double fontsize = 15 * (1 / transform.getMyy());
        double buttonWidth = 150 * (1 / transform.getMxx());
        double buttonHeight = 50 * (1 / transform.getMyy());

        //Setup deleteNode Button
        JFXButton deleteNode = new JFXButton();
        deleteNode.setText("Delete this Node");
        deleteNode.setOnAction(this::deleteConfirmation);
        deleteNode.setMaxWidth(buttonWidth);
        deleteNode.setMinWidth(buttonWidth);
        deleteNode.setMaxHeight(buttonHeight);
        deleteNode.setMinHeight(buttonHeight);
        deleteNode.setBackground(new Background(
                new BackgroundFill(Color.rgb(1, 41, 83),
                        CornerRadii.EMPTY, Insets.EMPTY)));
        deleteNode.setTextFill(Paint.valueOf("#FFFFFF"));
        deleteNode.setFont(Font.font(fontsize));

        //Setup editEdge Button
        JFXButton editEdge = new JFXButton();
        editEdge.setText("Edit Edges");
        editEdge.setOnAction(this::editEdge);
        editEdge.setMaxWidth(buttonWidth);
        editEdge.setMinWidth(buttonWidth);
        editEdge.setMaxHeight(buttonHeight);
        editEdge.setMinHeight(buttonHeight);
        editEdge.setBackground(new Background(
                new BackgroundFill(Color.rgb(1, 41, 83),
                        CornerRadii.EMPTY, Insets.EMPTY)));
        editEdge.setTextFill(Paint.valueOf("#FFFFFF"));
        editEdge.setFont(Font.font(fontsize));

        //Setup editNode Button
        JFXButton editNode = new JFXButton();
        editNode.setText("Edit this Node");
        editNode.setOnAction(this::drawEditNode);
        editNode.setMaxWidth(buttonWidth);
        editNode.setMinWidth(buttonWidth);
        editNode.setMaxHeight(buttonHeight);
        editNode.setMinHeight(buttonHeight);
        editNode.setBackground(new Background(
                new BackgroundFill(Color.rgb(1, 41, 83),
                        CornerRadii.EMPTY, Insets.EMPTY)));
        editNode.setTextFill(Paint.valueOf("#FFFFFF"));
        editNode.setFont(Font.font(fontsize));

        //Setup dragNode Button
        JFXButton dragNode = new JFXButton();
        dragNode.setText("Drag this Node");
        dragNode.setOnAction(this::dragNode);
        dragNode.setMaxWidth(buttonWidth);
        dragNode.setMinWidth(buttonWidth);
        dragNode.setMaxHeight(buttonHeight);
        dragNode.setMinHeight(buttonHeight);
        dragNode.setBackground(new Background(
                new BackgroundFill(Color.rgb(1, 41, 83),
                        CornerRadii.EMPTY, Insets.EMPTY)));
        dragNode.setTextFill(Paint.valueOf("#FFFFFF"));
        dragNode.setFont(Font.font(fontsize));

        //Declare AnchorPane and VBox and bind size
        AnchorPane optionPane = new AnchorPane();
        VBox newBox = new VBox();

        newBox.prefWidthProperty().bind(optionPane.widthProperty());
        newBox.prefHeightProperty().bind(optionPane.heightProperty());
        newBox.setSpacing(10 * (1 / transform.getMyy()));
        newBox.setAlignment(Pos.CENTER);

        //Add all items to VBox
        newBox.getChildren().add(editNode);
        newBox.getChildren().add(editEdge);
        newBox.getChildren().add(deleteNode);
        newBox.getChildren().add(dragNode);

        //Add VBox to AnchorPane
        optionPane.getChildren().add(newBox);

        //Set AnchorPane Properties
        optionPane.setBackground(new Background(
                new BackgroundFill(Color.rgb(255, 255, 255),
                        CornerRadii.EMPTY, Insets.EMPTY)));
        optionPane.setBorder(new Border(
                new BorderStroke(Paint.valueOf("#102953"),
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));


        double optionPaneWidth = 200 * (1 / transform.getMxx());
        double optionPaneHeight = 250 * (1 / transform.getMyy());

        optionPane.setMaxWidth(optionPaneWidth);
        optionPane.setPrefWidth(optionPaneWidth);
        optionPane.setMinWidth(optionPaneWidth);
        optionPane.setMinHeight(optionPaneHeight);
        optionPane.setPrefHeight(optionPaneHeight);
        optionPane.setMaxHeight(optionPaneHeight);

        double[] placement = placeAnchorPane(200, 250, sceneX,
                sceneY, optionPaneWidth, optionPaneHeight);

        optionPane.setLayoutX(placement[0]);
        optionPane.setLayoutY(placement[1]);

        anchorPaneCurrent.getChildren().add(optionPane);
    }

    @FXML
    private void deleteConfirmation(ActionEvent event) {
        instructionsLabel.setText("Please confirm you would like to delete "
                + selectedNode.getShortName() + " .");

        Transform transform = anchorPaneCurrent.getLocalToParentTransform();
        double fontsize = 15 * (1 / transform.getMyy());

        //Create a Label and set Properties
        Label text = new Label();
        double labelWidth = 150 * (1 / transform.getMxx());
        text.setText("Are you sure you would like to delete "
                + selectedNode.getShortName() + " ?");
        text.setMaxWidth(labelWidth);
        text.setMinWidth(labelWidth);
        text.setWrapText(true);
        text.setFont(Font.font(fontsize));

        //Create Yes Button and set Properties
        JFXButton yes = new JFXButton();
        double buttonWidth = 150 * (1 / transform.getMxx());
        double buttonHeight = 50 * (1 / transform.getMyy());
        yes.setText("Delete");
        yes.setMaxWidth(buttonWidth);
        yes.setMinWidth(buttonWidth);
        yes.setMaxHeight(buttonHeight);
        yes.setMinHeight(buttonHeight);
        yes.setOnAction(this::deleteNode);
        yes.setBackground(new Background(
                new BackgroundFill(Color.rgb(1, 41, 83),
                        CornerRadii.EMPTY, Insets.EMPTY)));
        yes.setTextFill(Paint.valueOf("#FFFFFF"));
        yes.setFont(Font.font(fontsize));


        //Create No Button and set Properties
        JFXButton no = new JFXButton();
        no.setText("Cancel");
        no.setMaxWidth(buttonWidth);
        no.setMinWidth(buttonWidth);
        no.setMinHeight(buttonHeight);
        no.setMaxHeight(buttonHeight);
        no.setOnAction(this::reset);
        no.setBackground(new Background(
                new BackgroundFill(Color.rgb(1, 41, 83),
                        CornerRadii.EMPTY, Insets.EMPTY)));
        no.setTextFill(Paint.valueOf("#FFFFFF"));
        no.setFont(Font.font(fontsize));

        //Create VBox and add items
        VBox newBox = new VBox();
        newBox.setSpacing(10 * (1 / transform.getMyy()));
        newBox.getChildren().add(text);
        newBox.getChildren().add(yes);
        newBox.getChildren().add(no);
        newBox.setAlignment(Pos.CENTER);

        //Create AnchorPane, add VBox, bind size
        AnchorPane optionPane = new AnchorPane();
        optionPane.getChildren().add(newBox);

        newBox.prefWidthProperty().bind(optionPane.widthProperty());
        newBox.prefHeightProperty().bind(optionPane.heightProperty());

        optionPane.setBackground(new Background(
                new BackgroundFill(Color.rgb(255, 255, 255),
                        CornerRadii.EMPTY, Insets.EMPTY)));
        optionPane.setBorder(new Border(new BorderStroke(Paint.valueOf("#102953"),
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        //Set AnchorPane properties
        double optionPaneWidth = 200 * (1 / transform.getMxx());
        double optionPaneHeight = 250 * (1 / transform.getMyy());

        optionPane.setMaxWidth(optionPaneWidth);
        optionPane.setPrefWidth(optionPaneWidth);
        optionPane.setMinWidth(optionPaneWidth);
        optionPane.setMinHeight(optionPaneHeight);
        optionPane.setPrefHeight(optionPaneHeight);
        optionPane.setMaxHeight(optionPaneHeight);

        //Set AnchorPane location in Map

        double sceneX = selectedNodeSceneX;
        double sceneY = selectedNodeSceneY;

        double[] placement = placeAnchorPane(200, 250, sceneX, sceneY,
                optionPaneWidth, optionPaneHeight);

        optionPane.setLayoutX(placement[0]);
        optionPane.setLayoutY(placement[1]);

        anchorPaneCurrent.getChildren().add(optionPane);
    }

    @FXML
    private void drawEditNode(ActionEvent event) {

        xcordText.setText("");
        ycordText.setText("");
        floorText.setText("");
        buildingText.setText("");
        nodeTypeText.setText("");
        shortNameText.setText("");
        longNameText.setText("");

        instructionsLabel.setText("Change information as needed, then click 'Edit Node'.");

        anchorPaneCurrent.getChildren().remove(anchorPaneCurrent.getChildren().size() - 1);
        Transform transform = anchorPaneCurrent.getLocalToParentTransform();
        double fontsize = 12 * (1 / transform.getMyy());

        //Create Finish Editing Button
        JFXButton button = new JFXButton();
        double buttonWidth = 150 * (1 / transform.getMxx());
        double buttonHeight = 30 * (1 / transform.getMyy());
        button.setMaxWidth(buttonWidth);
        button.setMinWidth(buttonWidth);
        button.setMaxHeight(buttonHeight);
        button.setMinHeight(buttonHeight);
        button.setOnAction(this::editNode);
        button.setBackground(new Background(
                new BackgroundFill(Color.rgb(1, 41, 83),
                        CornerRadii.EMPTY, Insets.EMPTY)));
        button.setTextFill(Paint.valueOf("#FFFFFF"));
        button.setText("Edit Node");
        button.setFont(Font.font(fontsize));

        //Create VBox, add items to VBox
        VBox newBox = new VBox();
        newBox.getChildren().add(xcordText);
        newBox.getChildren().add(ycordText);
        newBox.getChildren().add(floorText);
        newBox.getChildren().add(buildingText);
        newBox.getChildren().add(nodeTypeText);
        newBox.getChildren().add(longNameText);
        newBox.getChildren().add(shortNameText);
        newBox.getChildren().add(button);
        newBox.setAlignment(Pos.CENTER);


        double textBoxWidth = 150 * (1 / transform.getMxx());
        double textBoxHeight = 20 * (1 / transform.getMyy());

        //Set Properties of TextBoxes
        xcordText.setPromptText("X Coordinate");
        xcordText.setText(Integer.toString(selectedNode.getXCoord()));
        xcordText.setMaxWidth(textBoxWidth);
        xcordText.setMinWidth(textBoxWidth);
        xcordText.setMaxHeight(textBoxHeight);
        xcordText.setMinHeight(textBoxHeight);
        xcordText.setFont(Font.font(fontsize));

        ycordText.setPromptText("Y Coordinate");
        ycordText.setText(Integer.toString(selectedNode.getYCoord()));
        ycordText.setMaxWidth(textBoxWidth);
        ycordText.setMinWidth(textBoxWidth);
        ycordText.setMaxHeight(textBoxHeight);
        ycordText.setMinHeight(textBoxHeight);
        ycordText.setFont(Font.font(fontsize));

        floorText.setPromptText("Floor");
        floorText.setText(selectedNode.getFloor());
        floorText.setMaxWidth(textBoxWidth);
        floorText.setMinWidth(textBoxWidth);
        floorText.setMaxHeight(textBoxHeight);
        floorText.setMinHeight(textBoxHeight);
        floorText.setFont(Font.font(fontsize));

        buildingText.setPromptText("Building");
        buildingText.setText(selectedNode.getBuilding());
        buildingText.setMaxWidth(textBoxWidth);
        buildingText.setMinWidth(textBoxWidth);
        buildingText.setMaxHeight(textBoxHeight);
        buildingText.setMinHeight(textBoxHeight);
        buildingText.setFont(Font.font(fontsize));

        nodeTypeText.setPromptText("Node Type");
        nodeTypeText.setText(selectedNode.getNodeType());
        nodeTypeText.setMaxWidth(textBoxWidth);
        nodeTypeText.setMinWidth(textBoxWidth);
        nodeTypeText.setMaxHeight(textBoxHeight);
        nodeTypeText.setMinHeight(textBoxHeight);
        nodeTypeText.setFont(Font.font(fontsize));

        shortNameText.setPromptText("Short Name");
        shortNameText.setText(selectedNode.getShortName());
        shortNameText.setMaxWidth(textBoxWidth);
        shortNameText.setMinWidth(textBoxWidth);
        shortNameText.setMaxHeight(textBoxHeight);
        shortNameText.setMinHeight(textBoxHeight);
        shortNameText.setFont(Font.font(fontsize));

        longNameText.setPromptText("Long Name");
        longNameText.setText(selectedNode.getLongName());
        longNameText.setMaxWidth(textBoxWidth);
        longNameText.setMinWidth(textBoxWidth);
        longNameText.setMaxHeight(textBoxHeight);
        longNameText.setMinHeight(textBoxHeight);
        longNameText.setFont(Font.font(fontsize));

        //Declare AnchorPane and add Vbox, bind size
        AnchorPane editPane = new AnchorPane();
        newBox.prefWidthProperty().bind(editPane.widthProperty());
        newBox.prefHeightProperty().bind(editPane.heightProperty());
        newBox.setAlignment(Pos.CENTER);
        newBox.setSpacing(5 * (1 / transform.getMyy()));


        editPane.getChildren().add(newBox);
        editPane.setBackground(new Background(
                new BackgroundFill(Color.rgb(255, 255, 255),
                        CornerRadii.EMPTY, Insets.EMPTY)));
        editPane.setBorder(new Border(
                new BorderStroke(Paint.valueOf("#102953"),
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        //Set properties of AnchorPane
        double editPaneWidth = 200 * (1 / transform.getMxx());
        double editPaneHeight = 250 * (1 / transform.getMyy());
        editPane.setMaxWidth(editPaneWidth);
        editPane.setPrefWidth(editPaneWidth);
        editPane.setMinWidth(editPaneWidth);
        editPane.setMinHeight(editPaneHeight);
        editPane.setPrefHeight(editPaneHeight);
        editPane.setMaxHeight(editPaneHeight);


        double sceneX = selectedNodeSceneX;
        double sceneY = selectedNodeSceneY;

        double[] placement = placeAnchorPane(200, 250, sceneX, sceneY,
                editPaneWidth, editPaneHeight);

        editPane.setLayoutX(placement[0]);
        editPane.setLayoutY(placement[1]);

        anchorPaneCurrent.getChildren().add(editPane);
    }

    private double[] placeAnchorPane(double noScaleWidth, double noScaleHeight,
                                     double sceneX, double sceneY, double editPaneWidth,
                                     double editPaneHeight) {

        double[] placement = new double[]{0 , 0};

        double xdif = anchorPaneCurrent.getWidth() - (sceneX - 50);
        double ydif = anchorPaneCurrent.getHeight() - (sceneY - 50);

        if (((xdif) > (anchorPaneCurrent.getWidth() - (270) - noScaleWidth))
                && ((ydif) < ((120) + noScaleHeight))) {
            placement[0] = sceneXtoMapX(sceneX);
            placement[1] = sceneYtoMapY(sceneY) - editPaneHeight;
        } else if ((((xdif) < ((164)) + noScaleWidth))
                && ((ydif) < ((130)) + noScaleHeight)) {
            placement[0] = sceneXtoMapX(sceneX) - editPaneWidth;
            placement [1] = sceneYtoMapY(sceneY) - editPaneHeight;

        } else if (xdif < noScaleWidth && ydif < noScaleHeight) {
            //too close to right and bottom
            placement[0] = sceneXtoMapX(sceneX) - editPaneWidth;
            placement[1] = sceneYtoMapY(sceneY) - editPaneHeight;

        } else if (xdif < noScaleWidth) {
            //too close to right side
            placement[0] = sceneXtoMapX(sceneX) - editPaneWidth;
            placement[1] = sceneYtoMapY(sceneY);

        } else if (ydif < noScaleHeight) {
            //too close to bottom
            placement[0] = sceneXtoMapX(sceneX);
            placement[1] = sceneYtoMapY(sceneY) - editPaneHeight;

        } else {
            placement[0] = sceneXtoMapX(sceneX);
            placement[1] = sceneYtoMapY(sceneY);
        }

        return placement;
    }

    //Reset Functions ***********************************************************************
    @FXML
    private void reset(ActionEvent event) {
        cancelAction(event);
    }

    @FXML
    private void cancelAction(ActionEvent event) {

        xcordText.setText("");
        ycordText.setText("");
        floorText.setText("");
        buildingText.setText("");
        nodeTypeText.setText("");
        shortNameText.setText("");
        longNameText.setText("");

        selectedNode = null;
        toSelectedNode = null;
        selectNodeFromMap = true;
        editingEdges = false;
        newNodeToggle.setDisable(false);
        newNodeToggle.setSelected(false);
        addNodeFromMap = false;
        KioskApplication.setMapEnabled(true);
        cancelActionButton.setDisable(true);
        instructionsLabel.setText("Use toggle to add new node or select a node for options.");

        clearMap();
        drawonMap(anchorPaneCurrent.getWidth(), anchorPaneCurrent.getHeight());


    }


}
