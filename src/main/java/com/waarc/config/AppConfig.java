package com.waarc.config;

import io.javalin.Javalin;
import java.util.List;
import java.util.Arrays;

/**
 *
 * @author apracharya
 */
public class AppConfig {

    public AppConfig(Javalin app) {

        app.before(ctx -> {
            String requestedOrigin = ctx.header("Origin");
            List<String> allowedOrigins = Arrays.asList(
                    "http://localhost:5173",
                    "http://localhost:5174",
                    "http://localhost:8081"
            );
            
            if(allowedOrigins.contains(requestedOrigin)) {
                ctx.header("Access-Control-Allow-Origin", requestedOrigin);
            }
            
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
            
            
            if ("OPTIONS".equalsIgnoreCase(ctx.method().toString())) {
                ctx.status(204); // Set status to No Content
                ctx.result("");  // Ensure an empty body for the 204 response
                ctx.skipRemainingHandlers();


 // Crucial: Stop further processing for OPTIONS requests
            }
        });
    }

}
