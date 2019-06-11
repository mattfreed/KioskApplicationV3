package edu.wpi.cs3733d19.teamg;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import edu.wpi.cs3733d19.teamg.models.Edge;
import edu.wpi.cs3733d19.teamg.models.Node;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

public class EditEdgesController extends Controller {

    List<Node> nodes = new ArrayList<>();

    private boolean mapEnabledTerm = false;

    @FXML
    public VBox nodeSelectionVbox;
    public VBox toNodeSelectionVbox;
    public Button editNodesButton;
    public TableView edgeDisplayTableView;
    public Label origNodeLabel;
    public Label termNodeLabel;
    public JFXToggleButton mapToggleButtonTerm;
    public AnchorPane mainAnchorPane;


    private Node selectedNode;
    private Node toSelectedNode;

    @FXML
    private void initialize() {
        KioskApplication.getMapController().clearMap();

        nodes = Node.getAll(KioskApplication.getEntityManager());

        KioskApplication.setController(this);
        KioskApplication.setMapEnabled(true);

        setFloor();

        TableColumn<Node, String> longName = new TableColumn<>("Node Name");
        longName.setCellValueFactory(new PropertyValueFactory<>("longNameText"));
        longName.prefWidthProperty().bind(edgeDisplayTableView.widthProperty().multiply(0.75));
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
                    EntityManager em = KioskApplication.getEntityManager();
                    em.getTransaction().begin();
                    selectedNode.removeNeighbor(node);
                    node.removeNeighbor(selectedNode);
                    em.merge(node);
                    em.merge(selectedNode);
                    em.remove(Edge.getEdge(selectedNode, node, em));
                    em.remove(Edge.getEdge(node, selectedNode, em));
                    em.getTransaction().commit();
                    displayEdges(selectedNode);
                    KioskApplication.getMapController().clearMap();
                    drawonMap(KioskApplication.getMapController().getMapWidth(),
                            KioskApplication.getMapController().getMapHeight());
                });
            }
        });
        delete.prefWidthProperty().bind(edgeDisplayTableView.widthProperty().multiply(0.25));
        edgeDisplayTableView.getColumns().addAll(longName, delete);

        drawonMap(KioskApplication.getMapController().getMapWidth()
                        + (KioskApplication.getMainScreenController()
                        .getRightAnchorPaneWidth() - 800),
                KioskApplication.getMapController().getMapHeight());
    }


    /**
     * Method to set the Nodes displayed by Floor.
     */
    public void setFloor() {
        if (!nodeSelectionVbox.getChildren().isEmpty()) {
            nodeSelectionVbox.getChildren().clear();
            toNodeSelectionVbox.getChildren().clear();
        }

        for (Node node : nodes) {
            if (node.getFloor().equals(KioskApplication.getFloor())) {
                Button btnSelect = new Button();
                btnSelect.setText(node.getLongName() + "\n" + node.getBuilding() + " - "
                        + node.getFloor());
                btnSelect.setId(node.getNodeId());
                btnSelect.setOnAction(this::nodeHandler);
                btnSelect.setMinWidth(260);
                btnSelect.setTextAlignment(TextAlignment.LEFT);

                nodeSelectionVbox.getChildren().add(btnSelect);

                Button btnToSelect = new Button();
                btnToSelect.setMinWidth(260);
                btnToSelect.setText(node.getLongName() + "\n" + node.getBuilding() + " - "
                        + node.getFloor());
                btnToSelect.setId(node.getNodeId());
                btnToSelect.setOnAction(this::toNodeHandler);
                btnToSelect.setTextAlignment(TextAlignment.LEFT);

                toNodeSelectionVbox.getChildren().add(btnToSelect);
            }
        }
    }

    @FXML
    private void nodeHandler(ActionEvent event) {
        Button btn = (Button) event.getSource();
        selectedNode = this.findNodeWithId(btn.getId());
        origNodeLabel.setText(selectedNode.getShortName());
        displayEdges(selectedNode);
    }

    @FXML
    private void toNodeHandler(ActionEvent event) {
        Button btn = (Button) event.getSource();
        toSelectedNode = this.findNodeWithId(btn.getId());
        termNodeLabel.setText(toSelectedNode.getShortName());

    }

    private void displayEdges(Node node) {
        ObservableList<Node> neighbors = FXCollections.observableArrayList();
        neighbors.addAll(node.getNeighbors());
        edgeDisplayTableView.setItems(neighbors);
    }

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

    @FXML
    private void createNewEdge(ActionEvent actionEvent) {
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
                displayEdges(selectedNode);
                KioskApplication.getMapController().clearMap();
                drawonMap(KioskApplication.getMapController().getMapWidth(),
                        KioskApplication.getMapController().getMapHeight());
            }
        }
    }


    @FXML
    private void goToEditNodes(ActionEvent actionEvent) throws IOException {
        AnchorPane editNodesRoot = FXMLLoader.load(getClass()
                .getResource("/layout/UI_SoftEng_EditNodes.fxml"));
        KioskApplication.switchPanel(mainAnchorPane, editNodesRoot);
    }

    @FXML
    private void downloadFile(ActionEvent actionEvent) {
        List<Node> graph = Node.getAll(KioskApplication.getEntityManager());
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter("NewEdges.csv"));
            writer.write("edgeID,startNode,endNode\n");
            for (int i = 0; i < graph.size(); i++) {
                for (int j = i + 1; j < graph.size(); j++) {
                    Node n1 = graph.get(i);
                    Node n2 = graph.get(j);
                    if (n1.getNeighbors().contains(n2)) {
                        writer.write(n1.getNodeId() + "_" + n2.getNodeId() + ","
                                + n1.getNodeId() + "," + n2.getNodeId() + "\n");
                    }
                }
            }
            writer.close();
        } catch (IOException exception) {
            System.out.println("Could not write to file.");
            exception.printStackTrace();
        }
    }

    /**
     * Method to enable the map and draw the nodes on map.
     * @param actionEvent toggle switch event to indicate that nodes should be drawn.
     */
    public void mapEnabler(ActionEvent actionEvent) {

        JFXToggleButton switcher = (JFXToggleButton) actionEvent.getSource();
        if (switcher == mapToggleButtonTerm) {
            mapEnabledTerm = !mapEnabledTerm;
        }
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
            for (int j = 0; j < current.getNeighbors().size(); j++) {
                Node neighbor = current.getNeighbors().get(j);
                if (neighbor.getFloor().equals(current.getFloor())) {
                    KioskApplication.getMapController()
                            .drawLineEditMap(current.getXCoord() * widthRatio,
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
            KioskApplication.getMapController()
                    .drawCircleIdHandler(current.getXCoord() * widthRatio,
                            current.getYCoord() * heightRatio, current.getFloor(),
                            crossFloor ? Color.GOLD : Color.BLACK,
                            current.getNodeId(), this::selectedFromMap,
                            this::draggedOnMap, this::releasedFromDrag);
        }

    }

    @FXML
    private void selectedFromMap(MouseEvent mouseEvent) {
        Circle circle = (Circle) mouseEvent.getSource();
        if (mapEnabledTerm) {
            toSelectedNode = findNodeWithId(circle.getId());
            termNodeLabel.setText(toSelectedNode.getShortName());

        } else {

            selectedNode = findNodeWithId(circle.getId());
            origNodeLabel.setText(selectedNode.getShortName());
            displayEdges(selectedNode);
        }
    }

    @FXML
    private void draggedOnMap(MouseEvent dragEvent) {

    }

    private void releasedFromDrag(MouseEvent mouseEvent) {

    }

}
