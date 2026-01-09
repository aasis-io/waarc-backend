/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.waarc.user;

import java.util.List;

/**
 *
 * @author sachi
 */
public class UserRequest {
  
    private String name;
    
    private int department;
    
    
    private String email;
    
    private String password;
    
    private List<Integer> permissions;
    
    private String profileImage;

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public int getDepartment() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getPermissions() {
        return permissions;
    }
   

    public void setPermissions(List<Integer> permissions) {
        this.permissions = permissions;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRequest(String name,  String email, String password,int department, List<Integer> permissions,String profileImage) {
        this.name = name;
        this.department = department;
        this.email = email;
        this.password = password;
        this.permissions = permissions;
        this.profileImage = profileImage;
    }
 
   
    public UserRequest() {
    }
    
    
}
