package com.waarc.event;

import com.waarc.event.pojo.EventRequest;
import io.javalin.Javalin;

public class EventController {
    private final EventService eventService = new EventServiceImpl();

    public EventController(Javalin app){

        app.post("/event",ctx -> {
            EventRequest request = ctx.bodyAsClass(EventRequest.class);
            ctx.json(eventService.save(request)).status(200);
        });

        app.get("/event/{id}", ctx -> {
            int eventId = Integer.parseInt(ctx.pathParam("id"));
            ctx.json(eventService.getEvent(eventId)).status(200);
        });

        app.get("/event",ctx -> {
           ctx.json(eventService.getAllEvents()).json(200);
        });

        app.put("/event/{id}", ctx -> {
            EventRequest request = ctx.bodyAsClass(EventRequest.class);
            int EventId = Integer.parseInt(ctx.pathParam("id"));
            ctx.json(eventService.updateEvent(request,EventId)).status(200);
        });

        app.delete("/event/{id}", ctx -> {
            int EventId = Integer.parseInt(ctx.pathParam("id"));
            ctx.json(eventService.deleteEvent(EventId)).status(200);
        });


    }
}
