package com.waarc.event;

import com.waarc.event.pojo.EventRequest;
import com.waarc.exception.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository = new EventRepositoryImplementation();
    private final Logger log = LogManager.getLogger(EventServiceImpl.class);

    @Override
    public Event getEvent(int eventId) {
        Event event = eventRepository.getEvent(eventId).orElseThrow(()-> new ResourceNotFoundException("event Not Found with id :" + eventId));
        return new Event(event.getId(), event.getName());
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.getAllEvents();
    }

    @Override
    public Event save(EventRequest request) {
        return eventRepository.save(request);
    }

    @Override
    public Event updateEvent(EventRequest request, int eventId) {
        Event event = eventRepository.getEvent(eventId).orElseThrow(()-> new ResourceNotFoundException("event not found with the id : "+ eventId));

         return eventRepository.updateEvent(request,eventId);
    }

    @Override
    public Event deleteEvent(int eventId) {
        Event event = eventRepository.getEvent(eventId).orElseThrow(()-> new ResourceNotFoundException("event not found with the id : "+ eventId));

        return eventRepository.deleteEvent(eventId);
    }
}
