package com.example.kimjungwon.lessonapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by kimjungwon on 2017-03-14.
 */

public class activity_selectjob extends AppCompatActivity implements View.OnClickListener {
    Button Teacher, Student;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.join_select);

        Teacher = (Button) findViewById(R.id.student_btn);
        Student = (Button) findViewById(R.id.teacher_btn);

        Teacher.setOnClickListener(this);
        Student.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        Intent intent;

        Intent social = getIntent();

        if(social.hasExtra("social")){
            intent = new Intent(getApplicationContext(),activity_Profile.class);

            intent.putExtra("email",social.getStringExtra("email"));
            intent.putExtra("name",social.getStringExtra("name"));
            intent.putExtra("social",social.getStringExtra("social"));
            intent.putExtra("gender",social.getStringExtra("gender"));
            intent.putExtra("birthday",social.getStringExtra("birthday"));
        }else{
            intent = new Intent(getApplicationContext(),activity_JOIN.class);
        }

        switch (id){
            case R.id.teacher_btn:
                intent.putExtra("job","teacher");
                break;

            case R.id.student_btn:
                intent.putExtra("job","student");
                break;
        }


        startActivity(intent);
    }
}
