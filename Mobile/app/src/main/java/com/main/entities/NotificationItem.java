package com.main.entities;

public class NotificationItem {
    private String Description;
    private int profile_pic;
    private String reply_story;
    private String time;
    private int story_pic=0;

    public NotificationItem(String description, int profile_pic, String reply_story, String time, int story_pic) {
        Description = description;
        this.profile_pic = profile_pic;
        this.reply_story = reply_story;
        this.time = time;
        this.story_pic = story_pic;
    }

    public NotificationItem(String description, int profile_pic, String time, int story_pic) {
        Description = description;
        this.profile_pic = profile_pic;
        this.time = time;
        this.story_pic = story_pic;
    }

    public NotificationItem(String description, int profile_pic, String time) {
        Description = description;
        this.profile_pic = profile_pic;
        this.time = time;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(int profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getReply_story() {
        return reply_story;
    }

    public void setReply_story(String reply_story) {
        this.reply_story = reply_story;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStory_pic() {
        return story_pic;
    }

    public void setStory_pic(int story_pic) {
        this.story_pic = story_pic;
    }
}
