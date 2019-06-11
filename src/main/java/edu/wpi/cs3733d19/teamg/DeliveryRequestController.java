package edu.wpi.cs3733d19.teamg;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.cs3733d19.teamg.models.DeliveryRequest;
import edu.wpi.cs3733d19.teamg.models.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;

public class DeliveryRequestController extends RequestController {
    String floor = "3";
    private Node selectedPuLoc = null;
    private Node selectedDoLoc = null;

    @FXML
    public JFXTextField pickupLocTextField;
    public VBox pickupLocVbox;
    public JFXDatePicker pickupDatePicker;
    public JFXTimePicker pickupTimePicker;
    public JFXTextField dropoffLocTextField;
    public VBox dropoffLocVbox;
    public JFXDatePicker dropoffDatePicker;
    public JFXTimePicker dropoffTimePicker;
    public TextField reasoningTextField;
    public Label errorLabel;
    public Button submitServiceButton;


    @FXML
    private void initialize() {
        nodes = Node.getAll(KioskApplication.getEntityManager());

        KioskApplication.setController(this);

        setFloor();

        pickupLocTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<javafx.scene.Node> children = FXCollections.observableArrayList(
                    pickupLocVbox.getChildren());
            Collections.sort(children, new FuzzySearchComparator(pickupLocTextField
                    .getText(),node -> findNodeWithId(node.getId(), nodes).getShortName()));
            pickupLocVbox.getChildren().setAll(children);
        });

        dropoffLocTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<javafx.scene.Node> children = FXCollections.observableArrayList(
                    dropoffLocVbox.getChildren());
            Collections.sort(children, new FuzzySearchComparator(dropoffLocTextField
                    .getText(),node -> findNodeWithId(node.getId(), nodes).getShortName()));
            dropoffLocVbox.getChildren().setAll(children);
        });

        selectedPuLoc = KioskApplication.getDefaultLocation();
        if (selectedPuLoc != null) {
            pickupLocTextField.setText(KioskApplication.getDefaultLocation().getShortName());
        }
    }


    /**
     * Method to set the Nodes displayed by Floor.
     */
    public void setFloor() {
        if (!pickupLocVbox.getChildren().isEmpty()) {
            onFloor.clear();
            selectable.clear();
            pickupLocVbox.getChildren().clear();
            dropoffLocVbox.getChildren().clear();
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

            JFXButton btnstart = new JFXButton();
            btnstart.setText(node.getShortName());
            btnstart.setOnAction(this::dropoffLocationHandler);
            btnstart.setMinWidth(250);
            btnstart.setTextAlignment(TextAlignment.LEFT);
            btnstart.setId(node.getNodeId());

            pickupLocVbox.getChildren().add(btndest);
            dropoffLocVbox.getChildren().add(btnstart);
        }
    }

    @FXML
    void locationHandler(ActionEvent event) {
        Button selectedButton = (Button) event.getSource();
        pickupLocTextField.setText(selectedButton.getText());
        selectedPuLoc = findNodeWithId(selectedButton.getId());
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

    @FXML
    private void dropoffLocationHandler(ActionEvent event) {
        Button selectedButton = (Button) event.getSource();
        dropoffLocTextField.setText(selectedButton.getText());
        selectedDoLoc = findNodeWithId(selectedButton.getId());
    }

    /**
     * Method to test if request is complete and submit.
     * @param actionEvent   button selection to trigger request submit.
     */
    public void submitRequest(ActionEvent actionEvent) throws IOException {

        LocalDate pickupDate = pickupDatePicker.getValue();
        LocalDate dropoffDate = dropoffDatePicker.getValue();
        LocalTime pickupTime = pickupTimePicker.getValue();
        LocalTime dropoffTime = pickupTimePicker.getValue();

        if (selectedPuLoc == null) {
            errorLabel.setText("Please Select a pickup location");
        } else if (selectedDoLoc == null) {
            errorLabel.setText("Please Select a dropoff location");
        } else if (pickupDate == null || pickupTime == null) {
            errorLabel.setText("Please Select a pickup date and time");
        } else if (dropoffDate == null || dropoffTime == null) {
            errorLabel.setText("Please Select a dropoff date and time");
        } else {
            Date pickup = Date.from(pickupTime.atDate(pickupDate)
                    .atZone(ZoneId.systemDefault()).toInstant());
            Date dropoff = Date.from(dropoffTime.atDate(dropoffDate)
                    .atZone(ZoneId.systemDefault()).toInstant());

            if (pickup.compareTo(dropoff) > 0) {
                errorLabel.setText("Please Select a valid dropoff time");
            } else {
                //make delivery request here
                KioskApplication.getEntityManager().getTransaction().begin();
                DeliveryRequest deliveryRequest = new DeliveryRequest(selectedDoLoc,
                        KioskApplication.getCurrentUser(), new Date(), reasoningTextField.getText(),
                        selectedPuLoc, pickup, dropoff, KioskApplication.getEntityManager());
                KioskApplication.getCurrentUser().getServiceRequests().add(deliveryRequest);
                KioskApplication.getEntityManager().merge(KioskApplication.getCurrentUser());
                KioskApplication.getEntityManager().persist(deliveryRequest);
                KioskApplication.getEntityManager().getTransaction().commit();

                KioskApplication.getHistory().push(() -> {
                    try {
                        KioskApplication.getMainScreenController().changeToService(null);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

                AnchorPane serviceRequestRoot = FXMLLoader.load(getClass()
                        .getResource("/layout/UI_SoftEng_ServiceRequest.fxml"));
                KioskApplication.getMainScreenController().setRightPane(serviceRequestRoot);
            }
        }
    }
}
