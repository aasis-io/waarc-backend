package com.waarc.eventRegistration;

import com.waarc.MiddleWare.AuthMiddleWare;
import com.waarc.eventRegistration.pojo.EventRegistrationRequest;
import io.javalin.Javalin;

public class EventRegistrationController {
    private final EventRegistrationService eventRegistrationService = new EventRegistrationServiceImpl() {
    };

    public EventRegistrationController(Javalin app){

        app.before("/getEventRegistration", AuthMiddleWare.requireLogin);

        app.post("/eventRegistration",ctx -> {
            EventRegistrationRequest request = ctx.bodyAsClass(EventRegistrationRequest.class);
            ctx.json(eventRegistrationService.save(request)).status(200);
        });

        app.get("/getEventRegistration",ctx -> {
           ctx.json(eventRegistrationService.getAllEventRegistrations()).status(200);
        });

    }
}
