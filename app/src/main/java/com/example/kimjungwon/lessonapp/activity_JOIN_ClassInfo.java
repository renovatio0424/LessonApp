package com.example.kimjungwon.lessonapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.kimjungwon.lessonapp.URLconfig.ImgUpload_url;
import static com.example.kimjungwon.lessonapp.URLconfig.JoinURL;

/**
 * Created by kimjungwon on 2017-03-15.
 */

public class activity_JOIN_ClassInfo extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, DialogInterface.OnDismissListener {

    private static final String TAG = activity_JOIN_ClassInfo.class.getSimpleName();
    private static final int REQUEST_SEARCH_UNIV = 1;
    //subeject 희망과목순위_세부항목
    public Spinner[][] subjects, PlaceSpinner;
    public ArrayList<Area>[][] Placelist;
    public ArrayAdapter[][] LPadapter;
    public AreaHandler[][] mAreaHandler;
    public CheckBox LessonConsult;

    public Spinner[] days;
    public TextView[] times;
    public CheckBox[] possible;
    public TableRow[] timeRow;

    public Button Next;
    public TableRow etcRow1, etcRow2, etcRow3;
    public TableRow lpROW[];
//    public String[] StyleExplanation = {"스파르타처럼 빡세게 가르쳐주시는 스타일"
//            ,"수업 시간이 재밌는 스타일"
//            ,"개념 원리 이해를 중심으로 가르치는 스타일"
//            ,"자유 분방한 분위기로 가르쳐주시는 스타일"
//            ,"누구도 생각하지 못했던 방법으로 해결하는 스타일"
//            ,"문제를 많이 풀면서 이해하는 사람들에게 추천하는 스타일"
//            ,"열정적으로 가르쳐 주시는 스타일"
//            ,"차분하게 가르쳐 주시는 걸 좋아하는 분들에게 추천하는 스타일"
//            ,"꼼꼼하게 학생들을 가르쳐 주시는 스타일"};

    public EditText etcText1, etcText2, etcText3, LessonCount, LessonTime, LessonFee;

    //세부 정보
    public String LessonSubject, Fee, dealpossible = "", LessonPlace;
    public Spinner lesson_category_spinner;

    String url = "http://52.79.203.148/searcharea.php";

    Intent Data;

    LinearLayout TeacherForm, StudentForm;

    //학생
    Spinner tg_spinner;
    EditText multiText_st, tage1, tage2;
    TextView bytelength_st;

    //선생님
    TableLayout placetable;
    CheckBox[] StudentBox;
    EditText multiText_tc;
    TextView bytelength_tc,search_univ_view;
    TableRow collegeRow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lessonstyle_common);

        subjects = new Spinner[3][2];

        subjects[0][0] = (Spinner) findViewById(R.id.hopelesson1);
        subjects[0][1] = (Spinner) findViewById(R.id.hopelesson1_2);
        subjects[1][0] = (Spinner) findViewById(R.id.hopelesson2);
        subjects[1][1] = (Spinner) findViewById(R.id.hopelesson2_2);
        subjects[2][0] = (Spinner) findViewById(R.id.hopelesson3);
        subjects[2][1] = (Spinner) findViewById(R.id.hopelesson3_2);

        PlaceSpinner = new Spinner[5][3];

        PlaceSpinner[0][0] = (Spinner) findViewById(R.id.LessonPlace1_1);
        PlaceSpinner[0][1] = (Spinner) findViewById(R.id.LessonPlace1_2);
        PlaceSpinner[0][2] = (Spinner) findViewById(R.id.LessonPlace1_3);
        PlaceSpinner[1][0] = (Spinner) findViewById(R.id.LessonPlace2_1);
        PlaceSpinner[1][1] = (Spinner) findViewById(R.id.LessonPlace2_2);
        PlaceSpinner[1][2] = (Spinner) findViewById(R.id.LessonPlace2_3);
        PlaceSpinner[2][0] = (Spinner) findViewById(R.id.LessonPlace3_1);
        PlaceSpinner[2][1] = (Spinner) findViewById(R.id.LessonPlace3_2);
        PlaceSpinner[2][2] = (Spinner) findViewById(R.id.LessonPlace3_3);
        PlaceSpinner[3][0] = (Spinner) findViewById(R.id.LessonPlace4_1);
        PlaceSpinner[3][1] = (Spinner) findViewById(R.id.LessonPlace4_2);
        PlaceSpinner[3][2] = (Spinner) findViewById(R.id.LessonPlace4_3);
        PlaceSpinner[4][0] = (Spinner) findViewById(R.id.LessonPlace5_1);
        PlaceSpinner[4][1] = (Spinner) findViewById(R.id.LessonPlace5_2);
        PlaceSpinner[4][2] = (Spinner) findViewById(R.id.LessonPlace5_3);

//        student_level_spiner = (Spinner) findViewById(R.id.level_spinner);
//        student_category_spinner = (Spinner) findViewById(R.id.student_category_spinner);
        lesson_category_spinner = (Spinner) findViewById(R.id.lesson_category);

        LPadapter = new ArrayAdapter[5][3];

//        style1 = (CheckBox) findViewById(R.id.checkBox);
//        style2 = (CheckBox) findViewById(R.id.checkBox2);
//        style3 = (CheckBox) findViewById(R.id.checkBox3);
//        style4 = (CheckBox) findViewById(R.id.checkBox4);
//        style5 = (CheckBox) findViewById(R.id.checkBox5);
//        style6 = (CheckBox) findViewById(R.id.checkBox6);
//        style7 = (CheckBox) findViewById(R.id.checkBox7);
//        style8 = (CheckBox) findViewById(R.id.checkBox8);
//        style9 = (CheckBox) findViewById(R.id.checkBox9);

        LessonConsult = (CheckBox) findViewById(R.id.LessonConsult);

        Next = (Button) findViewById(R.id.next_btn);

        etcRow1 = (TableRow) findViewById(R.id.etcRow1);
        etcRow2 = (TableRow) findViewById(R.id.etcRow2);
        etcRow3 = (TableRow) findViewById(R.id.etcRow3);
//        styleRow = (TableRow) findViewById(R.id.styledesRow);

        placetable = (TableLayout) findViewById(R.id.placetable);

        lpROW = new TableRow[5];
        lpROW[0] = (TableRow) findViewById(R.id.lpROW1);
        lpROW[1] = (TableRow) findViewById(R.id.lpROW2);
        lpROW[2] = (TableRow) findViewById(R.id.lpROW3);
        lpROW[3] = (TableRow) findViewById(R.id.lpROW4);
        lpROW[4] = (TableRow) findViewById(R.id.lpROW5);

        etcText1 = (EditText) findViewById(R.id.etcET1);
        etcText2 = (EditText) findViewById(R.id.etcET2);
        etcText3 = (EditText) findViewById(R.id.etcET3);

        LessonCount = (EditText) findViewById(R.id.LessonCount);
        LessonTime = (EditText) findViewById(R.id.LessonTime);
        LessonFee = (EditText) findViewById(R.id.LessonFee);

