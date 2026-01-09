package com.waarc.plan;

public class Plan {

    private int id;
    private String tag;
    private String image;
    private String title;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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

    public Plan(int id, String tag, String image, String title, String description) {
        this.tag = tag;
        this.image = image;
        this.title = title;
        this.description = description;
        this.id = id;
    }
    public Plan(String tag, String image, String title, String description) {
        this.tag = tag;
        this.image = image;
        this.title = title;
        this.description = description;

    }
}
