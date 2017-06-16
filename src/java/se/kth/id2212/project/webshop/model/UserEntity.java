/*
 * User info entity for keeping record of the users persisted by JPA
 */
package se.kth.id2212.project.webshop.model;

import java.io.Serializable;
import java.util.List;
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
            name = "CheckingExistingUserWithName",
            query = "SELECT user FROM USER_INFO user WHERE user.userName LIKE :currentName"
    ),
    @NamedQuery(
            name = "SelectAllUsers",
            query = "SELECT user FROM USER_INFO user"
    )
})

@Entity(name = "USER_INFO")
public class UserEntity implements Serializable, UserDTO {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "UserName")
    private String userName;
    @Column(name = "Password")
    private String password;
    @Column(name = "PermitStatus")
    private String permit;
    @Column(name = "OnlineStatus")
    private String online;
    @Column(name = "Role")
    private String userRole;

    // OneToMany relation to the OrderEntity
    @OneToMany(targetEntity = OrderEntity.class, mappedBy = "user", cascade = CascadeType.ALL)
    private List<OrderEntity> orderList;

    public UserEntity() {
        // this thre are default. Admin will be registered manually
        this.permit = "permitted";
        this.online = "offline";
    }

    // constructor used by UserEJB
    public UserEntity(String userName, String password) {
        this.userName = userName;
        this.password = password;
        // this three are default. Admin will be registered manually
        this.permit = "permitted";
        this.online = "offline";
        this.userRole = "buyer";
    }

    public List<OrderEntity> getOrderList() {
        return orderList;
    }

    @Override
    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public void setOrderList(List<OrderEntity> orderList) {
        this.orderList = orderList;
    }

    @Override
    public String getOnlineStatus() {
        return online;
    }

    public void setOnlineStatus(String online) {
        this.online = online;
    }

    @Override
    public String getPermitStatus() {
        return permit;
    }

    public void setPermitStatus(String permit) {
        this.permit = permit;
    }

    // adding getter and setters method
    @Override
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        //hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserEntity)) {
            return false;
        }
        /*
        UserEntity other = (UserEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
         */
        return true;
    }

    @Override
    public String toString() {
        return userName;
    }

    // this method help trimming the username and password privided by the user
    public void trimUserInfo() {
        this.userName = this.userName.trim();
        this.password = this.password.trim();
    }
}
