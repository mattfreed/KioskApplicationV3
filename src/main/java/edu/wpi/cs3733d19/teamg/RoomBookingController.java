package edu.wpi.cs3733d19.teamg;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.cs3733d19.teamg.models.Booking;
import edu.wpi.cs3733d19.teamg.models.Node;
import edu.wpi.cs3733d19.teamg.models.Room;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Callback;

import java.lang.Math;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;


public class RoomBookingController extends Controller implements Initializable {
    private Node selectedNode = null;

    private static Image smallConf1B = new Image("Photos/RoomOutlines/SmallConf1B.png", true);
    private static Image smallConf2B = new Image("Photos/RoomOutlines/SmallConf2B.png", true);
    private static Image smallConf3B = new Image("Photos/RoomOutlines/SmallConf3B.png", true);
    private static Image smallConf4B = new Image("Photos/RoomOutlines/SmallConf4B.png", true);
    private static Image smallConf5B = new Image("Photos/RoomOutlines/SmallConf5B.png", true);
    private static Image smallConf6B = new Image("Photos/RoomOutlines/SmallConf6B.png", true);
    private static Image smallConf7B = new Image("Photos/RoomOutlines/SmallConf7B.png", true);
    private static Image smallConf8B = new Image("Photos/RoomOutlines/SmallConf8B.png", true);
    private static Image medConf1B = new Image("Photos/RoomOutlines/MediumConf1B.png", true);
    private static Image medConf2B = new Image("Photos/RoomOutlines/MediumConf2B.png", true);
    private static Image medConf3B = new Image("Photos/RoomOutlines/MediumConf3B.png", true);
    private static Image medConf4B = new Image("Photos/RoomOutlines/MediumConf4B.png", true);
    private static Image largeConf1B = new Image("Photos/RoomOutlines/LargeConf1B.png", true);
    private static Image largeConf2B = new Image("Photos/RoomOutlines/LargeConf2B.png", true);
    private static Image largeConf3B = new Image("Photos/RoomOutlines/LargeConf3B.png", true);
    private static Image smallComp1B = new Image("Photos/RoomOutlines/SmallComp1B.png", true);
    private static Image medComp1B = new Image("Photos/RoomOutlines/MediumComp1B.png", true);
    private static Image medComp2B = new Image("Photos/RoomOutlines/MediumComp2B.png", true);
    private static Image medComp3B = new Image("Photos/RoomOutlines/MediumComp3B.png", true);
    private static Image medComp4B = new Image("Photos/RoomOutlines/MediumComp4B.png", true);
    private static Image medComp5B = new Image("Photos/RoomOutlines/MediumComp5B.png", true);
    private static Image ampB = new Image("Photos/RoomOutlines/Ampb.png", true);

    private static final double ORIGINAL_PANE_WIDTH = 3636;
    private static final double ORIGINAL_PANE_HEIGHT = 2724;
    private static double paneWidth = ORIGINAL_PANE_WIDTH;
    private static double paneHeight = ORIGINAL_PANE_HEIGHT;

    private static Map<String, Image> book = new HashMap<>();
    private static Map<String, Image> bookRed = new HashMap<>();
    private static Map<String, Image> outline = new HashMap<>();

    @FXML
    public JFXDatePicker startDatePicker;
    public JFXDatePicker endDatePicker;
    public JFXTimePicker startTimePicker;
    public JFXTimePicker endTimePicker;

    @FXML
    ImageView imageView;
    @FXML
    Pane pane;

    private Room roomSelected;
    private Callback<Room, Void> roomSelectedCallback;
    private List<Circle> circleList = new ArrayList<>();
    private List<ImageView> imageList = new ArrayList<>();
    private ImageView imageSelected = new ImageView();
    private Circle circleSelected;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        KioskApplication.setController(this);

        imageView.setPreserveRatio(false);
        imageView.fitWidthProperty().bind(pane.widthProperty());
        imageView.fitHeightProperty().bind(pane.heightProperty());

        pane.widthProperty().addListener(((observable, oldValue, newValue) -> {
            pane.getChildren().remove(1, pane.getChildren().size());
            paneWidth = (double) newValue;
            displayRoomAndDeskColors();
        }));

        pane.heightProperty().addListener(((observable, oldValue, newValue) -> {
            pane.getChildren().remove(1, pane.getChildren().size());
            paneHeight = (double) newValue;
            displayRoomAndDeskColors();
        }));

