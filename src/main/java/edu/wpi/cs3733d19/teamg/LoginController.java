package edu.wpi.cs3733d19.teamg;

import edu.wpi.cs3733d19.teamg.models.Employee;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class LoginController extends Controller {

    @FXML
    public Label loginResponseLabel;
    public TextField employeeIdTextField;
    public TextField passwordTextField;
    public AnchorPane mainAnchorPane;

    private Runnable next;
    private boolean restrictedPage = false;

    public void setRestrictedPage(boolean restrictedPage) {
        this.restrictedPage = restrictedPage;
    }

    public void setNext(Runnable next) {
        this.next = next;
    }

    @FXML
    private void initialize() {
        KioskApplication.setController(this);
        Platform.runLater(() -> employeeIdTextField.requestFocus());
    }

    /**
     * Method to handle login and chance scenes.
     *
     * @param actionEvent actionEvent of selecting loginButton
     * @throws IOException IO exception if root load does not work
     */
    @FXML
    public void loginButtonHandler(ActionEvent actionEvent) throws IOException {
        Employee employee = null;
        if (!employeeIdTextField.getText().matches("\\d+")) {
            loginResponseLabel.setText("Invalid Employee ID");
            return;
        }
        int id = Integer.parseInt(employeeIdTextField.getText());
        for (Employee emp : Employee.getAll(KioskApplication.getEntityManager())) {
            if (emp.getId() == id) {
                employee = emp;
            }
        }
        if (employee == null) {
            loginResponseLabel.setText("Invalid Employee ID");
            return;
        }

        if (!employee.isEmployed()) {
            loginResponseLabel.setText("Employee is not authorized to use this system.");
            return;
        }

        if (employee.getPassword() == null || !employee.getPassword().equals(
                passwordTextField.getText())) {
            loginResponseLabel.setText("Invalid password.");
            return;
        }

        if (restrictedPage && !employee.isAdmin()) {
            loginResponseLabel.setText(
                    "Administrator privileges are required to access this page.");
            return;
        }
        loginResponseLabel.setText("");
        passwordTextField.setText("");
        employeeIdTextField.setText("");
        KioskApplication.setCurrentUser(employee);

        KioskApplication.getMainScreenController().setLoginName();

        next.run();
    }

}
