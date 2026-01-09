package com.waarc.dataLoader;

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

    private static BasicDataSource dataSource;

    private static Properties appProperties;

    private static String  PROPERTIES_FILE ="application.properties";

    static{

        appProperties = new Properties();

        log.info("Checking the application.properties !");
        try(InputStream input = DataLoader.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)){

            if(input == null){

                log.error("application.properties file not found");
                throw new IOException("IOException occured : "+PROPERTIES_FILE);
            }

            log.info("application.properties found !");
            appProperties.load(input);

        }catch(Exception ex){
        }
    }


    public void load() {
        loadAdminUser();
    }

    private void loadAdminUser() {

        if(!userRepository.findByEmail(appProperties.getProperty("AdminEmail")).isPresent()){
            String hashedPassword = BCrypt.hashpw(appProperties.getProperty("AdminPassword"), BCrypt.gensalt(10));
            User savedUser = userRepository.save(new User(appProperties.getProperty("AdminName"), appProperties.getProperty("AdminEmail"), hashedPassword));
            System.out.println(savedUser.getEmail());
        }

    }
}
