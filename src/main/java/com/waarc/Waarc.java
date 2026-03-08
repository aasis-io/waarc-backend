package com.waarc;

import com.waarc.about.AboutController;
import com.waarc.blog.BlogController;
import com.waarc.config.AppConfig;
import com.waarc.config.Config;
import com.waarc.dataLoader.DataLoader;
import com.waarc.event.EventController;
import com.waarc.eventRegistration.EventRegistrationController;
import com.waarc.exception.AppExceptionHandler;
import com.waarc.home.HomeController;
import com.waarc.link.LinkController;
import com.waarc.plan.PlanController;
import com.waarc.service.ServiceController;

import com.waarc.siteSetting.SiteSettingController;
import com.waarc.subscriber.SubscribeController;
import com.waarc.team.TeamController;
import com.waarc.user.UserController;
import com.waarc.work.WorkController;

import com.waarc.analytics.AnalyticsController;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.validation.ValidationException;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.Map;

/**
 * Main class for Waarc Javalin backend
 */
public class Waarc {

    // Get port from environment variables first (Render provides PORT), fallback to properties, default 7001
    private static final int SERVER_PORT = Integer.parseInt(
            Config.getProperty("server.port") != null
                    ? Config.getProperty("server.port")
                    : (System.getenv("PORT") != null ? System.getenv("PORT") : "7001")
    );

    static {
        // Use classpath-based log4j2.properties if available, fallback to default config
        var logConfigStream = Waarc.class.getClassLoader().getResourceAsStream("log4j2.properties");
        if (logConfigStream != null) {
            Configurator.initialize(null, "log4j2.properties");
        } else {
            Configurator.initialize(null, null); // default config
        }
    }

    public static void main(String[] args) {

        // Load initial data
        DataLoader dataLoader = new DataLoader();
        dataLoader.load();

        // Start Javalin server
        var app = Javalin.create(config -> {

            // Static file setup for uploads
            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath = "/uploads"; // URL prefix
                staticFileConfig.directory = "uploads";   // folder on disk
                staticFileConfig.location = Location.EXTERNAL; // external folder
            });
        }).start(SERVER_PORT);

        // Exception handling
        app.exception(ValidationException.class, (e, ctx) -> {
            ctx.status(400);
            ctx.json(Map.of(
                    "error", "Validation error",
                    "details", e.getErrors()
            ));
        });

        // Root endpoint
        app.get("/", ctx -> ctx.html("<h1> Welcome to Wisdom Academy and Research Academy </h1>"));

        // Initialize controllers
        new AppConfig(app);
        new HomeController(app);
        new TeamController(app);
        new EventController(app);
        new LinkController(app);
        new UserController(app);
        new ServiceController(app);
        new SubscribeController(app);
        new AboutController(app);
        new BlogController(app);
        new AnalyticsController(app);
        new EventRegistrationController(app);
        new PlanController(app);
        new WorkController(app);
        new SiteSettingController(app);
        new AppExceptionHandler(app);

    }
}