package edu.wpi.cs3733d19.teamg.models;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

@Entity
public class CustodianRequest extends ServiceRequest {

    protected CustodianRequest() {
    }

    /**
     * Create a new custodian request.
     *
     * @param location    the location of the request
     * @param requester   the employee who created the request
     * @param timeCreated when the request was created
     * @param issue       what the issue is
     * @param manager     the entity manager in which to store the request
     */
    public CustodianRequest(Node location, Employee requester, Date timeCreated,
                            String issue, EntityManager manager) {
        super(ServiceRequestCategory.getCustodianCategory(
                manager), location, requester, timeCreated, issue);
    }

    /**
     * Returns a list of all CustodianRequests in the database.
     *
     * @param entityManager the entityManager to search for CustodianRequests
     * @return all the CustodianRequests in the database
     */
    public static List<CustodianRequest> getAll(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CustodianRequest> cq =
                criteriaBuilder.createQuery(CustodianRequest.class);
        cq.from(CustodianRequest.class);
        TypedQuery<CustodianRequest> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public String getDetails() {
        StringBuilder toDisplay = new StringBuilder();
        toDisplay.append("No additional details");

        return toDisplay.toString();
    }
}
