package edu.wpi.cs3733d19.teamg.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;


@Entity
public class SecurityRequest extends ServiceRequest {

    protected SecurityRequest() {
    }

    /**
     * Creates a new Security Request.
     *
     * @param location    the location of the request
     * @param requester   the employee who created the request
     * @param timeCreated when the request was created
     * @param issue       what the issue is
     * @param manager     the entity manager in which to store the request
     */
    public SecurityRequest(Node location, Employee requester, Date timeCreated,
                           String issue, EntityManager manager) {
        super(ServiceRequestCategory.getSecurityCategory(manager), location,
                requester, timeCreated, issue);
    }

    /**
     * Returns a list of all SecurityRequests in the database.
     *
     * @param entityManager the entityManager to search for SecurityRequests
     * @return all the SecurityRequest in the database
     */
    public static List<SecurityRequest> getAll(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SecurityRequest> cq =
                criteriaBuilder.createQuery(SecurityRequest.class);
        cq.from(SecurityRequest.class);
        TypedQuery<SecurityRequest> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public String getDetails() {
        StringBuilder toDisplay = new StringBuilder();
        toDisplay.append("No additional details");

        return toDisplay.toString();
    }


}
