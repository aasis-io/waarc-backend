package com.waarc.work;

import com.waarc.MiddleWare.AuthMiddleWare;
import com.waarc.work.pojo.WorkRequest;
import io.javalin.Javalin;

public class WorkController {
    private final WorkService service = new WorkServiceImpl();

    public WorkController(Javalin app){

        app.before("/work", AuthMiddleWare.requireLogin);
        app.before("/work/*",AuthMiddleWare.requireLogin);

        app.post("/work",ctx -> {
            WorkRequest request = ctx.bodyAsClass(WorkRequest.class);
            ctx.json(service.save(request)).status(200);
        });

        app.get("/work/{id}", ctx -> {
            int WorkId = Integer.parseInt(ctx.pathParam("id"));
            ctx.json(service.getWork(WorkId)).status(200);
        });

        app.get("/getWorks",ctx -> {
           ctx.json(service.getAllWorks()).status(200);
        });

        app.put("/work/{id}", ctx -> {
            WorkRequest request = ctx.bodyAsClass(WorkRequest.class);
            int WorkId = Integer.parseInt(ctx.pathParam("id"));
            ctx.json(service.updateWork(request,WorkId)).status(200);
        });

        app.delete("/work/{id}", ctx -> {
            int WorkId = Integer.parseInt(ctx.pathParam("id"));
            ctx.json(service.deleteWork(WorkId)).status(200);
        });


    }
}
