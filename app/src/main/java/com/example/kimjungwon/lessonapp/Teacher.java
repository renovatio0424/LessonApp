package com.example.kimjungwon.lessonapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by kimjungwon on 2017-04-12.
 */

public class Teacher extends People implements Serializable{
    private Lesson lesson;
    private String CollegeName,lessoncategory;
    private ArrayList<String>LessonPlace,schedule;

    public Teacher(){
        LessonPlace = new ArrayList<>();
        schedule = new ArrayList<>();
    }

    public Lesson getLesson() {
        return lesson;
    }

    public String getCollegeName() {
        return CollegeName;
    }

    public ArrayList<String> getLessonPlace() {
        return LessonPlace;
    }

    public String getLessoncategory() {
        return lessoncategory;
    }

    public ArrayList<String> getSchedule() {
        return schedule;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public void setCollegeName(String collegeName) {
        CollegeName = collegeName;
    }

    public void setLessonPlace(String lessonPlace) {
        StringTokenizer tokenizer = new StringTokenizer(lessonPlace,"@");
        while(tokenizer.hasMoreElements()){
            this.LessonPlace.add(tokenizer.nextToken());
        }
    }

    public void setLessoncategory(String lessoncategory) {
        this.lessoncategory = lessoncategory;
    }

    public void setSchedule(String schedule) {
        StringTokenizer tokenizer = new StringTokenizer(schedule,"@");
        while(tokenizer.hasMoreElements()){
            this.schedule.add(tokenizer.nextToken());
        }
    }
}
