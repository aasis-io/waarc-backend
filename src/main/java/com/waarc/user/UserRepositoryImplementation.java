/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.waarc.user;

import com.waarc.config.DataBaseSourceClass;

import com.waarc.exception.OperationFailedException;
import com.waarc.exception.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

/**
 * @author sachi
 */
public class UserRepositoryImplementation implements UserRepository {

    private final Logger log = LogManager.getLogger(UserRepositoryImplementation.class);

    @Override
    public User save(User user) {

        log.info("User insertion initiated.");

        String sql = "insert into user(name,email,password) values (?,?,?) ";

        int userId = -1;

        UserResponse userResponse = new UserResponse();

        try (
                Connection conn = DataBaseSourceClass.getDataSource().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());


            int rowAffected = stmt.executeUpdate();

            if (!(rowAffected > 0)) {
                log.error("cannot insert employee");
                throw new OperationFailedException("Operation Failed ", 500);
            }

            try (ResultSet keySet = stmt.getGeneratedKeys()) {

                if (keySet.next()) {

                    return new User(
                            keySet.getInt(1),
                            user.getName(),
                            user.getEmail()

                    );
                }
            } catch (Exception e) {
                log.error("Failed",e.getMessage());
                throw new ResourceNotFoundException(e.getMessage());
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }
        return null;

    }

    @Override
    public Optional<User> findByEmail(String email) {

        String sql = "SELECT * " +
                " FROM user u " +
                " WHERE u.email = ?";

        Optional<User> user = Optional.empty();

        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                if (user.isEmpty()) {

                    user = Optional.of(new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password")
                    ));
                }
            }
            return user;

        } catch (Exception e) {
            log.error("Failed",e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }
    }

    @Override
    public String changePassword(String email, String password) {
        log.info("Change password initiated");
        String sql = "update user set password = ? where email=?";

        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);) {


            stmt.setString(1, password);
            stmt.setString(2, email);


            int affectedRow = stmt.executeUpdate();
            if (affectedRow < 0) {
                throw new OperationFailedException("Update Query Failed !", 500);
            }

            return password;


        } catch (Exception e) {
            log.error("Failed :",e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }

    }
}
