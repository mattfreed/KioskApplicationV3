package edu.wpi.cs3733d19.teamg;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733d19.teamg.models.Node;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;

public abstract class RequestController extends Controller {

    public List<Node> nodes = new ArrayList<>();
    protected List<Node> onFloor = new ArrayList<>();
    protected List<Node> selectable = new ArrayList<>();

    public VBox locationVbox;

    protected Node findNodeWithId(String idNode, List<Node> list) {
        Node fin = null;
        for (Node numNode : list) {
            if (idNode.equals(numNode.getNodeId())) {
                fin = numNode;
                break;
            }
        }
        return fin;
    }

    /**
     * Sets the available nodes to only nodes on the current floorText.
     */
    public void setFloor() {
        for (Node node : nodes) {
            if (node.getFloor().equals(KioskApplication.getFloor())) {
                onFloor.add(node);
            }
        }
        for (Node node : onFloor) {
            if (!node.getNodeType().trim().equals("HALL")) {
                selectable.add(node);
            }
        }

        for (Node node : selectable) {
            JFXButton btndest = new JFXButton();
            btndest.setText(node.getShortName());
            btndest.setOnAction(this::locationHandler);
            btndest.setMinWidth(250);
            btndest.setTextAlignment(TextAlignment.LEFT);
            btndest.setId(node.getNodeId());

            locationVbox.getChildren().add(btndest);
        }
    }

    @FXML
    abstract void locationHandler(ActionEvent event);
}
