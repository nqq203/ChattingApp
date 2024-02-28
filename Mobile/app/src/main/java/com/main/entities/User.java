package com.main.entities;

import java.time.LocalDate;

public class User {
    private String fullname;
    private String gender;
    private String date;
    private String phoneNumber;

    public User(String fullname, String gender, String date, String phoneNumber) {
        this.fullname = fullname;
        this.gender = gender;
        this.date = date;
        this.phoneNumber = phoneNumber;
    }

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
