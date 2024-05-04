package com.main.entities;

import java.util.Date;

public class Story {
    private String fullname;
    private String imageUrl;
    private Date timeCreated;
    private long duration;

    public Story(long duration, String imageUrl, Date timeCreated, String fullname) {
        this.fullname = fullname;
        this.imageUrl = imageUrl;
        this.timeCreated = timeCreated;
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
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

    public Date getTimeCreated() {
        return timeCreated;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
