package com.example.kimjungwon.lessonapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static com.example.kimjungwon.lessonapp.URLconfig.FindPeople_url;
import static com.example.kimjungwon.lessonapp.URLconfig.SearchArea2_url;
import static com.example.kimjungwon.lessonapp.URLconfig.SearchArea_url;

/**
 * Created by kimjungwon on 2017-03-14.
 */

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kimjungwon on 2017-02-23.
 */

public class activity_SelectArea extends AppCompatActivity{

    private static final String TAG = activity_SelectArea.class.getSimpleName();

    String url = "http://52.79.203.148/searcharea.php";

    HashMap<String, Integer> AreaMap, AreaMap2;

    Addr_Adapter addr_adapter1, addr_adapter2;

    int Select_Count = 0;

    //시 리스트뷰
    ListView listView;
    //구동 리스트뷰
    private ExpandableListView expandableListView;
    private CustomExpandableListViewAdapter mCustomExpListViewAdapter;

    public ArrayList<String> parentList; // ExpandableListView의 Parent 항목이 될 List 변수 선언
    public HashMap<String, ArrayList<Area>> childList; // 위 ParentList와 ChildList를 연결할 HashMap 변수 선언

    String result = "",result_시 = "";
    ArrayList<String> ResultList;
    Button next_btn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_selectarea);

        addr_adapter1 = new Addr_Adapter();
        addr_adapter2 = new Addr_Adapter();

        AreaMap = new HashMap<>();
        AreaMap2 = new HashMap<>();

        listView = (ListView) findViewById(R.id.lv1);
        next_btn = (Button) findViewById(R.id.next_btn);

        ResultList = new ArrayList<>();

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity_SelectArea.this, "result: " + arrayJoin("@",ResultList), Toast.LENGTH_SHORT).show();
                PHPRequest request = null;

                String TotalArea = arrayJoin("@",ResultList);

                Log.d(TAG,TotalArea);
                try {
                    request = new PHPRequest(FindPeople_url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id",getIntent().getStringExtra("id"));
                    Log.d("activity_SelectArea","id: " + getIntent().getStringExtra("id"));
                    jsonObject.put("address", TotalArea);
                    jsonObject.put("case",3);
                    jsonObject.put("page",0);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "POSTJSON3 before: " + jsonObject.toString());
                String result = request.POSTJSON(jsonObject.toString());
                Log.d(TAG, "POSTJSON3 after: " + result);

//                if(i == 0){
//                    TotalArea += "전체";
//                }

                Intent goSearchPeople = new Intent(getApplicationContext(),activity_searchpeople.class);

                goSearchPeople.putExtra("area", TotalArea);
                goSearchPeople.putExtra("students", result);

                setResult(RESULT_OK,goSearchPeople);
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                //색깔 넣기
//                LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.addr_item);
//                ColorDrawable viewcolor = (ColorDrawable) linearLayout.getBackground();
//                if(viewcolor.getColor() != getResources().getColor(R.color.Gray)){
//                    linearLayout.setBackgroundColor(getResources().getColor(R.color.Gray));
//                }else{
//                    linearLayout.setBackgroundColor(getResources().getColor(R.color.White));
//                }
//                addr_adapter1.setClickposition(i);
                if(!result_시.equals("")){
                    result_시 = "";
                }
                result_시 = adapterView.getItemAtPosition(i).toString();

                String cd = AreaMap.get(adapterView.getItemAtPosition(i)).toString();
                Log.d("cd_result",cd);
                final JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("cd", cd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new Thread() {
                    @Override
                    public void run() {
                        PHPRequest request = null;
                        try {
                            request = new PHPRequest(SearchArea2_url);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "POSTJSON2 before: " + jsonObject.toString());
                        String result = request.POSTJSON(jsonObject.toString());
                        Log.d(TAG, "POSTJSON2 after: " + result);

                        Bundle bundle = new Bundle();
                        bundle.putString("result", result);
                        Message msg = handler2.obtainMessage();
                        msg.setData(bundle);
                        handler2.sendMessage(msg);
                    }
                }.start();
            }
        });

        ListInit();

        expandableListView = (ExpandableListView) findViewById(R.id.expandablelist);

        parentList = new ArrayList<>();
        childList = new HashMap<>();

        // 위에서 선언한 ParentList와 ChildList를 HashMap을 통해
        childList = new HashMap<String, ArrayList<Area>>();

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPOS, int childPOS, long l) {
//                Toast.makeText(MainActivity.this, "Group position: " + groupPOS + "Child position: " + childPOS, Toast.LENGTH_SHORT).show();
                Log.d(TAG,"Select_Count: " + Select_Count);

                //무슨 아이템을 선택햇는가??
//                Toast.makeText(activity_SelectArea.this, "" + childList.get(parentList.get(groupPOS)).get(childPOS).getAreaname(), Toast.LENGTH_SHORT).show();

                ImageView checkview = (ImageView) view.findViewById(R.id.check_icon2);
                //선택한 지역
                String select_areas = parentList.get(groupPOS)+ " " +childList.get(parentList.get(groupPOS)).get(childPOS).getAreaname();

                Area click_area = childList.get(parentList.get(groupPOS)).get(childPOS);

                //'전체'가 아닌 항목을 클릭했는데 전체 항목이 체크 되어있을때
                if(!click_area.getAreaname().equals("전체") && childList.get(parentList.get(groupPOS)).get(0).getChecked()){
                    Log.d(TAG,"total checked!!!!!");
                    childList.get(parentList.get(groupPOS)).get(0).setChecked(false);
                    ResultList.remove(result_시 + " " + parentList.get(groupPOS) + " 전체");
                    Select_Count --;
                    mCustomExpListViewAdapter.notifyDataSetChanged();

                    Log.d(TAG,"remove: "+ "@" + result_시 + " " + parentList.get(groupPOS) + " 전체");
                }else if(click_area.getAreaname().equals("전체")){
                    int size = childList.get(parentList.get(groupPOS)).size();

                    for(int i = 0 ; i < size ; i++){
                        if(childList.get(parentList.get(groupPOS)).get(i).getChecked()){
                            childList.get(parentList.get(groupPOS)).get(i).setChecked(false);
                            ResultList.remove(result_시 + " " + parentList.get(groupPOS) + " " +childList.get(parentList.get(groupPOS)).get(i).getAreaname());
                            Select_Count --;
                        }
                    }

                    mCustomExpListViewAdapter.notifyDataSetChanged();
                }else{
//                    Toast.makeText(activity_SelectArea.this, TAG + "expandable listview onChildClick error", Toast.LENGTH_SHORT).show();
                }

                if(checkview.getVisibility() == View.GONE){
                    Log.d(TAG,"VISIBLE!");
                    if(Select_Count > 4){
                        Toast.makeText(activity_SelectArea.this, "5개 이상 선택하실 수 없습니다", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    checkview.setVisibility(View.VISIBLE);
                    childList.get(parentList.get(groupPOS)).get(childPOS).setChecked(true);
                    ResultList.add(result_시 + " " + select_areas);
//                    if(result.equals("")){
//                        result += result_시 + " " + select_areas;
//                    }else{
//                        result += "@" + result_시 + " " + select_areas;
//                    }
                    Select_Count ++;
                }else{
                    Log.d(TAG,"GONE!");
                    checkview.setVisibility(View.GONE);
                    childList.get(parentList.get(groupPOS)).get(childPOS).setChecked(false);
                    ResultList.remove(result_시 + " " + select_areas);
//                    result = result.replace("@" + result_시 + " " + select_areas,"");
                    Log.d(TAG,"result: " + result);
                    Select_Count --;
                }
                return false;
            }
        });

        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.

    }

    private void ListInit() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cd", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Thread() {
            @Override
            public void run() {
                PHPRequest request = null;
                try {
                    request = new PHPRequest(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "POSTJSON before: " + jsonObject.toString());
                String result = request.POSTJSON(jsonObject.toString());
                Log.d(TAG, "POSTJSON after: " + result);

                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                Message msg = handler.obtainMessage();
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        }.start();
    }

    //시구 리스트 뿌려주는 핸들러
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bun = msg.getData();
            String result = bun.getString("result");
            try {
                JSONObject object = new JSONObject(result);
                JSONArray jsonArray = object.getJSONArray("result");

                for (int i = 0; i < jsonArray.length(); i++) {
                    String addr_nm = jsonArray.getJSONObject(i).getString("addr_name");
                    Integer cd = Integer.valueOf(jsonArray.getJSONObject(i).getString("cd"));

                    AreaMap.put(addr_nm, cd);
                }

                //value 값으로 정렬해서 arraylist 만들기
                addr_adapter1.setItemList(sortByValue(AreaMap));
                listView.setAdapter(addr_adapter1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    //구군 뿌려주는 핸들러
    Handler handler2 = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bun = msg.getData();
            String result = bun.getString("result");
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("goolist");

                if(!parentList.isEmpty()){
                    parentList.clear();
                }
                if(!childList.isEmpty()){
                    childList.clear();
                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    String addr_name = jsonArray.get(i).toString();

                    parentList.add(addr_name);
                    Log.d("parentList","" + i + ": " + parentList.get(i));
                }

                for (int i = 0 ; i < parentList.size() ; i ++){
                    JSONArray jsonArray1 = jsonObject.getJSONArray(parentList.get(i));
                    ArrayList<Area> Arealist = new ArrayList<>();
                    Area total = new Area();
                    total.setAreaname("전체");
                    Arealist.add(total);

                    for(int j = 0 ; j < jsonArray1.length() ; j++){
                        String areaname = jsonArray1.get(j).toString();
                        Area area = new Area();
                        area.setAreaname(areaname);
                        Arealist.add(area);
                        Log.d("childlist",""+i+": "+areaname);
                    }
                    childList.put(parentList.get(i),Arealist);
                }
                mCustomExpListViewAdapter = new CustomExpandableListViewAdapter(getApplicationContext(), parentList, childList);
                expandableListView.setAdapter(mCustomExpListViewAdapter);
                mCustomExpListViewAdapter.notifyDataSetChanged();
//                listView2.setAdapter(addr_adapter2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public static String arrayJoin(String divider, ArrayList<String> arrayList) {
        String result = "";

        for (int i = 0; i < arrayList.size(); i++) {
            result += arrayList.get(i).replace(" 전체","");
            if (i < arrayList.size() - 1) result += divider;
        }
        return result;
    }

    //cd 값으로 정렬 (오름차순)
    public static ArrayList sortByValue(final Map map) {
        ArrayList<String> list = new ArrayList();
        list.addAll(map.keySet());

        Collections.sort(list, new Comparator() {

            public int compare(Object o1, Object o2) {
                Object v1 = map.get(o1);
                Object v2 = map.get(o2);

                return ((Comparable) v1).compareTo(v2);
            }

        });
        return list;
    }

}

