/*
 * This admin bean is for perform admin related jobs
 */
package se.kth.id2212.project.webshop.view;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import se.kth.id2212.project.webshop.controller.AdminEJB;
import se.kth.id2212.project.webshop.model.GnomeEntity;
import se.kth.id2212.project.webshop.model.UserEntity;

/**
 *
 * @author maaahad
 */
@Named(value = "adminBean")
@SessionScoped
public class AdminBean implements Serializable {

    @EJB
    private AdminEJB adminEJB;

    // list of users that a can achieve
    private List<UserEntity> usersList;
    private UserEntity selectedUser;

    private String userPermitStatus;
    private String userOnlineStatus;
    private String userRole;

    private String productType;
    private float productPrice;
    private int productTotal;
    private int productAvailable;

    private int amountToAdd;
    private String productID;

    @ManagedProperty(value = "#{userBean}")
    private UserBean uBean;

    /**
     * Creates a new instance of adminBean
     */
    public AdminBean() {
    }

    // getter and setters
    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public UserBean getuBean() {
        return uBean;
    }

    public void setuBean(UserBean uBean) {
        this.uBean = uBean;
    }

    public int getAmountToAdd() {
        return amountToAdd;
    }

    public void setAmountToAdd(int amountToAdd) {
        this.amountToAdd = amountToAdd;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(float productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductTotal() {
        return productTotal;
    }

    public void setProductTotal(int productTotal) {
        this.productTotal = productTotal;
    }

    public int getProductAvailable() {
        return productAvailable;
    }

    public void setProductAvailable(int productAvailable) {
        this.productAvailable = productAvailable;
    }

    public String getUserOnlineStatus() {
        return userOnlineStatus;
    }

    public void setUserOnlineStatus(String userOnlineStatus) {
        this.userOnlineStatus = userOnlineStatus;
    }

    public String getUserPermitStatus() {
        return userPermitStatus;
    }

    public void setUserPermitStatus(String userPermitStatus) {
        this.userPermitStatus = userPermitStatus;
        System.out.println("User's new permit status: " + this.userPermitStatus);
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
        System.out.println("User's new role: " + this.userRole);
    }

    public UserEntity getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(UserEntity selectedUser) {
        this.selectedUser = selectedUser;
    }

    public List<UserEntity> getUsersList() {
        this.usersList = adminEJB.showRegisteredUsers();
        return usersList;
    }

    public void setUsersList(List<UserEntity> usersList) {
        this.usersList = usersList;
    }

    // business logic
    public void onRowSelect(SelectEvent event) {
        System.out.println("Selecting new user...");
        this.selectedUser = (UserEntity) event.getObject();
        FacesContext context = FacesContext.getCurrentInstance();
        System.out.println("Selected User name: " + this.selectedUser.getUserName());
        if (this.selectedUser == null) {
            String msg = "No user is selected!!";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
        } else {
            String msg = "A User selected: " + this.selectedUser;
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
        }

    }

    public void onRowUnselect(UnselectEvent event) {
        this.selectedUser = null;
        String msg = "User unselected: " + this.selectedUser;
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
        if (this.selectedUser == null) {
            System.out.println(this.selectedUser + ": is unselected");
        }
    }

    // this method update the user's info
    public void updateUserInfo() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (this.selectedUser == null) {
            System.out.println("Selected user by admin in null. Check logic in the adminBean");
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected user is null. Bug!!! in the application admin service in changing user's info", null));
        } else {
            System.out.println("Admin: Everything is set nicely to change user status. ");
            try {
                adminEJB.updateUserInfo(this.selectedUser, userPermitStatus, userRole, userOnlineStatus);
                //this.selectedUser = null;
                //this.userPermitStatus = null;
                //this.userRole = null;
                //this.userOnlineStatus = null;
                System.out.println("Admin: user info updated succefully.");
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Admin: user info updated successfully.", null));
            } catch (Exception e) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
            }
        }
    }

    // this method add produt to the database
    public void addNewItem() {
        System.out.println("Adding new item to the database.");
        adminEJB.addNewItem(productType, productPrice, productTotal, productAvailable);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Admin: New Item added succesfully.", null));
    }

    // edit the amount of a existing product
    public void updateExistingProduct() {
        System.out.println("Updating Existing product... ");

        FacesContext context = FacesContext.getCurrentInstance();
        GnomeEntity gnome = (GnomeEntity) context.getApplication().evaluateExpressionGet(context, "#{userBean.selectedGnome}", GnomeEntity.class);
        if (gnome == null){
            System.out.println("Selected gnome is null");
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Admin: Selected product to be deleted is null. Bug!!! in the product list.", null));
        }
        System.out.println("Selected gnome ID: "+gnome.getId());
        adminEJB.updateExistingProduct(gnome, amountToAdd);
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Admin: Existing product updated.", null));

    }

    // this method delete the existing product
    public void deleteExistingProduct() {
        System.out.println("Deleting Existing product... ");
        FacesContext context = FacesContext.getCurrentInstance();
        GnomeEntity gnome = (GnomeEntity) context.getApplication().evaluateExpressionGet(context, "#{userBean.selectedGnome}", GnomeEntity.class);
        // need to handel when gnome is null
        if (gnome == null) {
            System.out.println("Selected gnome is null");
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Admin: Selected product to be deleted is null. Bug!!! in the product list.", null));
        }

        // call EJB to delete the product
        try {
            adminEJB.deleteExistingProduct(gnome);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Admin: Product deleted successfully.", null));
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Admin: Product deletion was unsuccessful.", null));
        }

    }

    // this method is for checking whether the selected user in null or not
    public void checkingSelectedUser() {
        System.out.println("UserName of the selected user: " + this.selectedUser.getUserName());
    }
}
