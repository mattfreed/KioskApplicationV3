package edu.wpi.cs3733d19.teamg;

import edu.wpi.cs3733d19.teamg.Controller;
import edu.wpi.cs3733d19.teamg.KioskApplication;
import javafx.fxml.FXML;

public class CreditsController extends Controller {
    @FXML
    private void initialize() {
        KioskApplication.setController(this);
    }

}
