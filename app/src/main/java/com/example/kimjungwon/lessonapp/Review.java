package com.example.kimjungwon.lessonapp;

/**
 * Created by kimjungwon on 2017-05-19.
 */

public class Review {
    private int id ;
    private String user_img;
    private String user_name;
    private float rating;
    private String content;
    private String date;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getRating() {
        return rating;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getUser_img() {
        return user_img;
    }

    public String getUser_name() {
        return user_name;
    }
}

