package edu.wpi.cs3733d19.teamg;

import edu.wpi.cs3733d19.teamg.models.Booking;
import edu.wpi.cs3733d19.teamg.models.CustodianRequest;
import edu.wpi.cs3733d19.teamg.models.DeliveryRequest;
import edu.wpi.cs3733d19.teamg.models.Employee;
import edu.wpi.cs3733d19.teamg.models.FloralRequest;
import edu.wpi.cs3733d19.teamg.models.ItRequest;
import edu.wpi.cs3733d19.teamg.models.LanguageInterpreterRequest;
import edu.wpi.cs3733d19.teamg.models.MaintenanceRequest;
import edu.wpi.cs3733d19.teamg.models.Node;
import edu.wpi.cs3733d19.teamg.models.ReligiousRequest;
import edu.wpi.cs3733d19.teamg.models.Room;
import edu.wpi.cs3733d19.teamg.models.SecurityRequest;
import edu.wpi.cs3733d19.teamg.models.ServiceRequest;
import edu.wpi.cs3733d19.teamg.models.TransportationRequest;
import javafx.scene.paint.Color;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.persistence.EntityManager;

public final class SampleData {
    private static final List<String> FIRST_NAMES = new ArrayList<>(Arrays.asList(
            "Harry", "Hermione", "Ron", "Severus", "Albus", "Neville", "Tom",
            "Luke", "Obi-Wan", "Han", "Leia", "Anakin", "Sheev",
            "Bilbo", "Frodo", "Samwise", "Gandalf", "Aragorn",
            "Tony", "Steve", "Thor", "Bruce", "Natasha", "Clint",
            "Jeff", "Kale"
    ));
    private static final List<String> LAST_NAMES = new ArrayList<>(Arrays.asList(
            "Potter", "Granger", "Weasley", "Snape", "Dumbledore", "Longbottom", "Riddle",
            "Skywalker", "Kenobi", "Solo", "Organa", "Skywalker", "Palpatine",
            "Baggins", "Baggins", "Gamgee", "the Grey", "son of Arathorn",
            "Stark", "Rogers", "Odinson", "Banner", "Romanoff", "Barton",
            "Geoff", "Chips"
    ));
    private static final int NUM_EMPLOYEES =
            FIRST_NAMES.size() == LAST_NAMES.size() ? FIRST_NAMES.size() : -1;
    private static final int NUM_BOOKINGS = 10;
    private static final int NUM_REQUESTS = 15;
    private static final boolean BOOKING_EMAIL = true;
    private static final boolean REQUEST_EMAIL = true;

    private static List<Employee> employees = new ArrayList<>();
    private static List<Booking> bookings = new ArrayList<>();
    private static List<ServiceRequest> requests = new ArrayList<>();
    private static List<Room> databaseRooms = new ArrayList<>();
    private static List<Node> databaseNodes = new ArrayList<>();
    private static Date[] bookingStart = new Date[NUM_BOOKINGS];
    private static Date[] bookingEnd = new Date[NUM_BOOKINGS];
    private static Timestamp[] requestsCreated = new Timestamp[NUM_REQUESTS];
    private static EntityManager entityManager;

    /**
     * Generates employee data and pushes it to the database.
     */
    static void generate() {
        entityManager = KioskApplication.getEntityManager();

        databaseNodes = Node.getAll(entityManager);
        loadRoomData();

        createEmployeeData();
        createBookingData();
        createRequestData();
    }

