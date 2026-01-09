package com.waarc.service;

import com.waarc.MiddleWare.AuthMiddleWare;
import com.waarc.service.pojo.ServiceRequest;
import com.waarc.user.UserService;
import com.waarc.user.UserServiceImplementation;
import io.javalin.Javalin;

public class ServiceController {
    private final ServiceService service = new ServiceServiceImpl();

    public ServiceController(Javalin app){
        app.before("/service",AuthMiddleWare.requireLogin);
        app.before("/service/*",AuthMiddleWare.requireLogin);

        app.get("/getServices",ctx -> {
            ctx.json(service.getAllServices()).status(200);
        });

        app.post("/service",ctx -> {
            ServiceRequest request = ctx.bodyAsClass(ServiceRequest.class);
            ctx.json(service.save(request)).status(200);
        });


        app.get("/service/{id}", ctx -> {
            int serviceId = Integer.parseInt(ctx.pathParam("id"));
            ctx.json(service.getService(serviceId)).status(200);
        });



        app.put("/service/{id}", ctx -> {
            ServiceRequest request = ctx.bodyAsClass(ServiceRequest.class);
            int serviceId = Integer.parseInt(ctx.pathParam("id"));
            ctx.json(service.updateService(request,serviceId)).status(200);
        });

        app.delete("/service/{id}", ctx -> {
            int serviceId = Integer.parseInt(ctx.pathParam("id"));
            ctx.json(service.deleteService(serviceId)).status(200);
        });


    }
}
