/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.waarc.exception;

/**
 *
 * @author sachi
 */
public class OperationFailedException extends RuntimeException {
    
    private final int statusCode;
 
    public OperationFailedException(String message,int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    
    
    public int getStatusCode()
    {
    return this.statusCode;
    }
  
}
