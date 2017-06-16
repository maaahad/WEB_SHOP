/*
 * This is the UserEJB for performing business logic reltaed to a user
 */
package se.kth.id2212.project.webshop.controller;

import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import se.kth.id2212.project.webshop.exception.RegisterException;
import se.kth.id2212.project.webshop.model.GnomeEntity;
import se.kth.id2212.project.webshop.model.OrderEntity;
import se.kth.id2212.project.webshop.model.UserEntity;

/**
 *
 * @author maaahad
 */
@Stateful
public class AdminEJB {

    @PersistenceContext(unitName = "PROJECT_WEBSHOPPU")
    EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    // business logic for the admin
    // return a list of registered users
    public List<UserEntity> showRegisteredUsers() {
        Query query = em.createNamedQuery("SelectAllUsers", UserEntity.class);
        List<UserEntity> usersList = query.getResultList();
        if (usersList.isEmpty()) {
            System.out.println("No user registered in the database.");
        }
        return usersList;
    }

    // update users info
    public void updateUserInfo(UserEntity user, String userPermitStatus, String userRole, String userOnlineStatus) throws RegisterException {
        // get the user from the database using the user's id : username
        Query query = em.createNamedQuery("CheckingExistingUserWithName", UserEntity.class);
        query.setParameter("currentName", user.getUserName());
        List<UserEntity> result = query.getResultList();
        if (result.isEmpty()) {
            throw new RegisterException("You are not registered!!! Should not ended up here. currentUser should not have cart option if not registered");
        } else if (result.size() > 1) {
            throw new RegisterException("Multiple user registered in the database!!! Bug!! in the application registration service (database).");
        }
        // now update the user
        if (!userPermitStatus.equals(" ")) {
            result.get(0).setPermitStatus(userPermitStatus);
        }
        if (!userRole.equals(" ")) {
            result.get(0).setUserRole(userRole);
        }
        if (!userOnlineStatus.equals(" ")) {
            result.get(0).setOnlineStatus(userOnlineStatus);
        }

        System.out.println("Admin: Succesfully updated user's info");
    }

    // this method add new item in the database
    public void addNewItem(String productType, float productPrice, int productTotal, int productAvailable) {
        // first we check is there any product with the same product id and same product type available
        Query query = em.createNamedQuery("SelecetGnomeUsingType", GnomeEntity.class);
        query.setParameter("gnomeType", productType);

        List<GnomeEntity> results = query.getResultList();
        System.out.println("Type: " + productType + "\nproductPrice: " + productPrice);

        if (!results.isEmpty()) {
            // update current persistence item. The result sould be only one item
            if (results.get(0).getGnomePrice() == productPrice) {
                results.get(0).setGnomeTotal(results.get(0).getGnomeTotal() + productTotal);
                results.get(0).setGnomeAvailable(results.get(0).getGnomeAvailable() + productAvailable);
                System.out.println("Existing gnome has been updated");
            } else {
                // persist as new item
                GnomeEntity gnome = new GnomeEntity(productType, productPrice, productTotal, productAvailable);
                em.persist(gnome);
                System.out.println("A new Gnome Item has been added to the database.");
            }
        } else {
            // persist as new item
            GnomeEntity gnome = new GnomeEntity(productType, productPrice, productTotal, productAvailable);
            em.persist(gnome);
            System.out.println("A new Gnome Item has been added to the database.");
        }
    }

    public void updateExistingProduct(GnomeEntity gnome, int amount) {
        GnomeEntity existingGnome = (GnomeEntity) em.find(GnomeEntity.class, gnome.getId());
        existingGnome.setGnomeAvailable(existingGnome.getGnomeAvailable() + amount);
        existingGnome.setGnomeTotal(existingGnome.getGnomeTotal() + amount);
        System.out.println("Existing product updated.");
    }

    public void deleteExistingProduct(GnomeEntity gnome) throws Exception {
        System.out.println("GnomeID: " + gnome.getId());
        // First we check whether the product is in the order list
        Query orderQuery = em.createNamedQuery("DeleteOrderForProduct", OrderEntity.class);
        orderQuery.setParameter("gnomeID", gnome.getId());
        int res = orderQuery.executeUpdate();
        if (res>0) {
            // need to delete from the orders list so that it is updated in the orders as well as in the productlist
            System.out.println("Product has been deleted from the orders list");
            // now we need to delete the product from the product list
            Query query = em.createNamedQuery("DeleteGnome", GnomeEntity.class);
            query.setParameter("gnomeID", gnome.getId());
            int rows = query.executeUpdate();
            if (rows == 1) {
                System.out.println("Product deleted successfully.");
            } else {
                System.out.println("Product deletion was unsuccesful!!!");
                throw new Exception("Product deletion was unsuccesful!!!");
            }
            
        } else {
            // need to delete from the product list
            Query query = em.createNamedQuery("DeleteGnome", GnomeEntity.class);
            query.setParameter("gnomeID", gnome.getId());
            int rows = query.executeUpdate();
            if (rows == 1) {
                System.out.println("Product deleted successfully.");
            } else {
                System.out.println("Product deletion was unsuccesful!!!");
                throw new Exception("Product deletion was unsuccesful!!!");
            }
        }

    }
}
