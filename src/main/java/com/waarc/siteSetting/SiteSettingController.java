package com.waarc.siteSetting;

import com.waarc.MiddleWare.AuthMiddleWare;
import com.waarc.siteSetting.pojo.SiteSettingRequest;
import io.javalin.Javalin;

public class SiteSettingController {
    private final SiteSettingService service = new SiteSettingServiceImpl();

    public SiteSettingController(Javalin app){
        // Auth middleware for POST, PUT, DELETE operations
        app.before("/siteSettings", AuthMiddleWare.requireLogin);


        // Public GET endpoint (no auth required)
        app.get("/getSiteSettings", ctx -> {
            ctx.json(service.getSiteSetting()).status(200);
        });

        // PUT - Update site settings (requires auth)
        app.put("/siteSettings", ctx -> {
            SiteSettingRequest request = ctx.bodyAsClass(SiteSettingRequest.class);
            ctx.json(service.updateSiteSetting(request)).status(200);
        });

    }
}