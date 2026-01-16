package com.waarc.siteSetting;

public class SiteSetting {

    private int id;
    private String phone;
    private String email;
    private String location;
    private String facebook;
    private String instagram;
    private String linkedin;
    private String youtube;

    // Getters
    public int getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getLocation() {
        return location;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public String getYoutube() {
        return youtube;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    // Constructors
    public SiteSetting() {
    }

    public SiteSetting(int id, String phone, String email, String location,
                       String facebook, String instagram, String linkedin, String youtube) {
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.location = location;
        this.facebook = facebook;
        this.instagram = instagram;
        this.linkedin = linkedin;
        this.youtube = youtube;
    }

    public SiteSetting(String phone, String email, String location,
                       String facebook, String instagram, String linkedin, String youtube) {
        this.phone = phone;
        this.email = email;
        this.location = location;
        this.facebook = facebook;
        this.instagram = instagram;
        this.linkedin = linkedin;
        this.youtube = youtube;
    }
}