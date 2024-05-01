package com.main.entities;

public class Story {
    private String fullname;
    private String imageUrl;
    private String idUser;
    private long duration;

    public Story(String fullname, String imageUrl, String idUser, long duration) {
        this.fullname = fullname;
        this.imageUrl = imageUrl;
        this.idUser = idUser;
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public void setId(String id) {
        this.idUser = id;
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
        return idUser;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
