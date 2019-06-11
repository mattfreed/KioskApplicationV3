package edu.wpi.cs3733d19.teamg;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Date;

public class AdminMenuController extends Controller {

    @FXML
    public Button assignEmployeeButton;
    public Button editMapButton;
    public Button statisticsButton;
    public Pane pane;
    public AnchorPane mainAnchorPane;
    public TextField timeoutTextField;
    public MenuButton changePathfindingButton;

    @FXML
    private void initialize() {
        KioskApplication.setController(this);
        timeoutTextField.setText(KioskApplication.getTimeout() / 1000 + "");
        timeoutTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            timeoutTextField.setText(timeoutTextField.getText().replaceAll("\\D", ""));
        }));

        String mode = (String) Settings.getSettings().get("pathfinding-mode");
        changePathfindingButton.setText("Currently: " + mode);
    }

    @FXML
    private void changePathfindingAlgorithm(ActionEvent event) {
        MenuItem selectedItem = (MenuItem) event.getSource();
        String selectedAlgorithm = selectedItem.getText();
        String selectedMode = "";

        switch (selectedAlgorithm) {
            case "A*":
                selectedMode = "astar";
                break;
            case "DFS":
                selectedMode = "dfs";
                break;
            case "BFS":
                selectedMode = "bfs";
                break;
            case "Dijkstra":
                selectedMode = "dijkstra";
                break;
            case "Best First":
                selectedMode = "best first";
                break;
            default:
                break;
        }

        if (selectedMode != "") {
            Settings.getSettings().set("pathfinding-mode", selectedMode);
        }
        String mode = (String) Settings.getSettings().get("pathfinding-mode");
        changePathfindingButton.setText("Currently: " + mode);
    }

    /**
     * Changes the screen to the Assign Employee screen.
     *
     * @param actionEvent when the button is pressed.
     * @throws IOException will not be thrown; scene must exist.
     */
    @FXML
    public void changeToAssignEmployee(ActionEvent actionEvent) throws IOException {
        KioskApplication.getHistory().push(() -> {
            try {
                KioskApplication.getMainScreenController().goToAdmin(null);
                changeToAssignEmployee(null);
            } catch (IOException ex) {
                //wont be thrown
            }
        });
        AnchorPane assignEmployeeRoot = FXMLLoader.load(getClass()
                .getResource("/layout/UI_SoftEng_AssignEmployee.fxml"));
        KioskApplication.getMainScreenController().setLeftPane(assignEmployeeRoot);
        KioskApplication.getMainScreenController().setRightPane(null);
    }

    /**
     * Changes the screen to Edit Map (when it is implemented), currently goes to Main Menu.
     *
     * @param actionEvent when the button is pressed.
     * @throws IOException will not be thrown; scene must exist.
     */
    @FXML
    public void changeToEditMap(ActionEvent actionEvent) throws IOException {
        KioskApplication.getHistory().push(() -> {
            try {
                KioskApplication.getMainScreenController().goToAdmin(null);
                changeToEditMap(null);
            } catch (IOException ex) {
                //wont be thrown
            }
        });
        AnchorPane editMapRoot = FXMLLoader.load(getClass()
                .getResource("/layout/UI_SoftEng_EditMap.fxml"));
        KioskApplication.getMainScreenController().setLeftPane(editMapRoot);
        KioskApplication.getMainScreenController().setRightPane(null);
    }

    /**
     * Chages the screen to Edit Employees.
     *
     * @param actionEvent the button being pressed
     * @throws IOException will not be thrown, the scene does exist
     */
    @FXML
    public void changeToEditEmployee(ActionEvent actionEvent) throws IOException {
        KioskApplication.getHistory().push(() -> {
            try {
                KioskApplication.getMainScreenController().goToAdmin(null);
                changeToEditEmployee(null);
            } catch (IOException ex) {
                //wont be thrown
            }
        });
        AnchorPane editEmployeeRoot = FXMLLoader.load(getClass().getResource(
                "/layout/UI_SoftEng_EditEmployee.fxml"));
        KioskApplication.getMainScreenController().setLeftPane(editEmployeeRoot);
        KioskApplication.getMainScreenController().setRightPane(null);
    }

    @FXML
    private void timeoutKeyPressed(KeyEvent event) {
        if (event.getCode().getName().equals("Enter")) {
            int newTimeout = Integer.parseInt(timeoutTextField.getText());
            if (newTimeout > 0) {
                KioskApplication.setTimeout(newTimeout * 1000);
            } else {
                timeoutTextField.setText(KioskApplication.getTimeout() / 1000 + "");
            }
        }
    }

    @FXML
    private void goToStatistics(ActionEvent event) throws IOException {
        KioskApplication.getHistory().push(() -> {
            try {
                KioskApplication.getMainScreenController().goToAdmin(null);
                goToStatistics(null);
            } catch (IOException ex) {
                //wont be thrown
            }
        });
        AnchorPane statisticsRoot = FXMLLoader.load(getClass().getResource(
                "/layout/UI_SoftEng_Statistics.fxml"));
        KioskApplication.getMainScreenController().setLeftPane(statisticsRoot);
        KioskApplication.getMainScreenController().setRightPane(null);
    }

}
