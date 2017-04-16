package com.example.kimjungwon.lessonapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Created by kimjungwon on 2017-02-17.
 */

public class activity_JOIN_Profile extends AppCompatActivity implements View.OnClickListener {
    ArrayList<Area> 시도_list, 구군_list, 동읍리_list;
    //    HashMap<String, Integer> AreaMap, AreaMap2;
    ArrayList<ArrayList<Area>> juso_list;
    ArrayAdapter[] ad;

    String imageUri;

    String TAG = activity_JOIN_Profile.class.getSimpleName();
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;

    public EditText name, phone, age;
    public RadioButton male, female;
    public Spinner[] AreaSpinners;
    public AreaHandler mAreaHandler;

    public String str_gender, str_address, str_age, str_phone, timestamp;

    String url = "http://52.79.203.148/searcharea.php";

    public Button nextstep;

    public Intent Data;

    TableRow[] AreaRow;

    ImageView profileimg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.join_profile2);

        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        age = (EditText) findViewById(R.id.age);

        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);

        profileimg = (ImageView) findViewById(R.id.ProfileImg);

        profileimg.setOnClickListener(this);

        AreaSpinners = new Spinner[3];

        AreaSpinners[0] = (Spinner) findViewById(R.id.area1);
        AreaSpinners[1] = (Spinner) findViewById(R.id.area2);
        AreaSpinners[2] = (Spinner) findViewById(R.id.area3);

        AreaRow = new TableRow[3];
        AreaRow[0] = (TableRow) findViewById(R.id.AreaRow1);
        AreaRow[1] = (TableRow) findViewById(R.id.AreaRow2);
        AreaRow[2] = (TableRow) findViewById(R.id.AreaRow3);

        nextstep = (Button) findViewById(R.id.nextstep);

        nextstep.setOnClickListener(this);

        ad = new ArrayAdapter[3];
//        AreaMap = new HashMap<>();
//        AreaMap2 = new HashMap<>();

        시도_list = new ArrayList<>();
        구군_list = new ArrayList<>();
        동읍리_list = new ArrayList<>();

        juso_list = new ArrayList<>();
        juso_list.add(시도_list);
        juso_list.add(구군_list);
        juso_list.add(동읍리_list);

//        makeSpinner(시도_list, area1, adapter);
//        makeSpinner(구군_list, area2, adapter2);

        Data = getIntent();

        //소셜 회원 가입
        if (Data.hasExtra("social")) {
            Data.getStringExtra("email");
            name.setText(Data.getStringExtra("User_name"));
            str_gender = Data.getStringExtra("gender");
            Data.getStringExtra("social");

            //성별 미리 체크
            if (str_gender.equals("M")) {
                male.setChecked(true);
                Toast.makeText(this, "남자", Toast.LENGTH_SHORT).show();
            } else {
                female.setChecked(true);
                Toast.makeText(this, "여자", Toast.LENGTH_SHORT).show();
            }

//            Toast.makeText(getApplicationContext(),"birthday: " + Birthday + "\ngender: " + Gender,Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "<일반 회원가입> id: " + Data.getStringExtra("id") + "pw: " + Data.getStringExtra("pw"), Toast.LENGTH_SHORT).show();
        }

        init_list();

