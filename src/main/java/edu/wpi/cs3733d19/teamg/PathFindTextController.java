package edu.wpi.cs3733d19.teamg;

import edu.wpi.cs3733d19.teamg.models.Node;
import edu.wpi.cs3733d19.teamg.pathfinding.PathFinding;
import edu.wpi.cs3733d19.teamg.pathfinding.PathFindingFactory;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class PathFindTextController extends Controller {

    private List<Node> path = new ArrayList<>();
    private List<String> floorList = new ArrayList<>();
    private ArrayList<ArrayList<String>> textPath = new ArrayList<>();
    int floor;
    int counter;
    private double originalWidth = 5000; //original width of image
    private double originalHeight = 3400; //original height of image
    private double paneWidth = KioskApplication.getMapController().anchorPaneF3.getWidth();
    private double paneHeight = KioskApplication.getMapController().anchorPaneF3.getHeight();
    //the ratio we want to scale the x cord to (1.0 right now)
    private double widthRatio = (paneWidth / originalWidth);
    //the ratio we want to scale the y cord to (1.0 right now)
    private double heightRatio = (paneHeight / originalHeight);
    private boolean toggled = false;
    private PathFinding pf = PathFindingFactory.makePathFinding(null, null, null);
    private ArrayList<Integer> startPos = new ArrayList<Integer>();

    private String textDirections = "";

    @FXML
    public Pane pane;
    public AnchorPane mainAnchorPane;
    public TextField textFieldEmail;
    public Button btnEmail;
    public Label lblEmailConfirmation;
    public Label lblStart;
    public Label lblDest;
    public Accordion accordionText;

    @FXML
    private void initialize() {
        KioskApplication.setController(this);
        path = PathFindController.getPath();
        KioskApplication.setFloor(path.get(0).getFloor());

        KioskApplication.getMapController().setOpenTab();

        textPath = pf.textualPath((ArrayList<Node>) this.path);
        path = PathFindController.getPath();
        String currF = path.get(0).getFloor();
        floorList.add(currF);


        startPos.add(0);
        KioskApplication.getMapController().disableTabs();
        for (int i = 0; i < path.size(); i++) {
            if (!currF.equals(path.get(i).getFloor())) {
                currF = path.get(i).getFloor();

                floorList.add(currF);
                startPos.add(i);

            }
        }
        floor = 0;
        lblStart.setText("Starting Location: " + path.get(0).getShortName());
        lblDest.setText("Destination: " + path.get(path.size() - 1).getShortName());


        displayDirections(0);

        KioskApplication.getMapController()
                .floor3Tab.setText("");
        KioskApplication.getMapController()
                .floor4Tab.setText("");
        KioskApplication.getMapController()
                .groundFloorTab.setText("");
        KioskApplication.getMapController()
                .lowerFloor1Tab.setText("");
        KioskApplication.getMapController()
                .floor1Tab.setText("");
        KioskApplication.getMapController()
                .lowerFloor2Tab.setText("");
        KioskApplication.getMapController()
                .floor2Tab.setText("");

        for (int i = 0; i < floorList.size(); i++) {
            TitledPane t1 = new TitledPane();
            TextArea tx = new TextArea(displayDirections(i));
            tx.setWrapText(true);
            if (floorList.get(i).equals("1")) {
                t1 = new TitledPane("Floor 1: Step " + (i + 1), tx);
                t1.setOnMouseClicked(this::switchToFloor1);
            } else if (floorList.get(i).equals("2")) {
                t1 = new TitledPane("Floor 2: Step " + (i + 1), tx);
                t1.setOnMouseClicked(this::switchToFloor2);
            } else if (floorList.get(i).equals("3")) {
                t1 = new TitledPane("Floor 3: Step " + (i + 1), tx);
                t1.setOnMouseClicked(this::switchToFloor3);
            } else if (floorList.get(i).equals("4")) {
                t1 = new TitledPane("Floor 4: Step " + (i + 1), tx);
                t1.setOnMouseClicked(this::switchToFloor4);
            } else if (floorList.get(i).equals("G")) {
                t1 = new TitledPane("Ground Floor: Step " + (i + 1), tx);
                t1.setOnMouseClicked(this::switchToFloorG);
            } else if (floorList.get(i).equals("L1")) {
                t1 = new TitledPane("Lower Floor 1: Step " + (i + 1), tx);
                t1.setOnMouseClicked(this::switchToFloorL1);
            } else if (floorList.get(i).equals("L2")) {
                t1 = new TitledPane("Lower Floor 2: Step " + (i + 1), tx);
                t1.setOnMouseClicked(this::switchToFloorL2);
            }
            t1.setBackground(new Background(
                    new BackgroundFill(Color.rgb(1, 41, 83),
                            CornerRadii.EMPTY, Insets.EMPTY)));
            accordionText.getPanes().addAll(t1);
        }
        zoomOnPath();
        drawPathArrows();

        accordionText.setExpandedPane(accordionText.getPanes().get(0));
    }

    @FXML
    private void switchToFloor1(MouseEvent actionEvent) {
        KioskApplication.setFloor("1");
        KioskApplication.getMapController().setOpenTab();
        floorUpdate("1");
    }

    @FXML
    private void switchToFloor2(MouseEvent actionEvent) {
        KioskApplication.setFloor("2");
        KioskApplication.getMapController().setOpenTab();
        floorUpdate("2");
    }

    @FXML
    private void switchToFloor3(MouseEvent actionEvent) {
        KioskApplication.setFloor("3");
        KioskApplication.getMapController().setOpenTab();
        floorUpdate("3");
    }

    @FXML
    private void switchToFloor4(MouseEvent actionEvent) {
        KioskApplication.setFloor("4");
        KioskApplication.getMapController().setOpenTab();
        floorUpdate("4");
    }

    @FXML
    private void switchToFloorG(MouseEvent actionEvent) {
        KioskApplication.setFloor("G");
        KioskApplication.getMapController().setOpenTab();
        floorUpdate("G");
    }

    @FXML
    private void switchToFloorL1(MouseEvent actionEvent) {
        KioskApplication.setFloor("L1");
        KioskApplication.getMapController().setOpenTab();
        floorUpdate("L1");
    }

    @FXML
    private void switchToFloorL2(MouseEvent actionEvent) {
        KioskApplication.setFloor("L2");
        KioskApplication.getMapController().setOpenTab();
        floorUpdate("L2");
    }

    @FXML
    private void sendEmail(ActionEvent actionEvent) {
        final String addressee = textFieldEmail.getText();
        if (!Email.validateEmail(addressee)) {
            lblEmailConfirmation.setText("Email " + addressee + " not valid");
            return;
        }

        if (textDirections.length() > 0) {
            final String subject = "Text directions from BWH";
            StringBuilder message =
                    new StringBuilder("Here are your requested directions from BWH:\n\n");
            for (int i = 0; i < floorList.size(); i++) {
                message.append(this.displayDirections(i));
            }
            final String finalMessage = message.toString();
            Executors.newSingleThreadExecutor().submit(() -> {
                Platform.runLater(() -> {
                    lblEmailConfirmation.setText("Sending email to " + addressee);
                });
                Email.sendHtmlEmail(subject, finalMessage, addressee);
                Platform.runLater(() -> {
                    lblEmailConfirmation.setText("Email sent to " + addressee);
                });
            });
        }
    }


    private String displayDirections(int section) {
        ArrayList<String> directions = this.textPath.get(section);
        StringBuilder toDisplay = new StringBuilder();

        for (String curDir : directions) {
            if (curDir.contains("left")) {
                toDisplay.append(" \uD83E\uDC78  ");
            } else if (curDir.contains("right")) {
                toDisplay.append(" \uD83E\uDC7A  ");
            } else if (curDir.contains("up")) {
                toDisplay.append(" \u2BC5  ");
            } else if (curDir.contains("down")) {
                toDisplay.append(" \u2BC6  ");
            } else if (curDir.contains("follow")) {
                toDisplay.append(" \uD83E\uDC79  ");
            } else {
                toDisplay.append("       ");
            }
            toDisplay.append(curDir);
            toDisplay.append("\n");
        }

        textDirections = toDisplay.toString();
        return textDirections;
    }

    /**
     * clears the map of path and callouts.
     */
    public void clearMap() {
        if (floorList.get(floor).equals("1")) {
            KioskApplication.getMapController().anchorPaneF1
                    .getChildren().remove(1, KioskApplication
                    .getMapController().anchorPaneF1.getChildren().size());
        } else if (floorList.get(floor).equals("2")) {
            KioskApplication.getMapController().anchorPaneF2
                    .getChildren().remove(1, KioskApplication
                    .getMapController().anchorPaneF2.getChildren().size());
        } else if (floorList.get(floor).equals("3")) {
            KioskApplication.getMapController().anchorPaneF3
                    .getChildren().remove(1, KioskApplication
                    .getMapController().anchorPaneF3.getChildren().size());
        } else if (floorList.get(floor).equals("4")) {
            KioskApplication.getMapController().anchorPaneF4
                    .getChildren().remove(1, KioskApplication
                    .getMapController().anchorPaneF4.getChildren().size());
        } else if (floorList.get(floor).equals("G")) {
            KioskApplication.getMapController().anchorPaneGf
                    .getChildren().remove(1, KioskApplication
                    .getMapController().anchorPaneGf.getChildren().size());
        } else if (floorList.get(floor).equals("L1")) {
            KioskApplication.getMapController().anchorPaneLf1
                    .getChildren().remove(1, KioskApplication
                    .getMapController().anchorPaneLf1.getChildren().size());
        } else if (floorList.get(floor).equals("L2")) {
            KioskApplication.getMapController().anchorPaneLf2
                    .getChildren().remove(1, KioskApplication
                    .getMapController().anchorPaneLf2.getChildren().size());
        }
        KioskApplication.getMapController().drawPath(path);
    }

    /**
     * Zooms in on the path.
     */
    public void zoomOnPath() {
        int start = startPos.get(floor);
        int end;
        if (floor == floorList.size() - 1) {
            end = path.size() - 1;
        } else {
            end = startPos.get(floor + 1) - 1;
        }
        int midX = (path.get(start).getXCoord() + path.get(end).getXCoord()) / 2;
        int midY = (path.get(start).getYCoord() + path.get(end).getYCoord()) / 2;

        int distance = path.get(start).distanceTo(path.get(end));
        double scale;
        if (distance > 400) {
            scale = 1.4;
        } else if (distance < 400) {
            scale = 2.0;
        } else {
            scale = 1.2;
        }
        int xcoordDistToBottomCorner = 5000 - midX;
        midX *= widthRatio;
        int ycoordDistToBottomCorner = 3400 - midY;
        midY *= heightRatio;

        xcoordDistToBottomCorner *= widthRatio;
        int xcoordOffset = xcoordDistToBottomCorner - midX;
        ycoordDistToBottomCorner *= heightRatio;
        int ycoordOffset = ycoordDistToBottomCorner - midY;

        if (path.get(start).getFloor().equals("1")) {
            KioskApplication.getMapController().anchorPaneF1.setTranslateX(xcoordOffset);
            KioskApplication.getMapController().anchorPaneF1.setTranslateY(ycoordOffset);
            KioskApplication.getMapController().anchorPaneF1.setScaleX(scale);
            KioskApplication.getMapController().anchorPaneF1.setScaleY(scale);
        } else if (path.get(start).getFloor().equals("2")) {
            KioskApplication.getMapController().anchorPaneF2.setTranslateX(xcoordOffset);
            KioskApplication.getMapController().anchorPaneF2.setTranslateY(ycoordOffset);
            KioskApplication.getMapController().anchorPaneF2.setScaleX(scale);
            KioskApplication.getMapController().anchorPaneF2.setScaleY(scale);
        } else if (path.get(start).getFloor().equals("3")) {
            KioskApplication.getMapController().anchorPaneF3.setTranslateX(xcoordOffset);
            KioskApplication.getMapController().anchorPaneF3.setTranslateY(ycoordOffset);
            KioskApplication.getMapController().anchorPaneF3.setScaleX(scale);
            KioskApplication.getMapController().anchorPaneF3.setScaleY(scale);
        } else if (path.get(start).getFloor().equals("4")) {
            KioskApplication.getMapController().anchorPaneF4.setTranslateX(xcoordOffset);
            KioskApplication.getMapController().anchorPaneF4.setTranslateY(ycoordOffset);
            KioskApplication.getMapController().anchorPaneF4.setScaleX(scale);
            KioskApplication.getMapController().anchorPaneF4.setScaleY(scale);
        } else if (path.get(start).getFloor().equals("G")) {
            KioskApplication.getMapController().anchorPaneGf.setTranslateX(xcoordOffset);
            KioskApplication.getMapController().anchorPaneGf.setTranslateY(ycoordOffset);
            KioskApplication.getMapController().anchorPaneGf.setScaleX(scale);
            KioskApplication.getMapController().anchorPaneGf.setScaleY(scale);
        } else if (path.get(start).getFloor().equals("L1")) {
            KioskApplication.getMapController().anchorPaneLf1.setTranslateX(xcoordOffset);
            KioskApplication.getMapController().anchorPaneLf1.setTranslateY(ycoordOffset);
            KioskApplication.getMapController().anchorPaneLf1.setScaleX(scale);
            KioskApplication.getMapController().anchorPaneLf1.setScaleY(scale);
        } else if (path.get(start).getFloor().equals("L2")) {
            KioskApplication.getMapController().anchorPaneLf2.setTranslateX(xcoordOffset);
            KioskApplication.getMapController().anchorPaneLf2.setTranslateY(ycoordOffset);
            KioskApplication.getMapController().anchorPaneLf2.setScaleX(scale);
            KioskApplication.getMapController().anchorPaneLf2.setScaleY(scale);
        }


    }

    private void floorUpdate(String floorName) {
        int index = paneIndex(accordionText.getExpandedPane());
        floor = index;
        zoomOnPath();
        accordionText.setExpandedPane(accordionText.getPanes().get(floor));
    }

    private int paneIndex(TitledPane tp) {
        int index = -1;
        ObservableList<TitledPane> allPane = accordionText.getPanes();
        for (int i = 0; i < allPane.size(); i++) {
            if (tp == allPane.get(i)) {
                index = i;
            }
        }

        return index;
    }

    void drawPathArrows() {

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

            if (!(path.get(i).getFloor().equals(path.get(i + 1).getFloor()))) {
                if (path.get(i).floorLookUp(path.get(i).getFloor())
                        > path.get(i + 1).floorLookUp(path.get(i + 1).getFloor())) {
                    drawDownArrow(x1, y1, path.get(i), true, path, i);
                } else {
                    drawUpArrow(x1, y1, path.get(i), true, path, i);
                }

            } else if (i > 0) {
                if (!(path.get(i).getFloor().equals(path.get(i - 1).getFloor()))) {
                    if (path.get(i).floorLookUp(path.get(i).getFloor())
                            > path.get(i - 1).floorLookUp(path.get(i - 1).getFloor())) {
                        drawDownArrow(x1, y1, path.get(i), false, path, i);
                    } else {
                        drawUpArrow(x1, y1, path.get(i), false, path, i);
                    }

                }
            }
        }

    }

    /**
     * Draing an up arrow at a specified point on map.
     *
     * @param x1   x value
     * @param y1   y value
     * @param node node
     * @param end  if it is an ending node
     */
    private void drawUpArrow(double x1, double y1, Node node,
                             boolean end, List<Node> path, int pos) {
        Image image;
        if (end) {
            image = new Image("/Photos/arrowUpRed.png");
        } else {
            image = new Image("/Photos/arrowUpGreen.png");
        }
        ImageView iv1 = new ImageView();
        iv1.setImage(image);
        iv1.setFitWidth(16);
        iv1.setFitHeight(16);
        iv1.setX(x1 - 8);
        iv1.setY(y1 - 8);
        if (end) {
            iv1.setOnMouseEntered(e ->
                    KioskApplication.getMapController().makeCallout(x1, y1, node, false, path));
            iv1.setOnMouseExited(e ->
                    KioskApplication.getMapController().removeCallout(node));
        } else {
            iv1.setOnMouseEntered(e ->
                    KioskApplication.getMapController().makeCallout(x1, y1, node, true, path));
            iv1.setOnMouseExited(e ->
                    KioskApplication.getMapController().removeCallout(node));
        }
        if (end) {
            iv1.setOnMouseClicked(e ->
                    changeFloor(path.get(pos + 1).getFloor(), true));
        } else {
            iv1.setOnMouseClicked(e ->
                    changeFloor(path.get(pos - 1).getFloor(), false));
        }
        if (node.getFloor().equals("1")) {
            KioskApplication.getMapController().anchorPaneF1.getChildren().add(iv1);
        } else if (node.getFloor().equals("2")) {
            KioskApplication.getMapController().anchorPaneF2.getChildren().add(iv1);
        } else if (node.getFloor().equals("3")) {
            KioskApplication.getMapController().anchorPaneF3.getChildren().add(iv1);
        } else if (node.getFloor().equals("4")) {
            KioskApplication.getMapController().anchorPaneF4.getChildren().add(iv1);
        } else if (node.getFloor().equals("G")) {
            KioskApplication.getMapController().anchorPaneGf.getChildren().add(iv1);
        } else if (node.getFloor().equals("L1")) {
            KioskApplication.getMapController().anchorPaneLf1.getChildren().add(iv1);
        } else if (node.getFloor().equals("L2")) {
            KioskApplication.getMapController().anchorPaneLf2.getChildren().add(iv1);
        }
    }

    /**
     * Draing a down arrow at a specified point on map.
     *
     * @param x1   x value
     * @param y1   y value
     * @param node node
     * @param end  if it is an ending node
     */
    private void drawDownArrow(double x1, double y1, Node node,
                               boolean end, List<Node> path, int pos) {
        Image image;
        if (end) {
            image = new Image("/Photos/arrowDownRed.png");
        } else {
            image = new Image("/Photos/arrowDownGreen.png");
        }
        ImageView iv1 = new ImageView();
        iv1.setImage(image);
        iv1.setFitWidth(16);
        iv1.setFitHeight(16);
        iv1.setX(x1 - 8);
        iv1.setY(y1 - 8);
        if (end) {
            iv1.setOnMouseEntered(e ->
                    KioskApplication.getMapController().makeCallout(x1, y1, node, false, path));
            iv1.setOnMouseExited(e ->
                    KioskApplication.getMapController().removeCallout(node));
        } else {
            iv1.setOnMouseEntered(e ->
                    KioskApplication.getMapController().makeCallout(x1, y1, node, true, path));
            iv1.setOnMouseExited(e ->
                    KioskApplication.getMapController().removeCallout(node));
        }
        if (end) {
            iv1.setOnMouseClicked(e ->
                    changeFloor(path.get(pos + 1).getFloor(), true));
        } else {
            iv1.setOnMouseClicked(e ->
                    changeFloor(path.get(pos - 1).getFloor(), false));
        }
        iv1.setOnMouseExited(e ->
                KioskApplication.getMapController().removeCallout(node));
        if (node.getFloor().equals("1")) {
            KioskApplication.getMapController().anchorPaneF1.getChildren().add(iv1);
        } else if (node.getFloor().equals("2")) {
            KioskApplication.getMapController().anchorPaneF2.getChildren().add(iv1);
        } else if (node.getFloor().equals("3")) {
            KioskApplication.getMapController().anchorPaneF3.getChildren().add(iv1);
        } else if (node.getFloor().equals("4")) {
            KioskApplication.getMapController().anchorPaneF4.getChildren().add(iv1);
        } else if (node.getFloor().equals("G")) {
            KioskApplication.getMapController().anchorPaneGf.getChildren().add(iv1);
        } else if (node.getFloor().equals("L1")) {
            KioskApplication.getMapController().anchorPaneLf1.getChildren().add(iv1);
        } else if (node.getFloor().equals("L2")) {
            KioskApplication.getMapController().anchorPaneLf2.getChildren().add(iv1);
        }
    }

    /**
     * Changing floors.
     *
     * @param floorName name of the floor you are changing.
     * @param forward whether you are going forwards or backwards
     */
    void changeFloor(String floorName, boolean forward) {
        KioskApplication.setFloor(floorName);
        KioskApplication.getMapController().setOpenTab();
        floorUpdateArrow(forward);
    }

    /**
     * Update Floor Value.
     *
     * @param forward whether you are going forwards or backwards
     */
    void floorUpdateArrow(boolean forward) {
        if (forward) {
            floor++;
        } else {
            floor--;
        }
        zoomOnPath();
        accordionText.setExpandedPane(accordionText.getPanes().get(floor));
    }


}
