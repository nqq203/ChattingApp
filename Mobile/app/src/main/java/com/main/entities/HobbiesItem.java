package com.main.entities;

public class HobbiesItem {
    private String Hobbies;
    private Boolean isSelected=false;
    private int PicHobbies;

    private int background;

    public HobbiesItem(String hobbies, int picHobbies, Boolean isSelected, int background) {
        Hobbies = hobbies;
        this.isSelected = isSelected;
        PicHobbies = picHobbies;
        this.background = background;
    }

    public String getHobbies() {
        return Hobbies;
    }

    public void setHobbies(String hobbies) {
        Hobbies = hobbies;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public int getPicHobbies() {
        return PicHobbies;
    }

    public void setPicHobbies(int picHobbies) {
        PicHobbies = picHobbies;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }
}
