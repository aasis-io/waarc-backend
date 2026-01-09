/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.waarc.user;

import com.waarc.user.forgetPassword.ForgetPasswordRequest;
import com.waarc.user.forgetPassword.SendForgetPasswordEmail;
import com.waarc.user.loginPojo.LoginRequest;
import com.waarc.user.loginPojo.LoginResponse;
import com.waarc.user.logout.LogoutResponse;
import com.waarc.user.passwordChange.ChangePasswordRequest;

/**
 *
 * @author sachi
 */
public interface UserService {

    LoginResponse login(LoginRequest loginRequest);

    UserResponse  getUser();
    LogoutResponse logout();
    
    UserResponse changePassword(ChangePasswordRequest request);
    
    UserResponse resetPassword(ForgetPasswordRequest request);

    String sendForgetPasswordEmail(SendForgetPasswordEmail request);
            
}
