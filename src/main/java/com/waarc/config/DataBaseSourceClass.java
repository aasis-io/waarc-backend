/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.waarc.config;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author sachi
 */
public class DataBaseSourceClass {
    
    private static final Logger log = LogManager.getLogger(DataBaseSourceClass.class);
    
    private static BasicDataSource dataSource;
    
    private static Properties appProperties;
    
    private static String  PROPERTIES_FILE ="application.properties";
    
    static{
        
        appProperties = new Properties();   
        
        log.info("Checking the application.properties !");
        try(InputStream input = DataBaseSourceClass.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)){
            
            if(input == null){
                
                log.error("application.properties file not found");
                throw new IOException("IOException occured : "+PROPERTIES_FILE);
            }
            
            
            log.info("application.properties found !");
            appProperties.load(input);
            
        }catch(Exception ex){
        }
    }
    
    public static BasicDataSource getDataSource(){
        
        if(dataSource == null){
            
           log.info("Creating new db instance");
           dataSource = new BasicDataSource();
          
           dataSource.setDriverClassName(appProperties.getProperty("db.DriverClassName"));
           dataSource.setUrl(appProperties.getProperty("db.url"));
           dataSource.setUsername(appProperties.getProperty("db.setUsername"));
           dataSource.setPassword(appProperties.getProperty("db.setPassword"));
           
           dataSource.setInitialSize(5);
           dataSource.setMaxTotal(20);
           dataSource.setMaxIdle(10);
           dataSource.setMinIdle(5);
            
           dataSource.setMaxWait(Duration.ofMillis(30000));
           dataSource.setMinEvictableIdle(Duration.ofMillis(600000));
           dataSource.setDurationBetweenEvictionRuns(Duration.ofMillis(1800000));
           
        }

        return dataSource;
    }
    
    
    public static void shutdown(){
        if(dataSource != null){
            try{
            dataSource.close();
            }catch(Exception e){
                System.err.println("Error Closing DataBase ......"+ e.getMessage());
            }
        }
    }
    
    
    
}
