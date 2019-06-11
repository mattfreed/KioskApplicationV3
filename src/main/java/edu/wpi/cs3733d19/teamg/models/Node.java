package edu.wpi.cs3733d19.teamg.models;

import edu.wpi.cs3733d19.teamg.KioskApplication;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Node {

    @Id
    @Column(name = "id")
    private String nodeId;

    @Column(nullable = false)
    private int xcoord;

    @Column(nullable = false)
    private int ycoord;

    @Column(nullable = false)
    private String floor;

    @Column(nullable = false)
    private String building;

    @Column(name = "type", nullable = false)
    private String nodeType;

    @Column(name = "long_name", nullable = false)
    private String longName;

    @Column(name = "short_name",nullable = false)
    private String shortName;

    @OneToMany(mappedBy = "fromNode")
    @Cascade({CascadeType.REMOVE, CascadeType.DELETE})
    private List<Edge> edges;

    @OneToMany(mappedBy = "location")
    @Cascade({CascadeType.REMOVE, CascadeType.DELETE})
    private List<ServiceRequest> serviceRequests;

    protected Node() {}

    /**
     * Constructor for Node.
     *
     * @param nodeId    the id of the node
     * @param xcoord    the x coordinate of the node on the map
     * @param ycoord    the y coordinate of the node on the map
     * @param floor     which floor the node is on
     * @param building  which building the node is in
     * @param nodeType  what type of node this is
     * @param longName  what is the long textual name of this node
     * @param shortName what is the short textual name of this node
     */
    public Node(String nodeId, int xcoord, int ycoord, String floor, String building,
                String nodeType, String longName, String shortName) {
        if (nodeId == null) {
            nodeId = UUID.randomUUID().toString();
        }
        this.nodeId = nodeId;
        this.xcoord = xcoord;
        this.ycoord = ycoord;
        this.floor = floor;
        this.building = building;
        this.nodeType = nodeType;
        this.longName = longName;
        this.shortName = shortName;
        this.edges = new ArrayList<>();
        this.serviceRequests = new ArrayList<>();
    }

    public String getNodeId() {
        return nodeId;
    }

    public int getXCoord() {
        return xcoord;
    }

    public void setXCoord(int xcoord) {
        this.xcoord = xcoord;
    }

    public int getYCoord() {
        return ycoord;
    }

    public void setYCoord(int ycoord) {
        this.ycoord = ycoord;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /**
     * removes an edge to a node.
     * @param node the node to me removed
     */
    public void removeNeighbor(Node node) {
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).getToNode() == node) {
                edges.remove(edges.get(i));
            }
        }
    }

    /**
     * Get a list of the nodes that neighbor this node.
     *
     * @return a list of the nodes that neighbor this node
     */
    public List<Node> getNeighbors() {
        List<Node> nodes = new ArrayList<>();
        for (Edge e : edges) {
            nodes.add(e.getToNode());
        }
        return nodes;
    }

    /**
     * Get the cost from this node to its neighbor.
     *
     * @param node the node you want the cost to
     * @return the cost to node or -1 if node is not a neighbor of this node
     */
    public int costTo(Node node) {
        for (Edge e : edges) {
            if (e.getToNode() == node) {
                return e.cost;
            }
        }
        return -1;
    }

    /**
     * Gets the euclidean distance to a node.
     * Distance between floors are much larger.
     *
     * @param node the target node
     * @return the distance in int
     */

    public int distanceTo(Node node) {
        int xdiff = getXCoord() - node.getXCoord();
        int ydiff = getYCoord() - node.getYCoord();
        int zdiff = floorLookUp(this.floor) - floorLookUp(node.getFloor());
        int cost = (int)Math.sqrt(xdiff * xdiff + ydiff * ydiff + zdiff * zdiff * 5000);
        return cost;
    }

    @Override
    public String toString() {
        return nodeId + "," + xcoord + "," + ycoord + "," + building + "," + floor + ","
                + nodeType + "," + longName + "," + shortName;
    }

    /**
     * Returns a list of all Nodes in the database.
     * @param entityManager the entityManager to search for Nodes
     * @return all Nodes in the database
     */
    public static List<Node> getAll(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Node> cq = criteriaBuilder.createQuery(Node.class);
        cq.select(cq.from(Node.class));
        return entityManager.createQuery(cq).getResultList();
    }

    /**
     * Returns a list of all Nodes on a specified floor.
     * @param entityManager the entityManager to search for Nodes
     * @return all Nodes on the specified floor
     */
    public static List<Node> getAllOnFloor(String floor, EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Node> cq = criteriaBuilder.createQuery(Node.class);
        Root<Node> root = cq.from(Node.class);
        cq.select(root);
        cq.where(criteriaBuilder.equal(root.get("floor"), floor));
        return entityManager.createQuery(cq).getResultList();
    }

    public void addEdge(Edge edge) {
        this.edges.add(edge);
    }

    /**
     * Check if the Y values of 2 nodes are close to one another.
     *
     * @param node the node to compare.
     */
    public boolean closeByY(Node node) {
        if (this.getYCoord() - node.getYCoord() < 300
                && this.getYCoord() - node.getYCoord() > -300) {
            return true;
        } else {
            return false;
        }
    }


    void addServiceRequest(ServiceRequest serviceRequest) {
        this.serviceRequests.add(serviceRequest);
    }

    public static int floorLookUp(String floor) {
        Map<String,Integer> lookUp = KioskApplication.getLookUp();
        return lookUp.get(floor);
    }
}