    private static void createRequestData() {
        initializeRequestTimes();

        for (int i = 0; i < NUM_REQUESTS; i++) {
            Node location = databaseNodes.get((10 * i) % databaseNodes.size());
            Employee requester = employees.get((7 * i) % employees.size());
            Timestamp created = requestsCreated[i];

            Random random = new Random();
            int requestNum = random.nextInt(9);
            if (requestNum == 0) {
                requests.add(new CustodianRequest(location,
                        requester, created, "Jeff threw up", entityManager));
            } else if (requestNum == 1) {

                Calendar pickupCal = Calendar.getInstance();
                pickupCal.setTime(created);
                pickupCal.add(Calendar.DATE, 1);

                Calendar dropoffCal = Calendar.getInstance();
                pickupCal.setTime(created);
                pickupCal.add(Calendar.DATE, 2);

                Date pickupTime = pickupCal.getTime();
                Date dropoffTime = dropoffCal.getTime();
                Node pickup = databaseNodes.get((5 * i) % databaseNodes.size());

                requests.add(new DeliveryRequest(location, requester,
                        created, "Jeff needs his meds",
                        pickup, pickupTime, dropoffTime, entityManager));
            } else if (requestNum == 2) {
                Color color = Color.GREEN;

                Calendar delivery = Calendar.getInstance();
                delivery.setTime(created);
                delivery.add(Calendar.DATE, 1);

                Date deliveryTime = delivery.getTime();
                requests.add(new FloralRequest(location, requester, created, color,
                        42, deliveryTime, entityManager));
            } else if (requestNum == 3) {
                requests.add(new ItRequest(location, requester, created,
                        "Jeff's computer broke", entityManager));
            } else if (requestNum == 4) {
                requests.add(new LanguageInterpreterRequest(location, requester, created,
                        "Australian English", entityManager));
            } else if (requestNum == 5) {
                requests.add(new MaintenanceRequest(location, requester, created,
                        "Jeff broke his door", entityManager));
            } else if (requestNum == 6) {
                Calendar service = Calendar.getInstance();
                service.setTime(created);
                service.add(Calendar.DATE, 1);

                Date serviceTime = service.getTime();

                requests.add(new ReligiousRequest(location, requester, created, "Pastafarian",
                        "Ceremonial consumption of pasta", serviceTime, entityManager));
            } else if (requestNum == 7) {
                requests.add(new SecurityRequest(location, requester, created,
                        "Jeff is yelling at patients", entityManager));
            } else if (requestNum == 8) {
                Calendar pickup = Calendar.getInstance();
                pickup.setTime(created);
                pickup.add(Calendar.DATE, 1);

                Date pickupTime = pickup.getTime();
                requests.add(new TransportationRequest(location, requester, created,
                        "Jeff needs his stool samples picked up",
                        pickupTime, entityManager));
            }
        }
    }

    private static void createBookingData() {
        initializeBookingTimes();

        for (int i = 0; i < NUM_BOOKINGS; i++) {
            Employee reserver = employees.get((5 * i) % employees.size());
            Room room = databaseRooms.get((13 * i) % databaseRooms.size());
            Date startTime = bookingStart[i];
            Date endTime = bookingEnd[i];

            bookings.add(new Booking("Test Booking",reserver, room, startTime, endTime));
        }
    }

    private static void createEmployeeData() {
        final int baseId = 100000;
        final String password = "Password";

        employees.add(new Employee(0, "staff", "staff",
                true, true, "staff", null, true, true));
        employees.add(new Employee(baseId - 1, "Karen", "Caren",
                true, true, password, null, true, true));
        employees.add(new Employee(31415, "Elie", "Hess",
                true, true, "eliehess", "eliemhess@gmail.com", true, true));

        for (int i = 0; i < NUM_EMPLOYEES; i++) {
            int id = baseId + i;
            String fname = FIRST_NAMES.get(i);
            String lname = LAST_NAMES.get(i);
            String pword = password + i;

            employees.add(new Employee(id, fname, lname, true, false, pword,
                    null, BOOKING_EMAIL, REQUEST_EMAIL));
        }
    }

    private static void loadRoomData() {
        for (Node node : databaseNodes) {
            if (node instanceof Room && !databaseRooms.contains(node)) {
                databaseRooms.add((Room) node);
            }
        }
    }

