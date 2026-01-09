/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.waarc.exception;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import io.javalin.Javalin;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author sachi
 */
public class AppExceptionHandler {
    
    private final Logger LOG = LogManager.getLogger(AppExceptionHandler.class);
    
    
    private Map<String,String> getErrors(String message){
        
        return Map.of("error ",message);
    }
    
    
    private Map<String,String>  getAuthErrorResponse(){
        
        return Map.of("error","Authentication Failed !");
    }
    
    public AppExceptionHandler(Javalin app){
        
        
        app.exception(ResourceNotFoundException.class, (e,ctx)->{
        
            ctx.json(getErrors(e.getMessage())).status(404);
        });
        
        
         app.exception(OperationFailedException.class,(e,ctx)->{
        
            ctx.json(getErrors(e.getMessage())).status(e.getStatusCode());
        });
//         app.exception(MismatchedInputException.class,(e,ctx)-> {
//             ctx.json(getErrors("Mismatched Input for the Request")).status(400);
//         });
//         app.exception(RuntimeException.class,(e,ctx)-> {
//             ctx.json(getErrors(e.getMessage())).status(400);
//         });

//
        app.exception(UnrecognizedPropertyException.class, (e, ctx) -> {
            ctx.status(400).json(Map.of(
                    "error", "Invalid request body",
                    "message", "Unknown field: " + e.getPropertyName()
            ));
        });
  
         app.exception(InvalidCredentialsException.class,(e,ctx)->{
        
            ctx.json(getErrors(e.getMessage())).status(404);
        });
        
        app.exception(DuplicateFoundException.class,(e,ctx)->{
             System.out.println("===========Duplicate found! " + e.getMessage());
            ctx.json(getErrors(e.getMessage())).status(409);
            return;
        });
         
           app.exception(UnauthorizedException.class,(e,ctx)->{
        
            ctx.json(getErrors(e.getMessage())).status(e.getStatusCode());
        });
           
   
           
           app.exception(SignatureException.class, (e, ctx) -> {
            ctx.status(401).json(getAuthErrorResponse());
            LOG.error("JWT Signature Error: ", e.getMessage());
        });
        app.exception(ExpiredJwtException.class, (e, ctx) -> {
            ctx.status(401).json(getAuthErrorResponse());
            LOG.error("JWT Expired Error: {}", e.getMessage());
        });
        app.exception(MalformedJwtException.class, (e, ctx) -> {
            ctx.status(401).json(getAuthErrorResponse());
            LOG.error("JWT Malformed Error: {}", e.getMessage());
        });
        app.exception(UnsupportedJwtException.class, (e, ctx) -> {
            ctx.status(401).json(getAuthErrorResponse());
            LOG.error("JWT Unsupported Error: {}", e.getMessage());
        });
        app.exception(io.jsonwebtoken.security.SecurityException.class, (e, ctx) -> {
            ctx.status(401).json(getAuthErrorResponse());
            LOG.error("JWT Security Error: {}", e.getMessage());
        });
 
    }
}
