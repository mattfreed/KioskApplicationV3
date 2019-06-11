package edu.wpi.cs3733d19.teamg;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.cs3733d19.teamg.models.Node;
import edu.wpi.cs3733d19.teamg.models.ReligiousRequest;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import javax.persistence.EntityManager;

public class ReligiousRequestController extends RequestController {
    String floor = "3";
    private Node selectedLocation = null;

    @FXML
    public VBox locationVbox;
    public Label errorLabel;
    public Label locationLabel;
    public JFXTextField religionTextField;
    public JFXTextField serviceTextField;
    public JFXDatePicker datePicker;
    public JFXTimePicker timePicker;
    public JFXTextField locationTextField;

    @FXML
    private void initialize() {
        KioskApplication.setController(this);

        nodes = Node.getAll(KioskApplication.getEntityManager());

        setFloor();

        locationTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<javafx.scene.Node> children = FXCollections.observableArrayList(
                    locationVbox.getChildren());
            Collections.sort(children, new FuzzySearchComparator(locationTextField
                    .getText(),node -> findNodeWithId(node.getId(), nodes).getShortName()));
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

        super.setFloor();
    }

    @FXML
    void locationHandler(ActionEvent event) {
        Button selectedButton = (Button) event.getSource();
        selectedLocation = findNodeWithId(selectedButton.getId(), selectable);
        locationTextField.setText(selectedButton.getText());
        locationLabel.setText("Location: " + selectedLocation.getShortName());
    }

    @FXML
    private void submitRequest(ActionEvent event) throws IOException {
        if (selectedLocation == null) {
            errorLabel.setText("Please select a location.");
            return;
        }
        if (religionTextField.getText().length() == 0) {
            errorLabel.setText("Please enter a religion.");
            return;
        }
        if (serviceTextField.getText().length() == 0) {
            errorLabel.setText("Please enter a service.");
            return;
        }
        if (datePicker.getValue() == null) {
            errorLabel.setText("Please select a date.");
            return;
        }
        if (timePicker.getValue() == null) {
            errorLabel.setText("Please select a time.");
            return;
        }
        EntityManager manager = KioskApplication.getEntityManager();
        manager.getTransaction().begin();
        Date serviceDate = Date.from(LocalDateTime.of(datePicker.getValue(),
                timePicker.getValue()).atZone(ZoneId.systemDefault()).toInstant());
        ReligiousRequest request = new ReligiousRequest(selectedLocation,
                KioskApplication.getCurrentUser(), new Date(), religionTextField.getText(),
                serviceTextField.getText(), serviceDate, manager);
        KioskApplication.getCurrentUser().getServiceRequests().add(request);
        KioskApplication.getEntityManager().merge(KioskApplication.getCurrentUser());
        manager.persist(request);
        manager.getTransaction().commit();

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
