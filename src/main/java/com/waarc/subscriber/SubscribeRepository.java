package com.waarc.subscriber;

import com.waarc.subscriber.pojo.SubscribeRequest;

import java.util.List;
import java.util.Optional;

public interface SubscribeRepository {


    List<Subscribe> getAllSubscriber();
    Subscribe save(SubscribeRequest request);

}
