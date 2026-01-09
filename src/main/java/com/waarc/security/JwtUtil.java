package com.waarc.security;

 

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Map;
/**
 *
 * @author sachi
 */
public class JwtUtil {
    
    private static final String SECRET_KEY ="hellothisismesachinkumarsingh@98";
    private static final long ACCESS_TOKEN_EXPIRATION = 1000*60*15;
    
    private static final long REFRESH_TOKEN_EXPIRATION=1000L *60*60*24*7;
    
    private static final Key key  =Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
         
    public static String generateAcessToken(String subject,Map<String,Object> claims){
        
        return Jwts.builder()
                .setSubject(subject)
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ACCESS_TOKEN_EXPIRATION))
                .signWith(key)
                .compact();
    }
    
    
    public static String generateRefreshToken(String subject,Map<String,Object> claims){
       
        return Jwts.builder()
                .setSubject(subject)
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+REFRESH_TOKEN_EXPIRATION))
                .signWith(key)
                .compact();
        
    }
  
    
    public static Claims validateToken(String token){
        
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    
     public static String generateVerificationToken(String subject){
        
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ACCESS_TOKEN_EXPIRATION))
                .signWith(key)
                .compact();
    }
     
     public static String extractEmailFromToken(String token){
         
          return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject();
     }
            
    
}
