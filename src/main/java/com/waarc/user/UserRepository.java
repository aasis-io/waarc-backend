/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.waarc.user;

import java.util.Optional;

/**
 *
 * @author sachi
 */
public interface UserRepository {

    Optional<User> findByEmail(String email);

    User save(User user);

    String changePassword(String email,String password);

}