//        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, 시도_list);
//        adapter2 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, 구군_list);
//        adapter3 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, 동읍리_list);
//
//        area1.setAdapter(adapter);
//        area2.setAdapter(adapter2);
//        area3.setAdapter(adapter3);

    }

    public boolean isCellphone(String phonenum) {
        return phonenum.matches("(01[016789])(\\d{3,4})(\\d{4})");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Log.i("nextstep", "click");
        switch (id) {
            case R.id.ProfileImg:
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

            case R.id.nextstep:
                str_age = age.getText().toString();
                Log.d("before join", "age: " + str_age);

                ////////////////////////////////////////////////주소
                str_address = "";
                for(Spinner sp : AreaSpinners) {
                    if(sp.getSelectedItem() != null) {
                        str_address += (String) sp.getSelectedItem() + " ";
                    }
                }
                Log.d("before join", "address: " + str_address);

                str_phone = phone.getText().toString();
                Log.d("before join", "phone: " + str_phone);

                //////////////////////////////////////////////이름
                if (name.getText().toString().length() == 0) {
                    Toast.makeText(this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                    break;
                }

                /////////////////////////////////////////////////성별
                if (!male.isChecked() && !female.isChecked()) {
                    Toast.makeText(this, "성별을 체크해주세요", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    if (male.isChecked()) {
                        str_gender = "M";
                    } else if (female.isChecked()) {
                        str_gender = "F";
                    } else {
                        Toast.makeText(this, "gender error", Toast.LENGTH_SHORT).show();
                    }
                }
                Log.d("before join", "gender: " + str_gender);

                /////////////////////////////////////////////////////////나이
                if (age.getText().toString().length() == 0) {
                    Toast.makeText(this, "나이를 입력해주세요", Toast.LENGTH_SHORT).show();
                    break;
                }

                if(AreaSpinners[2].getSelectedItemPosition() == 0){
                    Toast.makeText(this, "주소를 모두 선택해주세요", Toast.LENGTH_SHORT).show();
                    break;
                }
                ///////////////////////////////////////////////////////////주소 유효성 체크
//                str_address = area1.getSelectedItem() + " " + area2.getSelectedItem();

//                Toast.makeText(activity_JOIN_Profile.this, "" + SpinnerCheck(year,month,day) + SpinnerCheck(area1,area2,area3) , Toast.LENGTH_SHORT).show();
//                Toast.makeText(activity_JOIN_Profile.this, "birthday: " + str_birthday + "address: " + str_address + "phone: " + isCellphone(str_phone), Toast.LENGTH_SHORT).show();
                //////////////////////////////////////////////////////////연락처
                if (phone.getText().toString().length() == 0) {
                    Toast.makeText(this, "연락처를 입력해주세요", Toast.LENGTH_SHORT).show();
                    break;
                }

                ////////////////////////////////////////////////////////////연락처 유효성
                if (!isCellphone(str_phone)) {
                    Toast.makeText(this, "핸드폰 양식에 맞춰 입력해주세요", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    str_phone = phone.getText().toString();
                }

                Intent goClassInfo = new Intent(getApplicationContext(), activity_JOIN_ClassInfo.class);

                if (!Data.hasExtra("social")) {
                    goClassInfo.putExtra("id", Data.getStringExtra("id"));
                    goClassInfo.putExtra("pw", Data.getStringExtra("pw"));
                } else {
                    goClassInfo.putExtra("email", Data.getStringExtra("email"));
                    goClassInfo.putExtra("social", Data.getStringExtra("social"));
                }


                Log.d("activity_JOIN_Profile", "address" + str_address);

                if(profileimg.getDrawable() == null){
                    imageUri = "null";
                }

                goClassInfo.putExtra("image",imageUri);
                goClassInfo.putExtra("User_name", name.getText().toString());
                goClassInfo.putExtra("gender", str_gender);
                goClassInfo.putExtra("age", str_age);
                goClassInfo.putExtra("address", str_address);
                goClassInfo.putExtra("phone", str_phone);
                goClassInfo.putExtra("job", Data.getStringExtra("job"));

                //데이터 확인
                Log.d("goClassInfo","image: " + imageUri
                        + "\nUser_name: " + name.getText().toString()
                        + "\ngender: " + str_gender
                        + "\nage: " + str_age
                        + "\naddress: " + str_address
                        + "\nphone: " + str_gender
                        + "\njob: " + Data.getStringExtra("job"));
                ///////////////////////////////////////////////////////////////회원가입 페이지 스택에서 없애기
                goClassInfo.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goClassInfo);
        }
    }

    public void makeSpinner(final ArrayList<Area> arrayList, final Spinner spinner, final ArrayAdapter adapter) {
        Log.d("makeSpinner","call");
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
//                Toast.makeText(getBaseContext(),arrayList.get(i)+"을 선택하셨습니다",Toast.LENGTH_SHORT).show();

                if(adapterView.getChildAt(0) != null) {
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


                int list_index = -1;

                switch (spinner_id) {
                    case R.id.area1:

                        list_index = 1;
                        Log.d("makeSpinner", "list_index: " + list_index);

                        //선택 클릭 안햇을시
                        if(i != 0) {
                            AreaRow[1].setVisibility(View.VISIBLE);
                            mAreaHandler = new AreaHandler(juso_list.get(list_index), AreaSpinners[1]);
                            new Thread() {
                                @Override
                                public void run() {
                                    Log.d("onitemselected", "thread start case R.id.area1");
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

                                    Message msg = mAreaHandler.obtainMessage();
                                    msg.setData(bundle);
                                    mAreaHandler.sendMessage(msg);
//                        Message msg = handler2.obtainMessage();
//                        msg.setData(bundle);
//                        handler2.sendMessage(msg);
                                }
                            }.start();
                        }
                        //선택 클릭햇을경우
                        else{
                            AreaRow[1].setVisibility(View.GONE);
                            AreaRow[2].setVisibility(View.GONE);
//                                AreaSpinners[1].setSelection(0);
//                                AreaSpinners[2].setSelection(0);
                            ArrayList<String> list2 = new ArrayList<>();
                            ArrayList<String> list3 = new ArrayList<>();

                            list2.add("선택");
                            list3.add("선택");

                            ArrayAdapter adapter2 = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,list2);
                            ArrayAdapter adapter3 = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,list3);

                            AreaSpinners[1].setAdapter(adapter2);
                            ArrayAdapter ap = (ArrayAdapter) AreaSpinners[1].getAdapter();
                            AreaSpinners[1].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    if(adapterView.getChildAt(0) != null) {
                                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });

                            ap.notifyDataSetChanged();

                            AreaSpinners[2].setAdapter(adapter3);
                            ArrayAdapter ap2 = (ArrayAdapter) AreaSpinners[2].getAdapter();
                            AreaSpinners[2].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    if(adapterView.getChildAt(0) != null) {
                                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });

                            ap2.notifyDataSetChanged();


                            Log.d("makespinner","@@@@@refresh@@@@@");
                        }
                        break;

                    case R.id.area2:
                        list_index = 2;
                        Log.d("makeSpinner", "list_index: " + list_index);
                        if(i != 0) {
                            AreaRow[2].setVisibility(View.VISIBLE);
                            mAreaHandler = new AreaHandler(juso_list.get(list_index), AreaSpinners[2]);
                            new Thread() {
                                @Override
                                public void run() {
                                    Log.d("onitemselected", "thread start case R.id.area2");
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

                                    Message msg = mAreaHandler.obtainMessage();
                                    msg.setData(bundle);
                                    mAreaHandler.sendMessage(msg);
//                        Message msg = handler2.obtainMessage();
//                        msg.setData(bundle);
//                        handler2.sendMessage(msg);
                                }
                            }.start();
                        }

                        else{
//                                AreaSpinners[1].setSelection(0);
//                                AreaSpinners[2].setSelection(0);
                            AreaRow[2].setVisibility(View.GONE);
                            ArrayList<String> list3 = new ArrayList<>();

                            list3.add("선택");

                            ArrayAdapter adapter3 = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,list3);
//                                adapter3.notifyDataSetChanged();
                            AreaSpinners[2].setAdapter(adapter3);
                            ArrayAdapter ap = (ArrayAdapter) AreaSpinners[2].getAdapter();
                            ap.notifyDataSetChanged();

                            Log.d("makespinner","@@@@@refresh@@@@@");
                        }
                        break;
                    case R.id.area3:
                        list_index = 3;
                        Log.d("makeSpinner", "list_index: " + list_index);
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
            String array = "";
            ArrayList<String> strarealist = new ArrayList<>();
            Log.d("handleMessage",result);
            try {
                 JSONObject object = new JSONObject(result);
                JSONArray jsonArray = object.getJSONArray("result");
//                Area initarea = new Area();
//                initarea.setAreaname("선택");
//                initarea.setCd(-1);
//                arealist.add(initarea);
                arealist.clear();

                Area init = new Area();
                init.setAreaname("지역 선택");
                init.setCd(-1);
                arealist.add(init);

                strarealist.add("지역 선택");
                for (int i = 0; i < jsonArray.length(); i++) {
//                    array += jsonArray.getJSONObject(i).getString("addr_name") + "\n";
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

//                    areamap.put(addr_name, cd);
                }
                Log.d("handler","arealist start");
                for(int j = 0 ; j < arealist.size() ; j++){
                    Area sample = arealist.get(j);
                    Log.d("handleMessage","index " + j + ": " + sample.getAreaname() + ", " + sample.getCd());
                }
                //value 값으로 정렬해서 arraylist 만들기

                int id_spinner = spinner.getId();
                ArrayAdapter adapter;

                switch (id_spinner){
                    case R.id.area1:
                        ad[0] = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, strarealist);
                        makeSpinner(arealist,spinner,ad[0]);
                        break;
                    case R.id.area2:
                        ad[1] = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, strarealist);;
                        makeSpinner(arealist,spinner,ad[1]);
                        break;
                    case R.id.area3:
                        ad[2] = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, strarealist);;
                        makeSpinner(arealist,spinner,ad[2]);
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
        //list 받아오기
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cd", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAreaHandler = new AreaHandler(juso_list.get(0), AreaSpinners[0]);

        new Thread() {
            @Override
            public void run() {
                Log.d("init_list","thread start");
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

                Message msg = mAreaHandler.obtainMessage();
                msg.setData(bundle);
                mAreaHandler.sendMessage(msg);
//                Message msg = handler.obtainMessage();
//                msg.setData(bundle);
//                handler.sendMessage(msg);
            }
        }.start();

        ArrayList<String> list2 = new ArrayList<>();
        ArrayList<String> list3 = new ArrayList<>();

        list2.add("선택");
        list3.add("선택");

        ad[1] = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.area_select));
        ad[2] = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.area_select));

        AreaSpinners[1].setAdapter(ad[1]);
        AreaSpinners[1].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getChildAt(0) != null) {
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        AreaSpinners[2].setAdapter(ad[2]);
        AreaSpinners[2].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getChildAt(0) != null) {
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Log.d("init_list","@@@@@refresh@@@@@");
    }

    //init_list
    //시도 리스트에 데이터 넣어주는 핸들러

