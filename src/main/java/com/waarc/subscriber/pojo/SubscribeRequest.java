package com.waarc.subscriber.pojo;

public class SubscribeRequest {

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public SubscribeRequest(String email) {
         this.email = email;
    }

    public SubscribeRequest() {
    }
}
