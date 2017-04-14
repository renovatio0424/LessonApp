package com.example.kimjungwon.lessonapp;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by kimjungwon on 2017-02-25.
 */

public class Student extends People{

    String teacher_age,teacher_gender;

    //get
    public Student(){

    }

    public String getTeacher_age() {
        return teacher_age;
    }

    public String getTeacher_gender() {
        return teacher_gender;
    }

    public void setTeacher_age(String teacher_age) {
        this.teacher_age = teacher_age;
    }

    public void setTeacher_gender(String teacher_gender) {
        this.teacher_gender = teacher_gender;
    }

}
