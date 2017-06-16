/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id2212.project.webshop.exception;

/**
 *
 * @author maaahad
 */
public class LogInException extends Exception{
    
    public LogInException(String error){
        super(error);
    }
}
