package com.example.kimjungwon.lessonapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;

import static com.example.kimjungwon.lessonapp.URLconfig.LoadConsult_URL;

/**
 * Created by kimjungwon on 2017-04-22.
 */

public class activity_myconsulting extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerAdapter_consulting recyclerAdapter_consulting;
    String TAG = activity_myconsulting.class.getSimpleName();

    String id,name,job,token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_myconsulting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_chat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("과외 톡");

        DBHelper dbHelper = new DBHelper(this,"MyInfo.db",null,1);

        id = dbHelper.getResult("MyInfo","id");
        token = dbHelper.getResult("MyInfo","token");
        name = dbHelper.getResult("MyInfo","name");
        job = dbHelper.getResult("MyInfo","job");


        recyclerView = (RecyclerView) findViewById(R.id.consulting_rv);

        ConsultDB consultDB = new ConsultDB(activity_myconsulting.this,"Consulting.db",null,1);

        final ArrayList<Consult> consults = consultDB.getResult();

//        for(int i = 0 ; i < 10 ; i ++){
//            String reason = null;
//            if(i%3 == 0){
//                reason = "이유";
//            }
//            consults.add(new Consult("수업"+i,"선생님"+i,"학생"+i,reason,"상담중"));
//        }

//        for(int i = 0 ; i < consults.size() ; i ++){
//            Log.d(TAG,"consult:" +
//                    "\nlessonname: " + consults.get(i).getLessonname() +
//                    "\nteacher_id: " + consults.get(i).getTeacher_id() +
//                    "\nstudent_id: " + consults.get(i).getStudent_id() +
//                    "\nreason: " + consults.get(i).getReason() +
//                    "\nstate: " + consults.get(i).getState());
//        }

        recyclerAdapter_consulting = new RecyclerAdapter_consulting(activity_myconsulting.this,consults,R.layout.card_consulting);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter_consulting);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(),
                recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Consult consult = consults.get(position);
                        if(consult.getState().equals("과외 거절")){
                            Toast.makeText(activity_myconsulting.this, "거절된 과외입니다.", Toast.LENGTH_SHORT).show();
                        }else{
                            Intent goChat = new Intent(getApplicationContext(), activity_chatting.class);
                            goChat.putExtra("consult",consult);
                            startActivity(goChat);
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));

    }

//    public ArrayList<Consult> LoadConsult(){
//        ArrayList<Consult> result = new ArrayList<>();
//
//        try {
//            PHPRequest request = new PHPRequest(LoadConsult_URL);
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("id",id);
//            jsonObject.put("job",job);
//
//            String json = jsonObject.toString();
//            Log.d(TAG,"before json: " + json);
//            String result_json = request.POSTJSON(json);
//            Log.d(TAG,"after json: " + result_json);
//
//            JSONArray jsonArray = new JSONArray(result_json);
//            String LessonTitle,Category,yourname,State,Reason;
//            int Confirmation,id;
//            for(int i = 0 ; i < jsonArray.length() ; i++){
//                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                id = jsonObject1.getInt("id");
//                LessonTitle = jsonObject1.getString("LessonTitle");
//                Category = jsonObject1.getString("Category");
//                yourname = jsonObject1.getString("yourname");
//                State = jsonObject1.getString("State");
//                Reason = jsonObject1.getString("Reason");
//                Confirmation = jsonObject1.getInt("confirmation");
//                result.add(new Consult(id,LessonTitle,Category,yourname,Reason,State,Confirmation));
//            }
//
//            for(int i = 0 ; i < result.size() ; i ++){
//                String category = result.get(i).getCategory();
//                String yourname2 = result.get(i).getYourname();
//                String getstate = result.get(i).getState();
//                String Lessonname = result.get(i).getLessonname();
//                String Reason2 = result.get(i).getReason();
//                Log.d(TAG,"Reason2: " + Reason2);
//            }
////            JSONArray consult_array =
////            JSONObject result_object = new JSONObject(result_json);
////            String lessonname = result_object.getString("lessonname");
////            String teacher_id = result_object.getString("teacher_id");
////            String student_id = result_object.getString("student_id");
////            String reason = result_object.getString("reason");
////            String state = result_object.getString("state");
////            Consult consult = new Consult(lessonname,teacher_id,student_id,reason,state);
////            result.add(consult);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }
}
