package com.waarc.event.pojo;

public class EventRequest {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public EventRequest(String name) {
        this.name = name;
    }
    public EventRequest() {
    }
}