        setFloor();

        book.put("CONF2", KioskApplication.getSmallConf1());
        book.put("CONF5", KioskApplication.getSmallConf2());
        book.put("CONF8", KioskApplication.getSmallConf3());
        book.put("CONF9", KioskApplication.getSmallConf4());
        book.put("CONF10", KioskApplication.getSmallConf5());
        book.put("CONF11", KioskApplication.getSmallConf6());
        book.put("CONF12", KioskApplication.getSmallConf7());
        book.put("CONF15", KioskApplication.getSmallConf8());
        book.put("CONF1", KioskApplication.getMedConf1());
        book.put("CONF3", KioskApplication.getMedConf2());
        book.put("CONF4", KioskApplication.getMedConf3());
        book.put("CONF13", KioskApplication.getMedConf4());
        book.put("CONF6", KioskApplication.getLargeConf1());
        book.put("CONF7", KioskApplication.getLargeConf2());
        book.put("CONF14", KioskApplication.getLargeConf3());
        book.put("COMP1", KioskApplication.getSmallComp1());
        book.put("COMP2",  KioskApplication.getMedComp1());
        book.put("COMP3",  KioskApplication.getMedComp2());
        book.put("COMP4",  KioskApplication.getMedComp3());
        book.put("COMP5",  KioskApplication.getMedComp4());
        book.put("COMP6",  KioskApplication.getMedComp5());
        book.put("AMP",  KioskApplication.getAmp());


        bookRed.put("CONF2", KioskApplication.getSmallConf1R());
        bookRed.put("CONF5", KioskApplication.getSmallConf2R());
        bookRed.put("CONF8", KioskApplication.getSmallConf3R());
        bookRed.put("CONF9", KioskApplication.getSmallConf4R());
        bookRed.put("CONF10", KioskApplication.getSmallConf5R());
        bookRed.put("CONF11", KioskApplication.getSmallConf6R());
        bookRed.put("CONF12", KioskApplication.getSmallConf7R());
        bookRed.put("CONF15", KioskApplication.getSmallConf8R());
        bookRed.put("CONF1", KioskApplication.getMedConf1R());
        bookRed.put("CONF3", KioskApplication.getMedConf2R());
        bookRed.put("CONF4", KioskApplication.getMedConf3R());
        bookRed.put("CONF13", KioskApplication.getMedConf4R());
        bookRed.put("CONF6", KioskApplication.getLargeConf1R());
        bookRed.put("CONF7", KioskApplication.getLargeConf2R());
        bookRed.put("CONF14", KioskApplication.getLargeConf3R());
        bookRed.put("COMP1", KioskApplication.getSmallComp1R());
        bookRed.put("COMP2",  KioskApplication.getMedComp1R());
        bookRed.put("COMP3",  KioskApplication.getMedComp2R());
        bookRed.put("COMP4",  KioskApplication.getMedComp3R());
        bookRed.put("COMP5",  KioskApplication.getMedComp4R());
        bookRed.put("COMP6",  KioskApplication.getMedComp5R());
        bookRed.put("AMP",  KioskApplication.getAmpR());

        outline.put("CONF2", smallConf1B);
        outline.put("CONF5", smallConf2B);
        outline.put("CONF8",smallConf3B);
        outline.put("CONF9", smallConf4B);
        outline.put("CONF10", smallConf5B);
        outline.put("CONF11", smallConf6B);
        outline.put("CONF12", smallConf7B);
        outline.put("CONF15", smallConf8B);
        outline.put("CONF1", medConf1B);
        outline.put("CONF3", medConf2B);
        outline.put("CONF4", medConf3B);
        outline.put("CONF13", medConf4B);
        outline.put("CONF6", largeConf1B);
        outline.put("CONF7", largeConf2B);
        outline.put("CONF14",largeConf3B);
        outline.put("COMP1", smallComp1B);
        outline.put("COMP2",  medComp1B);
        outline.put("COMP3",  medComp2B);
        outline.put("COMP4",  medComp3B);
        outline.put("COMP5",  medComp4B);
        outline.put("COMP6",  medComp5B);
        outline.put("AMP",  ampB);

