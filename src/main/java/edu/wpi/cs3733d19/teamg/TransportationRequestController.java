package edu.wpi.cs3733d19.teamg;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.cs3733d19.teamg.models.Node;
import edu.wpi.cs3733d19.teamg.models.TransportationRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

public class TransportationRequestController extends RequestController {
    String floor = "3";
    private Node selectedLocation = null;


    @FXML
    public JFXTextField pickupLocTextField;
    public JFXTextField notesTextField;
    public VBox locationVbox;
    public JFXDatePicker pickupDatePicker;
    public JFXTimePicker pickupTimePicker;
    public Label errorLabel;
    public Button submitServiceButton;


    @FXML
    private void initialize() {
        nodes = Node.getAll(KioskApplication.getEntityManager());

        KioskApplication.setController(this);

        setFloor();

        pickupLocTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<javafx.scene.Node> children = FXCollections.observableArrayList(
                    locationVbox.getChildren());
            Collections.sort(children, new FuzzySearchComparator(pickupLocTextField
                    .getText(), node -> findNodeWithId(node.getId(), nodes).getShortName()));
            locationVbox.getChildren().setAll(children);
        });

        selectedLocation = KioskApplication.getDefaultLocation();
        if (selectedLocation != null) {
            pickupLocTextField.setText(KioskApplication.getDefaultLocation().getShortName());
        }
    }


    /**
     * Method to set the Nodes displayed by floorText.
     */
    public void setFloor() {
        if (!locationVbox.getChildren().isEmpty()) {
            onFloor.clear();
            selectable.clear();
            locationVbox.getChildren().clear();
        }

        super.setFloor();
    }

    @FXML
    void locationHandler(ActionEvent event) {
        Button selectedButton = (Button) event.getSource();
        pickupLocTextField.setText(selectedButton.getText());
        selectedLocation = findNodeWithId(selectedButton.getId(), selectable);
    }

    /**
     * Method to test if request is complete and submit.
     *
     * @param actionEvent button selection to trigger request submit.
     */
    public void submitRequest(ActionEvent actionEvent) throws IOException {

        LocalDate pickupDate = pickupDatePicker.getValue();
        LocalTime pickupTime = pickupTimePicker.getValue();

        if (selectedLocation == null) {
            errorLabel.setText("Please Select a pickup location");
            return;
        }
        if (pickupDate == null || pickupTime == null) {
            errorLabel.setText("Please Select a pickup date and time");
            return;
        }

        EntityManager entityManager = KioskApplication.getEntityManager();
        entityManager.getTransaction().begin();
        Date pickup = Date.from(LocalDateTime.of(pickupDate,
                pickupTime).atZone(ZoneId.systemDefault()).toInstant());
        TransportationRequest request = new TransportationRequest(selectedLocation,
                KioskApplication.getCurrentUser(), new Date(), notesTextField.getText(),
                pickup, entityManager);
        KioskApplication.getCurrentUser().getServiceRequests().add(request);
        KioskApplication.getEntityManager().merge(KioskApplication.getCurrentUser());
        entityManager.persist(request);
        entityManager.getTransaction().commit();

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
