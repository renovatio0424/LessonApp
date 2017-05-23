package com.example.kimjungwon.lessonapp;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;

import static com.example.kimjungwon.lessonapp.URLconfig.LoadConsult_URL;
import static com.example.kimjungwon.lessonapp.URLconfig.MyURL;

/**
 * Created by kimjungwon on 2017-03-18.
 */

public class activity_main extends TabActivity{

    String Tab1name ;
    String id,name,job;
    String TAG = activity_main.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        TabHost tabHost = getTabHost();

        if(getIntent().getStringExtra("job").equals("teacher")){
            Tab1name = "학생찾기";
        }else if(getIntent().getStringExtra("job").equals("student")){
            Tab1name = "과외찾기";
        }else{
            Tab1name = "no job";
        }

        id = getIntent().getStringExtra("id");
        job = getIntent().getStringExtra("job");
        name = getIntent().getStringExtra("User_name");

        TabHost.TabSpec tabSpecTab1 = tabHost.newTabSpec("TAB1").setIndicator(Tab1name).
                setContent(new Intent(this, activity_searchpeople.class).
                        putExtra("id",getIntent().getStringExtra("id")).
                        putExtra("job",getIntent().getStringExtra("job")).
                        putExtra("User_name",getIntent().getStringExtra("User_name"))
                );
        tabHost.addTab(tabSpecTab1);

        TabHost.TabSpec tabSpecTab2 = tabHost.newTabSpec("TAB2").setIndicator("과외 톡").
                setContent(new Intent(this, activity_myconsulting.class).
                        putExtra("id",getIntent().getStringExtra("id")).
                        putExtra("job",getIntent().getStringExtra("job")).
                        putExtra("User_name",getIntent().getStringExtra("User_name"))
                );
        tabHost.addTab(tabSpecTab2);

        TabHost.TabSpec tabSpecTab3 = tabHost.newTabSpec("TAB3").setIndicator("마이 페이지").
                setContent(new Intent(this, activity_mypage.class).
                        putExtra("id",getIntent().getStringExtra("id")).
                        putExtra("job",getIntent().getStringExtra("job")).
                        putExtra("User_name",getIntent().getStringExtra("User_name"))
                );
        tabHost.addTab(tabSpecTab3);

        if(getIntent().getBooleanExtra("notification",false)){
            tabHost.setCurrentTab(1);
        }else{
            tabHost.setCurrentTab(0);
        }


        FirebaseMessaging.getInstance().subscribeToTopic(job);
        String token = FirebaseInstanceId.getInstance().getToken();

        DBHelper dbHelper = new DBHelper(this,"MyInfo.db",null,1);
        dbHelper.update("MyInfo","id",id,"token",token);
        dbHelper.update("MyInfo","name",name,"token",token);
        dbHelper.update("MyInfo","job",job,"token",token);

        if(name.equals("손나은")){
            dbHelper.update("MyInfo","image",MyURL + "/uploads/naeun@naver.com_1492222678.jpg","token",token);
        }else {
            dbHelper.update("MyInfo","image",MyURL + "/uploads/drkstage@naver.com_1491291289.jpg","token",token);
        }

        PushIDService pushIDService = new PushIDService();
        pushIDService.sendRegistrationToServer(this);




//        consultDB.delete("state","과외거절");
//        consultDB.deleteAll();

    }

    @Override
    protected void onStart() {
        super.onStart();
        //SQLite에 저장
        ConsultDB consultDB = new ConsultDB(this,"Consulting.db",null,1);
        consultDB.deleteAll();
        ArrayList<Consult> consults = LoadConsult();

        for(int i = 0 ; i < consults.size() ; i++){
            consultDB.insert(consults.get(i));
        }

        if(consultDB.hasNewData("ConsultDB")){
            Toast.makeText(activity_main.this, "확인 안하신 상담이 있습니다", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(activity_main.this, "모두 확인했습니다", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<Consult> LoadConsult(){
        ArrayList<Consult> result = new ArrayList<>();

        try {
            PHPRequest request = new PHPRequest(LoadConsult_URL);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",id);
            jsonObject.put("job",job);

            String json = jsonObject.toString();
            Log.d(TAG,"before json: " + json);
            String result_json = request.POSTJSON(json);
            Log.d(TAG,"after json: " + result_json);

            JSONArray jsonArray = new JSONArray(result_json);
            String LessonTitle,Category,yourname,State,Reason;
            int Confirmation,id;
            for(int i = 0 ; i < jsonArray.length() ; i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                id = jsonObject1.getInt("id");
                LessonTitle = jsonObject1.getString("LessonTitle");
                Category = jsonObject1.getString("Category");
                yourname = jsonObject1.getString("yourname");
                State = jsonObject1.getString("State");
                Reason = jsonObject1.getString("Reason");
                Confirmation = jsonObject1.getInt("confirmation");
                result.add(new Consult(id,LessonTitle,Category,yourname,Reason,State,Confirmation));
            }

            for(int i = 0 ; i < result.size() ; i ++){
                String category = result.get(i).getCategory();
                String yourname2 = result.get(i).getYourname();
                String getstate = result.get(i).getState();
                String Lessonname = result.get(i).getLessonname();
                String Reason2 = result.get(i).getReason();
                int conformation = result.get(i).getConfiramtion();
                int id2 = result.get(i).getId();
                Log.d(TAG,"<LoadConsult> " +
                        "\nid: " + id2 +
                        "\ncategory: " + category +
                        "\nyourname: " + yourname2 +
                        "\nconfirmation: " +
                        "\nstate: " + getstate +
                        "\nlessonname: " + Lessonname +
                        "\nreason: " + Reason2 +
                        "\nconformation: " + conformation);
            }
//            JSONArray consult_array =
//            JSONObject result_object = new JSONObject(result_json);
//            String lessonname = result_object.getString("lessonname");
//            String teacher_id = result_object.getString("teacher_id");
//            String student_id = result_object.getString("student_id");
//            String reason = result_object.getString("reason");
//            String state = result_object.getString("state");
//            Consult consult = new Consult(lessonname,teacher_id,student_id,reason,state);
//            result.add(consult);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
