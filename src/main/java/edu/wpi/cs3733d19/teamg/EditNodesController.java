package edu.wpi.cs3733d19.teamg;

import com.jfoenix.controls.JFXToggleButton;
import edu.wpi.cs3733d19.teamg.models.Edge;
import edu.wpi.cs3733d19.teamg.models.Node;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Transform;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

public class EditNodesController extends Controller {
    List<Node> nodes = new ArrayList<>();

    private double paneWidth = 642; //this is where the pane width should go
    private double paneHeight = 497; //this is where the pane height should go

    private boolean addNodeFromMap = false;

    private double originalSceneX;
    private double originalSceneY;
    private double originalTranslateX;
    private double originalTranslateY;

    private boolean wasDragged = false;

    @FXML
    public TableView nodesTableView;
    public JFXToggleButton mapToggleButton;
    public Button addNodeButton;
    public Button downloadButton;
    public AnchorPane mainAnchorPane;

    @FXML
    private void initialize() {
        nodes = Node.getAll(KioskApplication.getEntityManager());

        KioskApplication.setController(this);

        KioskApplication.getMapController().clearMap();

        drawonMap(KioskApplication.getMapController().getMapWidth() + (KioskApplication
                        .getMainScreenController().getRightAnchorPaneWidth() - 800),
                KioskApplication.getMapController().getMapHeight());

        setupTable();

    }

