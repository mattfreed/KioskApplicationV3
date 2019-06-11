package edu.wpi.cs3733d19.teamg.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

@Entity
public class DeliveryRequest extends ServiceRequest {

    @ManyToOne
    private Node pickup;

    @Column()
    private Date pickupTime;

    @Column()
    private Date dropoffTime;

    protected DeliveryRequest() {
    }

    /**
     * This is the constructer for Delivery Request.
     *
     * @param location    dropoff location
     * @param requester   employee
     * @param timeCreated time stamp
     * @param issue       string
     * @param pickup      pickup location
     * @param pickupTime  Date
     * @param dropoffTime Date
     * @param manager the EntityManager in which to store the request
     */
    public DeliveryRequest(Node location, Employee requester,
                           Date timeCreated, String issue, Node pickup,
                           Date pickupTime, Date dropoffTime, EntityManager manager) {
        super(ServiceRequestCategory.getDeliveryCategory(manager),
                location, requester, timeCreated, issue);
        this.pickup = pickup;
        this.pickupTime = pickupTime;
        this.dropoffTime = dropoffTime;
    }

    public Node getPickup() {
        return pickup;
    }

    public void setPickup(Node pickup) {
        this.pickup = pickup;
    }

    public Date getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(Date pickupTime) {
        this.pickupTime = pickupTime;
    }

    public Date getDropoffTime() {
        return dropoffTime;
    }

    public void setDropoffTime(Date dropoffTime) {
        this.dropoffTime = dropoffTime;
    }

    /**
     * Returns a list of all DeliveryRequest in the database.
     *
     * @param entityManager the entityManager to search for MaintenanceRequests
     * @return all the MaintenanceRequests in the database
     */
    public static List<DeliveryRequest> getAll(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DeliveryRequest> cq =
                criteriaBuilder.createQuery(DeliveryRequest.class);
        cq.from(DeliveryRequest.class);
        TypedQuery<DeliveryRequest> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public String getDetails() {
        StringBuilder toDisplay = new StringBuilder();

        toDisplay.append("Pickup Time: ");
        toDisplay.append(this.pickupTime);
        toDisplay.append("\n");

        toDisplay.append("Pickup Location: ");
        toDisplay.append(this.pickup.getLongName());
        toDisplay.append("\n");

        toDisplay.append("Dropoff Time: ");
        toDisplay.append(this.dropoffTime);
        toDisplay.append("\n");

        toDisplay.append("Dropoff Location: ");
        toDisplay.append(this.getLocation().getLongName());
        toDisplay.append("\n");
        
        return toDisplay.toString();
    }
}