        LocalDate today = LocalDate.now();
        startDatePicker.setValue(today);
        endDatePicker.setValue(today);
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);
        startTimePicker.setValue(now);
        endTimePicker.setValue(now);
        Platform.runLater(() -> displayRoomAndDeskColors());
    }

    Room getRoomSelected() {
        return roomSelected;
    }

    private void setRoomSelected(Room roomSelected) {
        this.roomSelected = roomSelected;
        if (roomSelectedCallback != null) {
            roomSelectedCallback.call(roomSelected);
        }
        displayRoomAndDeskColors();
    }

    void setRoomSelectedCallback(Callback roomSelectedCallback) {
        this.roomSelectedCallback = roomSelectedCallback;
    }

    /**
     * Checks if there is a booking that interferes
     * with the current requested booking.
     *
     * @param start     the start date of the requested booking
     * @param end       the end date of the requested booking
     * @param checkroom the specific room that needs to get checked
     */
    private Boolean checkBookings(Date start, Date end, Room checkroom) {
        if (start == null || end == null || end.before(start)) {
            return null;
        }
        List<Booking> roomBookings = checkroom.getBookings();
        if (roomBookings != null) {
            for (Booking booking : roomBookings) {
                if (!booking.isCancelled()) {
                    if (start.after(booking.getStart())
                            && start.before(booking.getEnd())) {
                        //start time is between a booking
                        return false;
                    } else if (end.after(booking.getStart())
                            && end.before(booking.getEnd())) {
                        return false;
                    } else if (end.after(booking.getEnd())
                            && start.before(booking.getStart())) {
                        return false;
                    } else if (end.equals(booking.getEnd())
                            || start.equals(booking.getStart())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private Date getSelectedStartDateOrCurrentDate() {
        return getSelectedDateOrCurrentDate(startDatePicker, startTimePicker);
    }

    private Date getSelectedEndDateOrCurrentDate() {
        return getSelectedDateOrCurrentDate(endDatePicker, endTimePicker);
    }

    private Date getSelectedDateOrCurrentDate(JFXDatePicker datePicker, JFXTimePicker timePicker) {
        if (datePicker.getValue() != null && timePicker.getValue() != null) {
            return Date.from(LocalDateTime.of(datePicker.getValue(),
                    timePicker.getValue()).atZone(ZoneId.systemDefault()).toInstant());
        } else if (datePicker.getValue() == null && timePicker.getValue() == null) {
            return new Date();
        }
        return null;
    }

    /**
     * Draws a red or green polygon on top of a room.
     *
     * @param x1   the x coordinate of the room
     * @param y1   the y coordinate of the room
     * @param room the room being looked at
     */
    private Image drawRoomColor(double x1, double y1, Room room) {
        double widthRatio = paneWidth / ORIGINAL_PANE_WIDTH;
        double heightRatio = paneHeight / ORIGINAL_PANE_HEIGHT;
        Image newImage = null;
        Circle circle = new Circle(x1 * widthRatio, y1 * heightRatio, 40);
        if (!room.getShortName().contains("Desk")) {
            Boolean available = checkBookings(getSelectedStartDateOrCurrentDate(),
                    getSelectedEndDateOrCurrentDate(), room);
            if (available != null) {
                if (available) {
                    return book.get(room.getLongName());
                } else {
                    return bookRed.get(room.getLongName());
                }
            }
        }
        return null;
    }

    private Circle drawCircleDesks(double x1, double y1, Room room) {
        double widthRatio = paneWidth / ORIGINAL_PANE_WIDTH;
        double heightRatio = paneHeight / ORIGINAL_PANE_HEIGHT;
        Circle circle = new Circle(x1 * widthRatio, y1 * heightRatio, 5);
        if (room.getShortName().contains("Desk")) {
            circle.setOpacity(0.8);
            circle.setStroke(Color.BLACK);
            circle.setFill(Color.LIMEGREEN);
            setColorRed(circle, room);
        } else {
            circle.setOpacity(0.0);
        }
        return circle;
    }


    /**
     * Sets the circle color red if the room has a booking within the given time.
     *
     * @param cir  the circle that is being looked at
     * @param room the room being looked at
     */

    private void setColorRed(Circle cir, Room room) {
        if (checkBookings(getSelectedStartDateOrCurrentDate(),
                getSelectedEndDateOrCurrentDate(), room).equals(Boolean.FALSE)) {
            cir.setStroke(Color.BLACK);
            cir.setFill(Color.RED);
        }
    }

    private  void clearMap() {
        if (circleList != null || circleList.size() > 0) {
            for (int i = circleList.size() - 1; i >= 0; i--) {
                pane.getChildren().remove(circleList.get(i));
            }
        }
        if (imageList != null || imageList.size() > 0) {
            for (int i = imageList.size() - 1; i >= 0; i--) {
                pane.getChildren().remove(imageList.get(i));
            }
        }
    }

    @FXML
    public void displayRoomAndDeskColors() {
        displayRoomColors();
        displayDeskHelper();
    }

    private void displayRoomColors() {
        clearMap();
        for (Node node : Node.getAll(KioskApplication.getEntityManager())) {
            if (node instanceof Room) {
                Room loadRoom = (Room) node;
                Image imageAdd = drawRoomColor(loadRoom.getXCoord(),
                            loadRoom.getYCoord(), loadRoom);
                if (imageAdd == null) {
                    continue;
                }
                ImageView add = new ImageView();
                add.setImage(imageAdd);
                add.setPreserveRatio(false);
                add.fitWidthProperty().bind(pane.widthProperty());
                add.fitHeightProperty().bind(pane.heightProperty());
                pane.getChildren().add(add);
                imageList.add(add);
            }
        }
    }

    private void displayDeskHelper() {
        if (circleList != null || circleList.size() > 0) {
            for (int i = circleList.size() - 1; i >= 0; i--) {
                pane.getChildren().remove(circleList.get(i));
            }
        }
        for (Node node : Node.getAll(KioskApplication.getEntityManager())) {
            if (node instanceof Room) {
                Room loadRoom = (Room) node;
                Circle circleAdd = drawCircleDesks(loadRoom.getXCoord(),
                            loadRoom.getYCoord(), loadRoom);
                pane.getChildren().add(circleAdd);
                circleList.add(circleAdd);
            }
        }
    }

    /**
     * Gets the room/desk that was selected at the clicked location on the map.
     *
     * @param mouseEvent  the clicking of the mouse on the image
     */

    public void getXy(MouseEvent mouseEvent) {
        double widthRatio = paneWidth / ORIGINAL_PANE_WIDTH;
        double heightRatio = paneHeight / ORIGINAL_PANE_HEIGHT;

        double x1 = mouseEvent.getX();
        double y1 = mouseEvent.getY();

        setRoomSelected(closestRoom(x1, y1, widthRatio, heightRatio));
        selected(roomSelected);
    }

    private Room closestRoom(double x1Click, double y1Click, double width, double height) {
        double close = -1;
        Room roomClose = null;
        for (Node n : Room.getAllOnFloor("BookableRoom", KioskApplication.getEntityManager())) {
            double x1Check = n.getXCoord() * width;
            double y1Check = n.getYCoord() * height;

            double x1Distance = Math.abs(x1Click - x1Check);
            double y1Distance = Math.abs(y1Click - y1Check);

            double distance = Math.sqrt((x1Distance * x1Distance) + (y1Distance * y1Distance));

            if (close == -1) {
                close = distance;
                roomClose = (Room) n;
            } else {
                if (distance < close) {
                    close = distance;
                    roomClose = (Room) n;
                }
            }

        }

        return roomClose;
    }

    private void selected(Room room) {
        clearSelected();
        Image imageAdd = outline.get(room.getLongName());

        imageSelected.setImage(imageAdd);
        imageSelected.setPreserveRatio(false);
        imageSelected.fitWidthProperty().bind(pane.widthProperty());
        imageSelected.fitHeightProperty().bind(pane.heightProperty());
        pane.getChildren().add(imageSelected);
        imageList.add(imageSelected);

        if (room.getShortName().contains("Desk")) {
            double widthRatio = paneWidth / ORIGINAL_PANE_WIDTH;
            double heightRatio = paneHeight / ORIGINAL_PANE_HEIGHT;
            Circle circle = new Circle(room.getXCoord() * widthRatio,
                    room.getYCoord() * heightRatio, 5);
            circle.setStroke(Color.BLUE);
            circleSelected = circle;
            pane.getChildren().add(circleSelected);
        }
    }

    private  void clearSelected() {
        if (imageSelected != null) {
            pane.getChildren().remove(imageSelected);
        }
        if (circleSelected != null) {
            pane.getChildren().remove(circleSelected);
        }
    }
}
