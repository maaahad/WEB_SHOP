/*
 * Data Transfer Object for user
 */
package se.kth.id2212.project.webshop.model;

/**
 *
 * @author maaahad
 * User DataTransfer Interface
 */
public interface UserDTO {
    public String getUserName();
    public String getPassword();
    public String getPermitStatus();
    public String getOnlineStatus();
    public String getUserRole();
}
