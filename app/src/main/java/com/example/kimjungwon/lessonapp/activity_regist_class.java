package com.example.kimjungwon.lessonapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import android.Manifest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.kimjungwon.lessonapp.URLconfig.ImgUpload_url;
import static com.example.kimjungwon.lessonapp.URLconfig.LoadPersonInfo_URL;
import static com.example.kimjungwon.lessonapp.URLconfig.LoginURL;
import static com.example.kimjungwon.lessonapp.URLconfig.RegistClass_URL;

/**
 * Created by kimjungwon on 2017-04-13.
 */

public class activity_regist_class extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private static final int REQUEST_WRITE_STORAGE = 0;
    Spinner[][] lesson_spinner;
    Uri imguri;
    ImageView backgroundimg;
    Button regist_btn;
    CheckBox[] StudentBox;

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;

    String TAG = activity_regist_class.class.getSimpleName();

    String User_id,User_name,User_job;
    EditText lesson_title,lesson_intro;
    EditText[] etcText,lesson_fee;
    CheckBox dealpossible;
    TableRow[] etcRow;
    TextView byteview;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_regist_class);

        User_id = getIntent().getStringExtra("id");
        User_name = getIntent().getStringExtra("name");
        User_job = getIntent().getStringExtra("job");

        backgroundimg = (ImageView) findViewById(R.id.regist_lesson_background);
//        Glide.with(this).load(R.drawable.ic_backgroundimage).fitCenter().into(backgroundimg);

        backgroundimg.setOnClickListener(this);


        regist_btn = (Button) findViewById(R.id.regist_btn);
        regist_btn.setOnClickListener(this);


        lesson_spinner = new Spinner[3][2];
        lesson_spinner[0][0] = (Spinner) findViewById(R.id.hopelesson1);
        lesson_spinner[0][1] = (Spinner) findViewById(R.id.hopelesson1_2);
        lesson_spinner[1][0] = (Spinner) findViewById(R.id.hopelesson2);
        lesson_spinner[1][1] = (Spinner) findViewById(R.id.hopelesson2_2);
        lesson_spinner[2][0] = (Spinner) findViewById(R.id.hopelesson3);
        lesson_spinner[2][1] = (Spinner) findViewById(R.id.hopelesson3_2);

        lesson_spinner[0][0].setOnItemSelectedListener(this);
        lesson_spinner[1][0].setOnItemSelectedListener(this);
        lesson_spinner[2][0].setOnItemSelectedListener(this);

        lesson_title = (EditText) findViewById(R.id.regist_lesson_tiitle);
        lesson_intro = (EditText) findViewById(R.id.intro_lesson);
        lesson_intro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String result = "(" + Getbyte(charSequence.toString()) + " / 65535 byte)";
                byteview.setText(result);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        byteview = (TextView) findViewById(R.id.regist_byte_length);

        StudentBox = new CheckBox[8];

        StudentBox[0] = (CheckBox) findViewById(R.id.rb1);
        StudentBox[1] = (CheckBox) findViewById(R.id.rb2);
        StudentBox[2] = (CheckBox) findViewById(R.id.rb3);
        StudentBox[3] = (CheckBox) findViewById(R.id.rb4);
        StudentBox[4] = (CheckBox) findViewById(R.id.rb5);
        StudentBox[5] = (CheckBox) findViewById(R.id.rb6);
        StudentBox[6] = (CheckBox) findViewById(R.id.rb7);
        StudentBox[7] = (CheckBox) findViewById(R.id.rb8);

        setCheckBox();

        etcRow = new TableRow[3];
        etcRow[0] = (TableRow) findViewById(R.id.etcRow1);
        etcRow[1] = (TableRow) findViewById(R.id.etcRow2);
        etcRow[2] = (TableRow) findViewById(R.id.etcRow3);

        etcText = new EditText[3];
        etcText[0] = (EditText) findViewById(R.id.etcET1);
        etcText[1] = (EditText) findViewById(R.id.etcET2);
        etcText[2] = (EditText) findViewById(R.id.etcET3);

        lesson_fee = new EditText[3];
        lesson_fee[0] = (EditText) findViewById(R.id.LessonCount);
        lesson_fee[1] = (EditText) findViewById(R.id.LessonTime);
        lesson_fee[2] = (EditText) findViewById(R.id.LessonFee);

        dealpossible = (CheckBox) findViewById(R.id.LessonConsult);

        //권한
        int permissionCheckStorage = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA);
        if (permissionCheckStorage == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }
    }

    public Boolean check_boxes(CheckBox[] boxes){
        Boolean result = false;

        for (int i = 0 ; i < boxes.length ; i++){
            if(boxes[i].isChecked()){
                result = true;
            }
        }

        return  result;
    }

    public String GET_sb(CheckBox[] sb) {
        String result = "";

        for (int i = 0; i < sb.length; i++) {
            if (sb[i].isChecked()) {
                result += "@" + sb[i].getText();
            }
        }
        return result;
    }




    public Boolean Check_hopelesson(Context context) {

        int[][] index = new int[3][2];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                index[i][j] = lesson_spinner[i][j].getSelectedItemPosition();
            }
        }
