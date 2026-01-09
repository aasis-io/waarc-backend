package com.waarc.event;

import com.waarc.event.pojo.EventRequest;

import java.util.List;
import java.util.Optional;

public interface EventRepository {

    Optional<Event> getEvent(int id);
    List<Event> getAllEvents();
    Event save(EventRequest request);
    Event updateEvent(EventRequest request, int EventId);
    Event deleteEvent(int EventId);

}
