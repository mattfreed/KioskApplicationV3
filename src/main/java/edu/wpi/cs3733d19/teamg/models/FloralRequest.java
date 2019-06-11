package edu.wpi.cs3733d19.teamg.models;

import javafx.scene.paint.Color;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

@Entity
public class FloralRequest extends ServiceRequest {

    @Column()
    private int quantity;

    @Column()
    private Date deliveryDate;

    @Column()
    private Double red;

    @Column()
    private Double green;

    @Column()
    private Double blue;

    @Column()
    private Double opacity;

    protected FloralRequest() {
    }

    /**
     * Create a new floral request.
     *
     * @param location     where the request is made for
     * @param requester    who requested it
     * @param timeCreated  when the request was made
     * @param color        what color flowers
     * @param quantity     how many flowers
     * @param deliveryDate when they should be delivered
     * @param manager      the entityManager of the db
     */
    public FloralRequest(Node location, Employee requester, Date timeCreated, Color color,
                         int quantity, Date deliveryDate, EntityManager manager) {
        super(ServiceRequestCategory.getFloralCategory(manager), location, requester,
                timeCreated, quantity + " flowers requested");
        this.quantity = quantity;
        this.deliveryDate = deliveryDate;
        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
        this.opacity = color.getOpacity();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Color getColor() {
        return new Color(red, green, blue, opacity);
    }

    /**
     * Stores the RGBO of the passed color.
     * @param color the color to store the RGBO of
     */
    public void setColor(Color color) {
        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
        this.opacity = color.getOpacity();
    }

    /**
     * Returns a list of all ReligiousRequests in the database.
     *
     * @param entityManager the entityManager to search for ReligiousRequests
     * @return all the ReligiousRequests in the database
     */
    public static List<FloralRequest> getAll(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FloralRequest> cq =
                criteriaBuilder.createQuery(FloralRequest.class);
        cq.from(FloralRequest.class);
        TypedQuery<FloralRequest> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public String getDetails() {
        StringBuilder toDisplay = new StringBuilder();
        toDisplay.append("Quantity of flower: ");
        toDisplay.append(this.quantity);
        toDisplay.append("\n");

        toDisplay.append("Delivery Date: ");
        toDisplay.append(this.deliveryDate);
        toDisplay.append("\n");


        return toDisplay.toString();
    }
}
