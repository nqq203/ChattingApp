package com.main.entities;

public class MessageItem {

    private String id;
    private String senderName;
    private String content;
    private String imageUrl;
    private long timestamp;


    public MessageItem(String id, String senderName, String imageUrl) {
        this.id = id;
        this.senderName = senderName;
        this.imageUrl = imageUrl;
    }

    public MessageItem(String id, String senderName, String content, String imageUrl, long timestamp) {
        this.id = id;
        this.senderName = senderName;
        this.content = content;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
