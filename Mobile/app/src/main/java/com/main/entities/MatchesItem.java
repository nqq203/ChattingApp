package com.main.entities;

public class MatchesItem {
    private String name;
    private int picture;
    private int age;

    public MatchesItem(String name, int picture, int age) {
        this.name = name;
        this.picture = picture;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
