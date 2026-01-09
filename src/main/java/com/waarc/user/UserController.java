package com.waarc.user;

import com.waarc.MiddleWare.AuthMiddleWare;
import com.waarc.exception.UnauthorizedException;
import com.waarc.security.JwtUtil;
import com.waarc.user.forgetPassword.ForgetPasswordRequest;
import com.waarc.user.forgetPassword.SendForgetPasswordEmail;
import com.waarc.user.loginPojo.LoginRequest;
import com.waarc.user.passwordChange.ChangePasswordRequest;
import io.javalin.Javalin;

import java.util.Map;

/**
 *
 * @author sachi
 */
public class UserController {

    private final UserService userService = (UserService) new UserServiceImplementation();

    public UserController(Javalin app) {

        app.before("/user", AuthMiddleWare.requireLogin);
        app.before("/logout",AuthMiddleWare.requireLogin);

        app.get("/user",ctx->{
           ctx.status(200).json(userService.getUser());
        });
        app.post("/login", ctx -> {

            LoginRequest loginRequest = ctx.bodyAsClass(LoginRequest.class);

            ctx.status(200).json(userService.login(loginRequest));
        });

        app.get("/logout", ctx -> {

            ctx.json(userService.logout()).status(200);

        });

//        app.post("/user", ctx -> {
//
//            LoginRequest loginRequest = ctx.bodyAsClass(LoginRequest.class);
////            AuthMiddleWare.requirePermission(PermissionConstant.CREATE_USER).handle(ctx);
////            String name = ctx.formParam("name");
////            String email = ctx.formParam("email");
////            String password = ctx.formParam("password");
//
////            UploadedFile file = ctx.uploadedFile("image");
////            String fileName = null;
////
////            if (file == null) {
////                return;
////            }
////
////            String contentType = file.contentType();
////            if (!List.of("image/jpg", "image/png", "image/jpeg").contains(contentType)) {
////
////                ctx.status(400).json(Map.of("error", "Invalid Image type"));
////                return;
////            }
////
////            if (file.size() > 10 * 1024 * 1024) {
////
////                ctx.status(400).json(Map.of("error", "Maximum file size is 10 MB"));
////                return;
////            }
////
//////            fileName =UUID.randomUUID()+"_"+file.filename();
//////          now uploading to cloudinary
////            String profileImageUrl = "";
////            try (InputStream is = file.content(); ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
////
////                byte[] data = new byte[1024];
////                int nRead;
////                while ((nRead = is.read(data, 0, data.length)) != -1) {
////                    buffer.write(data, 0, nRead);
////                }
////                buffer.flush();
////
////                Map uploadResult = CloudinaryConfig.cloudinary.uploader().upload(
////                        buffer.toByteArray(),
////                        ObjectUtils.asMap("folder", "profile_images", "resource_type", "image")
////                );
////
////                profileImageUrl = (String) uploadResult.get("secure_url");
////            }
//
//            UserResponse userResponse = userService.createUser(new UserRequest(name, email, password));
//
//            //for file storage
////            Path path = Paths.get("uploads/" + fileName);
////
////            Files.createDirectories(path.getParent());
////
////            try (InputStream is = file.content()) {
////                Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);
////            }
//            ctx.status(201).json(userResponse);
//        });

        //refresh token
        app.post("/refresh", ctx -> {

            String token = ctx.bodyValidator(Map.class)
                    .check(b -> b.containsKey("refreshToken"), "refreshToken required")
                    .get()
                    .get("refreshToken").toString();

            try {

                var claims = JwtUtil.validateToken(token);

                var newClaims = Map.of(
                        "email", claims.get("email"),
                        "permissions", claims.get("permissions")
                );

                String newToken = JwtUtil.generateAcessToken(claims.getSubject(), claims);

                ctx.json(Map.of("accessToken", newToken)).status(200);

            } catch (Exception e) {

                throw new UnauthorizedException("Invalid refreshToken", 401);
            }

        });

        app.post("/forget-password", ctx -> {

            SendForgetPasswordEmail request = ctx.bodyAsClass(SendForgetPasswordEmail.class);

            ctx.json(userService.sendForgetPasswordEmail(request)).status(200);
        });

        app.post("/reset-password", ctx -> {
            ForgetPasswordRequest request = ctx.bodyAsClass(ForgetPasswordRequest.class);
            ctx.json(userService.resetPassword(request)).status(200);
        });

        app.post("/user/changePassword", ctx -> {

            ChangePasswordRequest request = ctx.bodyAsClass(ChangePasswordRequest.class);

            ctx.json(userService.changePassword(request)).status(200);

        });
    }

}