//      대과목만 선택하고 소과목은 선택하지 않은 경우 | '대과목 = 기타' 일 경우는 예외
        for(int i = 0 ; i < 3 ; i++){
            if(index[i][0] != 0 && index[i][1] == 0 && index[i][0] != 9){
                Toast.makeText(context, (i+1) + "번째 소과목을 선택해주세요", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if(index[0][0] == 0 ){
            if(index[1][0] != 0 | index[2][0] != 0){
                Toast.makeText(context, "순서 대로 입력해주세요", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else if(index[1][0] == 0){
            if(index[2][0] != 0){
                Toast.makeText(context, "순서 대로 입력해주세요", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

//      과목 중복선택 예외처리 (2개/3개) 소과목까지 모두 선택한 과목중에서
        if (index[0][0] == index[1][0] && index[0][0] != 0) {
            if (index[0][1] == index[1][1]) {
                Toast.makeText(context, "과목 중복 선택은 불가 합니다", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if (index[1][0] == index[2][0] && index[1][0] != 0) {
            if (index[1][1] == index[2][1]) {
                Toast.makeText(context, "과목 중복 선택은 불가 합니다", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if (index[2][0] == index[0][0] && index[0][0] != 0) {
            if (index[2][1] == index[0][1]) {
                Toast.makeText(context, "과목 중복 선택은 불가 합니다", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if (index[0][0] == index[1][0] && index[1][0] == index[2][0] && index[0][0] != 0) {
            if (index[0][1] == index[1][1] | index[1][1] == index[2][1] | index[2][1] == index[1][1]) {
                Toast.makeText(context, "과목 중복 선택은 불가 합니다", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public String GetLesson(Spinner[][] lessons){
        String result = "";
        for (int i = 0; i < lessons.length; i++) {
//            세부과목 체크 하지 않은 항목은 입력 제외
            if(lessons[i][0].getSelectedItemPosition() == 0 ){
                Log.d("GETLesson","index: " + lessons[i][0].getSelectedItemPosition());
                break;
            }

            String s1 = lessons[i][0].getSelectedItem().toString(), s2 = lessons[i][1].getSelectedItem().toString(), s3 = "";
            EditText editText = null;
            result += "@" + s1 + " - " + s2;

            if (lessons[i][0].getSelectedItem().toString().equals("기타") && lessons[i][1].getSelectedItem().toString().equals("기타")) {
                switch (i) {
                    case 0:
                        editText = etcText[0];
                        break;
                    case 1:
                        editText = etcText[1];
                        break;
                    case 2:
                        editText = etcText[2];
                        break;
                }
                s3 = editText.getText().toString();
                result += "(" + s3 + ")";
            } else if (lessons[i][1].getSelectedItem().toString().equals("기타")) {
                switch (i) {
                    case 0:
                        editText = etcText[0];
                        break;
                    case 1:
                        editText = etcText[1];
                        break;
                    case 2:
                        editText = etcText[2];
                        break;
                }
                s3 = editText.getText().toString();
                result += "(" + s3 + ")";
            }
        }

        return result;
    }

    private int Getbyte(String s) {
        return s.getBytes().length;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "쓰기 권한 허용", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "쓰기 권한 불가", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.regist_btn:
                //예외처리
                String imagename="",result_title="",result_student="",result_lesson="",result_intro="",result_fee="";
                //1. 수업 제목 입력
                if(lesson_title.length() == 0){
                    Toast.makeText(this, "수업 제목을 입력해주세요", Toast.LENGTH_SHORT).show();
                    break;
                }else{
                    result_title = lesson_title.getText().toString();
                }

                //2. 이미지를 등록 안했을 경우
                if(imguri == null){
                    imagename = "null";
                }else{
                    try {
                        //이미지 업로드
                        Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(),imguri);
                        imagename = lesson_title.getText().toString() + "_" + System.currentTimeMillis() / 1000;
                        new Upload(bm,imagename).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                //3. 과외대상 입력
                if(check_boxes(StudentBox)){
                    result_student = GET_sb(StudentBox);
                }else{
                    Toast.makeText(this, "과외 대상을 1개 이상 선택해주세요", Toast.LENGTH_SHORT).show();
                    break;
                }

                if(check_fee(lesson_fee)){
                    result_fee = get_fee(lesson_fee,dealpossible);
                }else{
                    Toast.makeText(this, "과외비를 입력해주세요", Toast.LENGTH_SHORT).show();
                    break;
                }

                //4. 과외 과목
                if(Check_hopelesson(getApplicationContext())){
                    result_lesson = GetLesson(lesson_spinner);
                }else{
                    break;
                }

                //5. 수업 소개
                if(lesson_intro.length() == 0){
                    Toast.makeText(this, "수업 소개를 적어주세요", Toast.LENGTH_SHORT).show();
                    break;
                }else {
                    result_intro = lesson_intro.getText().toString();
                }

                JSONObject jo = new JSONObject();
                try {
                    jo.put("teacher_id",User_id);
                    jo.put("lessonbackground",imagename);
                    jo.put("title",result_title);
                    jo.put("studentlevel",result_student);
                    jo.put("lessonfee",result_fee);
                    jo.put("lessonsubject",result_lesson);
                    jo.put("intro",result_intro);

                    String json = jo.toString();

                    PHPRequest request = new PHPRequest(RegistClass_URL);
                    Log.d(TAG,"before: " + json);
                    String result = request.POSTJSON(json);
                    Log.d(TAG,"after: " + result);

                    JSONObject jo2 = new JSONObject(result);
                    Boolean ok = jo2.getBoolean("result");
                    if(ok){
                        String msg = jo2.getString("msg");
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                Log.d(TAG,"success!!" +
                        "\nimagename: " + imagename +
                        "\ntitle: " + result_title +
                        "\nstudent: " + result_student +
                        "\nsubject: " + result_lesson +
                        "\nintro: " + result_intro);
                finish();
                break;
            case R.id.regist_lesson_background:
                DialogInterface.OnClickListener CameraListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        doTakePhotoAction();
                    }
                };
                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        doTakeAlbumAction();
                    }
                };
                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                };

                new AlertDialog.Builder(this).
                        setTitle("업로드할 이미지 선택").
                        setPositiveButton("사진촬영", CameraListener).
                        setNegativeButton("앨범 선택", albumListener).
                        setNeutralButton("취소", cancelListener).
                        show();
                break;
        }
    }

    private String get_fee(EditText[] lesson_fee, CheckBox dealpossible) {
        String result = "@" + lesson_fee[0].getText().toString();
        result += "@" + lesson_fee[1].getText().toString();
        result += "@" + lesson_fee[2].getText().toString();
        if(dealpossible.isChecked()){
            result += "@(협의가능)";
        }
        return result;
    }

    private boolean check_fee(EditText[] lesson_fee) {
        for(int i = 0 ; i < lesson_fee.length ; i++){
            if(lesson_fee[i].getText().length() == 0){
                return false;
            }
        }
        return true;
    }

    public void setCheckBox(){
        try {
            PHPRequest request = new PHPRequest(LoadPersonInfo_URL);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("column_name","id_usr");
            jsonObject.put("column_data",User_id);
            jsonObject.put("job",User_job);
            jsonObject.put("column_load","studentlevel");

            Log.d(TAG,"before: " + jsonObject.toString());
            String result = request.POSTJSON(jsonObject.toString());
            Log.d(TAG,"after: " + result);

            JSONObject resultjson = new JSONObject(result);
            Log.d(TAG,"studentlevel "+resultjson.get("studentlevel").toString());
            ArrayList<String> strings = new ArrayList<>();

            StringTokenizer tokenizer = new StringTokenizer(resultjson.get("studentlevel").toString(),"@");
            while(tokenizer.hasMoreElements()){
                strings.add(tokenizer.nextToken());
            }

            for(int i = 0 ; i < strings.size() ; i++){
                for(int j = 0 ; j < StudentBox.length ; j++){
                    if(strings.get(i).equals(StudentBox[j].getText().toString())){
                        StudentBox[j].setChecked(true);
                    }
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void doTakePhotoAction() {
        Log.d(TAG, "doTakePhotoAction");
        Intent goCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(goCamera, PICK_FROM_CAMERA);
    }

    private void doTakeAlbumAction() {
        Log.d(TAG, "doTakeAlbumAction");
        Intent goAlbum = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(goAlbum, PICK_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            Log.d(TAG, "onActivityResult fail");
        } else {
            switch (requestCode) {
                case PICK_FROM_ALBUM:
                    Uri image_album = data.getData();
                    CropImage.activity(image_album).setGuidelines(CropImageView.Guidelines.ON).start(this);
                    Log.d(TAG, "PICK FROM ALBUM OK");
                    break;
                case PICK_FROM_CAMERA:
                    Uri image_camera = data.getData();
                    CropImage.activity(image_camera).setGuidelines(CropImageView.Guidelines.ON).start(this);
                    Log.d(TAG, "PICK FROM CAMERA OK");
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri resultUri = result.getUri();
//                    backgroundimg.setImageURI(resultUri);
                    Glide.with(this).load(resultUri).into(backgroundimg);
                    imguri = resultUri;
                    Log.d(TAG, "CROP OK \nresultUri: " + resultUri);
                    break;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        ArrayAdapter adapter;

        int id = adapterView.getId();
        Spinner spinner;
        final TableRow ETCROW;
        switch (id) {
            case R.id.hopelesson1:
                spinner = lesson_spinner[0][1];
                ETCROW = etcRow[0];
                break;
            case R.id.hopelesson2:
                spinner = lesson_spinner[1][1];
                ETCROW = etcRow[1];
                break;
            case R.id.hopelesson3:
                spinner = lesson_spinner[2][1];
                ETCROW = etcRow[2];
                break;
            default:
                spinner = null;
                ETCROW = null;
        }

        //기타 선택시
        switch (i) {
            default:
                ETCROW.setVisibility(View.GONE);
                //없음
            case 0:
                adapter = new ArrayAdapter(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(R.array.nosubsection_array));
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                    }
                });
                break;
            //국어
            case 1:
                adapter = new ArrayAdapter(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(R.array.kor_subsection_array));
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                        if (i == 6) {
                            ETCROW.setVisibility(View.VISIBLE);
                            Log.d("ETCROW", "id: " + ETCROW.getId());
                        } else {
                            ETCROW.setVisibility(View.GONE);
                            Log.d("ETCROW", "id: " + ETCROW.getId());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                    }
                });
                break;
            //영어
            case 2:
                adapter = new ArrayAdapter(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(R.array.eng_subsection_array));
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                        if (i == 11) {
                            ETCROW.setVisibility(View.VISIBLE);
                            Log.d("ETCROW", "id: " + ETCROW.getId());
                        } else {
                            ETCROW.setVisibility(View.GONE);
                            Log.d("ETCROW", "id: " + ETCROW.getId());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                    }
                });
                break;
            //영어 자격증
            case 3:
                adapter = new ArrayAdapter(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(R.array.engTest_subsection_array));
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                        if (i == 11) {
                            ETCROW.setVisibility(View.VISIBLE);
                            Log.d("ETCROW", "id: " + ETCROW.getId());
                        } else {
                            ETCROW.setVisibility(View.GONE);
                            Log.d("ETCROW", "id: " + ETCROW.getId());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                    }
                });
                break;
            //수학
            case 4:
                adapter = new ArrayAdapter(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(R.array.math_subsection_array));
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                        if (i == 7) {
                            ETCROW.setVisibility(View.VISIBLE);
                            Log.d("ETCROW", "id: " + ETCROW.getId());
                        } else {
                            ETCROW.setVisibility(View.GONE);
                            Log.d("ETCROW", "id: " + ETCROW.getId());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                    }
                });
                break;
            //과학
            case 5:
                adapter = new ArrayAdapter(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(R.array.science_subsection_array));
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                        if (i == 8) {
                            ETCROW.setVisibility(View.VISIBLE);
                            Log.d("ETCROW", "id: " + ETCROW.getId());
                        } else {
                            ETCROW.setVisibility(View.GONE);
                            Log.d("ETCROW", "id: " + ETCROW.getId());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                    }
                });
                break;
            //사회
            case 6:
                adapter = new ArrayAdapter(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(R.array.society_subsection_array));
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                        if (i == 11) {
                            ETCROW.setVisibility(View.VISIBLE);
                            Log.d("ETCROW", "id: " + ETCROW.getId());
                        } else {
                            ETCROW.setVisibility(View.GONE);
                            Log.d("ETCROW", "id: " + ETCROW.getId());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                    }
                });
                break;
            //예체능
            case 7:
                adapter = new ArrayAdapter(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(R.array.amp_subsection_array));
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                        if (i == 10) {
                            ETCROW.setVisibility(View.VISIBLE);
                            Log.d("ETCROW", "id: " + ETCROW.getId());
                        } else {
                            ETCROW.setVisibility(View.GONE);
                            Log.d("ETCROW", "id: " + ETCROW.getId());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                    }
                });
                break;
            //제2외국어
            case 8:
                adapter = new ArrayAdapter(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(R.array.foreignlanguage_subsection_array));
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                        if (i == 11) {
                            ETCROW.setVisibility(View.VISIBLE);
                            Log.d("ETCROW", "id: " + ETCROW.getId());
                        } else {
                            ETCROW.setVisibility(View.GONE);
                            Log.d("ETCROW", "id: " + ETCROW.getId());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                    }
                });
                break;
            //기타
            case 9:
                adapter = new ArrayAdapter(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(R.array.etc_subsection_array));
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                        if (i == 0) {
                            ETCROW.setVisibility(View.VISIBLE);
                            Log.d("ETCROW", "id: " + ETCROW.getId());
                        } else {
                            ETCROW.setVisibility(View.GONE);
                            Log.d("ETCROW", "id: " + ETCROW.getId());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                    }
                });

//                ETCROW.setVisibility(View.VISIBLE);
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private class Upload extends AsyncTask<Void, Void, String> {
        private Bitmap image;
        private String name;

        public Upload(Bitmap image, String name) {
            this.image = image;
            this.name = name;
        }

        @Override
        protected String doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //compress the image to jpg format
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            /*
            * encode image to base64 so that it can be picked by saveImage.php file
            * */
            String encodeImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            //generate hashMap to store encodedImage and the User_name
            HashMap<String, String> detail = new HashMap<>();
            detail.put("name", name);
            detail.put("image", encodeImage);

            try {
                //convert this HashMap to encodedUrl to send to php file
                String dataToSend = hashMapToUrl(detail);
                //make a Http request and send data to saveImage.php file
                Log.d(TAG, "Upload dataToSend: " + dataToSend);
                String response = PHPRequest.ImgPOST(ImgUpload_url, dataToSend);
                Log.d(TAG,"Upload response: " + response);
                //return the response
                return response;

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "ERROR  " + e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            //show image uploaded
            Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
        }

        public String BitmapToString(Bitmap bitmap) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            String b64 = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            return b64;
//
//        StringBuilder result = new StringBuilder();
//        try {
//            result.append(URLEncoder.encode(b64,"UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return result.toString();
        }

        private String hashMapToUrl(HashMap<String, String> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            Log.d("hashMapToUrl", result.toString());

            return result.toString();
        }
    }
}
