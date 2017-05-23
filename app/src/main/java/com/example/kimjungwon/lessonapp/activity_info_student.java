package com.example.kimjungwon.lessonapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.kimjungwon.lessonapp.URLconfig.MyURL;
import static com.example.kimjungwon.lessonapp.URLconfig.RequestLesson_URL;

/**
 * Created by kimjungwon on 2017-04-21.
 */

public class activity_info_student extends AppCompatActivity {
    Student student;
    String job,name,myid;
    Button RequestLesson;
    HashMap<String,TextView> student_info_view, like_teacher_view;
    ImageView profileimg;
    private static String TAG = activity_info_student.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_info_student);

        student = (Student) getIntent().getSerializableExtra("person");
        myid = getIntent().getStringExtra("id");
        job = getIntent().getStringExtra("job");
        name = getIntent().getStringExtra("name");

        student_info_view = new HashMap<>();

        student_info_view.put("name",(TextView) findViewById(R.id.info_student_name));
        student_info_view.put("gender", (TextView) findViewById(R.id.info_student_gender));
        student_info_view.put("age", (TextView) findViewById(R.id.info_student_age));
        student_info_view.put("address", (TextView) findViewById(R.id.info_student_address));
        student_info_view.put("subject", (TextView) findViewById(R.id.info_student_subject));
        student_info_view.put("fee", (TextView) findViewById(R.id.info_student_intro));

        student_info_view.get("name").setText(student.getName());
        student_info_view.get("gender").setText(student.getGender());
        student_info_view.get("age").setText(student.getAge());
        student_info_view.get("address").setText(student.getAddress());
        String subject = ArrayToString(student.getSubject());
        student_info_view.get("subject").setText(subject);
        String fee = FeeToString(student.getFee());
        fee += student.getDealpossible().equals("y") ? " (협의 가능)" : " (협의 불가)";
        student_info_view.get("fee").setText(fee);

        like_teacher_view = new HashMap<>();

        like_teacher_view.put("teacher_age", (TextView) findViewById(R.id.info_student_ta));
        like_teacher_view.put("teacher_gender", (TextView) findViewById(R.id.info_student_tg));

        like_teacher_view.get("teacher_age").setText(student.getTeacher_age());
        like_teacher_view.get("teacher_gender").setText(student.getTeacher_gender());

        RequestLesson = (Button) findViewById(R.id.Request_teacher);

        RequestLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PHPRequest request = new PHPRequest(RequestLesson_URL);
                    JSONObject jsonObject = new JSONObject();
                    String id = student.getId();
                    jsonObject.put("id",id);
                    jsonObject.put("myid",myid);
                    jsonObject.put("name",name);
                    jsonObject.put("job",job);

                    String jo = jsonObject.toString();
                    Log.d(TAG,"before json: " + jo);
                    String result = request.POSTJSON(jo);
                    Log.d(TAG,"after json: " + result);

                    Toast.makeText(activity_info_student.this, result, Toast.LENGTH_SHORT).show();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        profileimg = (ImageView) findViewById(R.id.profile_img_student);

        if(profileimg.equals("null")){
            if(student.getGender().equals("남자")){
                profileimg.setImageResource(R.drawable.ic_male_student);
            }else{
                profileimg.setImageResource(R.drawable.ic_female_student);
            }
        }else{
            Glide.with(this).load(MyURL + student.getProfile_image()).centerCrop().into(profileimg);
        }
    }

    public String ArrayToString(ArrayList<String> list){
        String result = "";

        for(int i = 0 ; i < list.size() ; i ++){
            result += list.get(i);
            if(i != list.size() - 1){
                result += "\n";
            }
        }

        return result;
    }

    public String FeeToString(ArrayList<String> list){
        String result = "";

        result += "주 " + list.get(0) + "회 " + list.get(1) + "시간 " + list.get(2) + "만원";

        return result;
    }
}
