package com.waarc.eventRegistration;

import com.waarc.eventRegistration.pojo.EventRegistrationRequest;

import java.util.List;

public interface EventRegistrationService {


    List<EventRegistration> getAllEventRegistrations();

    EventRegistration save(EventRegistrationRequest request);
 ;
}
