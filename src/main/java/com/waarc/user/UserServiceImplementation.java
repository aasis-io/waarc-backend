package com.waarc.user;

import com.waarc.EmailService.EmailService;
import com.waarc.config.DataBaseSourceClass;
import com.waarc.exception.InvalidCredentialsException;
import com.waarc.exception.OperationFailedException;
import com.waarc.exception.ResourceNotFoundException;
import com.waarc.security.JwtUtil;
import com.waarc.user.forgetPassword.ForgetPasswordRequest;
import com.waarc.user.forgetPassword.SendForgetPasswordEmail;
import com.waarc.user.loginPojo.LoginRequest;
import com.waarc.user.loginPojo.LoginResponse;
import com.waarc.user.logout.LogoutResponse;
import com.waarc.user.passwordChange.ChangePasswordRequest;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author sachi
 */
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository = (UserRepository) new UserRepositoryImplementation();

    private static final Logger log = LogManager.getLogger(UserServiceImplementation.class);

    private static BasicDataSource dataSource;

    private static Properties appProperties;

    private static String  PROPERTIES_FILE ="application.properties";

    static{

        appProperties = new Properties();

        log.info("Checking the application.properties !");
        try(InputStream input = UserServiceImplementation.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)){

            if(input == null){

                log.error("application.properties file not found");
                throw new IOException("IOException occured : "+PROPERTIES_FILE);
            }


            log.info("application.properties found !");
            appProperties.load(input);

        }catch(Exception ex){
        }
    }

//
//    @Override
//    public UserResponse createUser(UserRequest user) {
//
//        Optional<User> checkUser = userRepository.findByEmail(user.getEmail());
//
//        if (checkUser.isPresent()) {
//            throw new DuplicateFoundException("Email Already Exists!");
//        }
//
//        UserResponse userResponse = new UserResponse();
//
//        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10));
//
//        Department department = departmentRepository.findById(user.getDepartment()).orElseThrow(() -> new ResourceNotFoundException("Department Not Found"));
//
//        User savedUser = userRepository.save(new User(user.getName(), user.getEmail(), hashedPassword, department, new ArrayList<>(), user.getProfileImage()));
//
//        userRepository.addNewUserPermissions(user.getPermissions(), savedUser.getId());
//
//        //Retrieving part
//        User fetchedUser = userRepository.findById(savedUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found with Id : " + savedUser.getId()));
//
//        userResponse.setId(fetchedUser.getId());
//        userResponse.setName(fetchedUser.getName());
//
//        userResponse.setEmail(fetchedUser.getEmail());
//
//        userResponse.setDepartment(new DepartmentResponse(department.getId(), department.getName()));
//
//        userResponse.setPermission(userRepository.getUserPermission(fetchedUser.getId()));
//
//        return userResponse;
//    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User not Found with email : " + loginRequest.getEmail()));

        try {

            if (BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {


                //this was for file system
//                String profileImageURL = user.getProfileImage() != null ? "http://localhost:7071/uploads/"+user.getProfileImage():"";

//              for coudinary it gives the link itself
//                String profileImageURL = user.getProfileImage() != null ? user.getProfileImage() : "";

//               
                Map<String, Object> claims = Map.of(
                        "email", user.getEmail(),
                        "id", user.getId(),
                        "name", user.getName()

                );

                String accessToken = JwtUtil.generateAcessToken(String.valueOf(user.getId()), claims);
                String refreshToken = JwtUtil.generateRefreshToken(String.valueOf(user.getId()), claims);

                log.info("User Logged In");
                return new LoginResponse(accessToken, refreshToken);

            } else {
                log.error("Invalid Email or Password");
                throw new InvalidCredentialsException("Invalid Email or Password !");
            }

        } catch (Exception e) {
            log.error("Login Error : "+e.getMessage());
            throw new InvalidCredentialsException(e.getMessage());
        }

    }

    @Override
    public UserResponse getUser() {

        User user = userRepository.findByEmail(appProperties.getProperty("AdminEmail")).orElseThrow(()-> new ResourceNotFoundException("Admin Not Found !"));
        log.info("Getting User info");
        return new UserResponse(user.getId(),user.getName(),user.getEmail());
    }


    @Override
    public LogoutResponse logout() {
        log.info("User logged out");
        return new LogoutResponse("Logout Success!");

    }

    @Override
    public String sendForgetPasswordEmail(SendForgetPasswordEmail request) {

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User not Found"));

        String emailSignedToken = JwtUtil.generateVerificationToken(request.getEmail());

        if (emailSignedToken == null) {
            throw new OperationFailedException("Token generation Failed", 500);
        }

        log.info("Sending forget password email...");
        String emailResponse = EmailService.sendEmail(request.getEmail(), emailSignedToken);
        log.info("Forget Password Email Sent !");
        return emailResponse;

    }

    @Override
    public UserResponse changePassword(ChangePasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!(BCrypt.checkpw(request.getCurrentPassword(), user.getPassword()))) {
            throw new OperationFailedException("Incorrect Current Password!", 400);

        }

        String hashedPw = BCrypt.hashpw(request.getNewPassword(), BCrypt.gensalt(10));

        String passwordResponse = userRepository.changePassword(request.getEmail(), hashedPw);

        log.info("Password Changed Successull !");
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail());

    }

    @Override
    public UserResponse resetPassword(ForgetPasswordRequest request) {

        String token = request.getEmailSignedToken();

        String extractedEmail = JwtUtil.extractEmailFromToken(token);

        User user = userRepository.findByEmail(extractedEmail).orElseThrow(() -> new ResourceNotFoundException("User not Found!"));

        String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt(10));

        String passwordResponse = userRepository.changePassword(extractedEmail, hashedPassword);
        log.info("Reset Password Done!");

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
                 );

    }
}
