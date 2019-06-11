package edu.wpi.cs3733d19.teamg;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733d19.teamg.models.Employee;
import edu.wpi.cs3733d19.teamg.models.Node;
import edu.wpi.cs3733d19.teamg.models.SecurityRequest;
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
import java.util.Collections;
import java.util.Date;
import javax.persistence.EntityManager;

public class SecurityRequestController extends RequestController {
    String floor = "3";
    private Node selectedLocation = null;

    @FXML
    public VBox locationVbox;
    public JFXTextField locationTextField;
    public Label errorLabel;
    public JFXTextField reasonTextField;

    @FXML
    private void initialize() {
        nodes = Node.getAll(KioskApplication.getEntityManager());

        KioskApplication.setController(this);

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

    @Override
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
        locationTextField.setText(selectedButton.getText());
        selectedLocation = findNodeWithId(selectedButton.getId(), selectable);
    }

    /**
     * Method to test if request is complete and submit.
     * @param actionEvent   button selection to trigger request submit.
     */
    public void submitRequest(ActionEvent actionEvent) throws IOException {
        if (selectedLocation == null) {
            errorLabel.setText("Please Select a Destination");
        } else {
            EntityManager em = KioskApplication.getEntityManager();
            em.getTransaction().begin();
            Employee requester = KioskApplication.getCurrentUser();
            SecurityRequest request =
                    new SecurityRequest(selectedLocation, requester, new Date(),
                            reasonTextField.getText(), em);
            KioskApplication.getCurrentUser().getServiceRequests().add(request);
            KioskApplication.getEntityManager().merge(KioskApplication.getCurrentUser());
            em.persist(request);
            em.getTransaction().commit();
            errorLabel.setText("");

            reasonTextField.setText("");

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
