package edu.wpi.cs3733d19.teamg.models;

import edu.wpi.cs3733d19.teamg.Email;
import edu.wpi.cs3733d19.teamg.KioskApplication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "reserver_id")
    private Employee reserver;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "datetime_start", nullable = false)
    private Date start;

    @Column(name = "datetime_end", nullable = false)
    private Date end;

    @Column(name = "is_cancelled", nullable = false)
    private boolean isCancelled = false;

    protected Booking() {}

    /**
     * Creates a new booking.
     *
     * @param reserver  the employee reserving the room
     * @param room      the room being reserved
     * @param startDate the start date of the booking
     * @param endDate   the end date of the booking
     * @param startTime the start time of the booking
     * @param endTime   the end time of the booking
     */
    public Booking(String title, Employee reserver, Room room, LocalDate startDate,
                   LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        this(title, reserver, room, Date.from(LocalDateTime.of(startDate,
                startTime).atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.of(endDate, endTime).atZone(ZoneId
                        .systemDefault()).toInstant()));
    }

    /**
     * creates a new booking.
     *
     * @param reserver who reserved this booking
     * @param room     the room the booking is for
     * @param start    the time the booking starts
     * @param end      the time the booking ends
     */
    public Booking(String title, Employee reserver, Room room, Date start, Date end) {
        this.title = title;
        this.reserver = reserver;
        this.room = room;
        this.start = start;
        this.end = end;

        reserver.addBooking(this);
        room.addBooking(this);

        if (reserver.receiveBookingEmail() && reserver.getReceiveEmail()) {
            final String subject = "Your room booking at BWH";
            final String message = getEmailMessage(reserver, room, start, end);
            String addressee = reserver.getEmail();
            if (addressee == null) {
                addressee = KioskApplication.DEFAULT_EMAIL;
            }
            final String finalAddressee = addressee;
            Executors.newSingleThreadExecutor().submit(() -> {
                Email.sendEmail(subject, message, finalAddressee);
            });
        }
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Employee getReserver() {
        return reserver;
    }

    public Room getRoom() {
        return room;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        isCancelled = true;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    /**
     * Returns all bookings in the database.
     *
     * @param entityManager the EntityManager to get bookings from
     * @return all bookings in the database
     */
    public static List<Booking> getAll(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Booking> cq = criteriaBuilder.createQuery(Booking.class);
        cq.from(Booking.class);
        TypedQuery<Booking> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    /**
     * Returns a list of all Bookings for a specified room.
     *
     * @param entityManager the entityManager to search for Rooms
     * @return all Rooms on the specified floor
     */
    public static List<Booking> getAllForRoom(Room room, EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Booking> cq = criteriaBuilder.createQuery(Booking.class);
        Root<Booking> root = cq.from(Booking.class);
        cq.select(root);
        cq.where(criteriaBuilder.equal(root.get("room"), room));
        return entityManager.createQuery(cq).getResultList();
    }

    private String getEmailMessage(Employee employee, Room room, Date start, Date end) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(start);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(end);

        String fin = "Hi, ";
        fin += employee.getFirstName();
        fin += " ";
        fin += employee.getLastName();
        fin += "!\n\nYou have successfully booked [";
        fin += room.getLongName();
        fin += "] from ";
        fin += calendarToString(startCal);
        fin += " to ";
        fin += calendarToString(endCal);
        fin += ".\n\nHave yourself a super splendiferous day!";

        return fin;
    }

    private String calendarToString(Calendar calendar) {
        String fin = "";
        fin += calendar.get(Calendar.HOUR_OF_DAY);
        fin += ":";
        int temp = calendar.get(Calendar.MINUTE);
        if (temp < 10) {
            fin += "0";
        }
        fin += temp;
        fin += " on ";
        fin += calendar.get(Calendar.MONTH);
        fin += "/";
        fin += calendar.get(Calendar.DAY_OF_MONTH);
        fin += "/";
        fin += calendar.get(Calendar.YEAR);
        return fin;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Booking booking = (Booking) object;
        return id == booking.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
