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

@Entity
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private String title;

    public int getId() {
        return id;
    }

    /**
     * Returns a list of all equipment in the database.
     * @param entityManager the entityManager to search for equipment
     * @return all the equipment in the database
     */
    public static List<Equipment> getAll(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Equipment> cq = criteriaBuilder.createQuery(Equipment.class);
        cq.from(Equipment.class);
        TypedQuery<Equipment> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}
