package com.waarc.event;

import com.waarc.event.pojo.EventRequest;

import java.util.List;
import java.util.Optional;

public interface EventRepository {

    Event getEvent();
    String save(EventRequest request);
    String deleteEvent();
}
