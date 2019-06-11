package edu.wpi.cs3733d19.teamg.models;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Entity
public class Edge {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "from_node_id", updatable = false)
    private Node fromNode;

    @ManyToOne
    @JoinColumn(name = "to_node_id", updatable = false)
    private Node toNode;

    @Column(nullable = false)
    int cost;

    protected Edge() {}

    /**
     * Generates a new edge between the given nodes with the given cost.
     * @param fromNode the node to start at
     * @param toNode the node to end at
     * @param cost the cost of the edge
     */
    public Edge(Node fromNode, Node toNode, int cost) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.cost = cost;
        fromNode.addEdge(this);
    }

    public int getId() {
        return id;
    }

    /**
     * Returns all Edges in the database.
     * @param entityManager the entityManager to retrieve Edges from
     * @return all Edges in the database
     */
    public static List<Edge> getAll(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Edge> cq = criteriaBuilder.createQuery(Edge.class);
        cq.from(Edge.class);
        TypedQuery<Edge> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    public Node getFromNode() {
        return fromNode;
    }

    /**
     * Returns the Edge between the two given nodes in the given entityManager.
     * @param from the Node to start from
     * @param to the Node to end at
     * @param entityManager the entityManager to retrieve from
     * @return the Edge between the two nodes
     */
    public static Edge getEdge(Node from, Node to, EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Edge> cq = criteriaBuilder.createQuery(Edge.class);
        Root<Edge> root = cq.from(Edge.class);
        cq.select(root);
        cq.where(criteriaBuilder.and(criteriaBuilder.equal(root.get("fromNode"), from),
                criteriaBuilder.equal(root.get("toNode"), to)));
        TypedQuery<Edge> query = entityManager.createQuery(cq);
        return query.getSingleResult();
    }

    public Node getToNode() {
        return toNode;
    }
}
