package edu.wpi.cs3733d19.teamg;

import edu.wpi.cs3733d19.teamg.models.Booking;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Date;

public class CancelBookingsController {
    public TableView tableView;

    @FXML
    private void initialize() {
        TableColumn<Booking, Date> startDate = new TableColumn<>("Start Time");
        startDate.setCellValueFactory(new PropertyValueFactory<>("start"));
        TableColumn<Booking, Date> endDate = new TableColumn<>("End Time");
        endDate.setCellValueFactory(new PropertyValueFactory<>("end"));
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
                });
            }
        });
        ObservableList<Booking> bookings = FXCollections.observableArrayList();
        if (KioskApplication.getCurrentUser() != null) {
            for (Booking b : KioskApplication.getCurrentUser().getBookings()) {
                if (!b.isCancelled()) {
                    bookings.add(b);
                }
            }
            tableView.getColumns().addAll(roomName, startDate, endDate, delete);
            tableView.setItems(bookings);
        }
    }
}
