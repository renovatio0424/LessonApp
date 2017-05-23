package com.example.kimjungwon.lessonapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by kimjungwon on 2017-04-12.
 */

public class Lesson implements Serializable{
    private int Lesson_id;
    private String Lesson_Title;
    private String Lesson_Background_Image;
    private ArrayList<String> studentlevel,Lesson_Subject,fee;
    private String intro;
    private int hits;
    private Boolean recruiting;
    private String reg_date;

    public Lesson (){
        studentlevel = new ArrayList<>();
        Lesson_Subject = new ArrayList<>();
        fee = new ArrayList<>();
    }

    public void setLesson_id(int lesson_id) {
        Lesson_id = lesson_id;
    }

    public int getLesson_id() {
        return Lesson_id;
    }

    public void setLesson_Background_Image(String lesson_Background_Image) {
        Lesson_Background_Image = lesson_Background_Image;
    }

    public void setStudentlevel(String studentlevel) {
        StringTokenizer tokenizer = new StringTokenizer(studentlevel,"@");
        while(tokenizer.hasMoreElements()){
            this.studentlevel.add(tokenizer.nextToken());
        }
    }

    public void setLesson_Subject(String lesson_Subject) {
        StringTokenizer tokenizer = new StringTokenizer(lesson_Subject,"@");
        while(tokenizer.hasMoreElements()){
            this.Lesson_Subject.add(tokenizer.nextToken());
        }
    }

    public void setLesson_Title(String lesson_Title) {
        Lesson_Title = lesson_Title;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public void setRecruiting(Boolean recruiting) {
        this.recruiting = recruiting;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getLesson_Background_Image() {
        return Lesson_Background_Image;
    }

    public ArrayList<String> getStudentlevel() {
        return studentlevel;
    }

    public ArrayList<String> getLesson_Subject() {
        return Lesson_Subject;
    }

    public String getLesson_Title() {
        return Lesson_Title;
    }

    public int getHits() {
        return hits;
    }

    public Boolean getRecruiting() {
        return recruiting;
    }

    public String getReg_date() {
        return reg_date;
    }

    public String getIntro() {
        return intro;
    }

    public void setFee (String fee){
        StringTokenizer tokenizer = new StringTokenizer(fee,"@");
        while(tokenizer.hasMoreElements()){
            this.fee.add(tokenizer.nextToken());
        }
    }

    public ArrayList<String> getFee(){
        return this.fee;
    }
}