    private static void initializeBookingTimes() {
        bookingStart[0] = makeDate(2019, 5, 1, 6, 30);
        bookingEnd[0] = makeDate(2019, 5, 1, 8, 30);
        bookingStart[1] = makeDate(2019, 5, 2, 12, 0);
        bookingEnd[1] = makeDate(2019, 5, 2, 12, 30);
        bookingStart[2] = makeDate(2019, 5, 1, 23, 0);
        bookingEnd[2] = makeDate(2019, 5, 2, 1, 0);
        bookingStart[3] = makeDate(2019, 5, 2, 4, 0);
        bookingEnd[3] = makeDate(2019, 5, 2, 5, 30);
        bookingStart[4] = makeDate(2019, 5, 2, 14, 0);
        bookingEnd[4] = makeDate(2019, 5, 2, 19, 0);
        bookingStart[5] = makeDate(2019, 5, 3, 1, 30);
        bookingEnd[5] = makeDate(2019, 5, 3, 2, 0);
        bookingStart[6] = makeDate(2019, 5, 3, 6, 0);
        bookingEnd[6] = makeDate(2019, 5, 3, 8, 0);
        bookingStart[7] = makeDate(2019, 5, 3, 15, 0);
        bookingEnd[7] = makeDate(2019, 5, 3, 16, 30);
        bookingStart[8] = makeDate(2019, 5, 3, 15, 0);
        bookingEnd[8] = makeDate(2019, 5, 3, 15, 30);
        bookingStart[9] = makeDate(2019, 5, 4, 0, 0);
        bookingEnd[9] = makeDate(2019, 5, 4, 0, 30);
    }

    private static void initializeRequestTimes() {
        requestsCreated[0] = makeTimestamp(makeDate(2019, 5, 1, 8, 0));
        requestsCreated[1] = makeTimestamp(makeDate(2019, 5, 1, 8, 30));
        requestsCreated[2] = makeTimestamp(makeDate(2019, 5, 1, 10, 0));
        requestsCreated[3] = makeTimestamp(makeDate(2019, 5, 1, 14, 30));
        requestsCreated[4] = makeTimestamp(makeDate(2019, 5, 1, 23, 0));
        requestsCreated[5] = makeTimestamp(makeDate(2019, 5, 2, 0, 30));
        requestsCreated[6] = makeTimestamp(makeDate(2019, 5, 2, 6, 0));
        requestsCreated[7] = makeTimestamp(makeDate(2019, 5, 2, 9, 0));
        requestsCreated[8] = makeTimestamp(makeDate(2019, 5, 2, 10, 30));
        requestsCreated[9] = makeTimestamp(makeDate(2019, 5, 2, 13, 0));
        requestsCreated[10] = makeTimestamp(makeDate(2019, 5, 3, 14, 0));
        requestsCreated[11] = makeTimestamp(makeDate(2019, 5, 3, 15, 0));
        requestsCreated[12] = makeTimestamp(makeDate(2019, 5, 3, 15, 0));
        requestsCreated[13] = makeTimestamp(makeDate(2019, 5, 3, 15, 0));
        requestsCreated[14] = makeTimestamp(makeDate(2019, 5, 3, 15, 30));
    }

    static List<Employee> getEmployees() {
        return employees;
    }

    static List<Booking> getBookings() {
        return bookings;
    }

    static List<ServiceRequest> getRequests() {
        return requests;
    }

    /**
     * Converts a Date to a Timestamp.
     *
     * @param date the Date to convert
     * @return the converted Timestamp.
     */
    private static Timestamp makeTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }

    /**
     * Returns a new Date object based off input with 0 seconds and 0 milliseconds.
     *
     * @param year   the year
     * @param month  the month
     * @param day    the day
     * @param hour   the hour
     * @param minute the minute
     * @return the created Date object
     */
    private static Date makeDate(int year, int month, int day, int hour, int minute) {
        return makeDate(year, month, day, hour, minute, 0);
    }

    /**
     * Returns a new Date object based off input with 0 milliseconds.
     *
     * @param year   the year
     * @param month  the month
     * @param day    the day
     * @param hour   the hour
     * @param minute the minute
     * @param second the second
     * @return the created Date object
     */
    private static Date makeDate(int year, int month, int day, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
