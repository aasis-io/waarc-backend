package com.waarc.about;

import com.waarc.MiddleWare.AuthMiddleWare;
import com.waarc.about.pojo.AboutRequest;
import io.javalin.Javalin;

public class AboutController {
    private final AboutService service = new AboutServiceImpl();

    public AboutController(Javalin app){
        app.before("/about", AuthMiddleWare.requireLogin);

        app.post("/about",ctx -> {
            AboutRequest request = ctx.bodyAsClass(AboutRequest.class);
            ctx.json(service.save(request)).status(200);
        });


        app.get("/getAbout",ctx -> {
           ctx.json(service.getAbout()).status(200);
        });

        app.put("/about", ctx -> {
            AboutRequest request = ctx.bodyAsClass(AboutRequest.class);
            ctx.json(service.updateAbout(request)).status(200);
        });
    }
}