    private void setupTable() {
        TableColumn<Node, String> nodeId = new TableColumn<>("Node ID");
        nodeId.setCellValueFactory(new PropertyValueFactory<>("nodeId"));
        TableColumn<Node, Integer> xcoord = new TableColumn<>("x coordinate");
        xcoord.setCellValueFactory(new PropertyValueFactory<>("xCoord"));
        xcoord.setCellFactory(TextFieldTableCell.forTableColumn(new SafeStringIntegerConverter()));
        xcoord.setId("xcoord");
        TableColumn<Node, Integer> ycoord = new TableColumn<>("y coordinate");
        ycoord.setCellValueFactory(new PropertyValueFactory<>("yCoord"));
        ycoord.setCellFactory(TextFieldTableCell.forTableColumn(new SafeStringIntegerConverter()));
        ycoord.setId("ycoord");
        TableColumn<Node, String> floor = new TableColumn<>("Floor");
        floor.setCellValueFactory(new PropertyValueFactory<>("floorText"));
        floor.setCellFactory(TextFieldTableCell.forTableColumn());
        floor.setId("floorText");
        TableColumn<Node, String> building = new TableColumn<>("Building");
        building.setCellValueFactory(new PropertyValueFactory<>("buildingText"));
        building.setCellFactory(TextFieldTableCell.forTableColumn());
        building.setId("buildingText");
        TableColumn<Node, String> nodeType = new TableColumn<>("Node Type");
        nodeType.setCellValueFactory(new PropertyValueFactory<>("nodeTypeText"));
        nodeType.setCellFactory(TextFieldTableCell.forTableColumn());
        nodeType.setId("nodeTypeText");
        TableColumn<Node, String> longName = new TableColumn<>("Long Name");
        longName.setCellValueFactory(new PropertyValueFactory<>("longNameText"));
        longName.setCellFactory(TextFieldTableCell.forTableColumn());
        longName.setId("longNameText");
        TableColumn<Node, String> shortName = new TableColumn<>("Short Name");
        shortName.setCellValueFactory(new PropertyValueFactory<>("shortNameText"));
        shortName.setCellFactory(TextFieldTableCell.forTableColumn());
        shortName.setId("shortNameText");
        TableColumn<Node, Node> delete = new TableColumn<>("Delete");
        delete.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue()));
        delete.setCellFactory(something -> new TableCell<Node, Node>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(Node node, boolean empty) {
                super.updateItem(node, empty);

                if (node == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    EntityManager manager = KioskApplication.getEntityManager();
                    getTableView().getItems().remove(node);
                    manager.getTransaction().begin();
                    for (Node neighbor : node.getNeighbors()) {
                        node.removeNeighbor(neighbor);
                        neighbor.removeNeighbor(node);
                        manager.merge(node);
                        manager.merge(neighbor);
                        manager.remove(Edge.getEdge(node, neighbor, manager));
                        manager.remove(Edge.getEdge(neighbor, node, manager));
                    }
                    manager.remove(node);
                    manager.getTransaction().commit();
                    System.out.println("Remove node " + node.getNodeId());
                    KioskApplication.getMapController().clearMap();
                    drawonMap(KioskApplication.getMapController().getMapWidth(),
                            KioskApplication.getMapController().getMapHeight());
                });
            }
        });

        nodesTableView.getColumns().setAll(nodeId, xcoord, ycoord, floor, building, nodeType,
                longName, shortName, delete);


        ObservableList<Node> nodes = FXCollections.observableArrayList();
        nodes.addAll(Node.getAll(KioskApplication.getEntityManager()));

        nodesTableView.setItems(nodes);
        for (Object col : nodesTableView.getColumns()) {
            if (col != delete) {
                ((TableColumn) col).setOnEditCommit(this::changedField);
            }
        }
    }

    /**
     * Trigger changing a field in a node.
     *
     * @param event the triggering event
     */
    private void changedField(Event event) {
        String nodeId = (String) ((TableColumn) nodesTableView.getColumns().get(0))
                .getCellData(nodesTableView.getEditingCell().getRow());
        Node editingNode = null;
        for (Node n : Node.getAll(KioskApplication.getEntityManager())) {
            if (n.getNodeId().equals(nodeId)) {
                editingNode = n;
            }
        }
        Object newValue = ((TableColumn.CellEditEvent<Node, Object>) event).getNewValue();
        if (editingNode != null) {
            switch (nodesTableView.getEditingCell().getTableColumn().getId()) {
                case "xcoord":
                    editingNode.setXCoord((Integer) newValue);
                    KioskApplication.getMapController().clearMap();
                    drawonMap(KioskApplication.getMapController().getMapWidth(),
                            KioskApplication.getMapController().getMapHeight());
                    break;
                case "ycoord":
                    editingNode.setYCoord((Integer) newValue);
                    KioskApplication.getMapController().clearMap();
                    drawonMap(KioskApplication.getMapController().getMapWidth(),
                            KioskApplication.getMapController().getMapHeight());
                    break;
                case "floorText":
                    editingNode.setFloor(((String) newValue).trim());
                    KioskApplication.getMapController().clearMap();
                    drawonMap(KioskApplication.getMapController().getMapWidth(),
                            KioskApplication.getMapController().getMapHeight());
                    break;
                case "buildingText":
                    editingNode.setBuilding(((String) newValue).trim());
                    break;
                case "nodeTypeText":
                    editingNode.setNodeType(((String) newValue).trim());
                    break;
                case "longNameText":
                    editingNode.setLongName(((String) newValue).trim());
                    break;
                case "shortNameText":
                    editingNode.setShortName(((String) newValue).trim());
                    break;
                default:
                    break;
            }
            KioskApplication.getEntityManager().getTransaction().begin();
            KioskApplication.getEntityManager().merge(editingNode);
            KioskApplication.getEntityManager().getTransaction().commit();
        }
    }

    /**
     * Creates a new node.
     *
     * @param actionEvent the triggering event
     */
    @FXML
    public void addNewNode(ActionEvent actionEvent) {
        Node newNode = new Node(null, 1113, 1743, KioskApplication.getFloor(), "", "", "", "");
        KioskApplication.getEntityManager().getTransaction().begin();
        KioskApplication.getEntityManager().persist(newNode);
        KioskApplication.getEntityManager().getTransaction().commit();
        nodesTableView.getItems().add(newNode);

        KioskApplication.getMapController().clearMap();
        drawonMap(KioskApplication.getMapController().getMapWidth(),
                KioskApplication.getMapController().getMapHeight());

    }

    /**
     * Saves the current node to newNodes.csv.
     *
     * @param actionEvent the triggering event
     */
    @FXML
    public void downloadCsv(ActionEvent actionEvent) {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter("NewNodes.csv"));
            writer.write("nodeID,xcoord,ycoord,floorText,buildingText,"
                    + "nodeTypeText,longNameText,shortNameText\n");
            for (Node n : Node.getAll(KioskApplication.getEntityManager())) {
                writer.write(n.getNodeId() + "," + n.getXCoord() + "," + n.getYCoord() + ","
                        + n.getFloor() + "," + n.getBuilding() + "," + n.getNodeType() + ","
                        + n.getLongName().trim() + "," + n.getShortName() + "\n");
            }
            writer.close();
        } catch (IOException exception) {
            System.out.println("Could not write to file.");
            exception.printStackTrace();
        }
    }

    /**
     * Switches to edit edges screen.
     *
     * @param actionEvent the triggering event
     * @throws IOException it wont throws this cause the file exists
     */
    @FXML
    public void goToEditEdges(ActionEvent actionEvent) throws IOException {
        AnchorPane editEdgesRoot = FXMLLoader.load(getClass()
                .getResource("/layout/UI_SoftEng_EditEdges.fxml"));
        KioskApplication.switchPanel(mainAnchorPane, editEdgesRoot);
    }

    /**
     * Method to enable the map and draw the nodes on map.
     *
     * @param actionEvent toggle switch event to indicate that nodes should be drawn.
     */
    public void mapEnabler(ActionEvent actionEvent) {

        if (!addNodeFromMap) {
            drawonMap(KioskApplication.getMapController().getMapWidth(),
                    KioskApplication.getMapController().getMapHeight());
            KioskApplication.setMapEnabled(false);
        } else {
            KioskApplication.setMapEnabled(true);
        }

        addNodeFromMap = !addNodeFromMap;
    }

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
            boolean crossFloor = false;
            for (int j = 0; j < current.getNeighbors().size(); j++) {
                Node neighbor = current.getNeighbors().get(j);
                if (!neighbor.getFloor().equals(current.getFloor())) {
                    crossFloor = true;
                }
            }
            KioskApplication.getMapController()
                    .drawCircleIdHandler(current.getXCoord() * widthRatio,
                            current.getYCoord() * heightRatio, current.getFloor(),
                            crossFloor ? Color.GOLD : Color.BLACK, current.getNodeId(),
                            this::selectedFromMap, this::draggedOnMap, this::releasedFromDrag);
        }
    }

    /**
     * getCoordinatesHandler adds new node with coordinates selected from map.
     *
     * @param mouseEvent mouseEvent with coordinates of selection.
     */
    private void getCoordinatesHandler(MouseEvent mouseEvent) {
        if (addNodeFromMap) {

            paneWidth = KioskApplication.getMapController().getMapWidth();
            paneHeight = KioskApplication.getMapController().getMapHeight();
            double originalHeight = 3400;
            double originalWidth = 5000;
            double heightRatio = (originalHeight / paneHeight);
            double widthRatio = (originalWidth / paneWidth);

            double xcoord = sceneXtoMapX(mouseEvent.getSceneX());
            double ycoord = sceneYtoMapY(mouseEvent.getSceneY());


            KioskApplication.getEntityManager().getTransaction().begin();
            Node newNode = new Node(null, (int) (xcoord * widthRatio), (int) (ycoord * heightRatio),
                    KioskApplication.getFloor(), "", "", "", "");
            KioskApplication.getEntityManager().persist(newNode);
            KioskApplication.getEntityManager().getTransaction().commit();
            nodesTableView.getItems().add(newNode);

            KioskApplication.getMapController().drawCircleIdHandler(xcoord,
                    ycoord, KioskApplication.getFloor(),
                    Color.BLACK, newNode.getNodeId(),
                    this::selectedFromMap, this::draggedOnMap, this::releasedFromDrag);

        }
    }

    void recieveMapSelection(MouseEvent event) {
        getCoordinatesHandler(event);
    }

    @FXML
    private void selectedFromMap(MouseEvent mouseEvent) {
        KioskApplication.setMapEnabled(false);
        originalSceneX = mouseEvent.getSceneX();
        originalSceneY = mouseEvent.getSceneY();
        originalTranslateX = ((Circle) (mouseEvent.getSource())).getTranslateX();
        originalTranslateY = ((Circle) (mouseEvent.getSource())).getTranslateY();
    }

    @FXML
    private void draggedOnMap(MouseEvent mouseEvent) {
        KioskApplication.setMapEnabled(false);
        wasDragged = true;


        double originalHeight = 3400;
        double originalWidth = 5000;
        double heightRatio = (originalHeight / paneHeight);
        double widthRatio = (originalWidth / paneWidth);

        Transform transform = KioskApplication.getMapController().getPaneTransform();
        double xtrans = transform.getTx();
        double ytrans = transform.getTy();

        double offsetX = mouseEvent.getSceneX() - originalSceneX;
        double offsetY = mouseEvent.getSceneY() - originalSceneY;
        double newTranslateX = originalTranslateX + (offsetX / transform.getMxx());
        double newTranslateY = originalTranslateY + (offsetY / transform.getMyy());


        ((Circle) mouseEvent.getSource()).setTranslateX(newTranslateX);
        ((Circle) mouseEvent.getSource()).setTranslateY(newTranslateY);
    }

    @FXML
    private void releasedFromDrag(MouseEvent mouseEvent) {
        if (wasDragged) {
            paneWidth = KioskApplication.getMapController().getMapWidth();
            paneHeight = KioskApplication.getMapController().getMapHeight();
            double originalHeight = 3400;
            double originalWidth = 5000;
            double heightRatio = (originalHeight / paneHeight);
            double widthRatio = (originalWidth / paneWidth);

            Node node = findNodeWithId(((Circle) mouseEvent.getSource()).getId());
            node.setXCoord((int) (sceneXtoMapX(mouseEvent.getSceneX()) * widthRatio));
            node.setYCoord((int) (sceneYtoMapY(mouseEvent.getSceneY()) * heightRatio));

            setupTable();
        }
        wasDragged = false;
        KioskApplication.setMapEnabled(true);
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

    private int sceneXtoMapX(double sceneX) {

        paneWidth = KioskApplication.getMapController().getMapWidth();

        Transform transform = KioskApplication.getMapController().getPaneTransform();

        double xtrans = transform.getTx();

        return (int) ((sceneX - 30 - xtrans) * (1 / transform.getMxx()));
    }

    private int sceneYtoMapY(double sceneY) {

        paneHeight = KioskApplication.getMapController().getMapHeight();

        Transform transform = KioskApplication.getMapController().getPaneTransform();

        double ytrans = transform.getTy();

        return (int) ((sceneY - 50 - ytrans) * (1 / transform.getMyy()));

    }

}
