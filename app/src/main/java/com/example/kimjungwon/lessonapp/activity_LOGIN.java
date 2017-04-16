package com.example.kimjungwon.lessonapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginDefine;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.kimjungwon.lessonapp.URLconfig.LoginURL;

/**
 * Created by kimjungwon on 2017-02-14.
 */

public class activity_LOGIN extends AppCompatActivity implements View.OnClickListener {

    public EditText usr_id, usr_pw;
    public Button goJoin, login;

    public OAuthLoginButton naverlogin;


    private static String OAUTH_CLIENT_ID = "8iDN8v5kgETJBA0W_30G";  // 1)에서 받아온 값들을 넣어좁니다
    private static String OAUTH_CLIENT_SECRET = "2fCKREFluR";
    private static String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인";

    private static OAuthLogin mOAuthLoginInstance;
    private static Context mContext;

    String email = "";
    String nickname = "";
    String enc_id = "";
    String profile_image = "";
    String age = "";
    String gender = "";
    String id = "";
    String name = "";
    String birthday = "";

    String accessToken = "";
    String tokenType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        NetworkUtil.setNetworkPolicy();

        //네이버 로그인
        OAuthLoginDefine.DEVELOPER_VERSION = true;
        mContext = this;

        initData();
        initView();

        usr_id = (EditText)findViewById(R.id.login_id);
        usr_pw = (EditText)findViewById(R.id.login_pw);

        login = (Button)findViewById(R.id.login);
        goJoin = (Button)findViewById(R.id.gojoin);

        naverlogin = (OAuthLoginButton) findViewById(R.id.button_naverlogin);
        naverlogin.setOAuthLoginHandler(mOAuthLoginHandler);

        login.setOnClickListener(this);
        goJoin.setOnClickListener(this);

    }

    private void initView() {
//        tv1 = (TextView)findViewById(R.id.tv1);
//        tv2 = (TextView)findViewById(R.id.tv2);
//        tv3 = (TextView)findViewById(R.id.tv3);
//        tv4 = (TextView)findViewById(R.id.tv4);
//        tv5 = (TextView)findViewById(R.id.tv5);
    }

    private void initData() {
        mOAuthLoginInstance = OAuthLogin.getInstance();

        mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);
    }

    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = mOAuthLoginInstance.getAccessToken(mContext);

                String token = accessToken;// 네이버 로그인 접근 토큰;
                String header = "Bearer " + token; // Bearer 다음에 공백 추가
                try {
                    String apiURL = "https://openapi.naver.com/v1/nid/me";
                    URL url = new URL(apiURL);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Authorization", header);
                    int responseCode = con.getResponseCode();
                    BufferedReader br;
                    if(responseCode==200) { // 정상 호출
                        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    } else {  // 에러 발생
                        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    }
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = br.readLine()) != null) {
                        response.append(inputLine);
                    }
                    br.close();

                    String result = response.toString();
                    JSONObject jsonObject = new JSONObject(result);

                    //소셜 값 추가
                    jsonObject.put("social","naver");
                    result = jsonObject.toString();

                    //네이버에서 회원 정보 받아왓다

                    PHPRequest request2 = new PHPRequest(LoginURL);

//                    Toast.makeText(mContext,"보내기전: " + result,Toast.LENGTH_LONG).show();
                    Log.d("login","before json: " + result);
                    String result_json = request2.POSTJSON(result);
                    Log.d("login","after json: " + result_json);

                    JSONObject jsonObject1 = new JSONObject(result_json);

                    Boolean result_login = jsonObject1.getBoolean("login");

