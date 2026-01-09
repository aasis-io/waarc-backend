package com.waarc.plan.pojo;

public class PlanRequest {

    private String tag;
    private String image;
    private String title;

    private String description;

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

    public PlanRequest(String tag, String image, String title, String description) {
        this.tag = tag;
        this.image = image;
        this.title = title;
        this.description = description;
    }

    public PlanRequest() {
    }
}
