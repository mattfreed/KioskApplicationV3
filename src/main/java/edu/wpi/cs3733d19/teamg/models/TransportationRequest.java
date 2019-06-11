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
public class TransportationRequest extends ServiceRequest {


    @Column()
    private Date pickupTime;

    protected TransportationRequest() {
    }

    /**
     * This is the constructer for Transportation Request.
     *
     * @param location      where the transportation request should take place
     * @param requester     employee
     * @param timeCreated   time stamp
     * @param issue         string
     * @param pickupTime    Date
     * @param entityManager the entity manager in which to store the request
     */
    public TransportationRequest(Node location, Employee requester,
                                 Date timeCreated, String issue, Date pickupTime,
                                 EntityManager entityManager) {
        super(ServiceRequestCategory.getTransportationCategory(
                entityManager), location, requester, timeCreated, issue);
        this.pickupTime = pickupTime;
    }

    public Date getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(Date pickupTime) {
        this.pickupTime = pickupTime;
    }

    /**
     * Returns a list of all TransportationRequest in the database.
     *
     * @param entityManager the entityManager to search for TransportationRequests
     * @return all the TransportationRequests in the database
     */
    public static List<TransportationRequest> getAll(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TransportationRequest> cq =
                criteriaBuilder.createQuery(TransportationRequest.class);
        cq.from(TransportationRequest.class);
        TypedQuery<TransportationRequest> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public String getDetails() {
        StringBuilder toDisplay = new StringBuilder();
        toDisplay.append("Pickup Time: ");
        toDisplay.append(this.pickupTime);
        toDisplay.append("\n");

        toDisplay.append("Pickup Location: ");
        toDisplay.append(this.getLocation().getLongName());
        toDisplay.append("\n");

        return toDisplay.toString();
    }
}
