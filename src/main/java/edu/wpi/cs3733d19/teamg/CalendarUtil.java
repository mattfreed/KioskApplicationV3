package edu.wpi.cs3733d19.teamg;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733d19.teamg.KioskApplication;
import edu.wpi.cs3733d19.teamg.models.Booking;
import edu.wpi.cs3733d19.teamg.models.Employee;
import edu.wpi.cs3733d19.teamg.models.Node;
import edu.wpi.cs3733d19.teamg.models.Room;
import javafx.scene.text.TextAlignment;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;


public final class CalendarUtil {

    /**
     * Get the calenders for specific employee.
     * @param  employee is the employee being parsed through
     */
    public static Calendar getEmployeeCalendar(Employee employee) {
        Calendar calendar = new Calendar("My Bookings");
        for (Booking booking : employee.getBookings()) {
            if (!booking.isCancelled()) {
                Entry calendarEntry = new Entry(booking.getTitle(), new Interval(
                        booking.getStart().toInstant().atZone(ZoneId.systemDefault()),
                        booking.getEnd().toInstant().atZone(ZoneId.systemDefault())));
                calendarEntry.setId(Integer.toString(booking.getId()));
                calendarEntry.setLocation(booking.getRoom().getShortName());
                calendar.addEntry(calendarEntry);
            }
        }
        return calendar;
    }

    /**
     * Get the calenders for each room.
     * @param  room is the room being parsed through
     */

    public static Calendar getRoomCalendar(Room room) {
        Calendar calendar = new Calendar(room.getShortName());
        for (Booking booking : room.getBookings()) {
            if (!booking.isCancelled()) {
                Entry calendarEntry = new Entry(booking.getTitle(),
                        new Interval(
                                booking.getStart().toInstant().atZone(ZoneId.systemDefault()),
                                booking.getEnd().toInstant().atZone(ZoneId.systemDefault())));
                calendarEntry.setLocation(booking.getRoom().getShortName());
                calendarEntry.setUserObject(booking);
                calendar.addEntry(calendarEntry);
            }
        }
        return calendar;
    }

    /**
     * Get the calenders for each room.
     * @param calendarEntry the calender entry being added
     */

    public static void updateBooking(Entry calendarEntry) {
        Booking booking = (Booking) calendarEntry.getUserObject();
        if (booking == null) {
            return;
        }
        booking.setStart(Date.from(calendarEntry.getStartAsZonedDateTime().toInstant()));
        booking.setEnd(Date.from(calendarEntry.getEndAsZonedDateTime().toInstant()));
        booking.setTitle(calendarEntry.getTitle());
        EntityManager em = KioskApplication.getEntityManager();
        em.getTransaction().begin();
        em.merge(booking);
        em.getTransaction().commit();
    }
}
