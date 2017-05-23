package com.example.kimjungwon.lessonapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;

import static com.example.kimjungwon.lessonapp.URLconfig.LoadConsult_URL;
import static com.example.kimjungwon.lessonapp.URLconfig.UpdateConsult_URL;

/**
 * Created by kimjungwon on 2017-04-22.
 */

public class activity_myconsulting extends AppCompatActivity {
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerAdapter_consulting recyclerAdapter_consulting;
    ArrayList<Consult> consults;
    ConsultDB consultDB;
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

    @Override
    protected void onStart() {
        super.onStart();
        recyclerView = (RecyclerView) findViewById(R.id.consulting_rv);

        consultDB = new ConsultDB(activity_myconsulting.this,"Consulting.db",null,1);

        consults = consultDB.getResult();

        recyclerAdapter_consulting = new RecyclerAdapter_consulting(activity_myconsulting.this,consults,R.layout.card_consulting);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter_consulting);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, final int position) {
                                final Consult consult = consults.get(position);
                                if(consult.getState().equals("과외 거절")){
                                    new MaterialDialog.Builder(activity_myconsulting.this)
                                            .title("거절된 상담입니다.")
                                            .content("거절된 상담을 목록에서 삭제 할까요?")
                                            .positiveText("확인")
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    //sqlite에서 지우기
                                                    consultDB.deleteByid("id", consult.getId());
                                                    //mysql 지우기
                                                    try {
                                                        PHPRequest phpRequest = new PHPRequest(UpdateConsult_URL);
                                                        JSONObject jsonObject = new JSONObject();
                                                        jsonObject.put("case",4);
                                                        jsonObject.put("id",consult.getId());
                                                        String before = jsonObject.toString();
                                                        Log.d(TAG,"delete consult before : " + before);
                                                        String result = phpRequest.POSTJSON(before);
                                                        Log.d(TAG,"delete consult after : " + result);
                                                    } catch (MalformedURLException e) {
                                                        e.printStackTrace();
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    //adapter지우기
                                                    consults.remove(position);
                                                    recyclerAdapter_consulting.notifyDataSetChanged();
                                                    Toast.makeText(activity_myconsulting.this, "상담을 삭제 했습니다", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .negativeText("취소")
                                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    Toast.makeText(activity_myconsulting.this, "취소", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                }
                                            })
                                            .show();
                                }else{
                                    Intent goChat = new Intent(getApplicationContext(), activity_chatting.class);
                                    goChat.putExtra("consult",consult);
                                    goChat.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    startActivity(goChat);
                                }
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {

                            }
                        }));

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.consulting_rv_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                consults = consultDB.getResult();
                swipeRefreshLayout.setRefreshing(true);

//                recyclerAdapter_consulting.notifyDataSetChanged();
                consults = LoadConsult();
                for(int i = 0 ; i < consults.size() ; i ++){
                    Log.d(TAG,"Refresh Consult\nstate: " + consults.get(i).getState() + "\nreason: " + consults.get(i).getReason());
                }

                recyclerAdapter_consulting.clear();
                recyclerAdapter_consulting.addAll(consults);
                recyclerAdapter_consulting.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        consults = LoadConsult();
        recyclerAdapter_consulting.notifyDataSetChanged();
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
