/*
 * Data Transfer Object for order
 */
package se.kth.id2212.project.webshop.model;

/**
 *
 * @author maaahad
 */
public interface OrderDTO {
    public String getOrderID();
    public int getAmountToBuy();
    public UserEntity getUser();
    
}
