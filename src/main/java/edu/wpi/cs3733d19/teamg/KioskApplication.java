package edu.wpi.cs3733d19.teamg;

import edu.wpi.cs3733d19.teamg.models.Edge;
import edu.wpi.cs3733d19.teamg.models.Employee;
import edu.wpi.cs3733d19.teamg.models.Node;
import edu.wpi.cs3733d19.teamg.models.Room;
import edu.wpi.cs3733d19.teamg.models.ServiceRequestCategory;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public final class KioskApplication extends Application {
    public static final String DEFAULT_EMAIL = "goldgermans@gmail.com";

    private static int timeout = 1000 * 30;
    //the amount of time before resetting to main screen

    private static EntityManager entityManager;
    private static Stage primaryStage;
    private static Employee currentUser = null;
    private static Map<String, Integer> lookUp;
    public static String floor = "3";
    private static MapController mapController;
    private static MainScreenController mainScreenController;
    private static Controller controller;
    private static Stack<Runnable> history;
    private static Boolean mapEnabled = true;
    private static Employee employeeSelected;
    private static long lastAction = System.currentTimeMillis();
    private static ScheduledExecutorService exec;

    private static Scene primaryScene = null;
    private static Image firstFloorImage = null;
    private static Image secondFloorImage = null;
    private static Image thirdFloorImage = null;
    private static Image fourthFloorImage = null;
    private static Image groundFloorImage = null;
    private static Image lowerLevelOneImage = null;
    private static Image lowerLevelTwoImage = null;
    private static Image oneIcon = null;
    private static Image twoIcon = null;
    private static Image threeIcon = null;
    private static Image fourIcon = null;
    private static Image groundIcon = null;
    private static Image Lf1Icon = null;
    private static Image Lf2Icon = null;
    private static Image duck = null;

    private static Image smallConf1 = null;
    private static Image smallConf2 = null;
    private static Image smallConf3 = null;
    private static Image smallConf4 = null;
    private static Image smallConf5 = null;
    private static Image smallConf6 = null;
    private static Image smallConf7 = null;
    private static Image smallConf8 = null;
    private static Image medConf1 = null;
    private static Image medConf2 = null;
    private static Image medConf3 = null;
    private static Image medConf4 = null;
    private static Image largeConf1 = null;
    private static Image largeConf2 = null;
    private static Image largeConf3 = null;
    private static Image smallComp1 = null;
    private static Image medComp1 = null;
    private static Image medComp2 = null;
    private static Image medComp3 = null;
    private static Image medComp4 = null;
    private static Image medComp5 = null;
    private static Image amp = null;

    private static Image smallConf1R = null;
    private static Image smallConf2R = null;
    private static Image smallConf3R = null;
    private static Image smallConf4R = null;
    private static Image smallConf5R = null;
    private static Image smallConf6R = null;
    private static Image smallConf7R = null;
    private static Image smallConf8R = null;
    private static Image medConf1R = null;
    private static Image medConf2R = null;
    private static Image medConf3R = null;
    private static Image medConf4R = null;
    private static Image largeConf1R = null;
    private static Image largeConf2R = null;
    private static Image largeConf3R = null;
    private static Image smallComp1R = null;
    private static Image medComp1R = null;
    private static Image medComp2R = null;
    private static Image medComp3R = null;
    private static Image medComp4R = null;
    private static Image medComp5R = null;
    private static Image ampR = null;

    private static Map<String, Image> book = new HashMap<>();
    private static Map<String, Image> bookRed = new HashMap<>();

    private static Node defaultLocation = null;

    public static Stack<Runnable> getHistory() {
        return history;
    }


    public static Employee getEmployeeSelected() {
        return employeeSelected;
    }

    public static void setEmployeeSelected(Employee employeeSelected) {
        KioskApplication.employeeSelected = employeeSelected;
    }

    public static Employee getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(Employee currentUser) {
        KioskApplication.currentUser = currentUser;
    }

    public static Map<String, Integer> getLookUp() {
        return lookUp;
    }

    public static MapController getMapController() {
        return mapController;
    }

    public static void setMapController(MapController mapController) {
        KioskApplication.mapController = mapController;
    }

    public static Scene getPrimaryScene() {
        return primaryScene;
    }

    public static Image getFirstFloorImage() {
        return firstFloorImage;
    }

    public static Image getSecondFloorImage() {
        return secondFloorImage;
    }

    public static Image getThirdFloorImage() {
        return thirdFloorImage;
    }

    public static Image getFourthFloorImage() {
        return fourthFloorImage;
    }

    public static Image getGroundFloorImage() {
        return groundFloorImage;
    }

    public static Image getLowerLevelOneImage() {
        return lowerLevelOneImage;
    }

    public static Image getLowerLevelTwoImage() {
        return lowerLevelTwoImage;
    }

    public static Image getSmallConf1() {
        return smallConf1;
    }

    public static Image getSmallConf2() {
        return smallConf2;
    }

    public static Image getSmallConf3() {
        return smallConf3;
    }

    public static Image getSmallConf4() {
        return smallConf4;
    }

    public static Image getSmallConf5() {
        return smallConf5;
    }

    public static Image getSmallConf6() {
        return smallConf6;
    }

    public static Image getSmallConf7() {
        return smallConf7;
    }

    public static Image getSmallConf8() {
        return smallConf8;
    }

    public static Image getMedConf1() {
        return medConf1;
    }

    public static Image getMedConf2() {
        return medConf2;
    }

    public static Image getMedConf3() {
        return medConf3;
    }

    public static Image getMedConf4() {
        return medConf4;
    }

    public static Image getLargeConf1() {
        return largeConf1;
    }

    public static Image getLargeConf2() {
        return largeConf2;
    }

    public static Image getLargeConf3() {
        return largeConf3;
    }

    public static Image getSmallComp1() {
        return smallComp1;
    }

    public static Image getMedComp1() {
        return medComp1;
    }

    public static Image getMedComp2() {
        return medComp2;
    }

    public static Image getMedComp3() {
        return medComp3;
    }

    public static Image getMedComp4() {
        return medComp4;
    }

    public static Image getMedComp5() {
        return medComp5;
    }

    public static Image getAmp() {
        return amp;
    }


    public static Image getSmallConf1R() {
        return smallConf1R;
    }

    public static Image getSmallConf2R() {
        return smallConf2R;
    }

    public static Image getSmallConf3R() {
        return smallConf3R;
    }

    public static Image getSmallConf4R() {
        return smallConf4R;
    }

    public static Image getSmallConf5R() {
        return smallConf5R;
    }

    public static Image getSmallConf6R() {
        return smallConf6R;
    }

    public static Image getSmallConf7R() {
        return smallConf7R;
    }

    public static Image getSmallConf8R() {
        return smallConf8R;
    }

    public static Image getMedConf1R() {
        return medConf1R;
    }

    public static Image getMedConf2R() {
        return medConf2R;
    }

    public static Image getMedConf3R() {
        return medConf3R;
    }

    public static Image getMedConf4R() {
        return medConf4R;
    }

    public static Image getLargeConf1R() {
        return largeConf1R;
    }

    public static Image getLargeConf2R() {
        return largeConf2R;
    }

    public static Image getLargeConf3R() {
        return largeConf3R;
    }

    public static Image getSmallComp1R() {
        return smallComp1R;
    }

    public static Image getMedComp1R() {
        return medComp1R;
    }

    public static Image getMedComp2R() {
        return medComp2R;
    }

    public static Image getMedComp3R() {
        return medComp3R;
    }

    public static Image getMedComp4R() {
        return medComp4R;
    }

    public static Image getMedComp5R() {
        return medComp5R;
    }

    public static Image getAmpR() {
        return ampR;
    }

    public static Image getDuck() {
        return duck;
    }

    public static String getFloor() {
        return floor;
    }

    public static void setFloor(String floor) {
        KioskApplication.floor = floor;
    }

    public static MainScreenController getMainScreenController() {
        return mainScreenController;
    }

    public static void setMainScreenController(MainScreenController mainScreenController) {
        KioskApplication.mainScreenController = mainScreenController;
    }

    public static Controller getController() {
        return controller;
    }

    public static void setController(Controller controller) {
        KioskApplication.controller = controller;
    }

    public static Node getDefaultLocation() {
        return defaultLocation;
    }

    public static void setDefaultLocation(Node defaultLocation) {
        KioskApplication.defaultLocation = defaultLocation;
    }

    private static void makeImages() {
        System.out.println("Images Started");

        firstFloorImage = new Image("Photos/Original Maps 4/01_thefirstfloor-sm.png", true);
        secondFloorImage = new Image("Photos/Original Maps 4/02_thesecondfloor-sm.png", true);
        thirdFloorImage = new Image("Photos/Original Maps 4/03_thethirdfloor-sm.png", true);
        fourthFloorImage = new Image("Photos/Original Maps 4/04_bookableroomsfloor4.png", true);
        groundFloorImage = new Image("Photos/Original Maps 4/00_thegroundfloor-sm.png", true);
        lowerLevelOneImage = new Image("Photos/Original Maps 4/00_thelowerlevel1-sm.png", true);
        lowerLevelTwoImage = new Image("Photos/Original Maps 4/00_thelowerlevel2-sm.png", true);
        duck = new Image("/Photos/duck.png", true);
        oneIcon = new Image("Photos/1.png", true);
        twoIcon = new Image("Photos/2.png", true);
        threeIcon = new Image("Photos/3.png", true);
        fourIcon = new Image("Photos/4.png", true);
        groundIcon = new Image("Photos/GF.png", true);
        Lf1Icon = new Image("Photos/LF1.png", true);
        Lf2Icon = new Image("Photos/LF2.png", true);


        smallConf1 = new Image("Photos/BookableRooms/SmallConf1.png", true);
        smallConf2 = new Image("Photos/BookableRooms/SmallConf2.png", true);
        smallConf3 = new Image("Photos/BookableRooms/SmallConf3.png", true);
        smallConf4 = new Image("Photos/BookableRooms/SmallConf4.png", true);
        smallConf5 = new Image("Photos/BookableRooms/SmallConf5.png", true);
        smallConf6 = new Image("Photos/BookableRooms/SmallConf6.png", true);
        smallConf7 = new Image("Photos/BookableRooms/SmallConf7.png", true);
        smallConf8 = new Image("Photos/BookableRooms/SmallConf8.png", true);
        medConf1 = new Image("Photos/BookableRooms/MediumConf1.png", true);
        medConf2 = new Image("Photos/BookableRooms/MediumConf2.png", true);
        medConf3 = new Image("Photos/BookableRooms/MediumConf3.png", true);
        medConf4 = new Image("Photos/BookableRooms/MediumConf4.png", true);
        largeConf1 = new Image("Photos/BookableRooms/LargeConf1.png", true);
        largeConf2 = new Image("Photos/BookableRooms/LargeConf2.png", true);
        largeConf3 = new Image("Photos/BookableRooms/LargeConf3.png", true);
        smallComp1 = new Image("Photos/BookableRooms/smallComp1.png", true);
        medComp1 = new Image("Photos/BookableRooms/MediumComp1.png", true);
        medComp2 = new Image("Photos/BookableRooms/MediumComp2.png", true);
        medComp3 = new Image("Photos/BookableRooms/MediumComp3.png", true);
        medComp4 = new Image("Photos/BookableRooms/MediumComp4.png", true);
        medComp5 = new Image("Photos/BookableRooms/MediumComp5.png", true);
        amp = new Image("Photos/BookableRooms/Amp.png", true);


        smallConf1R = new Image("Photos/BookableRooms/SmallConf1R.png", true);
        smallConf2R = new Image("Photos/BookableRooms/SmallConf2R.png", true);
        smallConf3R = new Image("Photos/BookableRooms/SmallConf3R.png", true);
        smallConf4R = new Image("Photos/BookableRooms/SmallConf4R.png", true);
        smallConf5R = new Image("Photos/BookableRooms/SmallConf5R.png", true);
        smallConf6R = new Image("Photos/BookableRooms/SmallConf6R.png", true);
        smallConf7R = new Image("Photos/BookableRooms/SmallConf7R.png", true);
        smallConf8R = new Image("Photos/BookableRooms/SmallConf8R.png", true);
        medConf1R = new Image("Photos/BookableRooms/MediumConf1R.png", true);
        medConf2R = new Image("Photos/BookableRooms/MediumConf2R.png", true);
        medConf3R = new Image("Photos/BookableRooms/MediumConf3R.png", true);
        medConf4R = new Image("Photos/BookableRooms/MediumConf4R.png", true);
        largeConf1R = new Image("Photos/BookableRooms/LargeConf1R.png", true);
        largeConf2R = new Image("Photos/BookableRooms/LargeConf2R.png", true);
        largeConf3R = new Image("Photos/BookableRooms/LargeConf3R.png", true);
        smallComp1R = new Image("Photos/BookableRooms/smallComp1R.png", true);
        medComp1R = new Image("Photos/BookableRooms/MediumComp1R.png", true);
        medComp2R = new Image("Photos/BookableRooms/MediumComp2R.png", true);
        medComp3R = new Image("Photos/BookableRooms/MediumComp3R.png", true);
        medComp4R = new Image("Photos/BookableRooms/MediumComp4R.png", true);
        medComp5R = new Image("Photos/BookableRooms/MediumComp5R.png", true);
        ampR = new Image("Photos/BookableRooms/AmpR.png", true);


        System.out.println("Images Successful.");
    }

    /**
     * main.
     *
     * @param args command line args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * All of the starting functionallity to be run while the loading screen is showing.
     */
    private static void onStart() {
        history = new Stack<>();
        PathFindController.setPath(new ArrayList<>());
        lookUp = new HashMap<>();
        lookUp.put("1", 1);
        lookUp.put("2", 2);
        lookUp.put("3", 3);
        lookUp.put("4", 4);
        lookUp.put("G", 0);
        lookUp.put("L1", -1);
        lookUp.put("L2", -2);

        File applicationData = new File("applicationData");
        boolean freshDb = !applicationData.exists();
        entityManager = Persistence.createEntityManagerFactory("kiosk")
                .createEntityManager();
        if (freshDb) { // replace this in the main method of KioskApplication
            System.out.println("New database detected. Filling with sample data.");
            loadData();
        } else if (Settings.getSettings().get("reinit").equals(true)) {
            System.out.println("New database reinitialization set. Dropping and rebuilding"
                    + " the database.");
            deleteData();
            loadData();
        }
        boolean foundDefault = false;
        for (Node node : Node.getAll(entityManager)) {
            if (Settings.getSettings().get("default-location").equals(node.getNodeId())) {
                setDefaultLocation(node);
                foundDefault = true;
                break;
            }
        }
        if (!foundDefault) {
            System.out.println("Could not find default location");
        }
        exec = Executors.newSingleThreadScheduledExecutor();
        setTimeoutDispatcher(primaryStage);
    }

    private static void deleteData() {
        entityManager.getTransaction().begin();
        entityManager.createQuery("delete from ServiceRequest").executeUpdate();
        entityManager.createQuery("delete from Booking").executeUpdate();
        entityManager.createQuery("delete from Employee").executeUpdate();
        entityManager.createQuery("delete from Edge").executeUpdate();
        entityManager.createQuery("delete from Node").executeUpdate();
        entityManager.createQuery("delete from ServiceRequestCategory").executeUpdate();
        entityManager.getTransaction().commit();
    }

    private static void loadData() {
        entityManager.getTransaction().begin();

        System.out.println("Loading nodes.csv.");
        File nodes = new File("nodes.csv");
        try {
            Scanner nodeScanner = new Scanner(nodes).useDelimiter("[,\n]");
            nodeScanner.nextLine();//skip header line
            while (nodeScanner.hasNext()) {
                String nodeId = nodeScanner.next().trim();
                int xcoord = nodeScanner.nextInt();
                int ycoord = nodeScanner.nextInt();
                String floor = nodeScanner.next().trim();
                String building = nodeScanner.next().trim();
                String nodeType = nodeScanner.next().trim();
                String longName = nodeScanner.next().trim();
                String shortName = nodeScanner.next();
                if (nodeType.equals("BR")) {
                    entityManager.persist(new Room(nodeId, xcoord, ycoord, floor,
                            building, nodeType, longName, shortName));
                } else {
                    entityManager.persist(new Node(nodeId, xcoord, ycoord, floor,
                            building, nodeType, longName, shortName));
                }
            }
            System.out.println("Nodes loaded.");
        } catch (FileNotFoundException exception) {
            System.out.println("nodes.csv not found.");
        }

        System.out.println("Loading edges.csv.");
        List<Node> graph = Node.getAll(entityManager);
        File edges = new File("edges.csv");
        try {
            Scanner edgeScanner = new Scanner(edges).useDelimiter("[,\n]");
            edgeScanner.nextLine();//skip header line
            List<Edge> createdEdges = new ArrayList<>();
            while (edgeScanner.hasNext()) {
                edgeScanner.next();//skip edge ID
                String nodeId1 = edgeScanner.next().trim();
                String nodeId2 = edgeScanner.next().trim();
                Node node1 = null;
                Node node2 = null;
                for (Node n : graph) {
                    if (n.getNodeId().equals(nodeId1)) {
                        node1 = n;
                    }
                    if (n.getNodeId().equals(nodeId2)) {
                        node2 = n;
                    }
                }
                if (node1 != null && node2 != null) {
                    int cost;
                    if (node1.getFloor().equals(node2.getFloor())) {
                        int xdiff = node1.getXCoord() - node2.getXCoord();
                        int ydiff = node1.getYCoord() - node2.getYCoord();
                        cost = (int) Math.sqrt(xdiff * xdiff + ydiff * ydiff);
                    } else {
                        cost = 100000;
                    }
                    boolean exists = false;
                    for (Edge edge : createdEdges) {
                        if (edge.getFromNode() == node1 && edge.getToNode() == node2) {
                            exists = true;
                        }
                    }
                    if (!exists) {
                        Edge e1 = new Edge(node1, node2, cost);
                        Edge e2 = new Edge(node2, node1, cost);
                        createdEdges.add(e1);
                        createdEdges.add(e2);
                        entityManager.persist(e1);
                        entityManager.persist(e2);
                    }
                }
            }
            System.out.println("Edges loaded.");
        } catch (FileNotFoundException exception) {
            System.out.println("Edges.csv not found.");
        }

        List<ServiceRequestCategory> categoryList = new ArrayList<>();
        categoryList.add(new ServiceRequestCategory("Maintenance"));
        categoryList.add(new ServiceRequestCategory("Custodian"));
        categoryList.add(new ServiceRequestCategory("Floral"));
        categoryList.add(new ServiceRequestCategory("Security"));
        categoryList.add(new ServiceRequestCategory("Religious"));
        categoryList.add(new ServiceRequestCategory("It"));
        categoryList.add(new ServiceRequestCategory("Delivery"));
        categoryList.add(new ServiceRequestCategory("LanguageInterpreter"));
        categoryList.add(new ServiceRequestCategory("Transportation"));
        categoryList.forEach(entityManager::persist);

        SampleData.generate();

        for (int i = 0; i < SampleData.getEmployees().size(); i++) {
            Employee tempEmployee = SampleData.getEmployees().get(i);
            if (tempEmployee.isAdmin()) {
                categoryList.forEach(tempEmployee::addCanService);
            } else {
                tempEmployee.addCanService(categoryList.get(i % categoryList.size()));
                tempEmployee.addCanService(categoryList.get((i * 3) % categoryList.size()));
            }
            entityManager.persist(tempEmployee);
        }

        SampleData.getBookings().forEach(entityManager::persist);
        SampleData.getRequests().forEach(entityManager::persist);

        entityManager.getTransaction().commit();
        System.out.println("Successfully loaded data.");
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Method to switch a current AnchorPane to a desired AnchorPane.
     *
     * @param current current AnchorPane displayed.
     * @param desired AnchorPane to be displayed.
     */
    public static void switchPanel(AnchorPane current, AnchorPane desired) {

        if (desired == null) {
            current.getChildren().clear();
        } else {
            AnchorPane.setLeftAnchor(desired, 0.0);
            AnchorPane.setRightAnchor(desired, 0.0);
            AnchorPane.setTopAnchor(desired, 0.0);
            AnchorPane.setBottomAnchor(desired, 0.0);

            current.getChildren().setAll(desired);
        }

    }

    public static Boolean getMapEnabled() {
        return mapEnabled;
    }

    public static void setMapEnabled(Boolean mapEnabled) {
        KioskApplication.mapEnabled = mapEnabled;
    }

    public static Image getOneIcon() {
        return oneIcon;
    }

    public static Image getTwoIcon() {
        return twoIcon;
    }

    public static Image getThreeIcon() {
        return threeIcon;
    }

    public static Image getFourIcon() {
        return fourIcon;
    }

    public static Image getGroundIcon() {
        return groundIcon;
    }

    public static Image getLf1Icon() {
        return Lf1Icon;
    }

    public static Image getLf2Icon() {
        return Lf2Icon;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        KioskApplication.primaryStage = primaryStage;
        Parent loadingRoot = FXMLLoader.load(getClass()
                .getResource("/layout/UI_SoftEng_LoadingScreen.fxml"));
        primaryStage.setScene(new Scene(loadingRoot));
        primaryStage.setFullScreen((Boolean) Settings.getSettings().get("fullscreen"));
        primaryScene = primaryStage.getScene();
        primaryScene.getStylesheets().add("/CSS/JFoenixTabPaneColor.css");
        primaryStage.setOnShown(value -> {
            Task task = new Task() {
                @Override
                protected Object call() {
                    onStart();
                    makeImages();
                    return null;
                }
            };
            task.setOnSucceeded(event -> {
                Parent mainMenuRoot = null;
                try {
                    mainMenuRoot = FXMLLoader.load(getClass()
                            .getResource("/layout/UI_SoftEng_G.fxml"));
                } catch (IOException exception) {
                    //will not be thrown file exists
                }

                KioskApplication.getPrimaryStage().getScene().setRoot(mainMenuRoot);
            });
            task.setOnFailed(fail -> {
                System.out.println("Failure on start up.");
                task.getException().printStackTrace();
                Platform.exit();
            });
            new Thread(task).start();
        });
        primaryStage.show();
    }


    private static void setTimeoutDispatcher(Stage current) {
        current.setEventDispatcher(new EventDispatcher() {
            final EventDispatcher currentEventDispatcher =
                    current.getEventDispatcher();
            Runnable run = new Runnable() {

                @Override
                public void run() {
                    if (System.currentTimeMillis() - lastAction >= timeout) {
                        setCurrentUser(null);
                        history.clear();
                        try {
                            Parent mainMenuRoot = FXMLLoader.load(getClass()
                                    .getResource("/layout/UI_SoftEng_G.fxml"));
                            KioskApplication.getPrimaryScene().setRoot(mainMenuRoot);
                        } catch (IOException exception) {
                            // This should never happen because the above file should always exist.
                        }
                    }
                }

            };
            Runnable platformRun = () -> Platform.runLater(run);

            @Override
            public Event dispatchEvent(Event event, EventDispatchChain
                    tail) {
                lastAction = System.currentTimeMillis();
                exec.schedule(platformRun, timeout, TimeUnit.MILLISECONDS);
                return currentEventDispatcher.dispatchEvent(event, tail);
            }
        });
    }

    public static int getTimeout() {
        return timeout;
    }

    public static void setTimeout(int timeout) {
        KioskApplication.timeout = timeout;
    }

    public void stop() {
        exec.shutdownNow();
        System.exit(0);
    }

    public static EntityManager getEntityManager() {
        return entityManager;
    }
}
