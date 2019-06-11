package edu.wpi.cs3733d19.teamg;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarEvent;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.page.DayPage;
import edu.wpi.cs3733d19.teamg.models.Booking;
import edu.wpi.cs3733d19.teamg.models.Employee;
import edu.wpi.cs3733d19.teamg.models.ServiceRequest;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.time.LocalTime;
import java.util.Date;

public class EmployeeProfileController extends Controller {
    Employee employee;

    @FXML
    CalendarView calendarView;
    @FXML
    private ToggleButton receiveEmailToggle;
    @FXML
    private ToggleButton serviceRequestToggle;
    @FXML
    private ToggleButton bookingsToggle;
    @FXML
    private TextField emailTextField;
    @FXML
    private Label emailConfirmedLabel;
    @FXML
    private Label greetingLabel;
    @FXML
    private TableView myServiceRequestTableView;
    @FXML
    private TableView assignedServiceRequestTableView;
    @FXML
    private GridPane gridPane;

    @FXML
    private void initialize() {
        KioskApplication.setController(this);
        greetingLabel.setText("Hello, " + KioskApplication.getCurrentUser().getName());
        emailTextField.setText(KioskApplication.getCurrentUser().getEmail());
        emailTextField.setOnKeyReleased(keyPress -> {
            if (keyPress.getCode().getName().equals("Enter")) {
                if (emailTextField.getText().isEmpty()) {
                    KioskApplication.getCurrentUser().setEmail(null);
                    emailConfirmedLabel.setText("Email removed.");
                } else if (Email.validateEmail(emailTextField.getText())) {
                    KioskApplication.getCurrentUser().setEmail(emailTextField.getText());
                    emailConfirmedLabel.setText("Email confirmed.");
                } else {
                    emailConfirmedLabel.setText("Invalid Email.");
                }
            } else {
                emailConfirmedLabel.setText("");
                if (emailTextField.getText().isEmpty()
                        || Email.validateEmail(emailTextField.getText())) {
                    emailTextField.setStyle("-fx-border-color: GREEN");
                } else {
                    emailTextField.setStyle("-fx-border-color: RED");
                }
            }
        });
        boolean receiveEmail = KioskApplication.getCurrentUser().getReceiveEmail();
        receiveEmailToggle.setSelected(receiveEmail);
        serviceRequestToggle.setSelected(
                KioskApplication.getCurrentUser().receiveServiceRequestEmail() && receiveEmail);
        serviceRequestToggle.setDisable(!receiveEmail);
        bookingsToggle.setSelected(
                KioskApplication.getCurrentUser().receiveBookingEmail() && receiveEmail);
        bookingsToggle.setDisable(!receiveEmail);

        TableColumn<Booking, Date> startDate = new TableColumn<Booking, Date>("Start Time");
        startDate.setCellValueFactory(new PropertyValueFactory<Booking, Date>("start"));

        TableColumn<Booking, Date> endDate = new TableColumn<Booking, Date>("End Time");
        endDate.setCellValueFactory(new PropertyValueFactory<Booking, Date>("end"));
        TableColumn<Booking, String> roomName = new TableColumn<>("Room");
        roomName.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getRoom().getShortName())
        );

        TableColumn<Booking, Booking> delete = new TableColumn<>("Delete");
        delete.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue()));
        delete.setCellFactory(something -> new TableCell<Booking, Booking>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(Booking booking, boolean empty) {
                super.updateItem(booking, empty);

                if (booking == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    getTableView().getItems().remove(booking);
                    KioskApplication.getEntityManager().getTransaction().begin();
                    booking.cancel();
                    KioskApplication.getEntityManager().merge(booking);
                    KioskApplication.getEntityManager().getTransaction().commit();
                    System.out.println("Remove booking " + booking.getId());
                    setEmployee(KioskApplication.getCurrentUser());
                });
            }
        });

        TableColumn<ServiceRequest, String> dateCreated = new TableColumn<>("Created");
        dateCreated.setCellValueFactory(new PropertyValueFactory<>("timeCreated"));
        dateCreated.prefWidthProperty().bind(
                myServiceRequestTableView.widthProperty().multiply(0.2125));

        TableColumn<ServiceRequest, String> type = new TableColumn<>("Type");
        type.setCellValueFactory(sRequest -> new SimpleStringProperty(sRequest.getValue()
                .getCategory().getTitle()));
        type.prefWidthProperty().bind(
                myServiceRequestTableView.widthProperty().multiply(0.2125));

        TableColumn<ServiceRequest, String> issue = new TableColumn<>("Issue");
        issue.setCellValueFactory(new PropertyValueFactory<>("issue"));
        issue.prefWidthProperty().bind(
                myServiceRequestTableView.widthProperty().multiply(0.2125));

        TableColumn<ServiceRequest, ServiceRequest> resolve = new TableColumn<>("Resolve");
        resolve.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue()));
        resolve.prefWidthProperty().bind(
                myServiceRequestTableView.widthProperty().multiply(0.2125));

        resolve.setCellFactory(something -> new TableCell<ServiceRequest, ServiceRequest>() {
            private final Button resolveButton = new Button("Resolve");

            @Override
            protected void updateItem(ServiceRequest serviceRequest, boolean empty) {
                if (serviceRequest == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(resolveButton);
                resolveButton.setOnAction(event -> {
                    getTableView().getItems().remove(serviceRequest);
                    KioskApplication.getEntityManager().getTransaction().begin();
                    serviceRequest.setTimeResolved(new Date());
                    KioskApplication.getEntityManager().merge(serviceRequest);
                    KioskApplication.getEntityManager().getTransaction().commit();
                });
            }
        });

        TableColumn<ServiceRequest, String> assignee = new TableColumn<>("Assigned To");
        assignee.setCellValueFactory(value -> new SimpleStringProperty(value.getValue()
                .getAssignee() == null ? "" : value.getValue().getAssignee().getName()));

        TableColumn<ServiceRequest, String> requester = new TableColumn<>("Requested By");
        requester.setCellValueFactory(value -> new SimpleStringProperty(value.getValue()
                .getRequester().getName()));

        myServiceRequestTableView.getColumns().addAll(dateCreated, type, issue, assignee, resolve);
        ObservableList<ServiceRequest> myServiceRequests = FXCollections.observableArrayList();
        ObservableList<ServiceRequest> assignedServiceRequest = FXCollections.observableArrayList();
        for (ServiceRequest sr : ServiceRequest.getAll(KioskApplication.getEntityManager())) {
            if (sr.getAssignee() == KioskApplication.getCurrentUser()
                    && sr.getTimeResolved() == null) {
                assignedServiceRequest.add(sr);
            }
            if (sr.getRequester() == KioskApplication.getCurrentUser()
                    && sr.getTimeResolved() == null) {
                myServiceRequests.add(sr);
            }
        }
        myServiceRequestTableView.setItems(myServiceRequests);

        if (KioskApplication.getCurrentUser().getCanService().size() == 0) {
            System.out.println("No can service.");
            gridPane.getChildren().remove(7);
        }

        dateCreated = new TableColumn<>("Created");
        dateCreated.setCellValueFactory(new PropertyValueFactory<>("timeCreated"));
        dateCreated.prefWidthProperty().bind(
                assignedServiceRequestTableView.widthProperty().multiply(0.2125));

        type = new TableColumn<>("Type");
        type.setCellValueFactory(sRequest ->
                new SimpleStringProperty(sRequest.getValue().getCategory().getTitle()));
        type.prefWidthProperty().bind(
                assignedServiceRequestTableView.widthProperty().multiply(0.15));

        issue = new TableColumn<>("Issue");
        issue.setCellValueFactory(new PropertyValueFactory<>("issue"));
        issue.prefWidthProperty().bind(
                assignedServiceRequestTableView.widthProperty().multiply(0.2125));

        resolve = new TableColumn<>("Resolve");
        resolve.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue()));
        resolve.prefWidthProperty().bind(
                assignedServiceRequestTableView.widthProperty().multiply(0.15));

        resolve.setCellFactory(something -> new TableCell<ServiceRequest, ServiceRequest>() {
            private final Button resolveButton = new Button("Resolve");

            @Override
            protected void updateItem(ServiceRequest serviceRequest, boolean empty) {
                if (serviceRequest == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(resolveButton);
                resolveButton.setOnAction(event -> {
                    KioskApplication.getEntityManager().getTransaction().begin();
                    serviceRequest.setTimeResolved(new Date());
                    KioskApplication.getEntityManager().merge(serviceRequest);
                    KioskApplication.getEntityManager().getTransaction().commit();
                    updateTables();
                });
            }
        });
        TableColumn<ServiceRequest, ServiceRequest> findPath = new TableColumn<>("Find Path");
        findPath.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue()));
        findPath.setCellFactory(something -> new TableCell<ServiceRequest, ServiceRequest>() {
            private final Button findButton = new Button("Find Path");

            @Override
            protected void updateItem(ServiceRequest serviceRequest, boolean empty) {
                if (serviceRequest == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(findButton);
                findButton.setOnAction(event -> {
                    KioskApplication.getMainScreenController()
                            .changeToDirections(null, serviceRequest.getLocation());
                });
            }
        });
        assignedServiceRequestTableView.getColumns().addAll(dateCreated,
                type, issue, requester, resolve, findPath);
        assignedServiceRequestTableView.setItems(assignedServiceRequest);

        // configure calendar
        calendarView.setShowAddCalendarButton(false);
        calendarView.setShowSourceTrayButton(false);
        calendarView.getDayPage().setDayPageLayout(DayPage.DayPageLayout.DAY_ONLY);
        calendarView.getDayPage().setShowDayPageLayoutControls(false);
        calendarView.setStartTime(LocalTime.of(8, 0, 0, 0));
        calendarView.setEndTime(LocalTime.of(17, 0, 0, 0));
    }

    private void updateTables() {
        ObservableList<ServiceRequest> myServiceRequests = FXCollections.observableArrayList();
        ObservableList<ServiceRequest> assignedServiceRequest = FXCollections.observableArrayList();
        for (ServiceRequest sr : ServiceRequest.getAll(KioskApplication.getEntityManager())) {
            if (sr.getAssignee() == KioskApplication.getCurrentUser()
                    && sr.getTimeResolved() == null) {
                assignedServiceRequest.add(sr);
            }
            if (sr.getRequester() == KioskApplication.getCurrentUser()
                    && sr.getTimeResolved() == null) {
                myServiceRequests.add(sr);
            }
        }
        myServiceRequestTableView.setItems(myServiceRequests);
        assignedServiceRequestTableView.setItems(assignedServiceRequest);
    }

    /**
     * Set the employee whose profile to show.
     * @param employee the employee whose profile to show
     */
    void setEmployee(Employee employee) {
        calendarView.getCalendarSources().clear();
        this.employee = employee;
        CalendarSource calSource = new CalendarSource(employee.getName());
        Calendar calendar = CalendarUtil.getEmployeeCalendar(employee);
        calSource.getCalendars().add(calendar);
        calendarView.getCalendarSources().add(calSource);
        EventHandler<CalendarEvent> handler = event -> calendarEventHandler(event);
        calendar.addEventHandler(handler);
    }

    private void calendarEventHandler(CalendarEvent event) {
        Entry entry = event.getEntry();
        if (event.getEventType() == CalendarEvent.ENTRY_INTERVAL_CHANGED) {
            CalendarUtil.updateBooking(entry);
        }
    }

    @FXML
    private void receiveEmailToggled(ActionEvent event) {
        boolean rec = receiveEmailToggle.isSelected();
        KioskApplication.getCurrentUser().setReceiveEmail(rec);
        if (!rec) {
            serviceRequestToggle.setSelected(false);
            KioskApplication.getCurrentUser().setReceiveServiceRequestEmail(false);
            bookingsToggle.setSelected(false);
            KioskApplication.getCurrentUser().setReceiveBookingEmail(false);
        }
        serviceRequestToggle.setDisable(!rec);
        bookingsToggle.setDisable(!rec);
    }

    public void serviceRequestToggled(ActionEvent event) {
        KioskApplication.getCurrentUser()
                .setReceiveServiceRequestEmail(serviceRequestToggle.isSelected());
    }

    public void bookingsToggled(ActionEvent event) {
        KioskApplication.getCurrentUser().setReceiveBookingEmail(bookingsToggle.isSelected());
    }
}
