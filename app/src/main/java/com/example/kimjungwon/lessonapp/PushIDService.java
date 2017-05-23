package com.example.kimjungwon.lessonapp;

import android.content.Context;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.example.kimjungwon.lessonapp.URLconfig.RegisterToken_URL;

/**
 * Created by kimjungwon on 2017-04-21.
 */

public class PushIDService extends FirebaseInstanceIdService{
    private static final String TAG = PushIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String token = FirebaseInstanceId.getInstance().getToken();
        //token 값이 null 일 경우 토큰이 발행된다
        Log.d(TAG, "Refreshed token: " + token);

        final DBHelper dbHelper = new DBHelper(this,"MyInfo.db",null,1);
        dbHelper.insert("","","",token,"");


//        // 생성등록된 토큰을 개인 앱서버에 보내 저장해 두었다가 추가 뭔가를 하고 싶으면 할 수 있도록 한다.
//        sendRegistrationToServer(token);
    }

    public void sendRegistrationToServer(Context context) {
        // Add custom implementation, as needed.

        DBHelper dbHelper = new DBHelper(context,"MyInfo.db",null,1);

        String id = dbHelper.getResult("MyInfo","id");
        String token = dbHelper.getResult("MyInfo","token");
        String name = dbHelper.getResult("MyInfo","name");
        String job = dbHelper.getResult("MyInfo","job");

        Log.d(TAG,"Token: " + token);
        Log.d(TAG,"id: " + id);
        Log.d(TAG,"name: " + name);
        Log.d(TAG,"job: " + job);

        try {
            PHPRequest request = new PHPRequest(RegisterToken_URL);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Token",token);
            jsonObject.put("id",id);
            jsonObject.put("name",name);
            jsonObject.put("job",job);
            String jo = jsonObject.toString();
            Log.d(TAG,"before json: " + jo);
            String result = request.POSTJSON(jo);
            Log.d(TAG,"after json: " + result);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = new FormBody.Builder()
//                .add("Token", token)
//                .add("id", id)
//                .build();
//
//        //request
//        Request request = new Request.Builder()
//                .url(RegisterToken_URL)
//                .post(body)
//                .build();
//
//        try {
//            client.newCall(request).execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}
