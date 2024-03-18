package com.main.entities;

public class Story {
    private String fullname;
    private String imageUrl;
    private String id;

    public Story(String fullname, String imageUrl, String id) {
        this.fullname = fullname;
        this.imageUrl = imageUrl;
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFullname() {
        return fullname;
    }

    public String getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
