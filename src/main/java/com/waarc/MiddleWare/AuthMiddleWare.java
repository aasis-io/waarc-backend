/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.waarc.MiddleWare;

import com.waarc.exception.UnauthorizedException;
import com.waarc.security.JwtUtil;
import com.waarc.user.UserService;
import com.waarc.user.UserServiceImplementation;
import io.javalin.http.Handler;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.util.List;

/**
 *
 * @author sachi
 */
public class AuthMiddleWare {
    
      private static final UserService userService = (UserService) new UserServiceImplementation();
 
       public static  Handler requireLogin = ctx->{
            
            String authHeader = ctx.header("Authorization");
            
            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                
                throw new UnauthorizedException("No Jwt Token",401);

            }
            String token = authHeader.substring(7);
            
            try{
                Claims claims = JwtUtil.validateToken(token);
 
                //attach user info to context
               
                ctx.sessionAttribute("userId",(Integer.parseInt(claims.getSubject())));
     
                ctx.sessionAttribute("email",claims.get("email"));
                
                ctx.sessionAttribute("permissions",claims.get("permissions"));
      
            }catch(ExpiredJwtException e){
                
                throw new UnauthorizedException("Token Expired ",401);
                
            }catch(SignatureException e){
                throw new UnauthorizedException("Invalid token Signature ",401);
                
            }catch(MalformedJwtException e){
                throw new UnauthorizedException(" Malformed JWT Token ",401);
            }catch(JwtException e){
                throw new UnauthorizedException("JWT Exception ",401);
            }
             
        };
       
       
     // Check if user has a specific permission
    public static Handler requirePermission(int permissionId) {
        return ctx -> {
            
            requireLogin.handle(ctx);
 
            Integer userId = ctx.sessionAttribute("userId");
            
            if (userId == null) {
                
                throw new UnauthorizedException("UnAuthorized! Please Log in First!", 401);
            }
 
            List<Integer> permissions = ctx.sessionAttribute("permissions");
            
             
            boolean hasPermission = permissions.stream()
                    .anyMatch(p -> p == permissionId);
                if (!hasPermission) {
                throw new UnauthorizedException("Not Authorized !!", 403);
            }
        };
    }
    
}
