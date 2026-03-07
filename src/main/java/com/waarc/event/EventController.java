package com.waarc.event;

import com.waarc.MiddleWare.AuthMiddleWare;
import com.waarc.event.pojo.EventRequest;
import io.javalin.Javalin;

public class EventController {
    private final EventService eventService = new EventServiceImpl();

    public EventController(Javalin app){
        app.before("/event", AuthMiddleWare.requireLogin);
        app.post("/event",ctx -> {
            ctx.json(eventService.save(ctx));
        });

        app.get("/getEvent", ctx -> {
            ctx.json(eventService.getEvent(ctx));
        });

        app.delete("/event", ctx -> {
            ctx.json(eventService.deleteEvent(ctx));
        });


    }
}
