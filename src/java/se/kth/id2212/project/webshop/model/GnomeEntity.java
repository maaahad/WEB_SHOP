/*
 * Gnome info entity for keeping record of the products, persisted by JPA
 */
package se.kth.id2212.project.webshop.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author maaahad
 */
@NamedQueries({
    @NamedQuery(
            name = "SelectAllGnome",
            query = "SELECT gnome FROM Gnome_Inventory gnome"
    ),
    @NamedQuery(
            name = "SelecetGnomeUsingType",
            query = "SELECT gnome FROM Gnome_Inventory gnome WHERE gnome.gnomeType LIKE :gnomeType"
    ),
    @NamedQuery(
            name = "DeleteGnome",
            query = "DELETE FROM Gnome_Inventory gnome WHERE gnome.id LIKE :gnomeID"
    )
})

@Entity(name = "Gnome_Inventory")
public class GnomeEntity implements Serializable, GnomeDTO {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private String id;
    @Column(name = "GnomeType")
    private String gnomeType;
    @Column(name = "GnomePrice")
    private float gnomePrice;
    @Column(name = "GnomeTotal")
    private int gnomeTotal;
    @Column(name = "GnomeAvailable")
    private int gnomeAvailable;

    // OneToMany relation to the OrderEntity
    @OneToMany(targetEntity = OrderEntity.class, mappedBy = "gnome", cascade = CascadeType.ALL)
    private List<OrderEntity> orderList;

    // default constructor
    public GnomeEntity() {
    }

    // Constructor used by the EJB
    public GnomeEntity(String gnomeType, float gnomePrice, int gnomeTotal, int gnomeAvailable) {
        //this.id = getRamdomID();
        this.id = generateID();
        this.gnomeType = gnomeType;
        this.gnomePrice = gnomePrice;
        this.gnomeTotal = gnomeTotal;
        this.gnomeAvailable = gnomeAvailable;
    }
    
    public GnomeEntity(String gnomeID,String gnomeType, float gnomePrice, int gnomeTotal, int gnomeAvailable) {
        this.id = gnomeID;
        this.gnomeType = gnomeType;
        this.gnomePrice = gnomePrice;
        this.gnomeTotal = gnomeTotal;
        this.gnomeAvailable = gnomeAvailable;
    }

    public List<OrderEntity> getOrderList() {
        return orderList;
    }

    @Override
    public int getGnomeAvailable() {
        return gnomeAvailable;
    }

    public void setGnomeAvailable(int gnomeAvailable) {
        this.gnomeAvailable = gnomeAvailable;
    }

    public void setOrderList(List<OrderEntity> orderList) {
        this.orderList = orderList;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getGnomeType() {
        return gnomeType;
    }

    public void setGnomeType(String gnomeType) {
        this.gnomeType = gnomeType;
    }

    @Override
    public float getGnomePrice() {
        return gnomePrice;
    }

    public void setGnomePrice(float gnomePrice) {
        this.gnomePrice = gnomePrice;
    }

    @Override
    public int getGnomeTotal() {
        return gnomeTotal;
    }

    public void setGnomeTotal(int gnomeTotal) {
        this.gnomeTotal = gnomeTotal;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GnomeEntity)) {
            return false;
        }
        GnomeEntity other = (GnomeEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.gnomeType;
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

    // another way of generating random ID
   private String getRamdomID(){
       return UUID.randomUUID().toString().substring(0, 8);
   }
}