//                    Toast.makeText(activity_LOGIN.this, result_json, Toast.LENGTH_SHORT).show();

                    //로그인
                    if(result_login){
                        String result_msg = jsonObject1.getString("msg");
                        Toast.makeText(mContext,result_msg,Toast.LENGTH_LONG).show();

                        Intent gomain = new Intent(mContext, activity_main.class);
                        gomain.putExtra("id",jsonObject1.getString("id"));
                        gomain.putExtra("User_name",jsonObject1.getString("name"));
                        gomain.putExtra("job",jsonObject1.getString("job"));

                        startActivity(gomain);
                        //회원 가입
                    }else{
                        Intent goSelectJob = new Intent(activity_LOGIN.this,activity_selectjob.class);
                        String result_email = jsonObject1.getString("email");
                        String result_name = jsonObject1.getString("name");
                        String result_social = jsonObject1.getString("social");
                        String result_gender = jsonObject1.getString("gender");
//                        Toast.makeText(mContext,"gender" + result_gender,Toast.LENGTH_SHORT).show();

                        String result_birthday = jsonObject1.getString("birthday");

                        goSelectJob.putExtra("email",result_email);
                        goSelectJob.putExtra("User_name",result_name);
                        goSelectJob.putExtra("social",result_social);
                        goSelectJob.putExtra("gender",result_gender);
                        goSelectJob.putExtra("birthday",result_birthday);

                        startActivity(goSelectJob);
                    }
                    //제이슨 오브젝트 파싱
//                    JSONObject jsonObject = new JSONObject(result);
//                    String result_response = jsonObject.getString("response");
//                    JSONObject jsonObject1 = new JSONObject(result_response);
//                    String result_mail = jsonObject1.getString("email");
//                    String result_nickname = jsonObject1.getString("nickname");
//                    String result_age = jsonObject1.getString("age");
//                    String result_gender = jsonObject1.getString("gender");
//                    String result_name = jsonObject1.getString("User_name");
//                    String result_birthday = jsonObject1.getString("birthday");
//
//                    String parsing = "mail: " + result_mail + "\nnickname: " + result_nickname + "\nage: " + result_age + "\ngender" + result_gender + "\nname: " + result_name + "\nbirthday: " + result_birthday;
                } catch (Exception e) {
                    System.out.println(e);
                }
                String refreshToken = mOAuthLoginInstance.getRefreshToken(mContext);
                long expiresAt = mOAuthLoginInstance.getExpiresAt(mContext);
                String tokenType = mOAuthLoginInstance.getTokenType(mContext);
            } else {
                String errorCode = mOAuthLoginInstance.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext);
                Toast.makeText(mContext, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }

        }

    };

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.gojoin:
                Intent intent = new Intent(getApplicationContext(),activity_selectjob.class);
                startActivity(intent);
                break;

            case R.id.login:
                Log.i("LOGIN","로그인 클릭!");
                try {
                    PHPRequest request = new PHPRequest(LoginURL);
                    String result = request.PHPLOGIN(String.valueOf(usr_id.getText()), String.valueOf(usr_pw.getText()));
                    Log.i("LOGIN","result: " + result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        Boolean result_login = jsonObject.getBoolean("login");
                        String result_msg = jsonObject.getString("msg");

                        Toast.makeText(getApplicationContext(),result_msg,Toast.LENGTH_SHORT).show();
//                        Toast.makeText(mContext, jsonObject.getString("id") + jsonObject.getString("User_name"), Toast.LENGTH_SHORT).show();
                        //아이디 중복이 안되었다면
                        if(result_login){
                            //자동으로 로그인 페이지 넘어가기
                            Intent goMain = new Intent(getApplicationContext(),activity_main.class);
                            goMain.putExtra("id",jsonObject.getString("id"));
                            goMain.putExtra("User_name",jsonObject.getString("name"));
                            goMain.putExtra("job",jsonObject.getString("job"));
                            //이번에 생긴 로그인 페이지는 스택에 쌓이지 않는당
                            startActivity(goMain);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.i("LOGIN",result);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.button_naverlogin:
                Log.i("naverloginclick","네이버 로그인");
                mOAuthLoginInstance.startOauthLoginActivity(activity_LOGIN.this,mOAuthLoginHandler);
                break;

        }
    }

}
