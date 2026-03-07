package com.waarc.event;

import com.waarc.event.pojo.EventRequest;
import com.waarc.home.Home;
import io.javalin.http.Context;

import java.util.List;
import java.util.Optional;

public interface EventService {

    String getEvent(Context ctx);
    String save(Context ctx);
    String deleteEvent(Context ctx);

}
