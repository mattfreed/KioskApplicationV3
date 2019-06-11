package edu.wpi.cs3733d19.teamg;

import static org.junit.Assert.assertTrue;

import edu.wpi.cs3733d19.teamg.models.Booking;
import edu.wpi.cs3733d19.teamg.models.Employee;
import edu.wpi.cs3733d19.teamg.models.Node;
import edu.wpi.cs3733d19.teamg.models.Room;
import edu.wpi.cs3733d19.teamg.models.ServiceRequest;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;


import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class DatabaseValidityTests {

    private static List<Employee> employees = new ArrayList<>();
    private static List<Node> graph = new ArrayList<>();
    private static List<Room> rooms = new ArrayList<>();
    private static List<Booking> bookings = new ArrayList<>();
    private static List<ServiceRequest> serviceRequests = new ArrayList<>();
    private static EntityManager entityManager;

    /**
     * Initializes data.
     */
    @BeforeClass
    public static void init() {
        entityManager = Persistence.createEntityManagerFactory("kiosk")
                .createEntityManager();

        graph = Node.getAll(entityManager);

        generateDatabaseRoomList();
        generateDatabaseBookingList();
        generateDatabaseRequestList();
    }

    private static void generateDatabaseRoomList() {
        int numRooms = 0;
        for (Node node : graph) {
            if (node instanceof Room && !rooms.contains(node)) {
                rooms.add((Room) node);
                numRooms++;
            }
        }
    }

    private static void generateDatabaseBookingList() {
        int numBookings = 0;
        for (Room room : rooms) {
            for (Booking booking : room.getBookings()) {
                if (!bookings.contains(booking)) {
                    bookings.add(booking);
                    numBookings++;
                }
            }
        }
    }

    private static void generateDatabaseRequestList() {
        int numRequests = 0;
        for (Employee employee : employees) {
            for (ServiceRequest sr : employee.getServiceRequests()) {
                if (!serviceRequests.contains(sr)) {
                    serviceRequests.add(sr);
                    numRequests++;
                }
            }
        }
    }

    @Test
    public void testDatabaseRequestsValid() {
        Assume.assumeTrue(graph.size() > 0);
        for (ServiceRequest sr : serviceRequests) {
            if (sr.getTimeResolved() != null) {
                assertTrue(sr.getTimeCreated().compareTo(sr.getTimeResolved()) <= 0);
            }
        }
    }

    @Test
    public void testDatabaseBookingsValid() {
        Assume.assumeTrue(rooms.size() > 0);
        for (Booking booking : bookings) {
            assertTrue(booking.getStart().before(booking.getEnd()));
        }
    }

    @Test
    public void testUniqueIDs() {
        for (int i = 0; i < employees.size(); i++) {
            for (int j = i; j < employees.size(); j++) {
                if (!employees.get(i).equals(employees.get(j))) {
                    assertTrue(employees.get(i).getId() != employees.get(j).getId());
                }
            }
        }
    }
}
