package edu.wpi.cs3733d19.teamg;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733d19.teamg.models.LanguageInterpreterRequest;
import edu.wpi.cs3733d19.teamg.models.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import javax.persistence.EntityManager;

public class LanguageInterpreterRequestController extends RequestController {
    String floor = "3";
    private Node selectedLocation = null;
    private String selectedLanguage = null;
    private boolean selectedPuLoc = false;
    private boolean selectedDoLoc = false;

    @FXML
    public VBox locationVbox;
    public Label errorLabel;
    public Label locationLabel;
    public VBox languageVbox;
    public JFXTextField locationTextField;
    public MenuButton languageBtn;


    @FXML
    private void initialize() {
        nodes = Node.getAll(KioskApplication.getEntityManager());

        KioskApplication.setController(this);

        setFloor();

        locationVbox.getChildren().clear();
        for (Node node : selectable) {
            JFXButton btndest = new JFXButton();
            btndest.setText(node.getShortName());
            btndest.setOnAction(this::locationHandler);
            btndest.setMinWidth(250);
            btndest.setTextAlignment(TextAlignment.LEFT);
            btndest.setId(node.getNodeId());

            locationVbox.getChildren().add(btndest);
        }

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

    @FXML
    private void locationvBoxHandler(ActionEvent event) {
        Button selectedButton = (Button) event.getSource();
        locationLabel.setText(selectedButton.getText());
        selectedPuLoc = true;
        selectedLocation = findNodeWithId(selectedButton.getId(), selectable);
    }

    @FXML
    private void languageBtn(ActionEvent event) {
        MenuItem selectedItem = (MenuItem) event.getSource();
        selectedDoLoc = true;
        selectedLanguage = selectedItem.getText();
        languageBtn.setText(selectedItem.getText());
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

        if (!selectedDoLoc) {
            errorLabel.setText("Please select a language.");
            return;
        }
        EntityManager manager = KioskApplication.getEntityManager();
        manager.getTransaction().begin();
        LanguageInterpreterRequest request = new LanguageInterpreterRequest(selectedLocation,
                KioskApplication.getCurrentUser(), new Date(),
                selectedLanguage, KioskApplication.getEntityManager());
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
