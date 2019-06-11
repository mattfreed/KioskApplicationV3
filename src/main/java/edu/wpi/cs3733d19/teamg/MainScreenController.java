package edu.wpi.cs3733d19.teamg;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import edu.wpi.cs3733d19.teamg.models.Booking;
import edu.wpi.cs3733d19.teamg.models.Node;
import edu.wpi.cs3733d19.teamg.models.Room;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class MainScreenController {
    @FXML
    public Button directionsButton;
    public Button bookingButton;
    public Button serviceButton;
    public Button adminButton;
    public AnchorPane anchorPane;
    public ImageView imageViewF2;
    public ImageView imageViewF1;
    public ImageView imageViewF3;
    public ImageView imageViewF4;
    public ImageView imageViewGf;
    public ImageView imageViewLf1;
    public ImageView imageViewLf2;
    public AnchorPane anchorPaneF1;
    public AnchorPane anchorPaneF2;
    public AnchorPane anchorPaneF3;
    public AnchorPane anchorPaneF4;
    public AnchorPane anchorPaneGf;
    public AnchorPane anchorPaneLf1;
    public AnchorPane anchorPaneLf2;
    public AnchorPane rightPane;
    public AnchorPane leftPane;
    public AnchorPane topPane;
    public JFXButton directionsTab;
    public JFXButton serviceTab;
    public JFXButton adminTab;
    public JFXButton bookingsTab;
    public JFXButton homeTab;
    public JFXButton dateTime;
    public HBox tabPaneTop;
    public AnchorPane mainAnchorPane;
    public JFXButton loginTab;
    public JFXButton logOutTab;
    public JFXButton backTab;
    public JFXButton voidTab;
    public JFXButton resetMapTab;
    public JFXTabPane mapTabPane;
    public Tab floor4Tab;
    public Tab floor3Tab;
    public Tab floor2Tab;
    public Tab floor1Tab;
    public Tab groundFloorTab;
    public Tab lowerfloor1Tab;
    public Tab lowerfloor2Tab;


    @FXML
    private void initialize() throws IOException {
        setGraphics();
        initClock();

        KioskApplication.setMainScreenController(this);
        KioskApplication.setMapController(null);

        setOpenTab();


        KioskApplication.setFloor("3");
        setOpenTab();

        if (KioskApplication.getCurrentUser() != null) {
            loginTab.setText(KioskApplication.getCurrentUser().getName());
            logOutTab.setText("Log Out");
        }

    }

    /**
     * bind Images method to bind the height and width of an image to its respective pane.
     *
     * @param imageView  imageView to be bound.
     * @param anchorPane pane to bind imageView to.
     */
    private void bindImages(ImageView imageView, AnchorPane anchorPane) {
        imageView.setPreserveRatio(false);
        imageView.fitWidthProperty().bind(anchorPane.widthProperty());
        imageView.fitHeightProperty().bind(anchorPane.heightProperty());
    }

    private void setGraphics() {
        /*logOutTab.setMinWidth(125);
        logOutTab.setPrefWidth(125);
        logOutTab.setMaxWidth(125);

        loginTab.setMinWidth(125);
        loginTab.setPrefWidth(125);
        loginTab.setMaxWidth(125);

        dateTime.setMinWidth(125);
        dateTime.setPrefWidth(125);
        dateTime.setMaxWidth(125);

        resetMapTab.setMinWidth(125);
        resetMapTab.setPrefWidth(125);
        resetMapTab.setMaxWidth(125);

        backTab.setMinWidth(125);
        backTab.setPrefWidth(125);
        backTab.setMaxWidth(125);*/

        tabPaneTop.setAlignment(Pos.CENTER);


        setButtonGraphics(directionsTab, new Image("/Photos/directions.png"), 50, 50);

        setButtonGraphics(serviceTab, new Image("/Photos/service.png"), 50, 50);

        setButtonGraphics(adminTab, new Image("/Photos/admin.png"), 50, 50);

        setButtonGraphics(bookingsTab, new Image("/Photos/calendar.png"), 50, 50);

        setButtonGraphics(homeTab, new Image("/Photos/house.png"), 50, 50);

        setTabGraphics(floor1Tab, KioskApplication.getOneIcon(), 40, 50);

        setTabGraphics(floor2Tab, KioskApplication.getTwoIcon(), 40, 50);

        setTabGraphics(floor3Tab, KioskApplication.getThreeIcon(), 40, 50);

        setTabGraphics(floor4Tab, KioskApplication.getFourIcon(), 40, 50);

        setTabGraphics(groundFloorTab, KioskApplication.getGroundIcon(), 35, 50);

        setTabGraphics(lowerfloor1Tab, KioskApplication.getLf1Icon(), 50, 50);

        setTabGraphics(lowerfloor2Tab, KioskApplication.getLf2Icon(), 50, 50);


        imageViewF1.setImage(KioskApplication.getFirstFloorImage());
        imageViewF2.setImage(KioskApplication.getSecondFloorImage());
        imageViewF3.setImage(KioskApplication.getThirdFloorImage());
        imageViewF4.setImage(KioskApplication.getFourthFloorImage());
        imageViewGf.setImage(KioskApplication.getGroundFloorImage());
        imageViewLf1.setImage(KioskApplication.getLowerLevelOneImage());
        imageViewLf2.setImage(KioskApplication.getLowerLevelTwoImage());
        bindImages(imageViewF1, anchorPaneF1);
        bindImages(imageViewF2, anchorPaneF2);
        bindImages(imageViewF3, anchorPaneF3);
        bindImages(imageViewF4, anchorPaneF4);
        bindImages(imageViewGf, anchorPaneGf);
        bindImages(imageViewLf1, anchorPaneLf1);
        bindImages(imageViewLf2, anchorPaneLf2);

        PannableCanvas canvas = new PannableCanvas();
        canvas.setTranslateX(100);
        canvas.setTranslateY(100);
        NodeGestures nodeGestures = new NodeGestures(canvas);

        nodeGestures.zoom(anchorPaneF1);
        nodeGestures.zoom(anchorPaneF2);
        nodeGestures.zoom(anchorPaneF3);
        nodeGestures.zoom(anchorPaneF4);
        nodeGestures.zoom(anchorPaneGf);
        nodeGestures.zoom(anchorPaneLf1);
        nodeGestures.zoom(anchorPaneLf2);

        anchorPaneF1.addEventFilter(MouseEvent.MOUSE_PRESSED,
                nodeGestures.getOnMousePressedEventHandler());
        anchorPaneF1.addEventFilter(MouseEvent.MOUSE_DRAGGED,
                nodeGestures.getOnMouseDraggedEventHandler());


        anchorPaneF2.addEventFilter(MouseEvent.MOUSE_PRESSED,
                nodeGestures.getOnMousePressedEventHandler());
        anchorPaneF2.addEventFilter(MouseEvent.MOUSE_DRAGGED,
                nodeGestures.getOnMouseDraggedEventHandler());

        anchorPaneF3.addEventFilter(MouseEvent.MOUSE_PRESSED,
                nodeGestures.getOnMousePressedEventHandler());
        anchorPaneF3.addEventFilter(MouseEvent.MOUSE_DRAGGED,
                nodeGestures.getOnMouseDraggedEventHandler());

        anchorPaneF4.addEventFilter(MouseEvent.MOUSE_PRESSED,
                nodeGestures.getOnMousePressedEventHandler());
        anchorPaneF4.addEventFilter(MouseEvent.MOUSE_DRAGGED,
                nodeGestures.getOnMouseDraggedEventHandler());

        anchorPaneGf.addEventFilter(MouseEvent.MOUSE_PRESSED,
                nodeGestures.getOnMousePressedEventHandler());
        anchorPaneGf.addEventFilter(MouseEvent.MOUSE_DRAGGED,
                nodeGestures.getOnMouseDraggedEventHandler());

        anchorPaneLf1.addEventFilter(MouseEvent.MOUSE_PRESSED,
                nodeGestures.getOnMousePressedEventHandler());
        anchorPaneLf1.addEventFilter(MouseEvent.MOUSE_DRAGGED,
                nodeGestures.getOnMouseDraggedEventHandler());

        anchorPaneLf2.addEventFilter(MouseEvent.MOUSE_PRESSED,
                nodeGestures.getOnMousePressedEventHandler());
        anchorPaneLf2.addEventFilter(MouseEvent.MOUSE_DRAGGED,
                nodeGestures.getOnMouseDraggedEventHandler());

        anchorPaneF1.addEventFilter(MouseEvent.MOUSE_PRESSED,
                nodeGestures.getOnMousePressedEventHandler());
        anchorPaneF1.addEventFilter(MouseEvent.MOUSE_DRAGGED,
                nodeGestures.getOnMouseDraggedEventHandler());
    }

    private void setTabGraphics(Tab tab, Image img, int height, int width) {
        ImageView imgview = new ImageView();
        imgview.setImage(img);
        imgview.setFitHeight(height);
        imgview.setFitWidth(width);
        tab.setGraphic(imgview);
    }

    private void setButtonGraphics(JFXButton button, Image img, int height, int width) {
        ImageView imgview = new ImageView();
        imgview.setImage(img);
        imgview.setFitHeight(height);
        imgview.setFitWidth(width);
        button.setGraphic(imgview);
        /*button.setMinWidth(125);
        button.setPrefWidth(125);
        button.setMaxWidth(125);*/
    }


    @FXML
    private void goToNonAgeRestrictedFunCorner(Event actionEvent) throws IOException {
        KioskApplication.getHistory().push(() -> {
            try {
                goToNonAgeRestrictedFunCorner(null);
            } catch (IOException ex) {
                //wont
            }
        });
        AnchorPane nonAgeRestrictedFunZone = FXMLLoader.load(getClass()
                .getResource("/layout/UI_SoftEng_Snake.fxml"));
        KioskApplication.switchPanel(leftPane, nonAgeRestrictedFunZone);
        KioskApplication.switchPanel(rightPane, null);
    }

    /**
     * Change to the admin page.
     * @param actionEvent the event that caused this
     * @throws IOException wont
     */
    @FXML
    public void goToAdmin(Event actionEvent) throws IOException {
        KioskApplication.getHistory().push(() -> {
            try {
                KioskApplication.getMainScreenController().goToAdmin(null);
            } catch (IOException ex) {
                //wont be thrown
            }
        });
        if (KioskApplication.getCurrentUser() != null
                && KioskApplication.getCurrentUser().isAdmin()) {
            AnchorPane adminMenuRoot = FXMLLoader.load(getClass()
                    .getResource("/layout/UI_SoftEng_AdminMenu.fxml"));
            KioskApplication.switchPanel(rightPane, adminMenuRoot);
        } else {
            AnchorPane loginRoot = FXMLLoader.load(getClass()
                    .getResource("/layout/UI_SoftEng_Login.fxml"));
            ((LoginController) KioskApplication.getController()).setNext(() -> {
                try {
                    AnchorPane adminMenuRoot = null;
                    adminMenuRoot = FXMLLoader.load(getClass()
                            .getResource("/layout/UI_SoftEng_AdminMenu.fxml"));
                    KioskApplication.switchPanel(rightPane, adminMenuRoot);
                } catch (IOException ex) {
                    //wont be thrown as the file exists
                }
            });
            ((LoginController) KioskApplication.getController()).setRestrictedPage(true);
            KioskApplication.switchPanel(rightPane, loginRoot);
        }

        AnchorPane map = FXMLLoader.load(getClass()
                .getResource("/layout/Map.fxml"));
        KioskApplication.switchPanel(leftPane, map);
    }


    @FXML
    private void goToLogin() throws IOException {
        KioskApplication.getHistory().push(() -> {
            try {
                KioskApplication.getMainScreenController().goToLogin();
            } catch (IOException ex) {
                //wont be thrown
            }
        });
        if (KioskApplication.getCurrentUser() == null) {
            AnchorPane loginRoot = FXMLLoader.load(getClass()
                    .getResource("/layout/UI_SoftEng_Login.fxml"));

            KioskApplication.switchPanel(rightPane, loginRoot);
            ((LoginController) KioskApplication.getController()).setNext(() -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(
                            getClass().getResource("/layout/UI_SoftEng_EmployeeProfile.fxml"));
                    AnchorPane employeeProfile = fxmlLoader.load();
                    EmployeeProfileController controller = fxmlLoader.getController();
                    controller.setEmployee(KioskApplication.getCurrentUser());
                    KioskApplication.switchPanel(leftPane, employeeProfile);
                    KioskApplication.switchPanel(rightPane, null);
                } catch (IOException ex) {
                    //wont be thrown as the file exists
                }
            });
            AnchorPane map = FXMLLoader.load(getClass()
                    .getResource("/layout/Map.fxml"));
            KioskApplication.switchPanel(leftPane, map);
        } else {

            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getResource("/layout/UI_SoftEng_EmployeeProfile.fxml"));

            AnchorPane employeeProfile = fxmlLoader.load();
            EmployeeProfileController controller = fxmlLoader.getController();
            controller.setEmployee(KioskApplication.getCurrentUser());
            KioskApplication.switchPanel(leftPane, employeeProfile);
            KioskApplication.switchPanel(rightPane, null);
        }

    }

    @FXML
    private void goToMainMenu() throws IOException {
        if (KioskApplication.getMapController() != null) {
            KioskApplication.getMapController().enableTabs();
        }
        KioskApplication.getHistory().push(() -> {
            try {
                KioskApplication.getMainScreenController().goToMainMenu();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        Parent mainMenuRoot = FXMLLoader.load(getClass()
                .getResource("/layout/UI_SoftEng_G.fxml"));
        KioskApplication.getPrimaryScene().setRoot(mainMenuRoot);
        KioskApplication.setMapController(null);

    }

    @FXML
    private void resetMap() {
        if (KioskApplication.getMapController() == null) {
            resetMainScreenMap();
        } else {
            KioskApplication.getMapController().resetMap();
        }
        if (KioskApplication.getController() instanceof EditMapController) {
            EditMapController current = (EditMapController) KioskApplication.getController();
            current.resetMap();
        }
    }

    /**
     * Change screen to the pathfinding screen
     *
     * @param actionEvent the action event being handled. This is a button press
     */
    @FXML
    public void changeToDirections(Event actionEvent) throws IOException {
        KioskApplication.getHistory().push(() -> {
            try {
                KioskApplication.getMainScreenController().changeToDirections(null);
            } catch (IOException ex) {
                ex.printStackTrace();
                ex.printStackTrace();
            }
        });
        AnchorPane map = FXMLLoader.load(getClass()
                .getResource("/layout/Map.fxml"));
        KioskApplication.switchPanel(leftPane, map);


        AnchorPane pathFindStartRoot = FXMLLoader.load(getClass()
                .getResource("/layout/UI_SoftEng_PathFindStart.fxml"));
        KioskApplication.switchPanel(rightPane, pathFindStartRoot);

    }


    /**
     * changes to the directions screen with the start and end nodes preselected.
     *
     * @param start the start node selected or null for no node selected
     * @param dest  the end node selected or null for no node selected
     */
    public void changeToDirections(Node start, Node dest) {
        try {
            changeToDirections(null);
        } catch (IOException ex) {
            //wont
        }
        if (start != null) {
            ((PathFindStartController) KioskApplication.getController()).setStart(start);
        }
        if (dest != null) {
            ((PathFindStartController) KioskApplication.getController()).setDest(dest);
        }
    }

    /**
     * Change screen to the Room Booking screen
     *
     * @param actionEvent the action event being handled. This is a button press
     */
    @FXML
    private void changeToRoomBooking(Event actionEvent) throws IOException {
        KioskApplication.getHistory().push(() -> {
            try {
                KioskApplication.getMainScreenController().changeToRoomBooking(null);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        if (KioskApplication.getCurrentUser() == null) {
            FXMLLoader mapLoader = new FXMLLoader(
                    getClass().getResource("/layout/UI_SoftEng_BookRoom.fxml"));
            FXMLLoader calLoader = new FXMLLoader(
                    getClass().getResource("/layout/UI_SoftEng_BookRoom_Cal.fxml"));
            FXMLLoader loginLoader = new FXMLLoader(
                    getClass().getResource("/layout/UI_SoftEng_Login.fxml"));
            final AnchorPane mapRoot = mapLoader.load();
            final AnchorPane calRoot = calLoader.load();
            final AnchorPane loginRoot = loginLoader.load();
            RoomBookingController mapController = mapLoader.getController();
            RoomBookingCalController calController = calLoader.getController();
            LoginController loginController = loginLoader.getController();
            mapController.setRoomSelectedCallback(new Callback<Room, Void>() {
                @Override
                public Void call(Room room) {
                    calController.setRoom(room);
                    return null;
                }
            });
            calController.updateMapCallback(()-> mapController.displayRoomAndDeskColors());
            loginController.setNext(() -> {
                KioskApplication.switchPanel(rightPane, calRoot);
            });
            KioskApplication.switchPanel(leftPane, mapRoot);
            KioskApplication.switchPanel(rightPane, loginRoot);
        } else {
            FXMLLoader mapLoader = new FXMLLoader(
                    getClass().getResource("/layout/UI_SoftEng_BookRoom.fxml"));
            FXMLLoader calLoader = new FXMLLoader(
                    getClass().getResource("/layout/UI_SoftEng_BookRoom_Cal.fxml"));
            AnchorPane mapRoot = mapLoader.load();
            AnchorPane calRoot = calLoader.load();
            RoomBookingController mapController = mapLoader.getController();
            RoomBookingCalController calController = calLoader.getController();
            mapController.setRoomSelectedCallback(new Callback<Room, Void>() {
                @Override
                public Void call(Room room) {
                    calController.setRoom(room);
                    return null;
                }
            });
            calController.updateMapCallback(()-> mapController.displayRoomAndDeskColors());
            KioskApplication.switchPanel(leftPane, mapRoot);
            KioskApplication.switchPanel(rightPane, calRoot);
        }
    }

    /**
     * Change screen to the Services screen
     *
     * @param actionEvent the action event being handled. This is a button press
     */
    @FXML
    public void changeToService(Event actionEvent) throws IOException {
        KioskApplication.getHistory().push(() -> {
            try {
                KioskApplication.getMainScreenController().changeToService(null);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        if (KioskApplication.getCurrentUser() == null) {
            AnchorPane loginRoot = FXMLLoader.load(getClass()
                    .getResource("/layout/UI_SoftEng_Login.fxml"));
            ((LoginController) KioskApplication.getController()).setNext(() -> {
                try {
                    AnchorPane serviceRequestRoot = FXMLLoader.load(getClass()
                            .getResource("/layout/UI_SoftEng_ServiceRequest.fxml"));
                    KioskApplication.switchPanel(rightPane, serviceRequestRoot);
                } catch (IOException ex) {
                    //wont be thrown as the file exists
                }
            });
            KioskApplication.switchPanel(rightPane, loginRoot);

        } else {
            AnchorPane serviceRequestRoot = FXMLLoader.load(getClass()
                    .getResource("/layout/UI_SoftEng_ServiceRequest.fxml"));
            KioskApplication.switchPanel(rightPane, serviceRequestRoot);
        }

        AnchorPane map = FXMLLoader.load(getClass()
                .getResource("/layout/Map.fxml"));
        KioskApplication.switchPanel(leftPane, map);

    }

    @FXML
    private void goToAbout() throws IOException {
        AnchorPane map = FXMLLoader.load(getClass()
                .getResource("/layout/Map.fxml"));
        KioskApplication.switchPanel(leftPane, map);

        KioskApplication.getHistory().push(() -> {
            try {
                KioskApplication.getMainScreenController().goToMainMenu();
                KioskApplication.getMainScreenController().goToAbout();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        AnchorPane aboutRoot = FXMLLoader.load(getClass()
                .getResource("/layout/UI_SoftEng_About.fxml"));

        KioskApplication.switchPanel(leftPane, aboutRoot);
    }

    @FXML
    private void goToCredits() throws IOException {
        KioskApplication.getHistory().push(() -> {
            try {
                KioskApplication.getMainScreenController().goToCredits();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        AnchorPane aboutRoot = FXMLLoader.load(getClass()
                .getResource("/layout/UI_SoftEng_Credits.fxml"));

        AnchorPane map = FXMLLoader.load(getClass()
                .getResource("/layout/Map.fxml"));
        KioskApplication.switchPanel(leftPane, map);

        KioskApplication.switchPanel(leftPane, aboutRoot);
    }

    /**
     * Log the user out of the system.
     *
     * @param actionEvent event to trigger logout.
     * @throws IOException IOException for FXMLLoader.
     */
    @FXML
    public void logOut(Event actionEvent) throws IOException {
        KioskApplication.setCurrentUser(null);
        KioskApplication.getHistory().clear();
        loginTab.setText("Login");
        goToMainMenu();
        logOutTab.setDisable(true);
    }

    /**
     * Method to indicate successful login.
     */
    @FXML
    public void setLoginName() {
        loginTab.setText(KioskApplication.getCurrentUser().getName());
        logOutTab.setDisable(false);
        logOutTab.setText("Log Out");
    }

    public AnchorPane getRightPane() {
        return rightPane;
    }

    public AnchorPane getLeftPane() {
        return leftPane;
    }

    /**
     * Method to switch right AnchorPane of display.
     *
     * @param newPane new AnchorPane to be displayed.
     */
    public void setRightPane(AnchorPane newPane) {
        if (newPane == null) {
            this.rightPane.getChildren().clear();
        } else {
            KioskApplication.switchPanel(rightPane, newPane);
        }
    }

    /**
     * Method to switch left AnchorPane of display.
     *
     * @param newPane new AnchorPane to be displayed.
     */
    public void setLeftPane(AnchorPane newPane) {
        if (newPane == null) {
            this.leftPane.getChildren().clear();
        } else {
            KioskApplication.switchPanel(leftPane, newPane);
        }
    }


    private void initClock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
            dateTime.setText(LocalDateTime.now().format(formatter));
            dateTime.setTextFill(Paint.valueOf("#FFFFFF"));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    /**
     * removes the top state from the history stack and then restores the next state.
     *
     * @throws IOException doesn't actually throw this
     */
    public void goBack() throws IOException {
        if (KioskApplication.getMapController() != null) {
            KioskApplication.getMapController().enableTabs();
        }
        if (!KioskApplication.getHistory().isEmpty()) {
            KioskApplication.getHistory().pop();
            if (!KioskApplication.getHistory().isEmpty()) {
                KioskApplication.getHistory().pop().run();
            } else {
                goToMainMenu();
            }
        } else {
            goToMainMenu();
        }

    }

    public double getRightAnchorPaneWidth() {
        return rightPane.getWidth();
    }

    /**
     * Change the tab/floorText of the map to a desired position.
     */
    public void setOpenTab() {
        SingleSelectionModel<Tab> selectionModel = mapTabPane.getSelectionModel();
        String floorName = KioskApplication.getFloor();
        if (floorName.equals("1")) {
            selectionModel.select(floor1Tab);
        } else if (floorName.equals("2")) {
            selectionModel.select(floor2Tab);
        } else if (floorName.equals("3")) {
            selectionModel.select(floor3Tab);
        } else if (floorName.equals("4")) {
            selectionModel.select(floor4Tab);
        } else if (floorName.equals("G")) {
            selectionModel.select(groundFloorTab);
        } else if (floorName.equals("L1")) {
            selectionModel.select(lowerfloor1Tab);
        } else if (floorName.equals("L2")) {
            selectionModel.select(lowerfloor2Tab);
        }
    }

    /**
     * method to reset zoom and pan of Main Screen map to default.
     */
    public void resetMainScreenMap() {
        String floorName = KioskApplication.getFloor();
        if (floorName.equals("1")) {
            anchorPaneF1.setScaleX(1);
            anchorPaneF1.setScaleY(1);
            anchorPaneF1.setTranslateX(0);
            anchorPaneF1.setTranslateY(0);
        } else if (floorName.equals("2")) {
            anchorPaneF2.setScaleX(1);
            anchorPaneF2.setScaleY(1);
            anchorPaneF2.setTranslateX(0);
            anchorPaneF2.setTranslateY(0);
        } else if (floorName.equals("3")) {
            anchorPaneF3.setScaleX(1);
            anchorPaneF3.setScaleY(1);
            anchorPaneF3.setTranslateX(0);
            anchorPaneF3.setTranslateY(0);
        } else if (floorName.equals("4")) {
            anchorPaneF4.setScaleX(1);
            anchorPaneF4.setScaleY(1);
            anchorPaneF4.setTranslateX(0);
            anchorPaneF4.setTranslateY(0);
        } else if (floorName.equals("G")) {
            anchorPaneGf.setScaleX(1);
            anchorPaneGf.setScaleY(1);
            anchorPaneGf.setTranslateX(0);
            anchorPaneGf.setTranslateY(0);
        } else if (floorName.equals("L1")) {
            anchorPaneLf1.setScaleX(1);
            anchorPaneLf1.setScaleY(1);
            anchorPaneLf1.setTranslateX(0);
            anchorPaneLf1.setTranslateY(0);
        } else if (floorName.equals("L2")) {
            anchorPaneLf2.setScaleX(1);
            anchorPaneLf2.setScaleY(1);
            anchorPaneLf2.setTranslateX(0);
            anchorPaneLf2.setTranslateY(0);
        }
    }

    @FXML
    private void floorHandler(Event event) {
        if (event.getSource() == floor1Tab) {
            KioskApplication.setFloor("1");
        } else if (event.getSource() == floor2Tab) {
            KioskApplication.setFloor("2");
        } else if (event.getSource() == floor3Tab) {
            KioskApplication.setFloor("3");
        } else if (event.getSource() == floor4Tab) {
            KioskApplication.setFloor("4");
        } else if (event.getSource() == groundFloorTab) {
            KioskApplication.setFloor("G");
        } else if (event.getSource() == lowerfloor1Tab) {
            KioskApplication.setFloor("L1");
        } else if (event.getSource() == lowerfloor2Tab) {
            KioskApplication.setFloor("L2");
        }
        if (KioskApplication.getController() != null) {
            KioskApplication.getController().setFloor();
        }
    }
}
