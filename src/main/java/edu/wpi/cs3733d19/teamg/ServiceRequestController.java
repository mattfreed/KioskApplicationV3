package edu.wpi.cs3733d19.teamg;

import bishopfishapi.Emergency;
import edu.wpi.cs3733d19.teamg.models.Employee;
import floral.api.FloralApi;
import foodRequest.FoodRequest;
import imaging.ImagingRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;


public class ServiceRequestController extends Controller {

    @FXML
    public Button maintenanceRequestButton;
    public Button custodianRequestButton;
    public Button deliveryRequestButton;
    public Button itRequestButton;
    public Button religiousRequestButton;
    public Button transportationRequestButton;
    public Button languageInterpreterRequestButton;
    public Button floralRequestButton;
    public Button securityRequestButton;
    public Pane pane;
    public AnchorPane mainAnchorPane;

    @FXML
    private void initialize() {
        KioskApplication.setController(this);
    }


    /**
     * Changes the screen to the Maintenance Request screen.
     *
     * @param actionEvent when the button is pressed.
     * @throws IOException will not be thrown; scene must exist.
     */
    @FXML
    public void changeToMaintenanceRequest(ActionEvent actionEvent) throws IOException {
        KioskApplication.getHistory().push(() -> {
            try {
                KioskApplication.getMainScreenController().changeToService(null);
                changeToMaintenanceRequest(null);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        AnchorPane maintenanceRequestRoot = FXMLLoader.load(getClass()
                .getResource("/layout/UI_SoftEng_MaintenanceRequest.fxml"));
        KioskApplication.getMainScreenController().setRightPane(maintenanceRequestRoot);
    }

    /**
     * Changes the screen to the Language Request screen.
     *
     * @param actionEvent when the button is pressed.
     * @throws IOException will not be thrown; scene must exist.
     */

    @FXML
    public void changeToLanguageInterpreterRequest(ActionEvent actionEvent) throws IOException {
        KioskApplication.getHistory().push(() -> {
            try {
                KioskApplication.getMainScreenController().changeToService(null);
                changeToLanguageInterpreterRequest(null);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        AnchorPane languageRequestRoot = FXMLLoader.load(getClass()
                .getResource("/layout/UI_SoftEng_LanguageInterpreterRequest.fxml"));
        KioskApplication.getMainScreenController().setRightPane(languageRequestRoot);
    }

    /**
     * Changes the screen to the security request screen.
     *
     * @param actionEvent when the button is pressed.
     * @throws IOException will not be thrown; scene must exist.
     */
    @FXML
    public void changeToSecurityRequest(ActionEvent actionEvent) throws IOException {
        KioskApplication.getHistory().push(() -> {
            try {
                KioskApplication.getMainScreenController().changeToService(null);
                changeToSecurityRequest(null);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        AnchorPane securityRequestRoot = FXMLLoader.load(getClass()
                .getResource("/layout/UI_SoftEng_SecurityRequest.fxml"));
        KioskApplication.getMainScreenController().setRightPane(securityRequestRoot);
    }

    /**
     * Changes the screen to the custodian request screen.
     *
     * @param actionEvent when the button is pressed.
     * @throws IOException will not be thrown; scene must exist.
     */
    @FXML
    public void changeToCustodianRequest(ActionEvent actionEvent) throws IOException {
        KioskApplication.getHistory().push(() -> {
            try {
                KioskApplication.getMainScreenController().changeToService(null);
                changeToCustodianRequest(null);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        AnchorPane custodianRequestRoot = FXMLLoader.load(getClass()
                .getResource("/layout/UI_SoftEng_CustodianRequest.fxml"));
        KioskApplication.getMainScreenController().setRightPane(custodianRequestRoot);
    }

    /**
     * Changes the screen to the Assign Employee screen.
     *
     * @param actionEvent when the button is pressed.
     * @throws IOException will not be thrown; scene must exist.
     */
    @FXML
    public void changeToDeliveryRequest(ActionEvent actionEvent) throws IOException {
        KioskApplication.getHistory().push(() -> {
            try {
                KioskApplication.getMainScreenController().changeToService(null);
                changeToDeliveryRequest(null);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        AnchorPane deliveryRequestRoot = FXMLLoader.load(getClass()
                .getResource("/layout/UI_SoftEng_DeliveryRequest.fxml"));
        KioskApplication.getMainScreenController().setRightPane(deliveryRequestRoot);
    }

    /**
     * Opens the floral request api.
     *
     * @param actionEvent when the button is pressed.
     */
    @FXML
    public void changeToFloralRequest(ActionEvent actionEvent) throws IOException {
        KioskApplication.getHistory().push(() -> {
            try {
                KioskApplication.getMainScreenController().changeToService(null);
                changeToFloralRequest(null);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        AnchorPane floralRequestRoot = FXMLLoader.load(getClass()
                .getResource("/layout/UI_SoftEng_FloralSelectRoom.fxml"));
        KioskApplication.getMainScreenController().setRightPane(floralRequestRoot);
    }

    /**
     * Opens the food request API.
     *
     * @param actionEvent when the button is pressed
     */
    @FXML
    public void changeToFoodRequest(ActionEvent actionEvent) throws Exception {
        FoodRequest foodRequest = new FoodRequest();
        foodRequest.run(0, 0, 1900, 1000, null, null, null);
    }

    /**
     * Opens the emergency request API.
     *
     * @param actionEvent when the button is pressed
     */
    @FXML
    public void changeToEmergencyRequest(ActionEvent actionEvent) throws Exception {
        KioskApplication.getHistory().push(() -> {
            try {
                KioskApplication.getMainScreenController().changeToService(null);
                changeToEmergencyRequest(null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        AnchorPane emergencyRequestRoot = FXMLLoader.load(getClass()
                .getResource("/layout/UI_SoftEng_EmergencySelectRoom.fxml"));
        KioskApplication.getMainScreenController().setRightPane(emergencyRequestRoot);
    }

    @FXML
    public void changeToMedicalImagingRequest(ActionEvent actionEvent) throws Exception {
        ImagingRequest imagingRequest = new ImagingRequest();
        imagingRequest.run(0,0,1900,1000, null, "Conference Room 1", "Conference Room 1");
    }


    /**
     * Changes the screen to the IT request screen.
     *
     * @param actionEvent when the button is pressed.
     * @throws IOException will not be thrown; scene must exist.
     */
    @FXML
    public void changeToItRequest(ActionEvent actionEvent) throws IOException {
        KioskApplication.getHistory().push(() -> {
            try {
                KioskApplication.getMainScreenController().changeToService(null);
                changeToItRequest(null);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        AnchorPane itRequestRoot = FXMLLoader.load(getClass()
                .getResource("/layout/UI_SoftEng_ItRequest.fxml"));
        KioskApplication.getMainScreenController().setRightPane(itRequestRoot);
    }

    /**
     * Changes the screen to the religious request screen.
     *
     * @param actionEvent when the button is pressed.
     * @throws IOException will not be thrown; scene must exist.
     */
    @FXML
    public void changeToReligiousRequest(ActionEvent actionEvent) throws IOException {
        KioskApplication.getHistory().push(() -> {
            try {
                KioskApplication.getMainScreenController().changeToService(null);
                changeToReligiousRequest(null);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        AnchorPane religiousRequestRoot = FXMLLoader.load(getClass()
                .getResource("/layout/UI_SoftEng_ReligiousRequest.fxml"));
        KioskApplication.getMainScreenController().setRightPane(religiousRequestRoot);
    }

    /**
     * Changes the screen to the transportation request screen.
     *
     * @param actionEvent when the button is pressed.
     * @throws IOException will not be thrown; scene must exist.
     */
    @FXML
    public void changeToTransportationRequest(ActionEvent actionEvent) throws IOException {
        KioskApplication.getHistory().push(() -> {
            try {
                KioskApplication.getMainScreenController().changeToService(null);
                changeToTransportationRequest(null);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        AnchorPane transportationRequestRoot = FXMLLoader.load(getClass()
                .getResource("/layout/UI_SoftEng_TransportationRequest.fxml"));
        KioskApplication.getMainScreenController().setRightPane(transportationRequestRoot);
    }

}
