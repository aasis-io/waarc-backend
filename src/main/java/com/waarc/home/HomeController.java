package com.waarc.home;

import com.waarc.MiddleWare.AuthMiddleWare;
import com.waarc.home.pojo.HomeRequest;
import io.javalin.Javalin;

public class HomeController {
    private final com.waarc.home.HomeService service = new com.waarc.home.HomeServiceImpl();

    public HomeController(Javalin app){
        app.before("/Home", AuthMiddleWare.requireLogin);

        app.post("/home",ctx -> {
            ctx.json(service.save(ctx));
        });


        app.get("/getHome",ctx -> {
           ctx.json(service.getHome(ctx));
        });

        app.put("/home", ctx -> {
            ctx.json(service.updateHome(ctx));
        });
    }
}
