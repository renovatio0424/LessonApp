package com.example.kimjungwon.lessonapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;

import static com.example.kimjungwon.lessonapp.URLconfig.Search_College_url;

/**
 * Created by kimjungwon on 2017-04-06.
 */

public class activity_searchUniversity extends AppCompatActivity {

    EditText insertdata;
    Button search;
    ArrayList<String> Colleges;
    RecyclerView recyclerView;
    UnivAdapter univAdapter;

    public static final int SEND_COLLEGE_LIST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchuniversity);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        insertdata = (EditText) findViewById(R.id.ed1);
        search = (Button) findViewById(R.id.btn);
        Colleges = new ArrayList<>();

        univAdapter = new UnivAdapter(getApplicationContext(), Colleges, R.layout.item_college);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(univAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String college = Colleges.get(position);
                Toast.makeText(activity_searchUniversity.this, "index:" + position + "=" + college, Toast.LENGTH_SHORT).show();
                Intent goback = new Intent();
                goback.putExtra("university", college);
                setResult(RESULT_OK, goback);
                finish();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }
        }));

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    JSONObject jo = new JSONObject();
                    String sn = insertdata.getText().toString();
                    //아무것도 입력하지 않았을 경우에 예외처리
                    if(sn.length() == 0){
                        Toast.makeText(activity_searchUniversity.this, "대학교 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                    }else {

                        jo.put("schoolname", sn);

                        final String[] result = {jo.toString()};
                        Log.d("POSTJSON", "before: " + result[0]);
                        final String[] rv = {""};
                        final CollegeHandler collegeHandler = new CollegeHandler();
                        Colleges.clear();
                        new Thread() {
                            @Override
                            public void run() {
                                PHPRequest request = null;
                                try {
                                    request = new PHPRequest(Search_College_url);
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                                result[0] = request.POSTJSON(result[0]);
                                Log.d("POSTJSON", "after: " + result[0]);

                                JSONArray jsonArray = null;
                                try {
                                    jsonArray = new JSONArray(result[0]);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        try {
                                            Colleges.add(jsonArray.get(i).toString());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    for (int i = 0; i < Colleges.size(); i++) {
                                        Log.d("Colleges", i + ") " + Colleges.get(i));
                                    }

                                    Message msg = collegeHandler.obtainMessage();
                                    msg.what = SEND_COLLEGE_LIST;
                                    collegeHandler.sendMessage(msg);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public class CollegeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case SEND_COLLEGE_LIST:
                    Log.d("handle Chat", "start!");
                    if(Colleges.get(0).equals("null")){
                        Toast.makeText(activity_searchUniversity.this, "검색 결과가 없습니다", Toast.LENGTH_SHORT).show();
                        Colleges.clear();
                        break;
                    }
                    univAdapter = new UnivAdapter(getApplicationContext(), Colleges, R.layout.item_college);
                    recyclerView.setAdapter(univAdapter);
                    univAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

}