//        StyleText = (TextView) findViewById(R.id.styleText);
//        StyleSymbol = (ImageView) findViewById(R.id.styleSymbol);

        TeacherForm = (LinearLayout) findViewById(R.id.teacher_form);
        StudentForm = (LinearLayout) findViewById(R.id.student_form);

        Placelist = new ArrayList[5][3];

//        setAllchangelistner(StyleList);

        subjects[0][0].setOnItemSelectedListener(this);

        subjects[1][0].setOnItemSelectedListener(this);

        subjects[2][0].setOnItemSelectedListener(this);

        //추가 입력 선생님
        collegeRow = (TableRow) findViewById(R.id.collegerow);

        days = new Spinner[12];

        days[0] = (Spinner) findViewById(R.id.TimeSpinner1);
        days[1] = (Spinner) findViewById(R.id.TimeSpinner2);
        days[2] = (Spinner) findViewById(R.id.TimeSpinner3);
        days[3] = (Spinner) findViewById(R.id.TimeSpinner4);
        days[4] = (Spinner) findViewById(R.id.TimeSpinner5);
        days[5] = (Spinner) findViewById(R.id.TimeSpinner6);
        days[6] = (Spinner) findViewById(R.id.TimeSpinner7);
        days[7] = (Spinner) findViewById(R.id.TimeSpinner8);
        days[8] = (Spinner) findViewById(R.id.TimeSpinner9);
        days[9] = (Spinner) findViewById(R.id.TimeSpinner10);
        days[10] = (Spinner) findViewById(R.id.TimeSpinner11);
        days[11] = (Spinner) findViewById(R.id.TimeSpinner12);

        for (int i = 0; i < 12; i++) {
            setVisible(days[i]);
        }

        times = new TextView[12];
        times[0] = (TextView) findViewById(R.id.time1);
        times[1] = (TextView) findViewById(R.id.time2);
        times[2] = (TextView) findViewById(R.id.time3);
        times[3] = (TextView) findViewById(R.id.time4);
        times[4] = (TextView) findViewById(R.id.time5);
        times[5] = (TextView) findViewById(R.id.time6);
        times[6] = (TextView) findViewById(R.id.time7);
        times[7] = (TextView) findViewById(R.id.time8);
        times[8] = (TextView) findViewById(R.id.time9);
        times[9] = (TextView) findViewById(R.id.time10);
        times[10] = (TextView) findViewById(R.id.time11);
        times[11] = (TextView) findViewById(R.id.time12);

        for (int i = 0; i < 12; i++) {
            setCustomDialog(times[i]);
        }

        possible = new CheckBox[12];
        possible[0] = (CheckBox) findViewById(R.id.deal1);
        possible[1] = (CheckBox) findViewById(R.id.deal2);
        possible[2] = (CheckBox) findViewById(R.id.deal3);
        possible[3] = (CheckBox) findViewById(R.id.deal4);
        possible[4] = (CheckBox) findViewById(R.id.deal5);
        possible[5] = (CheckBox) findViewById(R.id.deal6);
        possible[6] = (CheckBox) findViewById(R.id.deal7);
        possible[7] = (CheckBox) findViewById(R.id.deal8);
        possible[8] = (CheckBox) findViewById(R.id.deal9);
        possible[9] = (CheckBox) findViewById(R.id.deal10);
        possible[10] = (CheckBox) findViewById(R.id.deal11);
        possible[11] = (CheckBox) findViewById(R.id.deal12);

        timeRow = new TableRow[12];
        timeRow[0] = (TableRow) findViewById(R.id.timerow1);
        timeRow[1] = (TableRow) findViewById(R.id.timerow2);
        timeRow[2] = (TableRow) findViewById(R.id.timerow3);
        timeRow[3] = (TableRow) findViewById(R.id.timerow4);
        timeRow[4] = (TableRow) findViewById(R.id.timerow5);
        timeRow[5] = (TableRow) findViewById(R.id.timerow6);
        timeRow[6] = (TableRow) findViewById(R.id.timerow7);
        timeRow[7] = (TableRow) findViewById(R.id.timerow8);
        timeRow[8] = (TableRow) findViewById(R.id.timerow9);
        timeRow[9] = (TableRow) findViewById(R.id.timerow10);
        timeRow[10] = (TableRow) findViewById(R.id.timerow11);
        timeRow[11] = (TableRow) findViewById(R.id.timerow12);

        StudentBox = new CheckBox[8];

        StudentBox[0] = (CheckBox) findViewById(R.id.sb1);
        StudentBox[1] = (CheckBox) findViewById(R.id.sb2);
        StudentBox[2] = (CheckBox) findViewById(R.id.sb3);
        StudentBox[3] = (CheckBox) findViewById(R.id.sb4);
        StudentBox[4] = (CheckBox) findViewById(R.id.sb5);
        StudentBox[5] = (CheckBox) findViewById(R.id.sb6);
        StudentBox[6] = (CheckBox) findViewById(R.id.sb7);
        StudentBox[7] = (CheckBox) findViewById(R.id.sb8);

        search_univ_view = (TextView) findViewById(R.id.univ_search_view);
        search_univ_view.setOnClickListener(this);

        //추가입력 학생
        tg_spinner = (Spinner) findViewById(R.id.tgender);
        tage1 = (EditText) findViewById(R.id.tage1);
        tage2 = (EditText) findViewById(R.id.tage2);

        multiText_st = (EditText) findViewById(R.id.multiText_st);
        multiText_tc = (EditText) findViewById(R.id.multiText_tc);
        bytelength_tc = (TextView) findViewById(R.id.byte_length_tc);
        bytelength_st = (TextView) findViewById(R.id.byte_length_st);

        multiText_st.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String result = "(" + Getbyte(charSequence.toString()) + " / 65535 byte)";
                bytelength_st.setText(result);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        multiText_tc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String result = "(" + Getbyte(charSequence.toString()) + " / 65535 byte)";
                bytelength_tc.setText(result);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Next.setOnClickListener(this);


        Data = getIntent();

        if (Data.hasExtra("job")) {
            Toast.makeText(this, "job: " + Data.getStringExtra("job"), Toast.LENGTH_SHORT).show();
            if (Data.getStringExtra("job").equals("teacher")) {
                TeacherForm.setVisibility(View.VISIBLE);
                StudentForm.setVisibility(View.GONE);
                placetable.setVisibility(View.VISIBLE);
                collegeRow.setVisibility(View.VISIBLE);
            } else if (Data.getStringExtra("job").equals("student")) {
                TeacherForm.setVisibility(View.GONE);
                StudentForm.setVisibility(View.VISIBLE);
                placetable.setVisibility(View.GONE);
                collegeRow.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(this, "직업이 없음", Toast.LENGTH_SHORT).show();
        }

        init_list();

    }

