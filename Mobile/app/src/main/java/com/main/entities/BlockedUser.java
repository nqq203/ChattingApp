package com.main.entities;

public class BlockedUser {
    private String userId;
    private String name;

    public BlockedUser(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
}
