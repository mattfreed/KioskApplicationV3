package edu.wpi.cs3733d19.teamg.models;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

@Entity
public class ItRequest extends ServiceRequest {

    protected ItRequest() {
    }

    /**
     * Creates a new IT Request.
     *
     * @param location    the location of the request
     * @param requester   the employee who created the request
     * @param timeCreated when the request was created
     * @param issue       what the issue is
     * @param manager     the entity manager in which to store the request
     */
    public ItRequest(Node location, Employee requester, Date timeCreated,
                     String issue, EntityManager manager) {
        super(ServiceRequestCategory.getItCategory(manager), location,
                requester, timeCreated, issue);
    }

    /**
     * Returns a list of all ItRequests in the database.
     *
     * @param entityManager the entityManager to search for ItRequests
     * @return all the ItRequest in the database
     */
    public static List<ItRequest> getAll(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ItRequest> cq =
                criteriaBuilder.createQuery(ItRequest.class);
        cq.from(ItRequest.class);
        TypedQuery<ItRequest> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public String getDetails() {
        StringBuilder toDisplay = new StringBuilder();
        toDisplay.append("No additional details");

        return toDisplay.toString();
    }
}
