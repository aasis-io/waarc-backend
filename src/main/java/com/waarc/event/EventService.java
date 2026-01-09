package com.waarc.event;

import com.waarc.event.pojo.EventRequest;

import java.util.List;

public interface EventService {

    Event getEvent(int eventId);
    List<Event> getAllEvents();
    Event save(EventRequest request);
    Event updateEvent(EventRequest request, int eventId);
    Event deleteEvent(int eventId);


}
