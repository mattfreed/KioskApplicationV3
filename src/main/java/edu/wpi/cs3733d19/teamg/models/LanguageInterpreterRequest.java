package edu.wpi.cs3733d19.teamg.models;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

@Entity
public class LanguageInterpreterRequest extends ServiceRequest {

    protected LanguageInterpreterRequest() {
    }

    /**
     * Creates a new Language Interpreter Request.
     *
     * @param location    the location of the request
     * @param requester   the employee who created the request
     * @param timeCreated when the request was created
     * @param language    what language needs interpreting
     * @param manager     the entity manager in which to store the request
     */
    public LanguageInterpreterRequest(Node location, Employee requester, Date timeCreated,
                                      String language, EntityManager manager) {
        super(ServiceRequestCategory.getLanguageCategory(
                manager), location, requester, timeCreated, language);
    }

    /**
     * Returns a list of all LanguageInterpreterRequests in the database.
     *
     * @param entityManager the entityManager to search for LanguageInterpreterRequests
     * @return all the LanguageInterpreterRequests in the database
     */
    public static List<LanguageInterpreterRequest> getAll(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<LanguageInterpreterRequest> cq =
                criteriaBuilder.createQuery(LanguageInterpreterRequest.class);
        cq.from(LanguageInterpreterRequest.class);
        TypedQuery<LanguageInterpreterRequest> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public String getDetails() {
        StringBuilder toDisplay = new StringBuilder();
        toDisplay.append("No additional details");

        return toDisplay.toString();
    }
}
