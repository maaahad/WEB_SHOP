/*
 * Orders info entity for keeping record of the users, persisted by JPA
 */
package se.kth.id2212.project.webshop.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author maaahad
 */
@NamedQueries({
    @NamedQuery(
            name = "SelecetOrderForUserAndProduct",
            query = "SELECT order FROM ORDERS order WHERE order.user.userName LIKE :userName AND order.gnome.id LIKE :gnomeID"
    ),
    @NamedQuery(
            name = "SelectAllOrdersForUser",
            query = "SELECT order FROM ORDERS order WHERE order.user.userName LIKE :userName"
    ),
    @NamedQuery(
            name = "SelectAllOrdersForProduct",
            query = "SELECT order FROM ORDERS order WHERE order.gnome.id LIKE :gnomeID"
    ),
    @NamedQuery(
            name = "DeleteOrderForUser",
            query = "DELETE FROM ORDERS order WHERE order.orderID LIKE :orderID"
    ),
    @NamedQuery(
            name = "DeleteOrderForProduct",
            query = "DELETE FROM ORDERS order WHERE order.gnome.id LIKE :gnomeID"
    )
})

@Entity(name = "ORDERS")
public class OrderEntity implements Serializable, OrderDTO {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "OrderID")
    private String orderID;
    @Column(name = "Amount_To_Buy")
    private int amountToBuy;

    // Many to one mapping with usersEntity
    @ManyToOne
    @JoinColumn(name = "CustomerID")
    private UserEntity user;

    // OneToOne relation with gnome
    @ManyToOne
    @JoinColumn(name = "GnomeID")
    private GnomeEntity gnome;

    public GnomeEntity getGnome() {
        return gnome;
    }

    public void setGnome(GnomeEntity gnome) {
        this.gnome = gnome;
    }

    public OrderEntity() {
    }

    // Constructor
    public OrderEntity(int amountToBuy) {
        this.amountToBuy = amountToBuy;
        //this.orderID = UUID.randomUUID().toString();
        this.orderID = generateID();
    }

    @Override
    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    @Override
    public int getAmountToBuy() {
        return amountToBuy;
    }

    public void setAmountToBuy(int amountToBuy) {
        this.amountToBuy = amountToBuy;
    }

    @Override
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orderID != null ? orderID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrderEntity)) {
            return false;
        }
        OrderEntity other = (OrderEntity) object;
        if ((this.orderID == null && other.orderID != null) || (this.orderID != null && !this.orderID.equals(other.orderID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CustomeID: " + this.user.getUserName() + ", OrderID: " + this.orderID;
    }

    // this method creates a unique ID for order
    private String generateID() {
        String s = "";
        double d;
        for (int i = 1; i <= 16; i++) {
            d = Math.random() * 10;
            s = s + ((int) d);
            if (i % 4 == 0 && i != 16) {
                s = s + "-";
            }
        }
        return s;
    }

}
