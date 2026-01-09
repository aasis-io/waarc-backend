/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.waarc.user.forgetPassword;

/**
 *
 * @author sachi
 */
public class ForgetPasswordRequest {
    
    private String emailSignedToken;
    
    private String password;

    public String getEmailSignedToken() {
        return emailSignedToken;
    }

    public void setEmailSignedToken(String emailSignedToken) {
        this.emailSignedToken = emailSignedToken;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ForgetPasswordRequest(String emailSignedToken, String password) {
        this.emailSignedToken = emailSignedToken;
        this.password = password;
    }

    public ForgetPasswordRequest() {
    }
 
    
}
