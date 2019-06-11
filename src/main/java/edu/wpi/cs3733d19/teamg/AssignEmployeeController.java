package edu.wpi.cs3733d19.teamg;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733d19.teamg.models.Employee;
import edu.wpi.cs3733d19.teamg.models.ServiceRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;

public class AssignEmployeeController extends Controller implements Initializable {

    @FXML
    public Label requestLabel;
    public Label employeeLabel;
    public Label assignedEmployeeLabel;
    private Employee selectedEmployee;
    private ServiceRequest selectedRequest;
    public Button assignEmployeeButton;
    public Button resolveRequestButton;
    public VBox serviceRequestVbox;
    public VBox selectEmployeeVbox;
    public TextArea serviceDetail;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setRequests();
        KioskApplication.setController(this);


        serviceDetail.setStyle("-fx-border-color: white;"
                + "-fx-text-inner-color: white;" + "-fx-border-width: 5;");

    }

    private void setRequests() {

        serviceRequestVbox.getChildren().clear();
        selectEmployeeVbox.getChildren().clear();

        for (ServiceRequest request : ServiceRequest.getAll(KioskApplication.getEntityManager())) {
            if (request.getTimeResolved() == null) {
                JFXButton btndest = new JFXButton();
                btndest.setText(request.getCategory().getTitle() + " - "
                                +  request.getLocation().getLongName() + " - "
                                + request.getIssue());
                btndest.setOnAction(this::selectedRequestHandler);
                btndest.setId(request.getId() + "");
                btndest.setPrefWidth(500);
                btndest.setTextAlignment(TextAlignment.LEFT);
                btndest.setAlignment(Pos.CENTER_LEFT);
                btndest.setWrapText(true);
                serviceRequestVbox.setAlignment(Pos.CENTER_LEFT);

                serviceRequestVbox.getChildren().add(btndest);
            }
        }
    }

    @FXML
    private void selectedRequestHandler(ActionEvent event) {
        Button btn = (JFXButton) event.getSource();
        int selectedId = Integer.parseInt(btn.getId());
        for (ServiceRequest s : ServiceRequest.getAll(KioskApplication.getEntityManager())) {
            if (s.getId() == selectedId) {
                selectedRequest = s;
            }
        }
        requestLabel.setText(btn.getText());
        setDetails();

        if (selectedRequest.getAssignee() != null) {
            assignedEmployeeLabel.setText("Currently assigned: "
                    + selectedRequest.getAssignee().getName());
        } else {
            assignedEmployeeLabel.setText("Currently assigned: none");
        }

        selectEmployeeVbox.getChildren().clear();

        for (Employee employee : Employee.getAll(KioskApplication.getEntityManager())) {
            if (employee.isEmployed() && employee.getCanService()
                    .contains(selectedRequest.getCategory())) {
                JFXButton btndest = new JFXButton();
                btndest.setText("" + employee.getName());
                btndest.setOnAction(this::selectedEmployeeHandler);
                btndest.setId(employee.getId() + "");
                btndest.setPrefWidth(300);
                btndest.setTextAlignment(TextAlignment.LEFT);
                btndest.setAlignment(Pos.CENTER_LEFT);
                selectEmployeeVbox.setAlignment(Pos.CENTER_LEFT);


                selectEmployeeVbox.getChildren().add(btndest);
            }

        }

    }

    /**
     * Display additional details of the service request.
     */
    private void setDetails() {
        serviceDetail.setText(selectedRequest.getDetails());
    }

    private void selectedEmployeeHandler(ActionEvent event) {
        Button btn = (JFXButton) event.getSource();
        for (Employee e : Employee.getAll(KioskApplication.getEntityManager())) {
            if (e.getId() == Integer.parseInt(btn.getId())) {
                selectedEmployee = e;
            }
        }
        employeeLabel.setText("Selected employee: "
                + btn.getText());
    }

    /**
     * assigns an employee to a request.
     *
     * @param actionEvent when the button is pressed.
     */
    @FXML
    public void assignEmployee(ActionEvent actionEvent) {
        if ((selectedRequest != null) && (selectedEmployee != null)) {
            KioskApplication.getEntityManager().getTransaction().begin();
            selectedRequest.setAssignee(selectedEmployee);
            KioskApplication.getEntityManager().merge(selectedRequest);
            KioskApplication.getEntityManager().getTransaction().commit();

            assignedEmployeeLabel.setText("Currently assigned: "
                    + selectedRequest.getAssignee().getName());

            if (selectedEmployee.getReceiveEmail()) {
                final String subject = "You have been assigned a service request";
                final String message = Email.generateServiceRequestMessage(selectedEmployee,
                        selectedRequest, "An administrator has assigned you to ");
                String addressee = selectedEmployee.getEmail();
                if (addressee == null) {
                    addressee = KioskApplication.DEFAULT_EMAIL;
                }
                final String finalAddressee = addressee;
                Executors.newSingleThreadExecutor().submit(() -> {
                    Email.sendEmail(subject, message, finalAddressee);
                });
            }
        }
    }

    /**
     * resolves a request.
     *
     * @param actionEvent when the button is pressed.
     */
    @FXML
    public void resolveRequest(ActionEvent actionEvent) {
        if ((selectedRequest != null)) {
            selectedRequest.setTimeResolved(new Date());
            selectedRequest = null;
            setRequests();

            if (selectedEmployee != null) {
                selectedEmployee = null;
            }

            requestLabel.setText("");
            employeeLabel.setText("");
            assignedEmployeeLabel.setText("");

        }
    }

}
