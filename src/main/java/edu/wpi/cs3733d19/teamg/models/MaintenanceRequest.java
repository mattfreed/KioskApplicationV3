package edu.wpi.cs3733d19.teamg.models;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

@Entity
public class MaintenanceRequest extends ServiceRequest {

    protected MaintenanceRequest() {
    }

    /**
     * Create a new maintenance request.
     *
     * @param location    where the request is
     * @param requester   who requested this
     * @param timeCreated when was this made
     * @param issue       a description of the issue
     */
    public MaintenanceRequest(Node location, Employee requester, Date timeCreated,
                              String issue, EntityManager manager) {
        super(ServiceRequestCategory.getMaintenanceCategory(
                manager), location, requester, timeCreated, issue);
    }

    /**
     * Returns a list of all MaintenanceRequests in the database.
     *
     * @param entityManager the entityManager to search for MaintenanceRequests
     * @return all the MaintenanceRequests in the database
     */
    public static List<MaintenanceRequest> getAll(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<MaintenanceRequest> cq =
                criteriaBuilder.createQuery(MaintenanceRequest.class);
        cq.from(MaintenanceRequest.class);
        TypedQuery<MaintenanceRequest> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public String getDetails() {
        StringBuilder toDisplay = new StringBuilder();
        toDisplay.append("No additional details");

        return toDisplay.toString();
    }
}