//    public void setAllchangelistner(ArrayList<CheckBox> StyleList){
//        for(int i = 0 ; i < StyleList.size() ; i++){
//            final int finalI = i;
//            StyleList.get(i).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    if(b){
//                        StyleText.setText(StyleExplanation[finalI]);
//                        if(styleRow.getVisibility() == View.GONE){
//                            styleRow.setVisibility(View.VISIBLE);
//                        }
//                    }
//                }
//            });
//        }
//    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.univ_search_view:
                Intent GoSearchUniv = new Intent(getApplicationContext(), activity_searchUniversity.class);
                startActivityForResult(GoSearchUniv,REQUEST_SEARCH_UNIV);
                break;

            case R.id.next_btn:
                //과외비 체크
                if (!Check_lessonfee(getApplicationContext())) {
                    Toast.makeText(this, "희망 과외 비용을 다시 입력해주세요", Toast.LENGTH_SHORT).show();
                    break;
                }
                //직업 체크
                if (!Data.hasExtra("job")) {
                    Toast.makeText(this, "인텐트에 job이 없습니다", Toast.LENGTH_SHORT).show();
                    break;
                }

                //과목 체크
                if (!Check_hopelesson(getApplicationContext())) {
                    break;
                }

                //jsonObject에 담아서 서버로 보내기
                JSONObject jo = new JSONObject();
                Boolean Join;

                Data = getIntent();

                //다음 페이지로 넘어가기
                Intent gomain = new Intent(getApplicationContext(), activity_main.class);

//                sbj[0][1] = getETC(subject1,etcText1);
//
//                sbj2 = getETC(subject2,etcText2);
//
//                sbj3 = getETC(subject3,etcText3);


