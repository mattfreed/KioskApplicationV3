package edu.wpi.cs3733d19.teamg.models;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Entity
public class ServiceRequestCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private String title;

    public ServiceRequestCategory() {
    }

    public ServiceRequestCategory(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    /**
     * Returns a list of all ServiceRequestCategories in the database.
     *
     * @param entityManager the entityManger representing for the database
     * @return all ServiceRequestCategories in the database
     */
    public static List<ServiceRequestCategory> getAll(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ServiceRequestCategory> cq =
                criteriaBuilder.createQuery(ServiceRequestCategory.class);
        cq.from(ServiceRequestCategory.class);
        TypedQuery<ServiceRequestCategory> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    /**
     * Gets the ServiceRequestCategory that represents MaintenanceRequests.
     *
     * @param entityManager the entityManager to use for getting the ServiceRequestCategory
     * @return the ServiceRequestCategory that represents MaintenanceRequests
     */
    static ServiceRequestCategory getMaintenanceCategory(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ServiceRequestCategory> cq =
                criteriaBuilder.createQuery(ServiceRequestCategory.class);
        Root<ServiceRequestCategory> root = cq.from(ServiceRequestCategory.class);
        cq.select(root);
        cq.where(criteriaBuilder.equal(root.get("title"), "Maintenance"));
        return entityManager.createQuery(cq).getSingleResult();
    }

    /**
     * Gets the ServiceRequestCategory that represents CustodianRequests.
     *
     * @param entityManager the entityManager to use for getting the ServiceRequestCategory
     * @return the ServiceRequestCategory that represents CustodianRequests
     */
    static ServiceRequestCategory getCustodianCategory(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ServiceRequestCategory> cq =
                criteriaBuilder.createQuery(ServiceRequestCategory.class);
        Root<ServiceRequestCategory> root = cq.from(ServiceRequestCategory.class);
        cq.select(root);
        cq.where(criteriaBuilder.equal(root.get("title"), "Custodian"));
        return entityManager.createQuery(cq).getSingleResult();
    }

    /**
     * Gets the ServiceRequestCategory that represents SecurityRequests.
     *
     * @param entityManager the entityManager to use for getting the ServiceRequestCategory
     * @return the ServiceRequestCategory that represents SecurityRequests
     */
    static ServiceRequestCategory getSecurityCategory(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ServiceRequestCategory> cq =
                criteriaBuilder.createQuery(ServiceRequestCategory.class);
        Root<ServiceRequestCategory> root = cq.from(ServiceRequestCategory.class);
        cq.select(root);
        cq.where(criteriaBuilder.equal(root.get("title"), "Security"));
        return entityManager.createQuery(cq).getSingleResult();
    }

    /**
     * Gets the ItRequestCategory that represents ItRequests.
     *
     * @param entityManager the entityManager to use for getting the ServiceRequestCategory
     * @return the ServiceRequestCategory that represents ItRequests
     */
    static ServiceRequestCategory getItCategory(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ServiceRequestCategory> cq =
                criteriaBuilder.createQuery(ServiceRequestCategory.class);
        Root<ServiceRequestCategory> root = cq.from(ServiceRequestCategory.class);
        cq.select(root);
        cq.where(criteriaBuilder.equal(root.get("title"), "It"));
        return entityManager.createQuery(cq).getSingleResult();
    }

    /**
     * Gets the ServiceRequestCategory that represents FloralRequests.
     *
     * @param entityManager the entityManager to use for getting the ServiceRequestCategory
     * @return the ServiceRequestCategory that represents FloralRequests
     */
    static ServiceRequestCategory getFloralCategory(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ServiceRequestCategory> cq =
                criteriaBuilder.createQuery(ServiceRequestCategory.class);
        Root<ServiceRequestCategory> root = cq.from(ServiceRequestCategory.class);
        cq.select(root);

        cq.where(criteriaBuilder.equal(root.get("title"), "Floral"));
        return entityManager.createQuery(cq).getSingleResult();
    }

    /**
     * Gets the ServiceRequestCategory that represents DeliveryRequests.
     *
     * @param entityManager the entityManager to use for getting the ServiceRequestCategory
     * @return the ServiceRequestCategory that represents MaintenanceRequests
     */
    static ServiceRequestCategory getDeliveryCategory(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ServiceRequestCategory> cq =
                criteriaBuilder.createQuery(ServiceRequestCategory.class);
        Root<ServiceRequestCategory> root = cq.from(ServiceRequestCategory.class);
        cq.select(root);
        cq.where(criteriaBuilder.equal(root.get("title"), "Delivery"));
        return entityManager.createQuery(cq).getSingleResult();
    }

    /**
     * Gets the ServiceRequestCategory that represents ReligiousRequests.
     *
     * @param entityManager the entityManager to use for getting the ServiceRequestCategory
     * @return the ServiceRequestCategory that represents ReligiousRequests
     */
    static ServiceRequestCategory getReligiousCategory(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ServiceRequestCategory> cq =
                criteriaBuilder.createQuery(ServiceRequestCategory.class);
        Root<ServiceRequestCategory> root = cq.from(ServiceRequestCategory.class);
        cq.select(root);
        cq.where(criteriaBuilder.equal(root.get("title"), "Religious"));
        return entityManager.createQuery(cq).getSingleResult();
    }

    /**
     * Gets the ServiceRequestCategory that represents LanguageRequests.
     *
     * @param entityManager the entityManager to use for getting the ServiceRequestCategory
     * @return the ServiceRequestCategory that represents LanguageRequests
     */
    static ServiceRequestCategory getLanguageCategory(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ServiceRequestCategory> cq =
                criteriaBuilder.createQuery(ServiceRequestCategory.class);
        Root<ServiceRequestCategory> root = cq.from(ServiceRequestCategory.class);
        cq.select(root);
        cq.where(criteriaBuilder.equal(root.get("title"), "LanguageInterpreter"));
        return entityManager.createQuery(cq).getSingleResult();
    }

    /**
     * Gets the ServiceRequestCategory that represents Transportation.
     *
     * @param entityManager the entityManager to use for getting the ServiceRequestCategory
     * @return the ServiceRequestCategory that represents Transportation
     */
    static ServiceRequestCategory getTransportationCategory(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ServiceRequestCategory> cq =
                criteriaBuilder.createQuery(ServiceRequestCategory.class);
        Root<ServiceRequestCategory> root = cq.from(ServiceRequestCategory.class);
        cq.select(root);
        cq.where(criteriaBuilder.equal(root.get("title"), "Transportation"));
        return entityManager.createQuery(cq).getSingleResult();
    }
}
