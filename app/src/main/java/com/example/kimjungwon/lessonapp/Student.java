package com.example.kimjungwon.lessonapp;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by kimjungwon on 2017-02-25.
 */

public class Student {
    private String imageURL;
    private String name;
    private String gender;
    private ArrayList<String> subject,style,fee ;
    private String address;
    private Boolean dealpossible;

    //get

    public Student(){
        subject = new ArrayList<>();
        style = new ArrayList<>();
        fee = new ArrayList<>();
    }

    public String getImageURL(){
        return this.imageURL;
    }

    public String getName(){
        return this.name;
    }

    public ArrayList<String> getSubject(){
        return this.subject;
    }

    public String getAddress(){
        return this.address;
    }

    public ArrayList<String> getFee(){
        return this.fee;
    }

    public String getGender(){
        return this.gender;
    }

    public ArrayList<String> getStyle(){
        return this.style;
    }

    public Boolean getDealpossible(){
        return this.dealpossible;
    }

    //get
    //set

    public void setImageURL(String ImageURL){
        this.imageURL = ImageURL;
    }

    public void setName(String Name){
        this.name = Name;
    }

    public void setSubject (String subject){
        StringTokenizer tokenizer = new StringTokenizer(subject,"@");
        while(tokenizer.hasMoreElements()){
            this.subject.add(tokenizer.nextToken());
        }
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setFee (String fee){
        StringTokenizer tokenizer = new StringTokenizer(fee,"@");
        while(tokenizer.hasMoreElements()){
            this.fee.add(tokenizer.nextToken());
        }
    }

    public void setGender (String gender){
        this.gender = gender;
    }

    public void setStyle (String style){
        StringTokenizer tokenizer = new StringTokenizer(style,"@");
        while(tokenizer.hasMoreElements()){
            this.style.add(tokenizer.nextToken());
        }
    }

    public void setDealpossible(String dp){
        if(dp.equals("y")){
            this.dealpossible = true;
        }else{
            this.dealpossible = false;
        }
    }
}
