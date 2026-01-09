package com.waarc.service;

public class Service {

    private int id;
    private String image;
    private String title;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Service( int id,String image, String title, String description) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.id = id;
    }
    public Service( String image, String title, String description) {
        this.image = image;
        this.title = title;
        this.description = description;

    }
}
