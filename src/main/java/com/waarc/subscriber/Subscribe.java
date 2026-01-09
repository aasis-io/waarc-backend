package com.waarc.subscriber;

public class Subscribe {

    private int id;
    private String email;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Subscribe(int id, String email) {
        this.email = email;
        this.id = id;
    }
    public Subscribe(String email) {
        this.email = email;
    }
}
