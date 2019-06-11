package edu.wpi.cs3733d19.teamg;

import edu.wpi.cs3733d19.teamg.models.ServiceRequestCategory;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class EditServicePopupController {
    @FXML
    public TableView tableView;
    public Label employeeNameLabel;

    @FXML
    private void initialize() {
        TableColumn<ServiceRequestCategory, String> service = new TableColumn<>("Service");
        service.setCellValueFactory(serviceRequest -> new ReadOnlyStringWrapper(serviceRequest
                .getValue().getTitle()));
        TableColumn<ServiceRequestCategory, ServiceRequestCategory> checkBoxes
                = new TableColumn<>("Can be assigned");
        checkBoxes.setCellValueFactory(x -> new ReadOnlyObjectWrapper(x.getValue()));
        checkBoxes.setCellFactory(x ->
                new TableCell<ServiceRequestCategory, ServiceRequestCategory>() {
            private final CheckBox toggleCanService = new CheckBox();

            @Override
            protected void updateItem(ServiceRequestCategory serviceRequest, boolean empty) {
                super.updateItem(serviceRequest, empty);
                if (serviceRequest == null) {
                    setGraphic(null);
                    return;
                }
                toggleCanService.setSelected(KioskApplication.getEmployeeSelected().getCanService()
                        .contains(serviceRequest));
                toggleCanService.setOnAction(event -> {
                    if (toggleCanService.selectedProperty().getValue()) {
                        KioskApplication.getEmployeeSelected().addCanService(serviceRequest);
                    } else {
                        KioskApplication.getEmployeeSelected().removeCanService(serviceRequest);
                    }
                    KioskApplication.getEntityManager().getTransaction().begin();
                    KioskApplication.getEntityManager().merge(KioskApplication.getCurrentUser());
                    KioskApplication.getEntityManager().getTransaction().commit();
                });
                setGraphic(toggleCanService);
            }
        });
        tableView.getColumns().addAll(service, checkBoxes);
        ObservableList<ServiceRequestCategory> requests = FXCollections.observableArrayList();
        requests.addAll(ServiceRequestCategory.getAll(KioskApplication.getEntityManager()));
        tableView.setItems(requests);

        employeeNameLabel.setText(KioskApplication.getEmployeeSelected().getName());
    }
}
