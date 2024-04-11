package com.main.entities;

public class SubscriptionItem {
    private String title;
    private String plan;
    private String startDate;
    private String endDate;

    private String userId;

    public SubscriptionItem(String title, String plan, String startDate, String endDate, String userId) {
        this.title = title;
        this.plan = plan;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
    }

    public SubscriptionItem() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "SubscriptionItem{" +
                "title='" + title + '\'' +
                ", plan='" + plan + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
