/*
 * This is the UserEJB for performing business logic reltaed to a user
 */
package se.kth.id2212.project.webshop.controller;

import java.util.List;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import se.kth.id2212.project.webshop.exception.LogInException;
import se.kth.id2212.project.webshop.exception.LogOutException;
import se.kth.id2212.project.webshop.exception.CartException;
import se.kth.id2212.project.webshop.exception.RegisterException;
import se.kth.id2212.project.webshop.model.GnomeEntity;
import se.kth.id2212.project.webshop.model.OrderEntity;
import se.kth.id2212.project.webshop.model.UserEntity;

/**
 *
 * @author maaahad
 */
// stateless, no-interface view, 
@Stateful
public class UserEJB {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    // UserEJB is stateful and hold the record of the current user. 
    // It is initiated with offline and permitted status, as this is going to be use in the view before it is fully initialized after log in
    private UserEntity currentUser = new UserEntity();
    @PersistenceContext(unitName = "PROJECT_WEBSHOPPU")
    EntityManager em;

    // a simple business logic for creating an entity instance
    public void createUser(String userName, String password) {
        UserEntity user = new UserEntity(userName, password);
        System.out.println("Persisting user " + userName);
        em.persist(user);
    }

    // getter and setter method
    public UserEntity getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserEntity currentUser) {
        this.currentUser = currentUser;
    }

    //*************************** Business Logic ************************************/
    // this method provide log in service tot the users
    public void registerUser(String userName, String password) throws RegisterException {
        Query query = em.createNamedQuery("CheckingExistingUserWithName", UserEntity.class);
        query.setParameter("currentName", userName);
        List<UserEntity> results = query.getResultList();
        if (results.size() > 1) {
            // Exception.... Duplicate entry in the databse...
            System.out.println("Duplicate entries already in the databse. Need to recheck");
            throw new RegisterException("Duplicate entries already in the databse. Need to recheck the application in User Registration Service.");
        } else if (results.size() == 1) {
            // Exception.... Username already exist, try with different name
            System.out.println(userName + " is already registered.");
            throw new RegisterException(userName + " is already registered.");
        }
        // create a new user and persist it to the underlying relational database
        UserEntity user = new UserEntity(userName, password);
        System.out.println("Persisting user " + userName);
        em.persist(user);

    }

    // this method check the user name and the password and provide the user to log in or rejected
    public void logInUser(String userName, String password) throws LogInException {
        Query query = em.createNamedQuery("CheckingExistingUserWithName", UserEntity.class);
        query.setParameter("currentName", userName);
        List<UserEntity> results = query.getResultList();
        if (results.size() > 1) {
            throw new LogInException("Duplicate entries already in the databse. Need to recheck the application in User Registration Service.");
        } else if (results.isEmpty()) {
            throw new LogInException("No user registered with name: " + userName + ". Register first.");
        }
        // if one user exist.. match the password. if password match proceed wit log in otherwise LogInException
        // System.out.println("UserName: " + userName + "\nPassword: " + password);
        // Now we initialize our session user. This will be used throughout the session and will be destroyed when session end
        this.currentUser = results.get(0);
        if (!this.currentUser.getPassword().equals(password) || !userName.equals(this.currentUser.getUserName()) ) {
            throw new LogInException("Wrong Username and/or Password!!!");
        }
        // allow to log in if the user is not ban
        if (this.currentUser.getPermitStatus().equals("banned")) {
            throw new LogInException("You are banned!!!");
        }
        // check the user is already logged in
        if (this.currentUser.getOnlineStatus().equals("online")) {
            throw new LogInException("You are already logged in!!! Bug!!! in application view.");
        }
        // Everything ok. Logged in the user and change online status of the user to online
        this.currentUser.setOnlineStatus("online");
        System.out.println(this.currentUser.getUserName() + " is now: " + this.currentUser.getOnlineStatus());
        //return this.currentUser;
    }

    // This method log out a user if the user is online
    public void logOutUser() throws LogOutException {
        Query query = em.createNamedQuery("CheckingExistingUserWithName", UserEntity.class);
        query.setParameter("currentName", this.currentUser.getUserName());
        List<UserEntity> results = query.getResultList();
        if (results.isEmpty()) {
            throw new LogOutException("No user to log out!!! Bug!!! logout option should not be exposed!!.");
        } else if (results.size() > 1) {
            throw new LogOutException("Multuple user with the same username is registered. Bug!!! in the registration service.");
        }

        // now get the user to logout
        if (results.get(0).getOnlineStatus().equals("offline")) {
            throw new LogOutException("The user is already offline!!! Bug!!! logout option should not be exposed!!.");
        }
        //Everything ok. Log out the user and change the online status to offline in the database as well as 
        results.get(0).setOnlineStatus("offline");
        System.out.println(results.get(0).getUserName() + " is now " + results.get(0).getOnlineStatus());
    }

    // this method add items to the current users basket
    public void addToCart(GnomeEntity gnome, int amountToBuy) throws CartException {
        
        /*
        // checking whether the user is online
        Query query = em.createNamedQuery("CheckingExistingUserWithName", UserEntity.class);
        query.setParameter("currentName", currentUser.getUserName());
        List<UserEntity> results = query.getResultList();
        if (results.isEmpty()) {
            throw new CartException("You are not registered!!! Register first.");
        } else if (results.size() > 1) {
            throw new CartException("Multiple user registered in the database!!! Bug!! in the application registration service.");
        }
        // If oner user found checking wheter the user is online or offline
        if (results.get(0).getOnlineStatus().equals("offline")) {
            throw new CartException("The user is offline!!! Should not ended up here. currentUser should not have username after logout.");
        }
        */
        // online..... check whether the users already got an existing order with the same product
        Query orderQuery = em.createNamedQuery("SelecetOrderForUserAndProduct", OrderEntity.class);
        orderQuery.setParameter("userName", currentUser.getUserName());
        orderQuery.setParameter("gnomeID", gnome.getId());
        List<OrderEntity> orders = orderQuery.getResultList();

        if (orders.size() > 1) {
            throw new CartException("Multiple order for the same user and the same product!!! For the same user and product just need to adjust the amountToBuy property.");
        } else if (orders.size() == 1) {
            // just need to update the amount to buy element
            int quantity = orders.get(0).getAmountToBuy() + amountToBuy;
            orders.get(0).setAmountToBuy(quantity);
            // update the available gnome
            orders.get(0).getGnome().setGnomeAvailable(orders.get(0).getGnome().getGnomeAvailable() - amountToBuy);
            System.out.println("Amount has been updated to: " + orders.get(0).getGnome().getGnomeAvailable());
        } else {
            // this is a new order. just add it to the cart
            OrderEntity order = new OrderEntity(amountToBuy);
            order.setUser(currentUser);
            order.setGnome(gnome);
            em.persist(order);
            System.out.println("An order has been persisted");
            // get the genome persistance entity to update the available gnome
            GnomeEntity gnm = em.find(GnomeEntity.class, gnome.getId());
            // update the available gnome
            gnm.setGnomeAvailable(order.getGnome().getGnomeAvailable() - amountToBuy);
            System.out.println("Amount has been updated to: " + order.getGnome().getGnomeAvailable());
        }
    }

    // get the currentUsers orderlist
    public List<OrderEntity> getOrdersList() throws Exception {
        /*
        // checking whether the user is online
        Query query = em.createNamedQuery("CheckingExistingUserWithName", UserEntity.class);
        query.setParameter("currentName", currentUser.getUserName());
        List<UserEntity> users = query.getResultList();
        System.out.println("UserName: " + currentUser.getUserName());

        
        if (users.isEmpty()) {
            throw new RegisterException("You are not registered!!! Should not ended up here. currentUser should not have cart option if not registered");
        } else if (users.size() > 1) {
            throw new RegisterException("Multiple user registered in the database!!! Bug!! in the application registration service.");
        }
        // If oner user found checking wheter the user is online or offline
        if (users.get(0).getOnlineStatus().equals("offline")) {
            throw new CartException("The user is offline!!! Should not ended up here. currentUser should not have cart after logout.");
        }
         */
        
        // if online we get the list of orders for the current user
        Query orderQuery = em.createNamedQuery("SelectAllOrdersForUser", OrderEntity.class);
        orderQuery.setParameter("userName", currentUser.getUserName());
        List<OrderEntity> ordersList = orderQuery.getResultList();

        return ordersList;
    }

    // method related to the cart edit+delete+checkout
    public void deleteFromCart(OrderEntity order) throws CartException {
        // this GnomeEntity and amount is needed to update the available gnome in case deletion is successful
        GnomeEntity gnome = em.find(GnomeEntity.class, order.getGnome().getId());
        int amount = order.getAmountToBuy();
        // Perform the query
        Query query = em.createNamedQuery("DeleteOrderForUser", OrderEntity.class);
        query.setParameter("orderID", order.getOrderID());
        int rows = query.executeUpdate();
        if (rows > 1) {
            throw new CartException("Multiple orders with a same ID is found in the database!!! Bug!!! in the application add to cart service.");
        } else if (rows == 0) {
            throw new CartException("No order found with the given id. Bug!!! Why the order still in the user's cart list.");
        }

        System.out.println("Succesfully delete a row!!!");
        // now reset the available gnome in the inventory
        gnome.setGnomeAvailable(gnome.getGnomeAvailable() + amount);
        System.out.println("Gnome availability is updated in the database.");
    }

    // to check out
    public float toCheckOut() throws CartException {
        float totalPayment = 0.0f;
        // need to calculate price to be paid for all order in the users orders list
        Query orderQuery = em.createNamedQuery("SelectAllOrdersForUser", OrderEntity.class);
        orderQuery.setParameter("userName", currentUser.getUserName());
        List<OrderEntity> ordersList = orderQuery.getResultList();
        if (ordersList.isEmpty()) {
            throw new CartException("Cart is empty. Add item first.");
        }
        // now remove every order from the order database
        for (OrderEntity order : ordersList) {
            totalPayment = totalPayment + order.getAmountToBuy() * order.getGnome().getGnomePrice();
        }
        return totalPayment;
    }

    // payment and buy
    public void toPayAndBuy() throws CartException {
        // need to checkout for all order in the users orders list
        Query orderQuery = em.createNamedQuery("SelectAllOrdersForUser", OrderEntity.class);
        orderQuery.setParameter("userName", currentUser.getUserName());
        List<OrderEntity> ordersList = orderQuery.getResultList();
        if (ordersList.isEmpty()) {
            throw new CartException("Cart is empty. Add item first.");
        }
        // now remove every order from the order database
        for (OrderEntity order : ordersList) {
            Query query = em.createNamedQuery("DeleteOrderForUser", OrderEntity.class);
            query.setParameter("orderID", order.getOrderID());
            int rows = query.executeUpdate();
            if (rows > 1) {
                throw new CartException("Multiple orders with a same ID is found in the database!!! Bug!!! in the application add to cart service.");
            } else if (rows == 0) {
                throw new CartException("No order found with the given orderID. Bug!!! Why the order still in the user's cart list?");
            }
        }
        System.out.println("All orders from the cart has been bought...");
    }
}
