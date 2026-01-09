package com.waarc.eventRegistration;

import com.waarc.eventRegistration.pojo.EventRegistrationRequest;
import com.waarc.exception.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class EventRegistrationServiceImpl implements EventRegistrationService {
    private final EventRegistrationRepository eventRegistrationRepository = new EventRegistrationRepositoryImplementation();
    private final Logger log = LogManager.getLogger(EventRegistrationServiceImpl.class);



    @Override
    public List<EventRegistration> getAllEventRegistrations() {
        return eventRegistrationRepository.getEventRegistrations();
    }

    @Override
    public EventRegistration save(EventRegistrationRequest request) {
        return eventRegistrationRepository.save(request);
    }

}
