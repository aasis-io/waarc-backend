/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.waarc.EmailService;

import com.waarc.exception.OperationFailedException;
import com.waarc.exception.ResourceNotFoundException;
import jakarta.mail.Session;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author sachi
 */
public class EmailService {

    private static final String PROPERTIES_FILE = "application.properties";
    
    private static final String senderEmail;
    private static final String senderPassword;
    
    private static final Session session;

    static {
        
        try{
        Properties mailProps = loadMailProperties();

          senderEmail = mailProps.getProperty("mail.username");

          senderPassword =  mailProps.getProperty("mail.password");
           
           session = Session.getInstance(mailProps, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }

        });
           
        }catch(Exception e){
            throw new OperationFailedException(e.getMessage(), 500);
        }
    }

    public static String sendEmail(String recipient, String token) {

        try {

            String verificationLink = "http://localhost:5173/reset-password?token=" + token;

          
            String htmlContent =  "<h3>Reset Password</h3>"
                    + "<p>Click the link below to change password:</p>"
                    + "<a href='" + verificationLink + "'>Reset Password</a>";

            // Create and send message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject("WAARC - Reset your password.");
            message.setContent(htmlContent, "text/html");

            Transport.send(message);
            
            return "Check Your Email!";

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new OperationFailedException("Failed to Send Email", 500);
                     }
    }

    private static Properties loadMailProperties() {

        Properties prop = new Properties();

        try (InputStream input = EmailService.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {

            if (input == null) {
                throw new ResourceNotFoundException("application.properties not found!");
            }

            prop.load(input);

        } catch (Exception e) {
            throw new OperationFailedException("Failed to load properties!", 500);
        }

        return prop;

    }

}
