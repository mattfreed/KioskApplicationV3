package edu.wpi.cs3733d19.teamg;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.cs3733d19.teamg.models.FloralRequest;
import edu.wpi.cs3733d19.teamg.models.Node;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.persistence.EntityManager;


public class FloralRequestController extends RequestController {
    String floor = "3";
    private Node selectedLocation = null;

    @FXML
    public VBox locationVbox;
    public Label errorLabel;
    public Label locationLabel;
    public ColorPicker colorPicker;
    public TextField numTextField;
    public JFXTimePicker timePicker;
    public JFXDatePicker datePicker;


    @FXML
    private void initialize() {
        KioskApplication.setController(this);

        numTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    numTextField.setText(newValue.replaceAll("\\D", ""));
                }
            }
        });

        nodes = Node.getAll(KioskApplication.getEntityManager());

        setFloor();

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
        locationLabel.setText("Location: " + selectedLocation.getShortName());
    }

    @FXML
    private void submitRequest(ActionEvent event) throws IOException {
        if (selectedLocation == null) {
            errorLabel.setText("Please select a location.");
            return;
        }
        if (numTextField.getText().length() == 0) {
            errorLabel.setText("Please enter a quantity.");
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
        Date deliveryDate = Date.from(LocalDateTime.of(datePicker.getValue(),
                timePicker.getValue()).atZone(ZoneId.systemDefault()).toInstant());
        FloralRequest request = new FloralRequest(selectedLocation,
                KioskApplication.getCurrentUser(), new Date(), colorPicker.getValue(),
                Integer.parseInt(numTextField.getText()), deliveryDate, manager);
        KioskApplication.getCurrentUser().getServiceRequests().add(request);
        KioskApplication.getEntityManager().merge(KioskApplication.getCurrentUser());
        manager.persist(request);
        manager.getTransaction().commit();

        AnchorPane serviceRequestRoot = FXMLLoader.load(getClass()
                .getResource("/layout/UI_SoftEng_ServiceRequest.fxml"));
        KioskApplication.getMainScreenController().setRightPane(serviceRequestRoot);
    }

}
