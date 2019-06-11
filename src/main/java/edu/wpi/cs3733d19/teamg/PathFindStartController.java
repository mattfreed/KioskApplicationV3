package edu.wpi.cs3733d19.teamg;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import edu.wpi.cs3733d19.teamg.models.Node;
import edu.wpi.cs3733d19.teamg.pathfinding.PathFinding;
import edu.wpi.cs3733d19.teamg.pathfinding.PathFindingFactory;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PathFindStartController extends Controller {
    public List<Node> nodes = new ArrayList<>();
    List<Node> onFloor = new ArrayList<>();
    List<Node> selectable = new ArrayList<>();

    String floor;
    private Node selectedBtnStart = null;
    private Node selectedBtnDest = null;
    double originalWidth = 5000; //original width of image
    double originalHeight = 3400; //original height of image
    double f4OgWidth = 3636; //original width of image
    double f4OgHeight = 2724; //original height of image
    double paneWidth;
    double paneHeight;
    //the ratio we want to scale the x cord to (1.0 right now)
    double widthRatio;
    //the ratio we want to scale the y cord to (1.0 right now)
    double heightRatio;

    double widthRatioF4;
    double heightRatioF4;
    boolean start = true;

    @FXML
    public VBox boxScrollStart;
    public VBox boxScrollDestination;
    public AnchorPane mainAnchorPane;
    public Button getDirectionsBtn;
    public Label errorLabel;
    public JFXTextField destSearchTextField;
    public JFXTextField startSearchTextField;
    public MenuButton filterStartMenuButton;
    public JFXToggleButton startOrDestToggle;

    @FXML
    private void initialize() {
        nodes = Node.getAll(KioskApplication.getEntityManager());
        KioskApplication.setController(this);
        setFloor();
        addListener(destSearchTextField, boxScrollDestination);
        addListener(startSearchTextField, boxScrollStart);

        drawOnMap();

        setStart(KioskApplication.getDefaultLocation());
    }

    private void drawOnMap() {
        paneWidth = KioskApplication.getPrimaryScene().getWidth() - 350;
        paneHeight = KioskApplication.getPrimaryScene().getHeight() - 50;
        //the ratio we want to scale the x cord to (1.0 right now)
        widthRatio = (paneWidth / originalWidth);
        //the ratio we want to scale the y cord to (1.0 right now)
        heightRatio = (paneHeight / originalHeight);

        widthRatioF4 = (paneWidth / f4OgWidth);
        heightRatioF4 = (paneHeight / f4OgHeight);
        for (int i = 0; i < nodes.size(); i++) {
            if (!nodes.get(i).getNodeType().equals("HALL")) {
                if (nodes.get(i).getFloor().equals("4")) {
                    KioskApplication.getMapController().drawCircleForPathfindClicking(
                            nodes.get(i).getXCoord() * widthRatio,
                            nodes.get(i).getYCoord() * heightRatio,
                            nodes.get(i), this::selectedFromMap);
                } else {
                    KioskApplication.getMapController().drawCircleForPathfindClicking(
                            nodes.get(i).getXCoord() * widthRatio,
                            nodes.get(i).getYCoord() * heightRatio,
                            nodes.get(i), this::selectedFromMap);
                }
            }

        }
    }

    private void addListener(JFXTextField textField, VBox vbox) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<javafx.scene.Node> children =
                    FXCollections.observableArrayList(vbox.getChildren());
            Collections.sort(children, new FuzzySearchComparator(textField
                    .getText(), node -> findNodeWithId(node.getId()).getShortName()));
            vbox.getChildren().setAll(children);
        });
    }

    private JFXButton createButton(Node node, String handler) {
        JFXButton fin = new JFXButton();
        fin.setText(node.getShortName());
        if (handler.equals("start")) {
            fin.setOnAction(this::startHandler);
        } else if (handler.equals("dest")) {
            fin.setOnAction(this::destHandler);
        } else {
            return null;
        }
        fin.setMinWidth(250);
        fin.setTextAlignment(TextAlignment.LEFT);
        fin.setId(node.getNodeId());
        return fin;
    }

    /**
     * Method to set the Nodes displayed by Floor.
     */
    public void setFloor() {
        if (!onFloor.isEmpty()) {
            onFloor.clear();
            selectable.clear();
            boxScrollStart.getChildren().clear();
            boxScrollDestination.getChildren().clear();
        }

        selectable.clear();
        selectable.addAll(getNodesOnFloor());

        populateScrollPanes(selectable);
        filterStartMenuButton.setText("Current Floor");
    }

    private List<Node> getNodesOnFloor() {
        for (Node node : nodes) {
            if (node.getFloor().equals(KioskApplication.getFloor())) {
                onFloor.add(node);
            }
        }

        return getNonHallNodes(onFloor);
    }

    private List<Node> getNonHallNodes(List<Node> list) {
        List<Node> fin = new ArrayList<>();

        for (Node node : list) {
            if (!node.getNodeType().trim().equals("HALL")
                    && !node.getShortName().toLowerCase().contains("hall")) {
                fin.add(node);
            }
        }
        return fin;
    }

    private void clearScrollPanes() {
        boxScrollDestination.getChildren().clear();
        boxScrollStart.getChildren().clear();
    }

    private void populateScrollPanes(List<Node> selectable) {
        for (Node node : selectable) {
            Button btndest = createButton(node, "dest");
            boxScrollDestination.getChildren().add(btndest);

            Button btnstart = createButton(node, "start");
            boxScrollStart.getChildren().add(btnstart);
        }
    }

    private Node findNodeWithId(String idNode) {
        Node myNode = null;
        for (Node numNode : nodes) {
            if (idNode.equals(numNode.getNodeId())) {
                myNode = numNode;
                break;
            }
        }
        return myNode;
    }

    @FXML
    private void destHandler(ActionEvent event) {
        Button btn = (JFXButton) event.getSource();
        selectedBtnDest = this.findNodeWithId(btn.getId());
        destSearchTextField.setText(btn.getText());
    }


    public void setDest(Node node) {
        selectedBtnDest = node;
        destSearchTextField.setText(node.getShortName());
    }

    @FXML
    private void startHandler(ActionEvent event) {
        Button btn = (JFXButton) event.getSource();
        selectedBtnStart = this.findNodeWithId(btn.getId());
        startSearchTextField.setText(btn.getText());
    }

    public void setStart(Node node) {
        selectedBtnStart = node;
        startSearchTextField.setText(node.getShortName());
    }

    private void updateFilter(String filter) {
        clearScrollPanes();
        if (filter.equals("floor")) {
            populateScrollPanes(getNodesOnFloor());
            KioskApplication.getMapController().clearMap();
            drawOnMap();
        } else if (filter.equals("clear")) {
            populateScrollPanes(getNonHallNodes(nodes));
            KioskApplication.getMapController().clearMap();
            drawOnMap();
        } else {
            populateScrollPanes(getNodesOfType(filter, nodes));
            KioskApplication.getMapController().clearMap();
            drawOnMap();
            KioskApplication.getMapController().colorNodeByFilter(filter);
        }
    }

    private List<Node> getNodesOfType(String type, List<Node> list) {
        List<Node> fin = new ArrayList<>();
        for (Node node : list) {
            if (node.getNodeType().trim().equals(type)) {
                fin.add(node);
            }
        }
        return fin;
    }

    @FXML
    private void filterClear(ActionEvent event) {
        updateFilter("clear");
        filterStartMenuButton.setText("Entire Hospital");
    }

    @FXML
    private void filterFloor(ActionEvent event) {
        updateFilter("floor");
        filterStartMenuButton.setText("Current Floor");
    }

    @FXML
    private void filterConf(ActionEvent event) {
        updateFilter("CONF");
        filterStartMenuButton.setText("Conference Rooms");
    }

    @FXML
    private void filterDept(ActionEvent event) {
        updateFilter("DEPT");
        filterStartMenuButton.setText("Hospital Departments");
    }

    @FXML
    private void filterElev(ActionEvent event) {
        updateFilter("ELEV");
        filterStartMenuButton.setText("Elevators");
    }

    @FXML
    private void filterInfo(ActionEvent event) {
        updateFilter("INFO");
        filterStartMenuButton.setText("Information");
    }

    @FXML
    private void filterLabs(ActionEvent event) {
        updateFilter("LABS");
        filterStartMenuButton.setText("Laboratories");
    }

    @FXML
    private void filterRest(ActionEvent event) {
        updateFilter("REST");
        filterStartMenuButton.setText("Restrooms");
    }

    @FXML
    private void filterBath(ActionEvent event) {
        updateFilter("BATH");
        filterStartMenuButton.setText("Bathrooms");
    }

    @FXML
    private void filterStai(ActionEvent event) {
        updateFilter("STAI");
        filterStartMenuButton.setText("Stairs");
    }

    @FXML
    private void filterServ(ActionEvent event) {
        updateFilter("SERV");
        filterStartMenuButton.setText("Services");
    }

    @FXML
    private void filterRetl(ActionEvent event) {
        updateFilter("RETL");
        filterStartMenuButton.setText("Dining");
    }

    /**
     * getDirections handler for instantiating PathFinder object and switching scene.
     */
    @FXML
    public void getDirections(ActionEvent event) throws IOException {

        if (selectedBtnStart == null) {
            errorLabel.setText("Please Choose Start");
        } else if (selectedBtnDest == null) {
            errorLabel.setText("Please Choose Destination");
        } else if (selectedBtnStart.equals(selectedBtnDest)) {
            errorLabel.setText("Please Choose Different Locations");
        } else {
            //create path finding object
            PathFinding pf = PathFindingFactory.makePathFinding((ArrayList<Node>) nodes,
                    selectedBtnStart, selectedBtnDest);
            pf.pathFind();
            PathFindController.setPath(pf.getPath());


            if (pf.getPath().size() < 2) {
                errorLabel.setText("Invalid Path");
            } else {
                KioskApplication.getHistory().push(() -> {
                    KioskApplication.getMainScreenController().changeToDirections(
                            pf.getStart(), pf.getDest());
                    Platform.runLater(() -> {
                        try {
                            ((PathFindStartController) KioskApplication.getController())
                                    .getDirections(null);
                        } catch (IOException ex) {
                            //wont
                        }
                    });
                });
                KioskApplication.getMapController().drawPath(PathFindController.getPath());

                AnchorPane pathFindText = FXMLLoader.load(getClass()
                        .getResource("/layout/UI_SoftEng_PathFindText.fxml"));

                KioskApplication.switchPanel(mainAnchorPane, pathFindText);
            }
        }
    }

    @FXML
    private void selectedFromMap(MouseEvent mouseEvent) {
        Circle circle = (Circle) mouseEvent.getSource();
        if (start) {
            for (int i = 0; i < KioskApplication.getMapController()
                    .anchorPaneF1.getChildren().size(); i++) {
                if (KioskApplication.getMapController().anchorPaneF1
                        .getChildren().get(i) instanceof Circle) {
                    if (((Circle) KioskApplication.getMapController().anchorPaneF1
                            .getChildren().get(i)).getFill().equals(Color.LIMEGREEN)) {
                        ((Circle) KioskApplication.getMapController().anchorPaneF1
                                .getChildren().get(i)).setFill(Color.BLACK);
                    }
                    if (KioskApplication.getMapController().anchorPaneF1
                            .getChildren().get(i).getId().equals(circle.getId())) {
                        ((Circle) KioskApplication.getMapController().anchorPaneF1
                                .getChildren().get(i)).setFill(Color.LIMEGREEN);
                    }
                }
            }
            for (int i = 0; i < KioskApplication.getMapController()
                    .anchorPaneF2.getChildren().size(); i++) {
                if (KioskApplication.getMapController().anchorPaneF2
                        .getChildren().get(i) instanceof Circle) {
                    if (((Circle) KioskApplication.getMapController().anchorPaneF2
                            .getChildren().get(i)).getFill().equals(Color.LIMEGREEN)) {
                        ((Circle) KioskApplication.getMapController().anchorPaneF2
                                .getChildren().get(i)).setFill(Color.BLACK);
                    }
                    if (KioskApplication.getMapController().anchorPaneF2
                            .getChildren().get(i).getId().equals(circle.getId())) {
                        ((Circle) KioskApplication.getMapController().anchorPaneF2
                                .getChildren().get(i)).setFill(Color.LIMEGREEN);
                    }
                }

            }
            for (int i = 0; i < KioskApplication.getMapController()
                    .anchorPaneF3.getChildren().size(); i++) {
                if (KioskApplication.getMapController().anchorPaneF3
                        .getChildren().get(i) instanceof Circle) {
                    if (((Circle) KioskApplication.getMapController().anchorPaneF3
                            .getChildren().get(i)).getFill().equals(Color.LIMEGREEN)) {
                        ((Circle) KioskApplication.getMapController().anchorPaneF3
                                .getChildren().get(i)).setFill(Color.BLACK);
                    }
                    if (KioskApplication.getMapController().anchorPaneF3
                            .getChildren().get(i).getId().equals(circle.getId())) {
                        ((Circle) KioskApplication.getMapController().anchorPaneF3
                                .getChildren().get(i)).setFill(Color.LIMEGREEN);
                    }
                }

            }
            for (int i = 0; i < KioskApplication.getMapController()
                    .anchorPaneF4.getChildren().size(); i++) {
                if (KioskApplication.getMapController().anchorPaneF4
                        .getChildren().get(i) instanceof Circle) {
                    if (((Circle) KioskApplication.getMapController().anchorPaneF4
                            .getChildren().get(i)).getFill().equals(Color.LIMEGREEN)) {
                        ((Circle) KioskApplication.getMapController().anchorPaneF4
                                .getChildren().get(i)).setFill(Color.BLACK);
                    }
                    if (KioskApplication.getMapController().anchorPaneF4
                            .getChildren().get(i).getId().equals(circle.getId())) {
                        ((Circle) KioskApplication.getMapController().anchorPaneF4
                                .getChildren().get(i)).setFill(Color.LIMEGREEN);
                    }
                }

            }
            for (int i = 0; i < KioskApplication.getMapController()
                    .anchorPaneGf.getChildren().size(); i++) {
                if (KioskApplication.getMapController().anchorPaneGf
                        .getChildren().get(i) instanceof Circle) {
                    if (((Circle) KioskApplication.getMapController().anchorPaneGf
                            .getChildren().get(i)).getFill().equals(Color.LIMEGREEN)) {
                        ((Circle) KioskApplication.getMapController().anchorPaneGf
                                .getChildren().get(i)).setFill(Color.BLACK);
                    }
                    if (KioskApplication.getMapController().anchorPaneGf
                            .getChildren().get(i).getId().equals(circle.getId())) {
                        ((Circle) KioskApplication.getMapController().anchorPaneGf
                                .getChildren().get(i)).setFill(Color.LIMEGREEN);
                    }
                }

            }
            for (int i = 0; i < KioskApplication.getMapController()
                    .anchorPaneLf1.getChildren().size(); i++) {
                if (KioskApplication.getMapController().anchorPaneLf1
                        .getChildren().get(i) instanceof Circle) {
                    if (((Circle) KioskApplication.getMapController().anchorPaneLf1
                            .getChildren().get(i)).getFill().equals(Color.LIMEGREEN)) {
                        ((Circle) KioskApplication.getMapController().anchorPaneLf1
                                .getChildren().get(i)).setFill(Color.BLACK);
                    }
                    if (KioskApplication.getMapController().anchorPaneLf1
                            .getChildren().get(i).getId().equals(circle.getId())) {
                        ((Circle) KioskApplication.getMapController().anchorPaneLf1
                                .getChildren().get(i)).setFill(Color.LIMEGREEN);
                    }
                }

            }
            for (int i = 0; i < KioskApplication.getMapController()
                    .anchorPaneLf2.getChildren().size(); i++) {
                if (KioskApplication.getMapController().anchorPaneLf2
                        .getChildren().get(i) instanceof Circle) {
                    if (((Circle) KioskApplication.getMapController().anchorPaneLf2
                            .getChildren().get(i)).getFill().equals(Color.LIMEGREEN)) {
                        ((Circle) KioskApplication.getMapController().anchorPaneLf2
                                .getChildren().get(i)).setFill(Color.BLACK);
                    }
                    if (KioskApplication.getMapController().anchorPaneLf2
                            .getChildren().get(i).getId().equals(circle.getId())) {
                        ((Circle) KioskApplication.getMapController().anchorPaneLf2
                                .getChildren().get(i)).setFill(Color.LIMEGREEN);
                    }
                }

            }
        } else {
            for (int i = 0; i < KioskApplication.getMapController()
                    .anchorPaneF1.getChildren().size(); i++) {
                if (KioskApplication.getMapController().anchorPaneF1
                        .getChildren().get(i) instanceof Circle) {
                    if (((Circle) KioskApplication.getMapController().anchorPaneF1
                            .getChildren().get(i)).getFill().equals(Color.RED)) {
                        ((Circle) KioskApplication.getMapController().anchorPaneF1
                                .getChildren().get(i)).setFill(Color.BLACK);
                    }
                    if (KioskApplication.getMapController().anchorPaneF1
                            .getChildren().get(i).getId().equals(circle.getId())) {
                        ((Circle) KioskApplication.getMapController().anchorPaneF1
                                .getChildren().get(i)).setFill(Color.RED);
                    }
                }
            }
            for (int i = 0; i < KioskApplication.getMapController()
                    .anchorPaneF2.getChildren().size(); i++) {
                if (KioskApplication.getMapController().anchorPaneF2
                        .getChildren().get(i) instanceof Circle) {
                    if (((Circle) KioskApplication.getMapController().anchorPaneF2
                            .getChildren().get(i)).getFill().equals(Color.RED)) {
                        ((Circle) KioskApplication.getMapController().anchorPaneF2
                                .getChildren().get(i)).setFill(Color.BLACK);
                    }
                    if (KioskApplication.getMapController().anchorPaneF2
                            .getChildren().get(i).getId().equals(circle.getId())) {
                        ((Circle) KioskApplication.getMapController().anchorPaneF2
                                .getChildren().get(i)).setFill(Color.RED);
                    }
                }

            }
            for (int i = 0; i < KioskApplication.getMapController()
                    .anchorPaneF3.getChildren().size(); i++) {
                if (KioskApplication.getMapController().anchorPaneF3
                        .getChildren().get(i) instanceof Circle) {
                    if (((Circle) KioskApplication.getMapController().anchorPaneF3
                            .getChildren().get(i)).getFill().equals(Color.RED)) {
                        ((Circle) KioskApplication.getMapController().anchorPaneF3
                                .getChildren().get(i)).setFill(Color.BLACK);
                    }
                    if (KioskApplication.getMapController().anchorPaneF3
                            .getChildren().get(i).getId().equals(circle.getId())) {
                        ((Circle) KioskApplication.getMapController().anchorPaneF3
                                .getChildren().get(i)).setFill(Color.RED);
                    }
                }

            }
            for (int i = 0; i < KioskApplication.getMapController()
                    .anchorPaneF4.getChildren().size(); i++) {
                if (KioskApplication.getMapController().anchorPaneF4
                        .getChildren().get(i) instanceof Circle) {
                    if (((Circle) KioskApplication.getMapController().anchorPaneF4
                            .getChildren().get(i)).getFill().equals(Color.RED)) {
                        ((Circle) KioskApplication.getMapController().anchorPaneF4
                                .getChildren().get(i)).setFill(Color.BLACK);
                    }
                    if (KioskApplication.getMapController().anchorPaneF4
                            .getChildren().get(i).getId().equals(circle.getId())) {
                        ((Circle) KioskApplication.getMapController().anchorPaneF4
                                .getChildren().get(i)).setFill(Color.RED);
                    }
                }

            }
            for (int i = 0; i < KioskApplication.getMapController()
                    .anchorPaneGf.getChildren().size(); i++) {
                if (KioskApplication.getMapController().anchorPaneGf
                        .getChildren().get(i) instanceof Circle) {
                    if (((Circle) KioskApplication.getMapController().anchorPaneGf
                            .getChildren().get(i)).getFill().equals(Color.RED)) {
                        ((Circle) KioskApplication.getMapController().anchorPaneGf
                                .getChildren().get(i)).setFill(Color.BLACK);
                    }
                    if (KioskApplication.getMapController().anchorPaneGf
                            .getChildren().get(i).getId().equals(circle.getId())) {
                        ((Circle) KioskApplication.getMapController().anchorPaneGf
                                .getChildren().get(i)).setFill(Color.RED);
                    }
                }

            }
            for (int i = 0; i < KioskApplication.getMapController()
                    .anchorPaneLf1.getChildren().size(); i++) {
                if (KioskApplication.getMapController().anchorPaneLf1
                        .getChildren().get(i) instanceof Circle) {
                    if (((Circle) KioskApplication.getMapController().anchorPaneLf1
                            .getChildren().get(i)).getFill().equals(Color.RED)) {
                        ((Circle) KioskApplication.getMapController().anchorPaneLf1
                                .getChildren().get(i)).setFill(Color.BLACK);
                    }
                    if (KioskApplication.getMapController().anchorPaneLf1
                            .getChildren().get(i).getId().equals(circle.getId())) {
                        ((Circle) KioskApplication.getMapController().anchorPaneLf1
                                .getChildren().get(i)).setFill(Color.RED);
                    }
                }

            }
            for (int i = 0; i < KioskApplication.getMapController()
                    .anchorPaneLf2.getChildren().size(); i++) {
                if (KioskApplication.getMapController().anchorPaneLf2
                        .getChildren().get(i) instanceof Circle) {
                    if (((Circle) KioskApplication.getMapController().anchorPaneLf2
                            .getChildren().get(i)).getFill().equals(Color.RED)) {
                        ((Circle) KioskApplication.getMapController().anchorPaneLf2
                                .getChildren().get(i)).setFill(Color.BLACK);
                    }
                    if (KioskApplication.getMapController().anchorPaneLf2
                            .getChildren().get(i).getId().equals(circle.getId())) {
                        ((Circle) KioskApplication.getMapController().anchorPaneLf2
                                .getChildren().get(i)).setFill(Color.RED);
                    }
                }

            }
        }
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getNodeId().equals(circle.getId()) && start) {
                setStart(nodes.get(i));
            } else if (nodes.get(i).getNodeId().equals(circle.getId()) && !start) {
                setDest(nodes.get(i));
            }

        }

    }

    @FXML
    private void startOrDestHandeler() {
        if (start) {
            start = false;
        } else {
            start = true;
        }

    }

}
