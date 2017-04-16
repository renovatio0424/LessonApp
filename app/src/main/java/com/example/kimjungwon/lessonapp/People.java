package com.example.kimjungwon.lessonapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by kimjungwon on 2017-04-12.
 */

public class People implements Serializable{
    private String Name;
    private String Gender;
    private String profile_image;
    private String reg_date;
    private String last_connect_date;
    private ArrayList<String> subject,fee ;
    private String address,intro;
    private Boolean dealpossible;

    public People(){
        subject = new ArrayList<>();
        fee = new ArrayList<>();
    }

    public String getGender() {
        return Gender;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public String getName() {
        return Name;
    }

    public String getReg_date() {
        return reg_date;
    }

    public String getLast_connect_date() {
        return last_connect_date;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setLast_connect_date(String last_connect_date) {
        this.last_connect_date = last_connect_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
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


    public Boolean getDealpossible(){
        return this.dealpossible;
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

    public void setDealpossible(String dp){
        if(dp.equals("y")){
            this.dealpossible = true;
        }else{
            this.dealpossible = false;
        }
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getIntro() {
        return intro;
    }

}
