package com.waarc.subscriber;

import com.waarc.MiddleWare.AuthMiddleWare;
import com.waarc.subscriber.pojo.SubscribeRequest;
import io.javalin.Javalin;

public class SubscribeController {
    private final SubscribeService subscribeService = new SubscribeServiceImpl();

    public SubscribeController(Javalin app){
        app.before("/subscribe", AuthMiddleWare.requireLogin);

        app.post("/createSubscriber",ctx -> {
            SubscribeRequest request = ctx.bodyAsClass(SubscribeRequest.class);
            ctx.json(subscribeService.save(request)).status(200);
        });

        app.get("/subscribe",ctx -> {
           ctx.json(subscribeService.getAllSubscribers()).status(200);
        });

    }
}
