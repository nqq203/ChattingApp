package com.main.entities;

public class ChatList {
    private String mobile, name, message, date, time, type;

    public ChatList(String mobile, String name, String message, String date, String time, String type) {
        this.mobile = mobile;
        this.name = name;
        this.message = message;
        this.date = date;
        this.time = time;
        this.type = type;
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
}
