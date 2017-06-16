/*
 * This Managed Bean get the input from the user and get request from users 
 * Send the request to the UserEJB for performing business logic
 * Finally provide with the output
 * Handled by getter and setter method
 */
package se.kth.id2212.project.webshop.view;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import se.kth.id2212.project.webshop.controller.UserEJB;
import se.kth.id2212.project.webshop.exception.CartException;
import se.kth.id2212.project.webshop.model.GnomeEntity;
import se.kth.id2212.project.webshop.model.OrderEntity;
import se.kth.id2212.project.webshop.model.UserDTO;
import se.kth.id2212.project.webshop.model.UserEntity;

/**
 * 
 * @author maaahad
 */
// to get getter and setter for a field, put the cursor inside the class and press ctrl+i
@Named(value = "userBean")
@SessionScoped
public class UserBean implements Serializable {

    @EJB
    private UserEJB userEJB;

    /**
     * Creates a new instance of UserBean
     */
    private String userName;
    private String password;
    private Exception failure;
    private UserEntity currentUser;
    private GnomeEntity selectedGnome;
    private int amountToCart;
    private boolean isOnline;
    private float totalToPay;

    // Get a selected and the list of orders of the current user
    private OrderEntity selectedOrder;
    private List<OrderEntity> ordersList;

    // a @PostConstruct annotated method can be used to initiated the currentUser in the EJB
    // constructor
    public UserBean() {
        // we need initialized currentUser to be used in login
        //currentUser = new UserEntity();
        //isOnline = false;
    }

    // getter and setter methods
    public float getTotalToPay() {
        return totalToPay;
    }

    public void setTotalToPay(float totalToPay) {
        this.totalToPay = totalToPay;
    }

    public OrderEntity getSelectedOrder() {
        return selectedOrder;
    }

    public void setSelectedOrder(OrderEntity selectedOrder) {
        this.selectedOrder = selectedOrder;
    }

    public boolean isIsOnline() {
        //return isOnline;
        if (userEJB.getCurrentUser().getOnlineStatus().equals("online")) {
            return true;
        }
        return false;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public int getAmountToCart() {
        return amountToCart;
    }

    public void setAmountToCart(int amountToCart) {
        this.amountToCart = amountToCart;
    }

    public GnomeEntity getSelectedGnome() {
        return selectedGnome;
    }

    public void setSelectedGnome(GnomeEntity selectedGnome) {
        this.selectedGnome = selectedGnome;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getSuccessInRegistration() {
        return failure == null;
    }

    public UserDTO getCurrentUser() {
        //return currentUser;
        return userEJB.getCurrentUser();
    }

    public void setCurrentUser(UserEntity currentUser) {
        this.currentUser = currentUser;
    }

    public boolean getSuccessInLogIn() {
        return failure == null;
    }

    public boolean getSuccessInLogOut() {
        return failure == null;
    }

    public Exception getException() {
        return failure;
    }

    public List<OrderEntity> getOrdersList() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            // call the userEJB to get the collection of the orderlist
            this.ordersList = userEJB.getOrdersList();
        } catch (Exception e) {
            //context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), null));
            exceptionHandling(e);
        }
        return ordersList;
    }

    public void setOrdersList(List<OrderEntity> ordersList) {
        this.ordersList = ordersList;
    }

    // Exception Handling
    private void exceptionHandling(Exception e) {
        e.printStackTrace();
        failure = e;
    }

