package com.main.entities;

public class MatchesItem {
    private String name;
    private String pic;
    private int age;
    private String userid;

    public MatchesItem(String name, String pic, int age, String userid) {
        this.name = name;
        this.pic = pic;
        this.age = age;
        this.userid = userid;
    }

    public MatchesItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "MatchesItem{" +
                "name='" + name + '\'' +
                ", pic='" + pic + '\'' +
                ", age=" + age +
                ", userid='" + userid + '\'' +
                '}';
    }
}
