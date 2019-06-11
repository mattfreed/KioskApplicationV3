package edu.wpi.cs3733d19.teamg.models;

import edu.wpi.cs3733d19.teamg.KioskApplication;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Entity
public class Room extends Node {

    @OneToMany
    @JoinColumn(name = "equipment_id")
    @Cascade({CascadeType.REMOVE, CascadeType.DELETE})
    private List<Equipment> equipment;

    @Column()
    private int capacity;

    @OneToMany(mappedBy = "room")
    @Cascade({CascadeType.REMOVE, CascadeType.DELETE})
    private List<Booking> bookings;

    protected Room() {}

    /**
     * Creates a new room with the given parameters.
     * @param nodeId a unique node ID
     * @param xcoord the x-coordinate of the room
     * @param ycoord the y-coordinate of the room
     * @param floor which floor the room is on
     * @param building which building the room is in
     * @param nodeType what type of room it is
     * @param longName the long name of the room
     * @param shortName the short name of the room
     */
    public Room(String nodeId, int xcoord, int ycoord, String floor, String building,
                String nodeType, String longName, String shortName) {
        super(nodeId, xcoord, ycoord, floor, building, nodeType, longName, shortName);
        this.equipment = new ArrayList<>();
        this.bookings = new ArrayList<>();
    }

    public List<Equipment> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<Equipment> equipment) {
        this.equipment = equipment;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    void addBooking(Booking booking) {
        this.bookings.add(booking);
    }

    /**
     * Returns a list of all Rooms on a specified floor.
     * @param entityManager the entityManager to search for Nodes
     * @return all Rooms on the specified floor
     */
    public static List<Node> getAllOnFloor(String floor, EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Room> cq = criteriaBuilder.createQuery(Room.class);
        Root<Room> root = cq.from(Room.class);
        cq.select(root);
        cq.where(criteriaBuilder.equal(root.get("floor"), floor));
        return new ArrayList<Node>(entityManager.createQuery(cq).getResultList());
    }
}
