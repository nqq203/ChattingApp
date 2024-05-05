package com.main.entities;

import android.graphics.drawable.Drawable;

public class ChatList {
    private String mobile, name, message, date, time, type;
    private Drawable chatColor;

    public ChatList(String mobile, String name, String message, String date, String time, String type, Drawable chatColor) {
        this.mobile = mobile;
        this.name = name;
        this.message = message;
        this.date = date;
        this.time = time;
        this.type = type;
        this.chatColor = chatColor;
    }

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getType() { return type; }

    public void setType(String type) {
        this.type = type;
    }
    public Drawable getChatColor() {
        return chatColor;
    }
}
