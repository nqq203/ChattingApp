package com.main.entities;

import java.time.LocalDate;

public class User {
    private String fullname;
    private String gender;
    private String date;
    private String phoneNumber;
    private String password;
    private String imageUrl;
    private String location;
    private int height;
    public User(String fullname, String gender, String date, String phoneNumber, String password, String imageUrl) {
        this.fullname = fullname;
        this.gender = gender;
        this.date = date;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.imageUrl = imageUrl;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullname() {
        return fullname;
    }
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
