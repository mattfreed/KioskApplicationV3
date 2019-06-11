package edu.wpi.cs3733d19.teamg.models;

import edu.wpi.cs3733d19.teamg.KioskApplication;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

@Entity
public class Employee {

    @Id
    @Column()
    private int id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    // TODO: don't store passwords in plaintext
    @Column(name = "password")
    private String password;

    @Column(name = "is_employed", nullable = false)
    private boolean isEmployed;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin;

    @Column(name = "email")
    private String email;

    @Column(name = "receive_booking_email", nullable = false)
    private boolean receiveBookingEmail;

    @Column(name = "receive_service_request_email", nullable = false)
    private boolean receiveServiceRequestEmail;

    @Column(name = "block_all_email", nullable = false)
    private boolean receiveEmail;

    @OneToMany(mappedBy = "reserver")
    private List<Booking> bookings;

    @OneToMany(mappedBy = "requester")
    private List<ServiceRequest> serviceRequests;

    @ManyToMany()
    private List<ServiceRequestCategory> canService;

    protected Employee() {
    }

    /**
     * Creates a new employee.
     *
     * @param id         the id of this employee
     * @param isEmployed if they are currently employee
     * @param isAdmin    are they an admin
     * @param password   what is there password
     */
    public Employee(int id, String firstName, String lastName, boolean isEmployed,
                    boolean isAdmin, String password, String email,
                    boolean receiveBookingEmail, boolean receiveServiceRequestEmail) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isEmployed = isEmployed;
        this.isAdmin = isAdmin;
        this.password = password;
        bookings = new ArrayList<>();
        serviceRequests = new ArrayList<>();
        canService = new ArrayList<>();
        this.email = email;
        this.receiveBookingEmail = receiveBookingEmail;
        this.receiveServiceRequestEmail = receiveServiceRequestEmail;
        this.receiveEmail = false;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Sets a new password for this employee.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public int getId() {
        return id;
    }

    public boolean isEmployed() {
        return isEmployed;
    }

    public void setEmployed(boolean employed) {
        isEmployed = employed;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public List<Booking> getBookings() {
        return new ArrayList<>(bookings);
    }

    public List<ServiceRequest> getServiceRequests() {
        return new ArrayList<>(serviceRequests);
    }

    public List<ServiceRequestCategory> getCanService() {
        return new ArrayList<>(canService);
    }

    public void setCanService(List<ServiceRequestCategory> canService) {
        this.canService = canService;
    }

    public boolean receiveBookingEmail() {
        return receiveBookingEmail;
    }

    public void setReceiveBookingEmail(boolean receiveBookingEmail) {
        this.receiveBookingEmail = receiveBookingEmail;
    }

    public boolean receiveServiceRequestEmail() {
        return receiveServiceRequestEmail;
    }

    public void setReceiveServiceRequestEmail(boolean receiveServiceRequestEmail) {
        this.receiveBookingEmail = receiveServiceRequestEmail;
    }

    public boolean getReceiveEmail() {
        return receiveEmail;
    }

    public void setReceiveEmail(boolean receiveEmail) {
        this.receiveEmail = receiveEmail;
    }


    /**
     * Adds a ServiceRequestCategory to the list that this employee can service.
     *
     * @param category the ServiceRequestCategory to add
     */
    public void addCanService(ServiceRequestCategory category) {
        if (!this.canService.contains(category)) {
            this.canService.add(category);
        }
    }

    public void removeCanService(ServiceRequestCategory category) {
        this.canService.remove(category);
    }

    public void addBooking(Booking booking) {
        this.bookings.add(booking);
    }

    /**
     * Returns all Employees in the database.
     *
     * @param entityManager the entityManager to retrieve Employees from
     * @return a list of all Employees in the database
     */
    public static List<Employee> getAll(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = criteriaBuilder.createQuery(Employee.class);
        cq.from(Employee.class);
        TypedQuery<Employee> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}
