package com.example.kimjungwon.lessonapp;

import java.io.Serializable;

/**
 * Created by kimjungwon on 2017-04-27.
 */

public class Chat implements Serializable {

    Boolean ismychat = false;

    private String name;
    private String message;
    private String time;
    private String imageURL;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
