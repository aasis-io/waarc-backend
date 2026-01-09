/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.waarc.exception;

/**
 *
 * @author sachi
 */
public class UnauthorizedException extends RuntimeException{
    
    
    private int statusCode;
    
    public UnauthorizedException(String message,int statusCode){
        super(message);
        this.statusCode= statusCode;
    }
    
    public int getStatusCode(){
        return statusCode;
    }
}
