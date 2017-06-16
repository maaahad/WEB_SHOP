/*
 * This is inventory EJB for performing inventory related business logic 
 */
package se.kth.id2212.project.webshop.controller;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import se.kth.id2212.project.webshop.model.GnomeEntity;

/**
 *
 * @author maaahad
 */
@Stateless
public class InventoryEJB {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext(unitName = "PROJECT_WEBSHOPPU")
    EntityManager em;
    
    // this method add new gnomes to the data base
    public void addGnomesToDatabase(String gnomeType, float gnomePrice, int gnomeTotal, int gnomeAvailable){
        // query gnome based on type: price for same type should be same,, otherwise exception 
        GnomeEntity gnome = new GnomeEntity(gnomeType,gnomePrice,gnomeTotal,gnomeAvailable);
        em.persist(gnome);
        System.out.println("Gnome persisted...");
    }
    
    // this method return all genome list from the database
    public List<GnomeEntity> getGenomeList(){
        Query query = em.createNamedQuery("SelectAllGnome",GnomeEntity.class);
        List<GnomeEntity> gnomes = query.getResultList();
        if(gnomes.isEmpty()){
            // Need to throw an exception
        }
        
        return gnomes;
    }
}