//    Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            Bundle bun = msg.getData();
//            String result = bun.getString("result");
//            String array = "";
//
//            try {
//                JSONObject object = new JSONObject(result);
//                JSONArray jsonArray = object.getJSONArray("result");
//
////                AreaMap.put("선택해주세요",-1);
//                for (int i = 0; i < jsonArray.length(); i++) {
////                    array += jsonArray.getJSONObject(i).getString("addr_name") + "\n";
//                    String addr_name = jsonArray.getJSONObject(i).getString("addr_name");
//                    Integer cd = Integer.valueOf(jsonArray.getJSONObject(i).getString("cd"));
//
////                    AreaMap.put(addr_name, cd);
//                }
//
//                //value 값으로 정렬해서 arraylist 만들기
////                시도_list = sortByValue(AreaMap);
//                adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, 시도_list);
//                makeSpinner(시도_list, area1, adapter);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    };
//
//    //구군 리스트에 데이터 넣어주는 핸들러
//    Handler handler2 = new Handler() {
//        public void handleMessage(Message msg) {
//            Bundle bun = msg.getData();
//            String result = bun.getString("result");
//            String array = "";
//            try {
//                JSONObject object = new JSONObject(result);
//                JSONArray jsonArray = object.getJSONArray("result");
//
//                구군_list.clear();
//                for (int i = 0; i < jsonArray.length(); i++) {
////                    array += jsonArray.getJSONObject(i).getString("addr_name") + "\n";
//                    String addr_name = jsonArray.getJSONObject(i).getString("addr_name");
//
//
////                    구군_list.add(addr_name);
//                    Log.d("구군_list", "" + i + ": " + addr_name);
////                    Integer cd = Integer.valueOf(jsonArray.getJSONObject(i).getString("cd"));
////                    AreaMap2.put(addr_name, cd);
//                }
//
//                //value 값으로 정렬해서 arraylist 만들기
////                구군_list = sortByValue(AreaMap2);
//
//                adapter2 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, 구군_list);
//                area2.setAdapter(adapter2);
//                area2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> adapterView) {
//
//                    }
//                });
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    };

//    public static ArrayList sortByValue(final Map map) {
//        ArrayList<String> list = new ArrayList();
//        list.addAll(map.keySet());
//
//        Collections.sort(list, new Comparator() {
//
//            public int compare(Object o1, Object o2) {
//                Object v1 = map.get(o1);
//                Object v2 = map.get(o2);
//
//                return ((Comparable) v1).compareTo(v2);
//            }
//
//        });
////        Collections.reverse(list); // 주석시 오름차순
//        return list;
//    }

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
                    profileimg.setBackgroundResource(R.color.Transparent);
                    Glide.with(this).load(resultUri).into(profileimg);
                    imageUri = resultUri.toString();
                    Log.d(TAG, "CROP OK \nresultUri: " + resultUri);
                    break;
            }
        }
    }
}
