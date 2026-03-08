package com.waarc.link;

import com.waarc.MiddleWare.AuthMiddleWare;
import io.javalin.Javalin;

public class LinkController {
    private final LinkService service = new LinkServiceImpl();

    public LinkController(Javalin app){
        app.before("/link", AuthMiddleWare.requireLogin);

        app.get("/getLink",ctx -> {
           ctx.json(service.getLink(ctx));
        });
        app.get("/link",ctx -> {
            ctx.json(service.getIndividualLink(ctx));
        });
        app.post("/link",ctx -> {
            ctx.json(service.save(ctx));
        });

        app.put("/link", ctx -> {
            ctx.json(service.updateLink(ctx));
        });

        app.delete("/link", ctx -> {
            ctx.json(service.deleteLink(ctx));
        });
    }
}
