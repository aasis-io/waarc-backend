package com.waarc.blog;

import com.aayushatharva.brotli4j.common.annotations.Local;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;


public class Blog {

    private int id;
    private String image;
    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Blog(int id, LocalDateTime date, String image, String title, String description) {
        this.date = date;
        this.image = image;
        this.title = title;
        this.description = description;
        this.id = id;
    }
    public Blog(LocalDateTime date,String image, String title, String description) {
       this.date = date;
        this.image = image;
        this.title = title;
        this.description = description;

    }
}
