package com.example.kimjungwon.lessonapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.kimjungwon.lessonapp.URLconfig.idCheckURL;

public class activity_JOIN extends AppCompatActivity implements View.OnClickListener {

    public EditText usr_id, usr_pw1, usr_pw2;
    public ImageView id_check, pw1_check , pw_2check;

    public Boolean id_dup = false;
    private Button join_btn;
    Intent job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join2);

        NetworkUtil.setNetworkPolicy();
        //
        job = getIntent();
//        Toast.makeText(this, "job: " + job.getStringExtra("job"), Toast.LENGTH_SHORT).show();

//        if(job.hasExtra("social")){
//            Toast.makeText(this, "social: yes", Toast.LENGTH_SHORT).show();
//        }
        //
        usr_id = (EditText) findViewById(R.id.id);
        usr_pw1 = (EditText) findViewById(R.id.password1);
        usr_pw2 = (EditText) findViewById(R.id.password2);
        id_check = (ImageView) findViewById(R.id.id_iv);
        pw1_check = (ImageView) findViewById(R.id.pw1_iv);
        pw_2check = (ImageView) findViewById(R.id.pw2_iv);
        join_btn = (Button) findViewById(R.id.join);

        join_btn.setEnabled(false);
        join_btn.setOnClickListener(this);

        usr_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                //아이디 입력한 후 부터 적용
                if(usr_id.getText().length() != 0){
                    //이메일 중복 검사
                    JSONObject jo = new JSONObject();
                    try {
                        jo.put("id",usr_id.getText().toString());

                        PHPRequest request = new PHPRequest(idCheckURL);
                        String result = request.POSTJSON(jo.toString());
                        JSONObject jsResult = new JSONObject(result);

                        Toast.makeText(activity_JOIN.this, jsResult.getString("msg"), Toast.LENGTH_SHORT).show();

                        id_dup = jsResult.getBoolean("id_duplication");

                        if(!id_dup){
                            id_check.setVisibility(View.VISIBLE);
                            id_check.setImageResource(R.drawable.checked);
                            join_btn.setEnabled(true);
                        }else{
                            id_check.setVisibility(View.VISIBLE);
                            id_check.setImageResource(R.drawable.error);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        usr_pw1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String pw1 = String.valueOf(usr_pw1.getText());
                //password 유효성 체크
                if(pw1.length() != 0){
                    if (!isPassword(pw1)) {
                        Toast.makeText(getApplicationContext(), "비밀번호가 유효하지 않습니다", Toast.LENGTH_SHORT).show();
                        pw1_check.setVisibility(View.VISIBLE);
                        pw1_check.setImageResource(R.drawable.error);
                        Log.i("check", "pw_check: " + pw1);
                    }else{
                        pw1_check.setVisibility(View.VISIBLE);
                        pw1_check.setImageResource(R.drawable.checked);
                    }
                }
            }
        });
        usr_pw2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                if(actionID == EditorInfo.IME_ACTION_DONE){
                    String pw1 = String.valueOf(usr_pw1.getText());
                    String pw2 = String.valueOf(usr_pw2.getText());
                    if(pw2.length() != 0){
                        //pw1과pw2가 불일치한다면 회원 가입 불가
                        if (!pw1.equals(pw2)) {
                            pw_2check.setVisibility(View.VISIBLE);
                            pw_2check.setImageResource(R.drawable.error);
                            Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                            Log.i("check", "pw1: " + pw1 + "/pw2: " + pw2);
                        }else{
                            pw_2check.setVisibility(View.VISIBLE);
                            pw_2check.setImageResource(R.drawable.checked);
                        }
                    }
                }
                return false;
            }
        });
        
        //gojoin(소셜 회원가입)
        Intent intent = getIntent();
        if (intent != null) {
            Toast.makeText(getApplicationContext(), "social: " + intent.getStringExtra("social"), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.join:
                String pw1 = String.valueOf(usr_pw1.getText());
                String pw2 = String.valueOf(usr_pw2.getText());
                String check_id = String.valueOf(usr_id.getText());


                //아이디(이메일) 유효성 체크
                if (!isEmail(check_id)) {
                    Toast.makeText(getApplicationContext(), "아이디 양식이 맞지 않습니다", Toast.LENGTH_SHORT).show();
                    usr_id.requestFocus();
                    Log.i("check", "email: " + String.valueOf(usr_id));
                    break;
                }

                //이메일 중복 검사
                JSONObject jo = new JSONObject();
                try {
                    jo.put("id",usr_id.getText().toString());

                    PHPRequest request = new PHPRequest(idCheckURL);
                    String result = request.POSTJSON(jo.toString());
                    JSONObject jsResult = new JSONObject(result);

                    Toast.makeText(activity_JOIN.this, jsResult.getString("msg"), Toast.LENGTH_SHORT).show();

                    id_dup = jsResult.getBoolean("id_duplication");

                    if(!id_dup){
                        join_btn.setEnabled(true);
                    }else{
                        usr_id.requestFocus();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                //pw1과pw2가 불일치한다면 회원 가입 불가
                if (!pw1.equals(pw2)) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                    usr_pw2.requestFocus();
                    Log.i("check", "pw1: " + pw1 + "/pw2: " + pw2);
                    break;
                }

                //password 유효성 체크
                if (!isPassword(pw1)) {
                    usr_pw1.requestFocus();
                    Toast.makeText(getApplicationContext(), "비밀번호가 유효하지 않습니다", Toast.LENGTH_SHORT).show();
                    Log.i("check", "pw_check: " + pw1);
                    break;
                }

//                //이름 (빈칸) 유효성 체크
//                if(check_name.length() == 0){
//                    Toast.makeText(getApplicationContext(),"이름을 입력하지 않았습니다",Toast.LENGTH_SHORT).show();
//                    Log.i("check","name_check: "+check_name);
//                    break;
//                }

                //자동으로 로그인 페이지 넘어가기
                Intent goprofile = new Intent(getApplicationContext(), activity_JOIN_Profile.class);
                goprofile.putExtra("id",check_id);
                goprofile.putExtra("pw",pw1);
                goprofile.putExtra("job",job.getStringExtra("job"));

                if(job.hasExtra("social")){
                    goprofile.putExtra("email",job.getStringExtra("email"));
                    goprofile.putExtra("User_name",job.getStringExtra("User_name"));
                    goprofile.putExtra("social",job.getStringExtra("social"));
                    goprofile.putExtra("gender",job.getStringExtra("gender"));
                    goprofile.putExtra("birthday",job.getStringExtra("birthday"));
                }

                //회원가입 페이지 스택에서 없애기
                goprofile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goprofile);
                break;
        }
    }

    public static boolean isEmail(String email) {
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isNormal = m.matches();
        return isNormal;
    }

    public static boolean isPassword(String pw) {
        String regex = "^[a-zA-Z0-9~!@#$%^&*()]{8,16}";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(pw);
        boolean isNormal = m.matches();
        return isNormal;
    }
}
