/*
 * Data Transfer Object for gnome
 */
package se.kth.id2212.project.webshop.model;

/**
 *
 * @author maaahad
 */
public interface GnomeDTO {
    public String getId();
    public String getGnomeType();
    public float getGnomePrice();
    public int getGnomeTotal();
    public int getGnomeAvailable();
}