    // Business logic
    // meothod invokes by the action button by the user
    // this method is for checking
    public void registerUser() {
        String moveto = null;
        failure = null;
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            // validating userName and password
            // Removing all whitespace from the username and password
            userName = userName.replaceAll("\\s", "");
            password = password.replaceAll("\\s", "");
            System.out.println("UserName: " + userName + "\nPassword: " + password);
            if (userName.length() >= 1 && password.length() >= 4) {
                userEJB.registerUser(userName, password);
                moveto = "home_page.xhtml?faces-redirect=true";
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully registered.", null));
                System.out.println("User's have been succesfully registered...");
            } else {
                // requirement is not fulfilled
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Choose an username and password of length at least 4", null));
            }

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
            exceptionHandling(e);
        }
        //return moveto;
    }

    public void logInUser() {
        String moveto = null;
        failure = null;
        FacesContext context = FacesContext.getCurrentInstance();

        // validating userName and password
        // Removing all whitespace from the username and password
        userName = userName.replaceAll("\\s", "");
        password = password.replaceAll("\\s", "");

        System.out.println("UserName: " + userName + "\nPassword: " + password);
        try {
            // login will crreate current session user in the ejb
            userEJB.logInUser(userName, password);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Login successful", null));
            //isOnline = true;
            moveto = "user_page.xhtml?faces-redirect=true";
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
            exceptionHandling(e);
        }

        //return moveto;
    }

    public String logOutUser() {
        failure = null;
        //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userBean", null);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You are now logged out.", null));
        
        /*
        if (currentUser == null) {
            System.out.println("Current user in managed bean is null!!");
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Current user in managed bean is null!!! Bug!!! in the application user initializtion and/or registration service.", null));
        }
         */
        /*
        try {
            userEJB.logOutUser();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "log out successful.", null));
            //isOnline = false;
            //currentUser = new UserEntity();
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), null));
        }
        */
        // invalidate the session. After completion of the method the session for EJB will be invalid. 
        // This will automatically cally method annotatd @PreDestroy
        context.getExternalContext().invalidateSession();
        // need to recheck whether the the current user is set to null. It's important
        // redirect to the home page
        return "home_page.xhtml?faces-redirect=true";
    }

    public void toLogin() {
        failure = null;
    }

    // business logic related to the product selected
    public void onRowSelect(SelectEvent event) {
        this.selectedGnome = (GnomeEntity) event.getObject();
        FacesContext context = FacesContext.getCurrentInstance();

        if (this.selectedGnome == null) {
            String msg = "No product is selected!!";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
        } else {
            System.out.println("Gnome is selected in userBean");
            String msg = "Gnome selected: " + this.selectedGnome;
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
        }

    }

    public void onRowUnselect(UnselectEvent event) {
        this.selectedGnome = null;
        String msg = "Gnome unselected: " + this.selectedGnome;
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
        if (this.selectedGnome == null) {
            System.out.println(this.selectedGnome + ": is unselected");
        }
    }

    public void addToCart() {
        failure = null;
        String msg = null;
        System.out.println("Gnome: " + this.selectedGnome + "\nAmount: " + this.amountToCart);
        FacesContext context = FacesContext.getCurrentInstance();
        // call EJB'S business logic if this.amountToCart>0  and <=selectedGnome.amount for adding the selected gnome to the current user's basket
        if (this.amountToCart == 0) {
            msg = "Choose at least 1 Gnome";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
        } else if (this.amountToCart > this.selectedGnome.getGnomeAvailable()) {
            msg = "Available Gnome in stock: " + this.selectedGnome.getGnomeAvailable();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
        } else {
            // Call EJB
            try {
                userEJB.addToCart(selectedGnome, amountToCart);
                msg = "Added to the basket";
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
                this.selectedGnome = null;
                this.amountToCart = 0;
            } catch (CartException e) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
                exceptionHandling(e);
            }
        }
    }

    // method related to user cart... delete+edit+checkout
    public void onCartRowSelect(SelectEvent event) {
        this.selectedOrder = (OrderEntity) event.getObject();
        FacesContext context = FacesContext.getCurrentInstance();

        if (this.selectedOrder == null) {
            String msg = "No order is selected!!";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
        } else {
            System.out.println("Got an order to delete...");
            String msg = "Order selected: " + this.selectedOrder;
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
        }
    }

    public void onCartRowUnselect(UnselectEvent event) {
        this.selectedOrder = null;
        String msg = "Order unselected: " + this.selectedOrder;
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
        if (this.selectedOrder == null) {
            System.out.println(this.selectedOrder + ": is unselected");
        }
    }

    // this method delete the selected iorder from the cart
    public void deleteFromCart() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            System.out.println("I am here to delete a order!!!");
            userEJB.deleteFromCart(selectedOrder);
            // set null to the selectedOrder for next  use
            selectedOrder = null;
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Order succesfully deleted.", null));
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), null));
            exceptionHandling(e);
        }
    }

    public void toPayAndBuy() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            System.out.println("Selected Order ID: " + selectedOrder.getOrderID());
            userEJB.toPayAndBuy();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Congratulations. Payment Successful.", null));
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), null));
            exceptionHandling(e);
        }
    }

    // this method return the total amount to pay
    public void toCheckOut() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            this.totalToPay = userEJB.toCheckOut();
            System.out.println(this.totalToPay);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Total payment: " + this.totalToPay, null));
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), null));
            exceptionHandling(e);
        }
    }

    // this method will be call just before the session expired
    @PreDestroy
    public void clearUser() {
        System.out.println("Session is destroying");
        try {
            userEJB.logOutUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
