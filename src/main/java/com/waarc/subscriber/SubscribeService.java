package com.waarc.subscriber;

import com.waarc.subscriber.pojo.SubscribeRequest;

import java.util.List;

public interface SubscribeService {


    List<Subscribe> getAllSubscribers();
    Subscribe save(SubscribeRequest request);


}
