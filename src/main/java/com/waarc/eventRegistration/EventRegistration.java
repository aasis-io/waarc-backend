package com.waarc.eventRegistration;

public class EventRegistration {

    private int id;
    private String fullName;
    private String email;
    private String phone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public EventRegistration(int id, String fullName, String email, String phone) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
    }
    public EventRegistration(String fullName, String email, String phone) {
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;

    }
}
