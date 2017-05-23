package com.example.kimjungwon.lessonapp;

import java.io.Serializable;

/**
 * Created by kimjungwon on 2017-04-22.
 */

public class Consult implements Serializable {
    String lessonname;
    String Category;
    String yourname;
    String reason;
    String state;
    int confiramtion,id;

    public Consult(int id, String lessonname,String category,String yourname, String reason, String state, int confiramtion){
        this.id = id;
        this.lessonname = lessonname;
        this.Category = category;
        this.yourname = yourname;
        this.reason = reason;
        this.state = state;
        this.confiramtion = confiramtion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConfiramtion() {
        return confiramtion;
    }

    public void setConfiramtion(int confiramtion) {
        this.confiramtion = confiramtion;
    }

    public String getLessonname() {
        return lessonname;
    }

    public void setLessonname(String lessonname) {
        this.lessonname = lessonname;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getYourname() {
        return yourname;
    }

    public void setYourname(String yourname) {
        this.yourname = yourname;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
