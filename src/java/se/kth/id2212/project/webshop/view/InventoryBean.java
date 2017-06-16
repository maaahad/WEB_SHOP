/*
 * This inventoryBean provide the user with all possible products in the database
 * 
 */
package se.kth.id2212.project.webshop.view;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import se.kth.id2212.project.webshop.controller.InventoryEJB;
import se.kth.id2212.project.webshop.model.GnomeEntity;

/**
 *
 * @author maaahad
 */
@Named(value = "inventoryBean")
@SessionScoped
public class InventoryBean implements Serializable {

    private List<GnomeEntity> gnomes;
    @EJB
    private InventoryEJB inventoryEJB;
    
    public void addGnomesToDatabase(){
        inventoryEJB.addGnomesToDatabase("Silver", 100.0f, 10,10);
    }
    
    
    
    public InventoryBean() {
    }

    public List<GnomeEntity> getGnomes() {
        // For the final project the list should get from the database
        gnomes = inventoryEJB.getGenomeList();
        return gnomes;
    }

}
