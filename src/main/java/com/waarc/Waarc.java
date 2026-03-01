/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.waarc;

import com.waarc.about.AboutController;
import com.waarc.blog.BlogController;
import com.waarc.config.AppConfig;
import com.waarc.config.Config;
import com.waarc.dataLoader.DataLoader;
import com.waarc.eventRegistration.EventRegistrationController;
import com.waarc.exception.AppExceptionHandler;
import com.waarc.home.HomeController;
import com.waarc.plan.PlanController;
import com.waarc.service.ServiceController;

import com.waarc.siteSetting.SiteSettingController;
import com.waarc.subscriber.SubscribeController;
import com.waarc.user.UserController;
import com.waarc.work.WorkController;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.validation.ValidationException;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.Map;

/**
 * @author sachi
 */
public class Waarc {
    private static final int SERVER_PORT = Integer.parseInt(Config.getProperty("server.port"));
    static {
        Configurator.initialize(null, "etc/log4j2.properties");
    }

    public static void main(String[] args) {

        DataLoader dataLoader = new DataLoader();
        dataLoader.load();

        var app = Javalin.create(config -> {

            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath = "/uploads"; // URL prefix
                staticFileConfig.directory = "uploads";   // folder on disk
                staticFileConfig.location = Location.EXTERNAL; // external folder
            });


        }).start(SERVER_PORT);

        app.exception(ValidationException.class, (e, ctx) -> {

            ctx.json(400);
            ctx.json(Map.of(

                    "error", "Validation error",
                    "details", e.getErrors()

            ));
        });

        app.get("/", ctx ->
        {
            ctx.html("<h1> Welcome to Wisdom Academy and Research Academy </h1>");
        });

        new AppConfig(app);
        new HomeController(app);
        new UserController(app);
        new ServiceController(app);
        new SubscribeController(app);
        new AboutController(app);
        new BlogController(app);
        new EventRegistrationController(app);
        new PlanController(app);
        new WorkController(app);
        new SiteSettingController(app);
        new AppExceptionHandler(app);

    }


}
