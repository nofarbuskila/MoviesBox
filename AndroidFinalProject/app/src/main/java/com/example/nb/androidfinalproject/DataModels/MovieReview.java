package com.example.nb.androidfinalproject.DataModels;

import java.util.Date;

public class MovieReview {

    private int imageRes;
    private String rate;
    private String title;
    private String body;
    private String userName;
    private Date date;

    public MovieReview() {
    }

    public MovieReview(int imageRes, String rate, String title, String body, String userName, Date date) {
        this.imageRes = imageRes;
        this.rate = rate;
        this.title = title;
        this.body = body;
        this.userName = userName;
        this.date = date;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
