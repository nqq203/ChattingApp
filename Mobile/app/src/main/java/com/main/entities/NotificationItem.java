package com.main.entities;

public class NotificationItem {
    private String Description;
    private String profile_pic;
    private String Type;
    private String reply_story;
    private String time;
    private String story_pic;
    private int key;
    private String userid;

    public NotificationItem(String description, String profile_pic, String type, String reply_story, String time, String story_pic, int key, String userid) {
        Description = description;
        this.profile_pic = profile_pic;
        Type = type;
        this.reply_story = reply_story;
        this.time = time;
        this.story_pic = story_pic;
        this.key = key;
        this.userid = userid;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
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

    public String getStory_pic() {
        return story_pic;
    }

    public void setStory_pic(String story_pic) {
        this.story_pic = story_pic;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "NotificationItem{" +
                "Description='" + Description + '\'' +
                ", profile_pic='" + profile_pic + '\'' +
                ", Type='" + Type + '\'' +
                ", reply_story='" + reply_story + '\'' +
                ", time='" + time + '\'' +
                ", story_pic='" + story_pic + '\'' +
                ", key=" + key +
                ", userid='" + userid + '\'' +
                '}';
    }
}
