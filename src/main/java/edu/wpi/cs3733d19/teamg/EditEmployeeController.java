package edu.wpi.cs3733d19.teamg;

import edu.wpi.cs3733d19.teamg.models.Employee;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.io.IOException;


public class EditEmployeeController extends Controller {

    @FXML
    private TableView tableView;
    @FXML
    private TextField employeeIdTextField;
    @FXML
    private Label errorLabel;

    @FXML
    private void initialize() {
        KioskApplication.setController(this);
        employeeIdTextField.textProperty().addListener(((observable, oldValue, newValue) ->
                employeeIdTextField.setText(employeeIdTextField.getText().replaceAll("\\D", ""))));
        TableColumn<Employee, Integer> employeeId = new TableColumn<>("Employee ID");
        employeeId.setCellValueFactory(new PropertyValueFactory<>("id"));
        employeeId.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2125));
        TableColumn<Employee, String> firstName = new TableColumn<>("First Name");
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        firstName.setCellFactory(TextFieldTableCell.forTableColumn());
        firstName.setId("firstName");
        firstName.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2125));
        firstName.setOnEditCommit(this::changedField);
        TableColumn<Employee, String> lastName = new TableColumn<>("Last Name");
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        lastName.setCellFactory(TextFieldTableCell.forTableColumn());
        lastName.setId("lastName");
        lastName.setOnEditCommit(this::changedField);
        lastName.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2125));
        TableColumn<Employee, String> email = new TableColumn<>("Email");
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        email.setCellFactory(TextFieldTableCell.forTableColumn());
        email.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2125));
        TableColumn<Employee, Employee> editServices = new TableColumn<>("Services");
        editServices.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue()));
        editServices.setCellFactory(x -> new TableCell<Employee, Employee>() {
            private final Button editButton = new Button("Edit");

            @Override
            protected void updateItem(Employee employee, boolean empty) {
                super.updateItem(employee, empty);

                if (employee == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(editButton);
                editButton.setOnAction(event -> {
                    try {
                        KioskApplication.setEmployeeSelected(employee);
                        Parent root =
                                FXMLLoader.load(getClass().getResource(
                                        "/layout/UI_SoftEng_EditServicesPopup.fxml"));
                        Scene scene = new Scene(root);

                        Stage stage = new Stage();
                        stage.setTitle("Edit Services");
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException exception) {
                        //this won't happen the file exists
                    }
                });
            }
        });
        editServices.prefWidthProperty().bind(tableView.widthProperty().multiply(0.10));
        TableColumn<Employee, Employee> isEmployed
                = new TableColumn<>("Active");
        isEmployed.setCellValueFactory(x -> new ReadOnlyObjectWrapper(x.getValue()));
        isEmployed.setCellFactory(x -> new TableCell<Employee, Employee>() {
            private final CheckBox toggleCanService = new CheckBox();

            @Override
            protected void updateItem(Employee employee, boolean empty) {
                super.updateItem(employee, empty);
                if (employee == null) {
                    setGraphic(null);
                    return;
                }
                toggleCanService.setSelected(employee.isEmployed());
                toggleCanService.setOnAction(event -> {
                    employee.setEmployed(toggleCanService.isSelected());
                    KioskApplication.getEntityManager().getTransaction().begin();
                    KioskApplication.getEntityManager().merge(employee);
                    KioskApplication.getEntityManager().getTransaction().commit();
                });
                setGraphic(toggleCanService);
            }
        });
        isEmployed.prefWidthProperty().bind(tableView.widthProperty().multiply(0.05));

        tableView.getColumns().addAll(employeeId, firstName,
                lastName, email, editServices, isEmployed);

        ObservableList employees = FXCollections.observableArrayList();
        employees.addAll(Employee.getAll(KioskApplication.getEntityManager()));
        tableView.setItems(employees);
    }

    /**
     * Handler for when the there is a edit to the employee info tableView.
     *
     * @param event the event of the change
     */
    private void changedField(Event event) {
        int employeeId = (Integer) ((TableColumn) tableView.getColumns().get(0))
                .getCellData(tableView.getEditingCell().getRow());
        Employee editingEmployee = null;
        for (Employee e : Employee.getAll(KioskApplication.getEntityManager())) {
            if (e.getId() == employeeId) {
                editingEmployee = e;
            }
        }
        String newValue = (String) ((TableColumn.CellEditEvent<String, Object>) event)
                .getNewValue();

        if (editingEmployee != null) {
            switch (tableView.getEditingCell().getTableColumn().getId()) {
                case "firstName":
                    editingEmployee.setFirstName(newValue);
                    break;
                case "lastName":
                    editingEmployee.setLastName(newValue);
                    break;
                default:
                    break;
            }
            KioskApplication.getEntityManager().getTransaction().begin();
            KioskApplication.getEntityManager().merge(editingEmployee);
            KioskApplication.getEntityManager().getTransaction().commit();
        }
    }

    @FXML
    private void addEmployee(ActionEvent event) {
        if (employeeIdTextField.getText().isEmpty()) {
            errorLabel.setText("Employee ID is required.");
            return;
        }
        int id = Integer.parseInt(employeeIdTextField.getText());
        for (Employee employee : Employee.getAll(KioskApplication.getEntityManager())) {
            if (employee.getId() == id) {
                errorLabel.setText("Employee ID in use.");
                return;
            }
        }
        KioskApplication.getEntityManager().getTransaction().begin();
        Employee employee = new Employee(id, "", "", true, false, null, null, false, false);
        KioskApplication.getEntityManager().persist(employee);
        KioskApplication.getEntityManager().getTransaction().commit();
        errorLabel.setText("Employee added with ID: " + id);
        employeeIdTextField.setText("");
        tableView.getItems().add(employee);
    }

}
