package edu.wpi.cs3733d19.teamg;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733d19.teamg.models.Node;
import floral.api.FloralApi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.util.Collections;

public class FloralSelectRoomController extends RequestController {
    String floor = "3";
    private Node selectedLocation = null;

    @FXML
    public JFXTextField locationTextField;
    public VBox locationVbox;
    public Label errorLabel;
    public Button submitServiceButton;


    @FXML
    private void initialize() {
        nodes = Node.getAll(KioskApplication.getEntityManager());

        KioskApplication.setController(this);

        setFloor();

        locationTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<javafx.scene.Node> children = FXCollections.observableArrayList(
                    locationVbox.getChildren());
            Collections.sort(children, new FuzzySearchComparator(locationTextField
                    .getText(), node -> findNodeWithId(node.getId(), nodes).getShortName()));
            locationVbox.getChildren().setAll(children);
        });

        selectedLocation = KioskApplication.getDefaultLocation();
        if (selectedLocation != null) {
            locationTextField.setText(KioskApplication.getDefaultLocation().getShortName());
        }
    }

    /**
     * Method to set the Nodes displayed by Floor.
     */
    public void setFloor() {
        if (!locationVbox.getChildren().isEmpty()) {
            onFloor.clear();
            selectable.clear();
            locationVbox.getChildren().clear();
        }

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
    void locationHandler(ActionEvent event) {
        Button selectedButton = (Button) event.getSource();
        locationTextField.setText(selectedButton.getText());
        selectedLocation = findNodeWithId(selectedButton.getId());
    }

    private Node findNodeWithId(String idNode) {
        Node myNode = null;
        for (Node numNode : selectable) {
            if (idNode.equals(numNode.getNodeId())) {
                myNode = numNode;
                break;
            }
        }
        return myNode;
    }

    /**
     * Method to test if request is complete and submit.
     *
     * @param actionEvent button selection to trigger request submit.
     */
    public void submitRequest(ActionEvent actionEvent) throws IOException {
        if (selectedLocation == null) {
            errorLabel.setText("Please Select a pickup location");
        } else {
            FloralApi floralapi = new FloralApi();
            try {
                floralapi.run(0, 0, "default.css", selectedLocation.getLongName());
            } catch (Exception exception) {
                System.out.println("Failed to run API");
                exception.printStackTrace();
            }
        }
    }
}
