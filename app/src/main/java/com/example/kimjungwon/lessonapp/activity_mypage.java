package com.example.kimjungwon.lessonapp;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.Toast;

/**
 * Created by kimjungwon on 2017-03-18.
 */

public class activity_mypage extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar ;

    ImageView profileimage;
    TableRow change_Userinfo,change_lessoninfo,regist_lesson,manage_lesson,logout;

    String User_id, User_job, User_name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mypage);
        toolbar = (Toolbar) findViewById(R.id.toolbar_chat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("마이 페이지");

        //직업 별로 다르게 !
        User_id = getIntent().getStringExtra("id");
        User_name = getIntent().getStringExtra("name");
        User_job = getIntent().getStringExtra("job");

        profileimage = (ImageView) findViewById(R.id.mypage_profileimg);

        change_Userinfo = (TableRow) findViewById(R.id.mypage_profile_info);
        change_lessoninfo = (TableRow) findViewById(R.id.mypage_lesson_info);
        regist_lesson = (TableRow) findViewById(R.id.mypage_insert_lesson);
        manage_lesson = (TableRow) findViewById(R.id.mypage_manage_lesson);
        logout = (TableRow) findViewById(R.id.mypage_logout);

        //학생일 경우 수업 등록/수업 관리 안뜨도록
        if(User_job.equals("student")){
            regist_lesson.setVisibility(View.GONE);
            manage_lesson.setVisibility(View.GONE);
        }

        profileimage.setOnClickListener(this);
        change_Userinfo.setOnClickListener(this);
        change_lessoninfo.setOnClickListener(this);
        regist_lesson.setOnClickListener(this);
        manage_lesson.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.mypage_profileimg:
                Toast.makeText(this, "profile img !!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mypage_profile_info:
                Toast.makeText(this, "profile info !!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mypage_lesson_info:
                Toast.makeText(this, "lesson info !!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mypage_insert_lesson:
                Toast.makeText(this, "insert info !!", Toast.LENGTH_SHORT).show();
                Intent goRegistClass = new Intent(getApplicationContext(), activity_regist_class.class).
                        putExtra("id",User_id).putExtra("name",User_name).putExtra("job",User_job);
                startActivity(goRegistClass);
                break;
            case R.id.mypage_manage_lesson:
                Toast.makeText(this, "manage lesson !!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mypage_logout:
                Toast.makeText(this, "logout !!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
