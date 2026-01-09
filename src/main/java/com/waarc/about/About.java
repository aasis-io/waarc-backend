package com.waarc.about;

public class About {

    private int id;

    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public About(int id,  String description) {

        this.description = description;
        this.id = id;
    }
    public About( String description) {
        this.description = description;
    }
    public About() {}
}
