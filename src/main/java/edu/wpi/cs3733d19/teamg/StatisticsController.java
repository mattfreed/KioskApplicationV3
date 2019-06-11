package edu.wpi.cs3733d19.teamg;

import edu.wpi.cs3733d19.teamg.models.CustodianRequest;
import edu.wpi.cs3733d19.teamg.models.DeliveryRequest;
import edu.wpi.cs3733d19.teamg.models.FloralRequest;
import edu.wpi.cs3733d19.teamg.models.ItRequest;
import edu.wpi.cs3733d19.teamg.models.LanguageInterpreterRequest;
import edu.wpi.cs3733d19.teamg.models.MaintenanceRequest;
import edu.wpi.cs3733d19.teamg.models.ReligiousRequest;
import edu.wpi.cs3733d19.teamg.models.SecurityRequest;
import edu.wpi.cs3733d19.teamg.models.ServiceRequest;
import edu.wpi.cs3733d19.teamg.models.TransportationRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManager;


public class StatisticsController extends Controller {
    @FXML
    private PieChart typesPieChart;
    @FXML
    private PieChart timePieChart;
    @FXML
    private AnchorPane anchor;
    @FXML
    private GridPane grid;

    private List<CustodianRequest> custodianRequests;
    private List<DeliveryRequest> deliveryRequests;
    private List<FloralRequest> floralRequests;
    private List<ItRequest> itRequests;
    private List<LanguageInterpreterRequest> languageInterpreterRequests;
    private List<MaintenanceRequest> maintenanceRequests;
    private List<ReligiousRequest> religiousRequests;
    private List<SecurityRequest> securityRequests;
    private List<TransportationRequest> transportationRequests;

    private ObservableList<PieChart.Data> typesData;
    private ObservableList<PieChart.Data> timeData;

    @FXML
    private void initialize() {
        KioskApplication.setController(this);
        EntityManager em = KioskApplication.getEntityManager();
        custodianRequests = CustodianRequest.getAll(em);
        deliveryRequests = DeliveryRequest.getAll(em);
        floralRequests = FloralRequest.getAll(em);
        itRequests = ItRequest.getAll(em);
        languageInterpreterRequests = LanguageInterpreterRequest.getAll(em);
        maintenanceRequests = MaintenanceRequest.getAll(em);
        religiousRequests = ReligiousRequest.getAll(em);
        securityRequests = SecurityRequest.getAll(em);
        transportationRequests = TransportationRequest.getAll(em);
        List<ServiceRequest> requests = new ArrayList<>();
        requests.addAll(custodianRequests);
        requests.addAll(deliveryRequests);
        requests.addAll(floralRequests);
        requests.addAll(itRequests);
        requests.addAll(languageInterpreterRequests);
        requests.addAll(maintenanceRequests);
        requests.addAll(religiousRequests);
        requests.addAll(securityRequests);
        requests.addAll(transportationRequests);

        typesData = FXCollections.observableArrayList(
                new PieChart.Data("Custodian", custodianRequests.size()),
                new PieChart.Data("Delivery", deliveryRequests.size()),
                new PieChart.Data("Floral", floralRequests.size()),
                new PieChart.Data("IT", itRequests.size()),
                new PieChart.Data("Language Interpreter", languageInterpreterRequests.size()),
                new PieChart.Data("Maintenance", maintenanceRequests.size()),
                new PieChart.Data("Religious", religiousRequests.size()),
                new PieChart.Data("Security", securityRequests.size()),
                new PieChart.Data("Transportation", transportationRequests.size())
        );
        typesPieChart.setData(typesData);
        typesPieChart.setTitle("ServiceRequest Breakdown by Type");

        int[] timeRequests = new int[24];
        for (int i : timeRequests) {
            i = 0;
        }

        for (ServiceRequest request : requests) {
            Calendar cal = Calendar.getInstance();
            if (request instanceof DeliveryRequest) {
                cal.setTime(((DeliveryRequest) request).getPickupTime());
            } else if (request instanceof FloralRequest) {
                cal.setTime(((FloralRequest) request).getDeliveryDate());
            } else if (request instanceof ReligiousRequest) {
                cal.setTime((((ReligiousRequest) request).getServiceDate()));
            } else if (request instanceof TransportationRequest) {
                cal.setTime(((TransportationRequest) request).getPickupTime());
            } else {
                cal.setTime(request.getTimeCreated());
            }
            timeRequests[cal.get(Calendar.HOUR_OF_DAY)]++;
        }

        timeData = FXCollections.observableArrayList(
                new PieChart.Data("midnight to 1am", timeRequests[0]),
                new PieChart.Data("1am to 2am", timeRequests[1]),
                new PieChart.Data("2am to 3am", timeRequests[2]),
                new PieChart.Data("3am to 4am", timeRequests[3]),
                new PieChart.Data("4am to 5am", timeRequests[4]),
                new PieChart.Data("5am to 6am", timeRequests[5]),
                new PieChart.Data("6am to 7am", timeRequests[6]),
                new PieChart.Data("7am to 8am", timeRequests[7]),
                new PieChart.Data("8am to 9am", timeRequests[8]),
                new PieChart.Data("9am to 10am", timeRequests[9]),
                new PieChart.Data("10am to 11am", timeRequests[10]),
                new PieChart.Data("11am to noon", timeRequests[11]),
                new PieChart.Data("noon to 1pm", timeRequests[12]),
                new PieChart.Data("1pm to 2pm", timeRequests[13]),
                new PieChart.Data("2pm to 3pm", timeRequests[14]),
                new PieChart.Data("3pm to 4pm", timeRequests[15]),
                new PieChart.Data("4pm to 5pm", timeRequests[16]),
                new PieChart.Data("5pm to 6pm", timeRequests[17]),
                new PieChart.Data("6pm to 7pm", timeRequests[18]),
                new PieChart.Data("7pm to 8pm", timeRequests[19]),
                new PieChart.Data("8pm to 9pm", timeRequests[20]),
                new PieChart.Data("9pm to 10pm", timeRequests[21]),
                new PieChart.Data("10pm to 11pm", timeRequests[22]),
                new PieChart.Data("11pm to midnight", timeRequests[23])
        );
        timePieChart.setData(timeData);
        timePieChart.setTitle("ServiceRequest Breakdown by Time Requested");
    }
}
