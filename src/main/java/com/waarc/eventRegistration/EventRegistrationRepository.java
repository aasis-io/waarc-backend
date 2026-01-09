package com.waarc.eventRegistration;

import com.waarc.eventRegistration.pojo.EventRegistrationRequest;

import java.util.List;
import java.util.Optional;

public interface EventRegistrationRepository {

//    Optional<EventRegistration> getRegistrationById(int eventRegistrationId);
//    List<EventRegistration> getEventRegistration(int eventId);
////    List<EventRegistration> getAllEventRegistrations();
//    EventRegistration save(EventRegistrationRequest request,int eventId);
//    EventRegistration deleteEventRegistration(int eventId,int eventRegistrationId);


List<EventRegistration> getEventRegistrations();
EventRegistration save(EventRegistrationRequest request);

}
