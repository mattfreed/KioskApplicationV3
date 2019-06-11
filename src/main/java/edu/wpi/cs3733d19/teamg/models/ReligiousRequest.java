package edu.wpi.cs3733d19.teamg.models;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;


@Entity
public class ReligiousRequest extends ServiceRequest {

    @Column()
    private String religion;

    @Column()
    private String service;

    @Column()
    private Date serviceDate;

    protected ReligiousRequest() {
    }

    /**
     * Create a new floral request.
     *
     * @param location    where the request is made for
     * @param requester   who requested it
     * @param timeCreated  when the request was made
     * @param religion    religion for the service
     * @param service     the type of religious service
     * @param serviceDate when the service should be performed
     * @param manager     the entityManager of the db
     */
    public ReligiousRequest(Node location, Employee requester, Date timeCreated, String religion,
                            String service, Date serviceDate, EntityManager manager) {
        super(ServiceRequestCategory.getReligiousCategory(manager), location, requester,
                timeCreated, religion + " " + service);
        this.service = service;
        this.religion = religion;
        this.serviceDate = serviceDate;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    /**
     * Returns a list of all ReligiousRequests in the database.
     *
     * @param entityManager the entityManager to search for ReligiousRequests
     * @return all the ReligiousRequests in the database
     */
    public static List<ReligiousRequest> getAll(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ReligiousRequest> cq =
                criteriaBuilder.createQuery(ReligiousRequest.class);
        cq.from(ReligiousRequest.class);
        TypedQuery<ReligiousRequest> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public String getDetails() {
        StringBuilder toDisplay = new StringBuilder();

        toDisplay.append("Service: ");
        toDisplay.append(this.service);
        toDisplay.append("\n");

        toDisplay.append("Religion: ");
        toDisplay.append(this.religion);
        toDisplay.append("\n");

        toDisplay.append("Scheduled Time: ");
        toDisplay.append(this.serviceDate);
        toDisplay.append("\n");

        return toDisplay.toString();
    }
}
