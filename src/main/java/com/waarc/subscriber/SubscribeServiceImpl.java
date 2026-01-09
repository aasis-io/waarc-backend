package com.waarc.subscriber;

import com.waarc.subscriber.pojo.SubscribeRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class SubscribeServiceImpl implements SubscribeService {
    private final SubscribeRepository subscribeRepository = new SubscribeRepositoryImplementation();
    private final Logger log = LogManager.getLogger(SubscribeServiceImpl.class);


    @Override
    public List<Subscribe> getAllSubscribers() {
        return subscribeRepository.getAllSubscriber();
    }

    @Override
    public Subscribe save(SubscribeRequest request) {
        return subscribeRepository.save(request);
    }

}
