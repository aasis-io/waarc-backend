package com.waarc.about;

import com.waarc.MiddleWare.AuthMiddleWare;
import com.waarc.about.pojo.AboutRequest;
import io.javalin.Javalin;

public class AboutController {
    private final AboutService service = new AboutServiceImpl();

    public AboutController(Javalin app){
        app.before("/about", AuthMiddleWare.requireLogin);

        app.post("/about",ctx -> {
            ctx.json(service.save(ctx)).status(200);
        });

        app.get("/getAbout",ctx -> {
           ctx.json(service.getAbout(ctx));
        });

        app.put("/about", ctx -> {
            ctx.json(service.updateAbout(ctx));
        });
    }
}
