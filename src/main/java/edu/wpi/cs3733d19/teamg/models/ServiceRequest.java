package edu.wpi.cs3733d19.teamg.models;

import edu.wpi.cs3733d19.teamg.Email;
import edu.wpi.cs3733d19.teamg.KioskApplication;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
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

@Entity
public abstract class ServiceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn()
    private ServiceRequestCategory category;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Node location;

    @ManyToOne
    @JoinColumn(name = "requester_id") // TODO: make not nullable
    private Employee requester;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private Employee assignee;

    @Column(name = "datetime_created", nullable = false)
    private Date timeCreated;

    @Column(name = "datetime_resolved")
    private Date timeResolved;

    @Column()
    private String issue;

    protected ServiceRequest() {
    }

    /**
     * Creates a new service request and possibly inserts it into the db.
     *
     * @param location    where the service is requested to
     * @param requester   who requested the service
     * @param timeCreated when was the request made
     */
    public ServiceRequest(ServiceRequestCategory category, Node location,
                          Employee requester, Date timeCreated, String issue) {
        this.category = category;
        this.location = location;
        this.requester = requester;
        this.timeCreated = new Timestamp(timeCreated.getTime());
        this.issue = issue;
        location.addServiceRequest(this);

        if (requester.receiveServiceRequestEmail() && requester.getReceiveEmail()) {
            sendEmail();
        }
    }

    public String getIssue() {
        return issue;
    }

    /**
     * Set the description of the issue.
     *
     * @param issue the new description of the issue
     */
    public void setIssue(String issue) {
        this.issue = issue;
    }

    public int getId() {
        return id;
    }

    public ServiceRequestCategory getCategory() {
        return category;
    }

    public Node getLocation() {
        return location;
    }

    public void setLocation(Node location) {
        this.location = location;
    }

    public Employee getRequester() {
        return requester;
    }

    public void setRequester(Employee requester) {
        this.requester = requester;
    }

    public Employee getAssignee() {
        return assignee;
    }

    public void setAssignee(Employee assignee) {
        this.assignee = assignee;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Date getTimeResolved() {
        return timeResolved;
    }

    public void setTimeResolved(Date timeResolved) {
        this.timeResolved = timeResolved;
    }

    /**
     * Returns a list of all ServiceRequests in the database.
     *
     * @param entityManager the entityManager to search for ServiceRequests
     * @return all the ServiceRequests in the database
     */
    public static List<? extends ServiceRequest> getAll(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ServiceRequest> cq = criteriaBuilder.createQuery(ServiceRequest.class);
        cq.from(ServiceRequest.class);
        TypedQuery<ServiceRequest> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    private void sendEmail() {
        final String subject = "You have created a service request";
        final String message =
                Email.generateServiceRequestMessage(requester,
                        this, "You have created ");
        String addressee = requester.getEmail();
        if (addressee == null) {
            addressee = KioskApplication.DEFAULT_EMAIL;
        }
        final String finalAddressee = addressee;
        Executors.newSingleThreadExecutor().submit(() -> {
            Email.sendEmail(subject, message, finalAddressee);
        });
    }

    public abstract String getDetails();

}
