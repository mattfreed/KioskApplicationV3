package edu.wpi.cs3733d19.teamg;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class ChangePathfindingAlgorithmController extends Controller {

    @FXML
    public Button astarButton;
    public Button bfsButton;
    public Button dfsButton;
    public Button dijkstraButton;
    public Button confirmButton;
    public Label currentlySelectedLabel;
    public Button bestButton;

    private String selectedMode;

    @FXML
    private void initialize() {
        KioskApplication.setController(this);

        String mode = (String) Settings.getSettings().get("pathfinding-mode");
        currentlySelectedLabel.setText("Currently using: " + mode);
    }

    /**
     * Changes the selected algorithm buffer to a*.
     *
     * @param actionEvent on button pressed.
     * @throws IOException should not happen.
     */
    @FXML
    public void useAstar(ActionEvent actionEvent) throws IOException {

        currentlySelectedLabel.setText("Selected: A*");
        selectedMode = "astar";

    }

    /**
     * Changes the selected algorithm buffer to DFS.
     *
     * @param actionEvent on button pressed.
     */
    @FXML
    public void useDfs(ActionEvent actionEvent) {
        currentlySelectedLabel.setText("Selected: DFS");
        selectedMode = "dfs";
    }

    /**
     * Changes the selected algorithm buffer to BFS.
     *
     * @param actionEvent on button pressed.
     * @throws IOException should not happen.
     */
    @FXML
    public void useBfs(ActionEvent actionEvent) throws IOException {

        currentlySelectedLabel.setText("Selected: BFS");
        selectedMode = "bfs";

    }

    /**
     * Changes the selected algorithm buffer to Dijkstra.
     *
     * @param actionEvent on button pressed.
     * @throws IOException should not happen.
     */
    @FXML
    public void useDijkstra(ActionEvent actionEvent) throws IOException {

        currentlySelectedLabel.setText("Selected: Dijkstra");
        selectedMode = "dijkstra";

    }

    /**
     * Changes the selected algorithm buffer to best first.
     *
     * @param actionEvent on button pressed.
     * @throws IOException should not happen.
     */
    @FXML
    public void useBestFirst(ActionEvent actionEvent) throws IOException {

        currentlySelectedLabel.setText("Selected: Best First");
        selectedMode = "best first";

    }

    /**
     * Changes the pathfinding algorithm for the application to the buffer.
     *
     * @param actionEvent on button pressed.
     * @throws IOException should not happen.
     */
    @FXML
    public void confirmAlgorithm(ActionEvent actionEvent) throws IOException {

        if (selectedMode != null) {
            Settings.getSettings().set("pathfinding-mode", selectedMode);
        }

        String mode = (String) Settings.getSettings().get("pathfinding-mode");
        currentlySelectedLabel.setText("Currently using: " + mode);
    }

}
