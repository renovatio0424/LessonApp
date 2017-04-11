package com.example.kimjungwon.lessonapp;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.StrictMode;

/**
 * Created by kimjungwon on 2017-02-14.
 */

public class NetworkUtil {
    @SuppressLint("NewApi")
    static public void setNetworkPolicy(){
        if(Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
}
