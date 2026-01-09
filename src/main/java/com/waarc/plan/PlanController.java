package com.waarc.plan;

import com.waarc.MiddleWare.AuthMiddleWare;
import com.waarc.plan.pojo.PlanRequest;
import io.javalin.Javalin;

public class PlanController {
    private final PlanService service = new PlanServiceImpl();

    public PlanController(Javalin app){
        app.before("/plan", AuthMiddleWare.requireLogin);
        app.before("/plan/*",AuthMiddleWare.requireLogin);

        app.post("/plan",ctx -> {
            PlanRequest request = ctx.bodyAsClass(PlanRequest.class);
            ctx.json(service.save(request)).status(200);
        });

        app.get("/plan/{id}", ctx -> {
            int planId = Integer.parseInt(ctx.pathParam("id"));
            ctx.json(service.getplan(planId)).status(200);
        });

        app.get("/getPlans",ctx -> {
           ctx.json(service.getAllplans()).status(200);
        });

        app.put("/plan/{id}", ctx -> {
            PlanRequest request = ctx.bodyAsClass(PlanRequest.class);
            int planId = Integer.parseInt(ctx.pathParam("id"));
            ctx.json(service.updateplan(request,planId)).status(200);
        });

        app.delete("/plan/{id}", ctx -> {
            int planId = Integer.parseInt(ctx.pathParam("id"));
            ctx.json(service.deleteplan(planId)).status(200);
        });


    }
}
