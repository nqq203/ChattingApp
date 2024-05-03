package com.main.entities;

public class MessageList {
    private String name, mobile, lastMessage, imageUrl, chatKey;
    private int unseenMessage;

    public MessageList(String name, String mobile, String lastMessage, String imageUrl, int unseenMessage, String chatKey) {
        this.name = name;
        this.mobile = mobile;
        this.lastMessage = lastMessage;
        this.unseenMessage = unseenMessage;
        this.imageUrl = imageUrl;
        this.chatKey = chatKey;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public int getUnseenMessage() {
        return unseenMessage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getChatKey() {
        return chatKey;
    }

    public void setUnseenMessage(int unseenMessage) {
        this.unseenMessage = unseenMessage;
    }
}