//                LessonSubject = "@" + sbj1 + "@" + sbj2 + "@" + sbj3;
//                Log.d("ClassInfo",LessonSubject);

                LessonSubject = SET_SUBJECT();
                Fee = SET_FEE();
                dealpossible = SET_dp();
                LessonPlace = GETLessonPlace();
                String intro = "";


                try {
                    if (!Data.hasExtra("social")) {
                        jo.put("id", Data.getStringExtra("id"));
                        jo.put("pw", Data.getStringExtra("pw"));
                    } else {
                        jo.put("id", Data.getStringExtra("email"));
                        jo.put("social", Data.getStringExtra("social"));
                    }

                    //선생님일 경우
                    if (Data.getStringExtra("job").equals("teacher")) {
                        String StudentLevel, LessonCategory,collegeName, schedules = "";
                        collegeName = search_univ_view.getText().toString();
                        if(collegeName.equals("클릭해주세요")){
                            Toast.makeText(this, "출신 학교를 선택해주세요", Toast.LENGTH_SHORT).show();
                            break;
                        }
//                        collegeName = college_et.getText().toString();
//
//                        //대학교 입력 && 입력한 문자열 뒤에서 3글자가 '대학교' 라는 단어가 들어가 있지 않았다면 break;
//                        if (collegeName.length() != 0 && !collegeName.substring(collegeName.length() - 3, collegeName.length()).contains("대학교")) {
//                            Toast.makeText(this, "대학교 이름을 올바르게 입력해주세요!", Toast.LENGTH_SHORT).show();
//                            break;
//                        }
                        //과외 가능 지역 체크
                        if (!Check_LessonPlace()) {
                            Toast.makeText(this, "과외 가능 지역을 다시 확인해주세요", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        StudentLevel = GET_sb(StudentBox);
                        LessonCategory = lesson_category_spinner.getSelectedItem().toString();
                        schedules = Get_schedule();

                        jo.put("collegename",collegeName);
                        jo.put("studentlevel", StudentLevel);
                        jo.put("lessoncategory", LessonCategory);
                        jo.put("schedule", schedules);

                        intro = multiText_tc.getText().toString();
                    }

                    //학생일 경우
                    else if (Data.getStringExtra("job").equals("student")) {
                        if (!check_teacherage()) {
                            break;
                        }
                        String teacher_gender, teacher_age = "";
                        teacher_gender = tg_spinner.getSelectedItem().toString();
                        teacher_age = tage1.getText().toString() + "~" + tage2.getText().toString();
                        jo.put("teacher_gender", teacher_gender);
                        jo.put("teacher_age", teacher_age);

                        intro = multiText_st.getText().toString();
                    }

                    String image = Data.getStringExtra("image");
                    String imagename;
                    if (!image.equals("null")) {
                        Bitmap b = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(image));
                        Long tslong = System.currentTimeMillis() / 1000;
                        imagename = Data.getStringExtra("id") + "_" + tslong.toString();

                        new Upload(b, imagename).execute();

                    } else {
                        imagename = "null";
                    }

                    jo.put("profile_image", imagename);

                    jo.put("name", Data.getStringExtra("User_name"));
                    jo.put("gender", Data.getStringExtra("gender"));
                    jo.put("age", Data.getStringExtra("age"));
                    jo.put("address", Data.getStringExtra("address"));
                    jo.put("phone", Data.getStringExtra("phone"));
                    jo.put("job", Data.getStringExtra("job"));
                    jo.put("LessonPlace", LessonPlace);
                    jo.put("LessonSubject", LessonSubject);
                    jo.put("LessonFee", Fee);
                    jo.put("dealpossible", dealpossible);
                    jo.put("intro", intro);

                    String str_json = jo.toString();

                    Log.d("classinfo", "before json: " + str_json);

                    PHPRequest request = new PHPRequest(JoinURL);

                    String result = request.POSTJSON(str_json);

                    Log.d("classinfo", "after json: " + result);

//                    Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

                    JSONObject getJSON = new JSONObject(result);

                    Join = getJSON.getBoolean("join");

                    String msg = getJSON.getString("msg");
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

                    if (Join) {
                        if (!Data.hasExtra("social")) {
                            gomain.putExtra("id", Data.getStringExtra("id"));
                            gomain.putExtra("pw", Data.getStringExtra("pw"));
                        } else {
                            gomain.putExtra("id", Data.getStringExtra("email"));
                            gomain.putExtra("social", Data.getStringExtra("social"));
                        }

                        gomain.putExtra("job", Data.getStringExtra("job"));
                        gomain.putExtra("User_name", Data.getStringExtra("User_name"));

                        //회원가입 페이지 스택에서 없애기
                        gomain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(gomain);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                Toast.makeText(this, "모두 오케이!!", Toast.LENGTH_SHORT).show();

                //회원가입 페이지 스택에서 없애기

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_SEARCH_UNIV:
                if(resultCode == RESULT_OK){
                    String result = data.getStringExtra("university");
                    search_univ_view.setText(result);
                }
                break;
        }
    }

    private boolean check_teacherage() {
        if (Integer.valueOf(tage1.getText().toString()) < Integer.valueOf(tage2.getText().toString())) {
            return true;
        }
        Toast.makeText(this, "원하시는 선생님의 나이를 바르게 입력해주세요", Toast.LENGTH_SHORT).show();
        return false;
    }

    public Boolean Check_LessonPlace() {
        for (int i = 0; i < PlaceSpinner.length; i++) {
            if (PlaceSpinner[0][0].getSelectedItemPosition() == 0) {
                return false;
            }

            if (PlaceSpinner[i][0].getSelectedItemPosition() != 0) {
                if (PlaceSpinner[i][2].getSelectedItemPosition() == 0 || PlaceSpinner[i][2].getAdapter() == null) {
                    return false;
                }
            }
        }

        return true;
    }

    public String GETLessonPlace() {
        String place = "";
        for (int i = 0; i < PlaceSpinner.length; i++) {
            if (PlaceSpinner[i][0].getSelectedItemPosition() == 0 || PlaceSpinner[i][0].getAdapter() == null) {
                break;
            }
            place += "@";
            for (int j = 0; j < PlaceSpinner[i].length; j++) {
                if (j != 0) {
                    place += " ";
                }
                place += PlaceSpinner[i][j].getSelectedItem().toString();
            }
        }
        return place;
    }

    public String Get_schedule() {
        String result = "";
        for (int i = 0; i < days.length; i++) {
            if (days[i].getSelectedItemPosition() == 0) {
                break;
            } else {
                String deal = (possible[i].isChecked()) ? "(협의 가능)" : "(협의 불가)";
                result += "@" + days[i].getSelectedItem().toString() + ") " + times[i].getText().toString() + " " + deal;
            }
        }

        return result;
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

    public String SET_SUBJECT() {
        String result = "";
        for (int i = 0; i < subjects.length; i++) {
            String s1 = subjects[i][0].getSelectedItem().toString(), s2 = subjects[i][1].getSelectedItem().toString(), s3 = "";
            EditText editText = null;
            result += "@" + s1 + " - " + s2;
            if (subjects[i][0].getSelectedItem().toString().equals("기타") && subjects[i][1].getSelectedItem().toString().equals("기타")) {
                switch (i) {
                    case 0:
                        editText = etcText1;
                        break;
                    case 1:
                        editText = etcText2;
                        break;
                    case 2:
                        editText = etcText3;
                        break;
                }
                s3 = editText.getText().toString();
                result += "(" + s3 + ")";
            } else if (subjects[i][1].getSelectedItem().toString().equals("기타")) {
                switch (i) {
                    case 0:
                        editText = etcText1;
                        break;
                    case 1:
                        editText = etcText2;
                        break;
                    case 2:
                        editText = etcText3;
                        break;
                }
                s3 = editText.getText().toString();
                result += "(" + s3 + ")";
            }
        }

        return result;
    }

    public String SET_FEE() {
        String fee = "@" + LessonCount.getText().toString() +
                "@" + LessonTime.getText().toString() +
                "@" + LessonFee.getText().toString();

        return fee;
    }

    public String SET_dp() {
        String result = "";
        if (LessonConsult.isChecked()) {
            result = "y";
        } else {
            result = "n";
        }
        return result;
    }

    public Boolean Check_hopelesson(Context context) {

        int[][] index = new int[3][2];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                index[i][j] = subjects[i][j].getSelectedItemPosition();
            }
        }

        //모든 과목을 선택했는가??
        if (subjects[0][1].getSelectedItemPosition() == 0 || subjects[1][1].getSelectedItemPosition() == 0 || subjects[0][1].getSelectedItemPosition() == 0) {
            Toast.makeText(context, "희망 과목을 모두 선택해 주세요", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (index[0][0] == index[1][0]) {
            if (index[0][1] == index[1][1]) {
                Toast.makeText(context, "과목 중복 선택은 불가 합니다", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if (index[1][0] == index[2][0]) {
            if (index[1][1] == index[2][1]) {
                Toast.makeText(context, "과목 중복 선택은 불가 합니다", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if (index[2][0] == index[0][0]) {
            if (index[2][1] == index[0][1]) {
                Toast.makeText(context, "과목 중복 선택은 불가 합니다", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if (index[0][0] == index[1][0] && index[1][0] == index[2][0]) {
            if (index[0][1] == index[1][1] | index[1][1] == index[2][1] | index[2][1] == index[1][1]) {
                Toast.makeText(context, "과목 중복 선택은 불가 합니다", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public Boolean Check_lessonfee(Context context) {
        //협의가능이 체크 되어있다면
        if (!LessonConsult.isChecked()) {
            //입력을 안했을 경우
            if (LessonCount.getText().length() == 0 || LessonTime.getText().length() == 0 || LessonFee.getText().length() == 0) {
                Toast.makeText(context, "희망 과외비 항목을 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                return false;
            }
            //주 7회 이상일 경우
            if (Integer.valueOf(LessonCount.getText().toString()) > 7) {
                Toast.makeText(context, "주 7회 이상 수업은 불가능 합니다", Toast.LENGTH_SHORT).show();
                return false;
            }
            //수업시간이 24시간 이상일 경우
            if (Integer.valueOf(LessonTime.getText().toString()) > 24) {
                Toast.makeText(context, "24시간 이상 수업은 불가능 합니다", Toast.LENGTH_SHORT).show();
                return false;
            }
            //과외비를 0원 이하 입력했을 경우
            if (Integer.valueOf(LessonFee.getText().toString()) < 0) {
                Toast.makeText(context, "과외비는 0원 이상 입력하셔야 합니다", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        ArrayAdapter adapter;

        int id = adapterView.getId();
        Spinner spinner;
        final TableRow ETCROW;
        switch (id) {
            case R.id.hopelesson1:
                spinner = subjects[0][1];
                ETCROW = etcRow1;
                break;
            case R.id.hopelesson2:
                spinner = subjects[1][1];
                ETCROW = etcRow2;
                break;
            case R.id.hopelesson3:
                spinner = subjects[2][1];
                ETCROW = etcRow3;
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

                    }
                });

//                ETCROW.setVisibility(View.VISIBLE);
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void setVisible(final Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int NextIndex = -1;
                int id = spinner.getId();

                if (i != 0) {
                    switch (id) {
                        case R.id.TimeSpinner1:
                            NextIndex = 1;
                            break;
                        case R.id.TimeSpinner2:
                            NextIndex = 2;
                            break;
                        case R.id.TimeSpinner3:
                            NextIndex = 3;
                            break;
                        case R.id.TimeSpinner4:
                            NextIndex = 4;
                            break;
                        case R.id.TimeSpinner5:
                            NextIndex = 5;
                            break;
                        case R.id.TimeSpinner6:
                            NextIndex = 6;
                            break;
                        case R.id.TimeSpinner7:
                            NextIndex = 7;
                            break;
                        case R.id.TimeSpinner8:
                            NextIndex = 8;
                            break;
                        case R.id.TimeSpinner9:
                            NextIndex = 9;
                            break;
                        case R.id.TimeSpinner10:
                            NextIndex = 10;
                            break;
                        case R.id.TimeSpinner11:
                            NextIndex = 11;
                            break;
                        case R.id.TimeSpinner12:
                            NextIndex = 11;
                            break;
                    }
                    VisibleNextRow(NextIndex);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void VisibleNextRow(int CurrentIndex) {
        timeRow[CurrentIndex].setVisibility(View.VISIBLE);
    }

    public void makeSpinner(final ArrayList<Area> arrayList, final Spinner spinner, final ArrayAdapter adapter) {
        Log.d("makeSpinner", "call");
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
//                Toast.makeText(getBaseContext(),arrayList.get(i)+"을 선택하셨습니다",Toast.LENGTH_SHORT).show();

                if (adapterView.getChildAt(0) != null) {
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                }

                //"선택"을 눌렀다면

                String cd = String.valueOf(arrayList.get(i).getCd());
                int spinner_id = spinner.getId();

//                if(i != 0) {
//                String cd = areaMap.get(arrayList.get(i)).toString();
                Log.d("cd_result", cd);
                final JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("cd", cd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                int next_index = -1;
                int column = -1;
                switch (spinner_id) {
                    case R.id.LessonPlace1_1:
                        column = 0;
                        next_index = 1;
                        Log.d("makeSpinner", "list_index: " + next_index);

                        //선택 클릭 안햇을시
                        if (i != 0) {
//                            AreaRow[1].setVisibility(View.VISIBLE);
                            //다음 리스트 만들기
                            mAreaHandler[column][next_index] = new AreaHandler(Placelist[column][next_index], PlaceSpinner[column][next_index]);
                            final int finalNext_index1 = next_index;
                            final int finalColumn = column;
                            new Thread() {
                                @Override
                                public void run() {
                                    Log.d("onitemselected", "thread start");
                                    PHPRequest request = null;
                                    try {
                                        request = new PHPRequest(url);
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }

                                    Log.d("profile_POSTJSON", "before: " + jsonObject.toString());
                                    String result = request.POSTJSON(jsonObject.toString());
                                    Log.d("profile_POSTJSON", "after: " + result);

                                    Bundle bundle = new Bundle();
                                    bundle.putString("result", result);

                                    Message msg = mAreaHandler[finalColumn][finalNext_index1].obtainMessage();
                                    msg.setData(bundle);
                                    mAreaHandler[finalColumn][finalNext_index1].sendMessage(msg);
                                }
                            }.start();
                        }

                        Log.d("makespinner", "@@@@@refresh@@@@@");
                        break;

                    case R.id.LessonPlace1_2:
                        column = 0;
                        next_index = 2;
                        Log.d("makeSpinner", "list_index: " + next_index);

                        //선택 클릭 안햇을시
                        if (i != 0) {
//                            AreaRow[1].setVisibility(View.VISIBLE);
                            mAreaHandler[column][next_index] = new AreaHandler(Placelist[column][next_index], PlaceSpinner[column][next_index]);
                            final int finalNext_index = next_index;
                            final int finalColumn = column;
                            new Thread() {
                                @Override
                                public void run() {
                                    Log.d("onitemselected", "thread start");
                                    PHPRequest request = null;
                                    try {
                                        request = new PHPRequest(url);
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }

                                    Log.d("profile_POSTJSON", "before: " + jsonObject.toString());
                                    String result = request.POSTJSON(jsonObject.toString());
                                    Log.d("profile_POSTJSON", "after: " + result);

                                    Bundle bundle = new Bundle();
                                    bundle.putString("result", result);

                                    Message msg = mAreaHandler[finalColumn][finalNext_index].obtainMessage();
                                    msg.setData(bundle);
                                    mAreaHandler[finalColumn][finalNext_index].sendMessage(msg);
                                }
                            }.start();
                        }
                        break;
                    case R.id.LessonPlace1_3:
                        next_index = 3;
                        Log.d("makeSpinner", "list_index: " + next_index);
//                        mAreaHandler = new AreaHandler(juso_list.get(list_index), spinner);
//                        new Thread() {
//                            @Override
//                            public void run() {
//                                Log.d("onitemselected","thread start case R.id.area2");
//                                PHPRequest request = null;
//                                try {
//                                    request = new PHPRequest(url);
//                                } catch (MalformedURLException e) {
//                                    e.printStackTrace();
//                                }
//
//                                Log.d("profile_POSTJSON", "before: " + jsonObject.toString());
//                                String result = request.POSTJSON(jsonObject.toString());
//                                Log.d("profile_POSTJSON", "after: " + result);
//                                Bundle bundle = new Bundle();
//                                bundle.putString("result", result);
//
//                                Message msg = mAreaHandler.obtainMessage();
//                                msg.setData(bundle);
//                                mAreaHandler.sendMessage(msg);
////                        Message msg = handler2.obtainMessage();
////                        msg.setData(bundle);
////                        handler2.sendMessage(msg);
//                            }
//                        }.start();
                        //선택 클릭 안햇을시
                        if (i != 0) {
                            lpROW[1].setVisibility(View.VISIBLE);
                        }
                        //선택 클릭햇을경우
                        break;

                    case R.id.LessonPlace2_1:
                        column = 1;
                        next_index = 1;
                        Log.d("makeSpinner", "list_index: " + next_index);

                        //선택 클릭 안햇을시
                        if (i != 0) {
//                            AreaRow[1].setVisibility(View.VISIBLE);
                            //다음 리스트 만들기
                            mAreaHandler[column][next_index] = new AreaHandler(Placelist[column][next_index], PlaceSpinner[column][next_index]);
                            final int finalNext_index1 = next_index;
                            final int finalColumn = column;
                            new Thread() {
                                @Override
                                public void run() {
                                    Log.d("onitemselected", "thread start");
                                    PHPRequest request = null;
                                    try {
                                        request = new PHPRequest(url);
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }

                                    Log.d("profile_POSTJSON", "before: " + jsonObject.toString());
                                    String result = request.POSTJSON(jsonObject.toString());
                                    Log.d("profile_POSTJSON", "after: " + result);

                                    Bundle bundle = new Bundle();
                                    bundle.putString("result", result);

                                    Message msg = mAreaHandler[finalColumn][finalNext_index1].obtainMessage();
                                    msg.setData(bundle);
                                    mAreaHandler[finalColumn][finalNext_index1].sendMessage(msg);
                                }
                            }.start();
                        }

                        break;

                    case R.id.LessonPlace2_2:
                        column = 1;
                        next_index = 2;
                        Log.d("makeSpinner", "list_index: " + next_index);

                        //선택 클릭 안햇을시
                        if (i != 0) {
//                            AreaRow[1].setVisibility(View.VISIBLE);
                            mAreaHandler[column][next_index] = new AreaHandler(Placelist[column][next_index], PlaceSpinner[column][next_index]);
                            final int finalNext_index = next_index;
                            final int finalColumn = column;
                            new Thread() {
                                @Override
                                public void run() {
                                    Log.d("onitemselected", "thread start");
                                    PHPRequest request = null;
                                    try {
                                        request = new PHPRequest(url);
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }

                                    Log.d("profile_POSTJSON", "before: " + jsonObject.toString());
                                    String result = request.POSTJSON(jsonObject.toString());
                                    Log.d("profile_POSTJSON", "after: " + result);

                                    Bundle bundle = new Bundle();
                                    bundle.putString("result", result);

                                    Message msg = mAreaHandler[finalColumn][finalNext_index].obtainMessage();
                                    msg.setData(bundle);
                                    mAreaHandler[finalColumn][finalNext_index].sendMessage(msg);
                                }
                            }.start();
                        }
                        break;
                    case R.id.LessonPlace2_3:
                        next_index = 3;
                        Log.d("makeSpinner", "list_index: " + next_index);
//                        mAreaHandler = new AreaHandler(juso_list.get(list_index), spinner);
//                        new Thread() {
//                            @Override
//                            public void run() {
//                                Log.d("onitemselected","thread start case R.id.area2");
//                                PHPRequest request = null;
//                                try {
//                                    request = new PHPRequest(url);
//                                } catch (MalformedURLException e) {
//                                    e.printStackTrace();
//                                }
//
//                                Log.d("profile_POSTJSON", "before: " + jsonObject.toString());
//                                String result = request.POSTJSON(jsonObject.toString());
//                                Log.d("profile_POSTJSON", "after: " + result);
//                                Bundle bundle = new Bundle();
//                                bundle.putString("result", result);
//
//                                Message msg = mAreaHandler.obtainMessage();
//                                msg.setData(bundle);
//                                mAreaHandler.sendMessage(msg);
////                        Message msg = handler2.obtainMessage();
////                        msg.setData(bundle);
////                        handler2.sendMessage(msg);
//                            }
//                        }.start();
                        //선택 클릭 안햇을시
                        if (i != 0) {
                            lpROW[2].setVisibility(View.VISIBLE);
                        }
                        //선택 클릭햇을경우
                        break;

                    case R.id.LessonPlace3_1:
                        column = 2;
                        next_index = 1;
                        Log.d("makeSpinner", "list_index: " + next_index);

                        //선택 클릭 안햇을시
                        if (i != 0) {
//                            AreaRow[1].setVisibility(View.VISIBLE);
                            //다음 리스트 만들기
                            mAreaHandler[column][next_index] = new AreaHandler(Placelist[column][next_index], PlaceSpinner[column][next_index]);
                            final int finalNext_index1 = next_index;
                            final int finalColumn = column;
                            new Thread() {
                                @Override
                                public void run() {
                                    Log.d("onitemselected", "thread start");
                                    PHPRequest request = null;
                                    try {
                                        request = new PHPRequest(url);
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }

                                    Log.d("profile_POSTJSON", "before: " + jsonObject.toString());
                                    String result = request.POSTJSON(jsonObject.toString());
                                    Log.d("profile_POSTJSON", "after: " + result);

                                    Bundle bundle = new Bundle();
                                    bundle.putString("result", result);

                                    Message msg = mAreaHandler[finalColumn][finalNext_index1].obtainMessage();
                                    msg.setData(bundle);
                                    mAreaHandler[finalColumn][finalNext_index1].sendMessage(msg);
                                }
                            }.start();
                        }
                        break;

                    case R.id.LessonPlace3_2:
                        column = 2;
                        next_index = 2;
                        Log.d("makeSpinner", "list_index: " + next_index);

                        //선택 클릭 안햇을시
                        if (i != 0) {
//                            AreaRow[1].setVisibility(View.VISIBLE);
                            mAreaHandler[column][next_index] = new AreaHandler(Placelist[column][next_index], PlaceSpinner[column][next_index]);
                            final int finalNext_index = next_index;
                            final int finalColumn = column;
                            new Thread() {
                                @Override
                                public void run() {
                                    Log.d("onitemselected", "thread start");
                                    PHPRequest request = null;
                                    try {
                                        request = new PHPRequest(url);
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }

                                    Log.d("profile_POSTJSON", "before: " + jsonObject.toString());
                                    String result = request.POSTJSON(jsonObject.toString());
                                    Log.d("profile_POSTJSON", "after: " + result);

                                    Bundle bundle = new Bundle();
                                    bundle.putString("result", result);

                                    Message msg = mAreaHandler[finalColumn][finalNext_index].obtainMessage();
                                    msg.setData(bundle);
                                    mAreaHandler[finalColumn][finalNext_index].sendMessage(msg);
                                }
                            }.start();
                        }
                        break;
                    case R.id.LessonPlace3_3:
                        next_index = 3;
                        Log.d("makeSpinner", "list_index: " + next_index);
//                        mAreaHandler = new AreaHandler(juso_list.get(list_index), spinner);
//                        new Thread() {
//                            @Override
//                            public void run() {
//                                Log.d("onitemselected","thread start case R.id.area2");
//                                PHPRequest request = null;
//                                try {
//                                    request = new PHPRequest(url);
//                                } catch (MalformedURLException e) {
//                                    e.printStackTrace();
//                                }
//
//                                Log.d("profile_POSTJSON", "before: " + jsonObject.toString());
//                                String result = request.POSTJSON(jsonObject.toString());
//                                Log.d("profile_POSTJSON", "after: " + result);
//                                Bundle bundle = new Bundle();
//                                bundle.putString("result", result);
//
//                                Message msg = mAreaHandler.obtainMessage();
//                                msg.setData(bundle);
//                                mAreaHandler.sendMessage(msg);
////                        Message msg = handler2.obtainMessage();
////                        msg.setData(bundle);
////                        handler2.sendMessage(msg);
//                            }
//                        }.start();
                        //선택 클릭 안햇을시
                        if (i != 0) {
                            lpROW[3].setVisibility(View.VISIBLE);
                        }
                        //선택 클릭햇을경우
                        break;
                    case R.id.LessonPlace4_1:
                        column = 3;
                        next_index = 1;
                        Log.d("makeSpinner", "list_index: " + next_index);

                        //선택 클릭 안햇을시
                        if (i != 0) {
//                            AreaRow[1].setVisibility(View.VISIBLE);
                            //다음 리스트 만들기
                            mAreaHandler[column][next_index] = new AreaHandler(Placelist[column][next_index], PlaceSpinner[column][next_index]);
                            final int finalNext_index1 = next_index;
                            final int finalColumn = column;
                            new Thread() {
                                @Override
                                public void run() {
                                    Log.d("onitemselected", "thread start");
                                    PHPRequest request = null;
                                    try {
                                        request = new PHPRequest(url);
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }

                                    Log.d("profile_POSTJSON", "before: " + jsonObject.toString());
                                    String result = request.POSTJSON(jsonObject.toString());
                                    Log.d("profile_POSTJSON", "after: " + result);

                                    Bundle bundle = new Bundle();
                                    bundle.putString("result", result);

                                    Message msg = mAreaHandler[finalColumn][finalNext_index1].obtainMessage();
                                    msg.setData(bundle);
                                    mAreaHandler[finalColumn][finalNext_index1].sendMessage(msg);
                                }
                            }.start();
                        }
                        break;

                    case R.id.LessonPlace4_2:
                        column = 3;
                        next_index = 2;
                        Log.d("makeSpinner", "list_index: " + next_index);

                        //선택 클릭 안햇을시
                        if (i != 0) {
//                            AreaRow[1].setVisibility(View.VISIBLE);
                            mAreaHandler[column][next_index] = new AreaHandler(Placelist[column][next_index], PlaceSpinner[column][next_index]);
                            final int finalNext_index = next_index;
                            final int finalColumn = column;
                            new Thread() {
                                @Override
                                public void run() {
                                    Log.d("onitemselected", "thread start");
                                    PHPRequest request = null;
                                    try {
                                        request = new PHPRequest(url);
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }

                                    Log.d("profile_POSTJSON", "before: " + jsonObject.toString());
                                    String result = request.POSTJSON(jsonObject.toString());
                                    Log.d("profile_POSTJSON", "after: " + result);

                                    Bundle bundle = new Bundle();
                                    bundle.putString("result", result);

                                    Message msg = mAreaHandler[finalColumn][finalNext_index].obtainMessage();
                                    msg.setData(bundle);
                                    mAreaHandler[finalColumn][finalNext_index].sendMessage(msg);
                                }
                            }.start();
                        }
                        break;
                    case R.id.LessonPlace4_3:
                        next_index = 3;
                        Log.d("makeSpinner", "list_index: " + next_index);
//                        mAreaHandler = new AreaHandler(juso_list.get(list_index), spinner);
//                        new Thread() {
//                            @Override
//                            public void run() {
//                                Log.d("onitemselected","thread start case R.id.area2");
//                                PHPRequest request = null;
//                                try {
//                                    request = new PHPRequest(url);
//                                } catch (MalformedURLException e) {
//                                    e.printStackTrace();
//                                }
//
//                                Log.d("profile_POSTJSON", "before: " + jsonObject.toString());
//                                String result = request.POSTJSON(jsonObject.toString());
//                                Log.d("profile_POSTJSON", "after: " + result);
//                                Bundle bundle = new Bundle();
//                                bundle.putString("result", result);
//
//                                Message msg = mAreaHandler.obtainMessage();
//                                msg.setData(bundle);
//                                mAreaHandler.sendMessage(msg);
////                        Message msg = handler2.obtainMessage();
////                        msg.setData(bundle);
////                        handler2.sendMessage(msg);
//                            }
//                        }.start();
                        //선택 클릭 안햇을시
                        if (i != 0) {
                            lpROW[4].setVisibility(View.VISIBLE);
                        }
                        //선택 클릭햇을경우
                        break;

                    case R.id.LessonPlace5_1:
                        column = 4;
                        next_index = 1;
                        Log.d("makeSpinner", "list_index: " + next_index);

                        //선택 클릭 안햇을시
                        if (i != 0) {
//                            AreaRow[1].setVisibility(View.VISIBLE);
                            //다음 리스트 만들기
                            mAreaHandler[column][next_index] = new AreaHandler(Placelist[column][next_index], PlaceSpinner[column][next_index]);
                            final int finalNext_index1 = next_index;
                            final int finalColumn = column;
                            new Thread() {
                                @Override
                                public void run() {
                                    Log.d("onitemselected", "thread start");
                                    PHPRequest request = null;
                                    try {
                                        request = new PHPRequest(url);
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }

                                    Log.d("profile_POSTJSON", "before: " + jsonObject.toString());
                                    String result = request.POSTJSON(jsonObject.toString());
                                    Log.d("profile_POSTJSON", "after: " + result);

                                    Bundle bundle = new Bundle();
                                    bundle.putString("result", result);

                                    Message msg = mAreaHandler[finalColumn][finalNext_index1].obtainMessage();
                                    msg.setData(bundle);
                                    mAreaHandler[finalColumn][finalNext_index1].sendMessage(msg);
                                }
                            }.start();
                        }
                        break;

                    case R.id.LessonPlace5_2:
                        column = 4;
                        next_index = 2;
                        Log.d("makeSpinner", "list_index: " + next_index);

                        //선택 클릭 안햇을시
                        if (i != 0) {
//                            AreaRow[1].setVisibility(View.VISIBLE);
                            mAreaHandler[column][next_index] = new AreaHandler(Placelist[column][next_index], PlaceSpinner[column][next_index]);
                            final int finalNext_index = next_index;
                            final int finalColumn = column;
                            new Thread() {
                                @Override
                                public void run() {
                                    Log.d("onitemselected", "thread start");
                                    PHPRequest request = null;
                                    try {
                                        request = new PHPRequest(url);
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }

                                    Log.d("profile_POSTJSON", "before: " + jsonObject.toString());
                                    String result = request.POSTJSON(jsonObject.toString());
                                    Log.d("profile_POSTJSON", "after: " + result);

                                    Bundle bundle = new Bundle();
                                    bundle.putString("result", result);

                                    Message msg = mAreaHandler[finalColumn][finalNext_index].obtainMessage();
                                    msg.setData(bundle);
                                    mAreaHandler[finalColumn][finalNext_index].sendMessage(msg);
                                }
                            }.start();
                        }
                        break;
                    case R.id.LessonPlace5_3:
                        next_index = 3;
                        Log.d("makeSpinner", "list_index: " + next_index);
                        //선택 클릭햇을경우
                        break;
                }
            }


//                mAreaHandler = new AreaHandler(juso_list.get(list_index), spinner);

//            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        Dialog_TimeInsert td = (Dialog_TimeInsert) dialogInterface;
    }

    public void setCustomDialog(final TextView tv) {
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog_TimeInsert td = new Dialog_TimeInsert(activity_JOIN_ClassInfo.this, "시작 시간과 종료시간을 설정해주세요", "시작시간", "종료시간", tv);
                td.setOnDismissListener(activity_JOIN_ClassInfo.this);
                td.show();
            }
        });
    }

    public class AreaHandler extends Handler {
        ArrayList<Area> arealist;
        Spinner spinner;

        public AreaHandler(ArrayList<Area> Arealist, Spinner spinner) {
            this.arealist = Arealist;
            this.spinner = spinner;
        }

        @Override
        public void handleMessage(Message msg) {
            Bundle bun = msg.getData();
            String result = bun.getString("result");
            ArrayList<String> strarealist = new ArrayList<>();
            Log.d("handleMessage", result);
            try {
                JSONObject object = new JSONObject(result);
                JSONArray jsonArray = object.getJSONArray("result");
//                Area initarea = new Area();
//                initarea.setAreaname("선택");
//                initarea.setCd(-1);
//                arealist.add(initarea);
                arealist.clear();
                strarealist.clear();

                Area init = new Area();
                init.setAreaname("지역 선택");
                init.setCd(-1);
                arealist.add(init);
                strarealist.add("지역 선택");
                for (int i = 0; i < jsonArray.length(); i++) {
                    String addr_name = jsonArray.getJSONObject(i).getString("addr_name");
                    Integer cd = Integer.valueOf(jsonArray.getJSONObject(i).getString("cd"));
//                    if(cd == -1){
//                        break;
//                    }
                    strarealist.add(addr_name);

                    Area area = new Area();
                    area.setAreaname(addr_name);
                    area.setCd(cd);

                    arealist.add(area);
                }
                Log.d("handler", "arealist start");
                for (int j = 0; j < arealist.size(); j++) {
//                    Area sample = arealist.get(j);
//                    Log.d("handleMessage","index " + j + ": " + sample.getAreaname() + ", " + sample.getCd());
                    Log.d("handleMessage", "strarealist " + j + ": " + strarealist.get(j));
                }
                //value 값으로 정렬해서 arraylist 만들기

                int id_spinner_1 = spinner.getId();
                int spinnerStyle = android.R.layout.simple_spinner_dropdown_item;

                switch (id_spinner_1) {
                    case R.id.LessonPlace1_1:
                        LPadapter[0][0] = new ArrayAdapter(getApplicationContext(), spinnerStyle, strarealist);
                        makeSpinner(arealist, spinner, LPadapter[0][0]);
                        break;
                    case R.id.LessonPlace1_2:
                        LPadapter[0][1] = new ArrayAdapter(getApplicationContext(), spinnerStyle, strarealist);
                        makeSpinner(arealist, spinner, LPadapter[0][1]);
                        break;
                    case R.id.LessonPlace1_3:
                        strarealist.add(1, "전체");
                        LPadapter[0][2] = new ArrayAdapter(getApplicationContext(), spinnerStyle, strarealist);
                        makeSpinner(arealist, spinner, LPadapter[0][2]);
                        break;
                    case R.id.LessonPlace2_1:
                        LPadapter[1][0] = new ArrayAdapter(getApplicationContext(), spinnerStyle, strarealist);
                        makeSpinner(arealist, spinner, LPadapter[1][0]);
                        break;
                    case R.id.LessonPlace2_2:
                        LPadapter[1][1] = new ArrayAdapter(getApplicationContext(), spinnerStyle, strarealist);
                        makeSpinner(arealist, spinner, LPadapter[1][1]);
                        break;
                    case R.id.LessonPlace2_3:
                        strarealist.add(1, "전체");
                        LPadapter[1][2] = new ArrayAdapter(getApplicationContext(), spinnerStyle, strarealist);
                        makeSpinner(arealist, spinner, LPadapter[1][2]);
                        break;
                    case R.id.LessonPlace3_1:
                        LPadapter[2][0] = new ArrayAdapter(getApplicationContext(), spinnerStyle, strarealist);
                        makeSpinner(arealist, spinner, LPadapter[2][0]);
                        break;
                    case R.id.LessonPlace3_2:
                        LPadapter[2][1] = new ArrayAdapter(getApplicationContext(), spinnerStyle, strarealist);
                        makeSpinner(arealist, spinner, LPadapter[2][1]);
                        break;
                    case R.id.LessonPlace3_3:
                        strarealist.add(1, "전체");
                        LPadapter[2][2] = new ArrayAdapter(getApplicationContext(), spinnerStyle, strarealist);
                        makeSpinner(arealist, spinner, LPadapter[2][2]);
                        break;
                    case R.id.LessonPlace4_1:
                        LPadapter[3][0] = new ArrayAdapter(getApplicationContext(), spinnerStyle, strarealist);
                        makeSpinner(arealist, spinner, LPadapter[3][0]);
                        break;
                    case R.id.LessonPlace4_2:
                        LPadapter[3][1] = new ArrayAdapter(getApplicationContext(), spinnerStyle, strarealist);
                        makeSpinner(arealist, spinner, LPadapter[3][1]);
                        break;
                    case R.id.LessonPlace4_3:
                        strarealist.add(1, "전체");
                        LPadapter[3][2] = new ArrayAdapter(getApplicationContext(), spinnerStyle, strarealist);
                        makeSpinner(arealist, spinner, LPadapter[3][2]);
                        break;
                    case R.id.LessonPlace5_1:
                        LPadapter[4][0] = new ArrayAdapter(getApplicationContext(), spinnerStyle, strarealist);
                        makeSpinner(arealist, spinner, LPadapter[4][0]);
                        break;
                    case R.id.LessonPlace5_2:
                        LPadapter[4][1] = new ArrayAdapter(getApplicationContext(), spinnerStyle, strarealist);
                        makeSpinner(arealist, spinner, LPadapter[4][1]);
                        break;
                    case R.id.LessonPlace5_3:
                        strarealist.add(1, "전체");
                        LPadapter[4][2] = new ArrayAdapter(getApplicationContext(), spinnerStyle, strarealist);
                        makeSpinner(arealist, spinner, LPadapter[4][2]);
                        break;
                }

//                makeSpinner(arealist,spinner,adapter);
//                int sp_id = spinner.getId();
//                Spinner nextsp;
//                switch (sp_id){
//                    case R.id.area1:
//                        nextsp = AreaSpinners[1];
//                        makeSpinner(arealist, nextsp, adapter);
//                        break;
//                    case R.id.area2:
//                        nextsp = AreaSpinners[2];
//                        makeSpinner(arealist, nextsp, adapter);
//                        break;
//                    case R.id.area3:
//                        makeSpinner(arealist,AreaSpinners[2],adapter);
//                        Log.d("handleMessage","끝");
//                        break;
//                    default:
//                        nextsp = null;
//                }

//                makeSpinner(arealist, nextsp, adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void init_list() {

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                Placelist[i][j] = new ArrayList<>();
            }
        }

        mAreaHandler = new AreaHandler[5][3];


        //list 받아오기
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cd", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAreaHandler[0][0] = new AreaHandler(Placelist[0][0], PlaceSpinner[0][0]);
        mAreaHandler[1][0] = new AreaHandler(Placelist[1][0], PlaceSpinner[1][0]);
        mAreaHandler[2][0] = new AreaHandler(Placelist[2][0], PlaceSpinner[2][0]);
        mAreaHandler[3][0] = new AreaHandler(Placelist[3][0], PlaceSpinner[3][0]);
        mAreaHandler[4][0] = new AreaHandler(Placelist[4][0], PlaceSpinner[4][0]);

        new Thread() {
            @Override
            public void run() {
                Log.d("init_list", "thread start");
                PHPRequest request = null;
                try {
                    request = new PHPRequest(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                Log.d("POSTJSON", "before: " + jsonObject.toString());
                String result = request.POSTJSON(jsonObject.toString());
                Log.d("POSTJSON", "after: " + result);

                Bundle bundle = new Bundle();
                bundle.putString("result", result);

//                Message msg = mAreaHandler[0][0].obtainMessage();
//                msg.setData(bundle);
//                mAreaHandler[0][0].sendMessage(msg);

                for (int i = 0; i < 5; i++) {
                    Message msg = mAreaHandler[i][0].obtainMessage();
                    msg.setData(bundle);
                    mAreaHandler[i][0].sendMessage(msg);
                }

//                Message msg = handler.obtainMessage();
//                msg.setData(bundle);
//                handler.sendMessage(msg);
            }
        }.start();

//        ArrayList<String> list2 = new ArrayList<>();
//        ArrayList<String> list3 = new ArrayList<>();
//
//        list2.add("선택");
//        list3.add("선택");
//
//        ad[1] = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.area_select));
//        ad[2] = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.area_select));
//
//        AreaSpinners[1].setAdapter(ad[1]);
//        AreaSpinners[1].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if(adapterView.getChildAt(0) != null) {
//                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        AreaSpinners[2].setAdapter(ad[2]);
//        AreaSpinners[2].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if(adapterView.getChildAt(0) != null) {
//                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        Log.d("init_list", "@@@@@refresh@@@@@");
    }

    private int Getbyte(String string) {
        return string.getBytes().length;
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

    //async task to upload image
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
                Log.d("Upload", "dataToSend: " + dataToSend);
                String response = PHPRequest.ImgPOST(ImgUpload_url, dataToSend);

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
    }

}
