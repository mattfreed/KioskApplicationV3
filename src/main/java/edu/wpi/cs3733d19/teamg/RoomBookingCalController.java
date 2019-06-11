package edu.wpi.cs3733d19.teamg;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarEvent;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.page.DayPage;
import edu.wpi.cs3733d19.teamg.models.Booking;
import edu.wpi.cs3733d19.teamg.models.Room;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Callback;

import java.awt.print.Book;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.ResourceBundle;
import javax.persistence.EntityManager;

public class RoomBookingCalController implements Initializable {

    private Runnable mapUpdater;

    private Room room;

    @FXML
    private CalendarView calendarView;

    @FXML
    private Label title;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        calendarView.setShowAddCalendarButton(false);
        calendarView.setShowSourceTrayButton(false);
        calendarView.getWeekPage().setHidden(true);
        calendarView.getMonthPage().setHidden(true);
        calendarView.getYearPage().setHidden(true);
        calendarView.setShowPageSwitcher(false);
        calendarView.getDayPage().setDayPageLayout(DayPage.DayPageLayout.DAY_ONLY);
        calendarView.getDayPage().setShowDayPageLayoutControls(false);
        calendarView.setStartTime(LocalTime.of(8, 0, 0, 0));
        calendarView.setEndTime(LocalTime.of(17, 0, 0, 0));
    }

    /**
     * Set the room whose bookings to display.
     *
     * @param room the room whose bookings to display
     */
    void setRoom(Room room) {
        if (room == null) {
            assert false;
            title.setText(null);
            calendarView.getCalendarSources().clear();
        }
        calendarView.getCalendarSources().clear();
        this.room = room;
        CalendarSource calSource = new CalendarSource(room.getShortName());
        Calendar calendar = CalendarUtil.getRoomCalendar(room);
        calSource.getCalendars().add(calendar);
        calendarView.getCalendarSources().add(calSource);
        EventHandler<CalendarEvent> handler = event -> calendarEventHandler(event);
        calendar.addEventHandler(handler);
        title.setText(room.getLongName());
    }

    private void calendarEventHandler(CalendarEvent event) {
        Entry entry = event.getEntry();
        if (room.getShortName().contains("Desk")) {
            if (Duration.between(ZonedDateTime.now(), entry.getStartAsZonedDateTime())
                    .compareTo(Duration.ofMinutes(15)) > 0) {
                entry.setInterval(ZonedDateTime.now(), entry.getEndAsZonedDateTime());
            }
        }
        Date start = Date.from(entry.getStartAsZonedDateTime().toInstant());
        Date end = Date.from(entry.getEndAsZonedDateTime().toInstant());
        EventType<? extends Event> eventType = event.getEventType();
        boolean conflict = false;
        for (Booking b : Booking.getAllForRoom(room, KioskApplication.getEntityManager())) {
            if (!b.isCancelled() && (entry.getUserObject() == null || !entry.getUserObject().equals(b))) {
                if ((start.compareTo(b.getEnd()) <= 0
                        && start.compareTo(b.getStart()) >= 0)
                        || (end.compareTo(b.getStart()) >= 0
                        && end.compareTo(b.getEnd()) <= 0)
                        || (start.compareTo(b.getStart())) < 0 &&
                        end.compareTo(b.getEnd()) > 0) {
                    conflict = true;
                }
            }
        }
        if (eventType == CalendarEvent.ENTRY_CALENDAR_CHANGED) {
            if (entry.getCalendar() != null) {
                if (entry.getUserObject() == null) {
                    if (conflict) {
                        entry.getCalendar().removeEntry(entry);
                        return;
                    }
                    Booking booking = new Booking(entry.getTitle(), KioskApplication.getCurrentUser(), room,
                            Date.from(entry.getStartAsZonedDateTime().toInstant()),
                            Date.from(entry.getEndAsZonedDateTime().toInstant()));
                    EntityManager em = KioskApplication.getEntityManager();
                    em.getTransaction().begin();
                    em.persist(booking);
                    em.getTransaction().commit();
                    entry.setLocation(room.getLongName());
                    entry.setUserObject(booking);
                }
            } else {
                if (entry.getUserObject() != null) {
                    Booking bookingToCancel = (Booking) entry.getUserObject();
                    bookingToCancel.cancel();
                    EntityManager em = KioskApplication.getEntityManager();
                    em.getTransaction().begin();
                    em.merge(bookingToCancel);
                    em.getTransaction().commit();
                }
            }
            mapUpdater.run();
        } else if (eventType == CalendarEvent.ENTRY_INTERVAL_CHANGED
                || eventType == CalendarEvent.ENTRY_TITLE_CHANGED) {
            if (conflict) {
                Booking b = (Booking) entry.getUserObject();
                entry.setInterval(LocalDateTime.ofInstant(b.getStart().toInstant(), ZoneId.systemDefault()),
                        LocalDateTime.ofInstant(b.getEnd().toInstant(), ZoneId.systemDefault()));
                return;
            }
            CalendarUtil.updateBooking(entry);
            mapUpdater.run();

        }
        mapUpdater.run();
    }

    public void updateMapCallback(Runnable mapCallBack) {
        this.mapUpdater = mapCallBack;
    }
}
