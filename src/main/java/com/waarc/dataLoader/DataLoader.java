package com.waarc.dataLoader;

import com.waarc.config.Config;
import com.waarc.user.User;
import com.waarc.user.UserRepository;
import com.waarc.user.UserRepositoryImplementation;
import org.apache.commons.dbcp2.BasicDataSource;

import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataLoader {
    private final UserRepository userRepository = new UserRepositoryImplementation();

    private static final Logger log = LogManager.getLogger(DataLoader.class);


    public void load() {
        loadAdminUser();
    }

    private void loadAdminUser() {

        if(!userRepository.findByEmail(Config.getProperty("Admin_Email")).isPresent()){
            String hashedPassword = BCrypt.hashpw(Config.getProperty("Admin_Password"), BCrypt.gensalt(10));
            User savedUser = userRepository.save(new User(Config.getProperty("Admin_Name"),Config.getProperty("Admin_Email"), hashedPassword));
            System.out.println(savedUser.getEmail());
        }

    }
}
