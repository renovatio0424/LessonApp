package com.example.kimjungwon.lessonapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ThemedSpinnerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;
import com.nhn.android.naverlogin.OAuthLogin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.kimjungwon.lessonapp.URLconfig.FindPeople_url;

public class activity_searchpeople extends AppCompatActivity implements View.OnClickListener {

    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    HashMap<String, Integer> AreaMap, AreaMap2;
    ArrayList<String> 시도_list, 구군_list;

    static String[] subjects = {"신규", "국어", "영어", "수학", "사회", "과학", "예체능", "제2 외국어"};
    static int[] Current_pages = {0, 0, 0, 0, 0, 0, 0, 0, 0};

    static ArrayList<ArrayList<Student>> Total_student_list;

    static ArrayList<Student> student_list, 국어_student_list, 영어_student_list, 수학_student_list, 사회_student_list, 과학_student_list, 예체능_student_list, 제2외국어_student_list;
    Spinner spinner1, spinner2;
    ArrayAdapter adapter, adapter2;

    public static TextView BarTitle, sort_search, detailed_search;

    final public int Request_area = 1, goback_area = 2;

    static String Usr_id;
    static String name;
    static String area_title;



    static RecyclerAdapter recyclerAdapter;

    public OAuthLogin mOAuthLoginInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchpeople);

        mOAuthLoginInstance = OAuthLogin.getInstance();

        BarTitle = (TextView) findViewById(R.id.Title);

        BarTitle.setText("지역 선택 ▼");

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        BarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(activity_searchpeople.this, "toolbar click!!", Toast.LENGTH_SHORT).show();
                Intent goSelectArea = new Intent(getApplicationContext(), activity_SelectArea.class);
                Usr_id = getIntent().getStringExtra("id");
                goSelectArea.putExtra("id", getIntent().getStringExtra("id"));
                Log.d("goselectArea", getIntent().getStringExtra("id"));

                startActivityForResult(goSelectArea, Request_area);

            }
        });

//        toolbar.setClickable(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setTitle("지역선택");
//        int toolbar_id = getResources().getIdentifier("toolbar","R.id.toolbar","com.example.kimjunwon.lessonapp");
//        findViewById(toolbar_id).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(activity_searchpeople.this, "Click AppBar!!!", Toast.LENGTH_SHORT).show();
//            }
//        });

        AreaMap = new HashMap<>();
        AreaMap2 = new HashMap<>();

        시도_list = new ArrayList<>();
        구군_list = new ArrayList<>();


        Total_student_list = new ArrayList<>();
        student_list = new ArrayList<>();
        국어_student_list = new ArrayList<>();
        영어_student_list = new ArrayList<>();
        수학_student_list = new ArrayList<>();
        사회_student_list = new ArrayList<>();
        과학_student_list = new ArrayList<>();
        예체능_student_list = new ArrayList<>();
        제2외국어_student_list = new ArrayList<>();

        Total_student_list.add(student_list);
        Total_student_list.add(국어_student_list);
        Total_student_list.add(영어_student_list);
        Total_student_list.add(수학_student_list);
        Total_student_list.add(사회_student_list);
        Total_student_list.add(과학_student_list);
        Total_student_list.add(예체능_student_list);
        Total_student_list.add(제2외국어_student_list);

        sort_search = (TextView) findViewById(R.id.serch_dialog);
        detailed_search = (TextView) findViewById(R.id.detailed_search);

        sort_search.setOnClickListener(this);
        detailed_search.setOnClickListener(this);

//        init_list();

        //setup spinner 1:시-도 2:구-군
//        spinner1 = (Spinner) findViewById(R.id.spinner);
//        spinner2 = (Spinner) findViewById(R.id.spinner2);

        // Create the adapter that will return a fragment for each of every fragment
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

//        //Drawer Nav
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

//        //헤더뷰 값 바꾸기
//        View nav_header_view = navigationView.getHeaderView(0);
//
//        TextView nav_header_name = (TextView) nav_header_view.findViewById(R.id.nav_header_name);
//        TextView nav_header_id = (TextView) nav_header_view.findViewById(R.id.nav_header_id);
//
        Usr_id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        Log.d("main", "id: " + Usr_id + "name: " + name);
//
//        if (getIntent().hasExtra("id") && getIntent().hasExtra("name")) {
//            nav_header_id.setText(getIntent().getStringExtra("id"));
//            nav_header_name.setText(getIntent().getStringExtra("name"));
//        } else {
//            nav_header_name.setText("이름");
//            nav_header_id.setText("아이디");
//        }

        //학생리스트 초기값
        setStudents();
        recyclerAdapter = new RecyclerAdapter(getApplicationContext(), student_list, R.layout.student_card);
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerAdapter.notifyDataSetChanged();
        mSectionsPagerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Request_area:
                if (resultCode == RESULT_OK) {
                    String title = data.getStringExtra("area");
                    area_title = data.getStringExtra("area");
                    String[] title_array = title.split("@");

                    if(title_array.length > 1){

                        title = title_array[0] + " 외 " + (title_array.length - 1) + "곳";
                    }else{
                        title = title_array[0];
                    }
                    BarTitle.setText(title + " ▼");

                    Boolean all = data.getBooleanExtra("all", false);

                    if (all) {
                        setStudents();
                        break;
                    }
                    String students = data.getStringExtra("students");

//                    Toast.makeText(this, "" + students, Toast.LENGTH_SHORT).show();

                    try {
                        JSONObject jo = new JSONObject(students);
//                        JSONArray ja = new JSONArray(students);
                        student_list.clear();

                        for (int j = 0; j < Total_student_list.size(); j++) {
                            //담기 전에 초기화
                            Total_student_list.get(j).clear();

                            JSONArray ja = jo.getJSONArray(subjects[j]);

                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject jo2 = ja.getJSONObject(i);
                                String name = jo2.getString("name");
                                String gender = jo2.getString("gender");
                                String address = jo2.getString("address");
                                String LessonFee = jo2.getString("LessonFee");
                                String LessonSubject = jo2.getString("LessonSubject");
                                String LessonStyle = jo2.getString("LessonStyle");
                                String dealpossible = jo2.getString("deal_possible");

                                Log.d("student", "" + subjects[j] + ") name: " + name + " gender: " + gender + " address: " + address + " LessonFee: " + LessonFee + " LessonSubject: " + LessonSubject + " LessonStyle: " + LessonStyle + " deal_possible: " + dealpossible);

                                Student st = new Student();
                                st.setName(name);
                                st.setGender(gender);
                                st.setAddress(address);
                                st.setFee(LessonFee);
                                st.setStyle(LessonStyle);
                                st.setSubject(LessonSubject);
                                st.setDealpossible(dealpossible);

                                Total_student_list.get(j).add(st);

                                mSectionsPagerAdapter.notifyDataSetChanged();
//                            String total = name + gender + id ;
//                            Log.d("JSONARRAY",""+ i + ":" + tot
                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    for (int i = 0; i < student_list.size(); i++) {
//                        String name = student_list.get(i).getName();
//                        String gender = student_list.get(i).getGender();
//
//                        Log.d("ArrayList", "" + i + ":" + name + " " + gender);
//                    }
//                    Toast.makeText(this, "students: " + students, Toast.LENGTH_SHORT).show();
//                    for()
//                    Student
//                    ArrayList students = new ArrayList<Student>();
                } else {
                    Toast.makeText(this, "지역 가져오기 실패", Toast.LENGTH_SHORT).show();
                }
                break;

            case goback_area:
                setStudents();
                break;
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.detailed_search:
//                Intent goDetailSearch = new Intent(getApplicationContext(),)
                break;
            case R.id.serch_dialog:
                Dialog_sort_search dialog_sort_search = new Dialog_sort_search(activity_searchpeople.this, "정렬 방법을 선택해주세요");
                dialog_sort_search.show();
                break;
        }
    }


    //    private void init_list() {
//        //list 받아오기
//        final JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("cd", "");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        new Thread() {
//            @Override
//            public void run() {
//                PHPRequest request = null;
//                try {
//                    request = new PHPRequest(SearchArea_url);
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
//
//                Log.d("POSTJSON", "before: " + jsonObject.toString());
//                String result = request.POSTJSON(jsonObject.toString());
//                Log.d("POSTJSON", "after: " + result);
//
//                Bundle bundle = new Bundle();
//                bundle.putString("result", result);
//                Message msg = handler.obtainMessage();
//                msg.setData(bundle);
//                handler.sendMessage(msg);
//            }
//        }.start();
//    }

//    Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            Bundle bun = msg.getData();
//            String result = bun.getString("result");
//            String array = "";
//            try {
//                JSONObject object = new JSONObject(result);
//                JSONArray jsonArray = object.getJSONArray("result");
//
//                for (int i = 0; i < jsonArray.length(); i++) {
////                    array += jsonArray.getJSONObject(i).getString("addr_name") + "\n";
//                    String addr_name = jsonArray.getJSONObject(i).getString("addr_name");
//                    Integer cd = Integer.valueOf(jsonArray.getJSONObject(i).getString("cd"));
//
//                    AreaMap.put(addr_name, cd);
//                }
//
//                //value 값으로 정렬해서 arraylist 만들기
//                시도_list = sortByValue(AreaMap);
//
//                adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, 시도_list);
//
////                try {
////                    Field popup = Spinner.class.getDeclaredField("mPopup");
////                    popup.setAccessible(true);
////                    android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner1);
////                    popupWindow.setHeight(500);
////                } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
////                }
//
//                spinner1.setAdapter(new MyAdapter(
//                        toolbar.getContext(), 시도_list));
//                spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
////                Toast.makeText(activity_searchpeople.this, "cd: " + AreaMap.get(arrayList.get(i)).toString(), Toast.LENGTH_SHORT).show();
//                        //글씨색 검정
////                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
//
//                        String cd = AreaMap.get(시도_list.get(i)).toString();
//                        Log.d("cd_result", cd);
//                        final JSONObject jsonObject = new JSONObject();
//
//                        try {
//                            jsonObject.put("cd", cd);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        new Thread() {
//                            @Override
//                            public void run() {
//                                PHPRequest request = null;
//                                try {
//                                    request = new PHPRequest(SearchArea_url);
//                                } catch (MalformedURLException e) {
//                                    e.printStackTrace();
//                                }
//
//                                Log.d("POSTJSON2", "before: " + jsonObject.toString());
//                                String result = request.POSTJSON(jsonObject.toString());
//                                Log.d("POSTJSON2", "after: " + result);
//
//                                Bundle bundle = new Bundle();
//                                bundle.putString("result", result);
//                                Message msg = handler2.obtainMessage();
//                                msg.setData(bundle);
//                                handler2.sendMessage(msg);
//                            }
//                        }.start();
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
//
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
//
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
//                    구군_list.add(addr_name);
//
//                    Log.d("구군_list", "" + i + ": " + addr_name);
////                    Integer cd = Integer.valueOf(jsonArray.getJSONObject(i).getString("cd"));
////                    AreaMap2.put(addr_name, cd);
//                }
//
//                //value 값으로 정렬해서 arraylist 만들기
////                구군_list = sortByValue(AreaMap2);
//                try {
//                    Field popup = Spinner.class.getDeclaredField("mPopup");
//                    popup.setAccessible(true);
//                    android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner2);
//                    popupWindow.setHeight(500);
//                } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
//                }
//
//                spinner2.setAdapter(new MyAdapter(
//                        toolbar.getContext(), 구군_list
//                ));
//
//                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                        //글씨색 검정
////                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
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
//
//
//        }
//    };

//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.select_area) {
//            Toast.makeText(this, "select area!!", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private boolean isLoading = true;

        GridLayoutManager layoutManager;
        RecyclerView recyclerView;
        RecyclerAdapter recyclerAdapter;


        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        //데이터 입력 => bundle에 데이터를 담는다
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        // 뷰생성 ==> bundle에 입력된 데이터를 꺼내와 뷰를 생성
        // sectionNumber -> 섹션 번호
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            recyclerView = (RecyclerView) rootView.findViewById(R.id.RecyclerView);

            final ArrayList<Student> list = Total_student_list.get(getArguments().getInt(ARG_SECTION_NUMBER));
//            if (student_list.size() == 0) {
//                String URL, String name, String place, String subject, String fee
//                recyclerAdapter = new RecyclerAdapter(getContext(), student_list, R.layout.student_card);


//                for (int i = 0; i < 8; i++) {
//                    recyclerAdapter.addItem(
//                            "이름\n-section: " + getArguments().getInt(ARG_SECTION_NUMBER) + "\n-index: " + i,
//                            "장소\n-section: " + getArguments().getInt(ARG_SECTION_NUMBER) + "\n-index: " + i,
//                            "과목\n-section: " + getArguments().getInt(ARG_SECTION_NUMBER) + "\n-index: " + i,
//                            "과외비\n-section: " + getArguments().getInt(ARG_SECTION_NUMBER) + "\n-index: " + i);
//                }
//            } else {
            recyclerAdapter = new RecyclerAdapter(getContext(), list, R.layout.student_card);
//            }

//            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//            layoutManager.scrollToPosition(0);
            recyclerAdapter.notifyDataSetChanged();

            layoutManager = new GridLayoutManager(getContext(), 2);
            layoutManager.setOrientation(GridLayoutManager.VERTICAL);

//            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, 1);
//            layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
//            layoutManager.setOrientation(StaggeredGridLayoutManager.VERTICAL);

            recyclerView.setLayoutManager(layoutManager);

            recyclerView.setAdapter(recyclerAdapter);

            recyclerView.addOnScrollListener(infiniteScrollListener());
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(getContext(), "index" + position + "name: " + list.get(position).getName(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onItemLongClick(View view, int position) {

                }
            }));
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.detach(this).attach(this).commit();
//            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
//            RecyclerView.setAdapter(adapter);
//            for (int i = 0; i < 10; i++) {
//                adapter.add("section: " + getArguments().getInt(ARG_SECTION_NUMBER) + " = test " + i);
//            }

//            adapter.notifyDataSetChanged();


            //페이징
//            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                    super.onScrollStateChanged(recyclerView, newState);
//                }
//
//                @Override
//                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                    super.onScrolled(recyclerView, dx, dy);
//                    int visibleItemCount = layoutManager.getChildCount();
//                    int totalItemCount = layoutManager.getItemCount();
//                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
//
//                    if (!isLoading) {
//                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
//                            isLoading = false;
//                            loadMoreItems();
//                        }
//                    }
//                }
//            });
            return rootView;
        }

        private InfiniteScrollListener infiniteScrollListener() {
            return new InfiniteScrollListener(10, layoutManager) {
                @Override
                public void onScrolledToEnd(int firstVisibleItemPosition) {
                    Log.d("onScrolledToEnd", "called!!!!!!!!");

//                    String area = BarTitle.getText().toString().replace(" ▼","");
//                    if(area.equals("지역 선택")){
//                        area = area.replace("지역 선택","");
//                    }

                    String subject = subjects[getArguments().getInt(ARG_SECTION_NUMBER)];
                    int page = Current_pages[getArguments().getInt(ARG_SECTION_NUMBER)] + 1;

                    ArrayList<Student> NewList = LoadMoreStudents(area_title, subject, page);

                    if(NewList.isEmpty()){
                        Toast.makeText(getContext(), "등록된 학생이 없습니다", Toast.LENGTH_SHORT).show();
                    }else{
                        Total_student_list.get(getArguments().getInt(ARG_SECTION_NUMBER)).addAll(NewList);

                        for(int i = 0 ; i < Total_student_list.get(getArguments().getInt(ARG_SECTION_NUMBER)).size() ; i ++){
                            Student st = Total_student_list.get(getArguments().getInt(ARG_SECTION_NUMBER)).get(i);
                            Log.d("LoadMoreStudents","name: " + st.getName());
                        }

                        refreshView(recyclerView,
                                new RecyclerAdapter(getContext(), Total_student_list.get(getArguments().getInt(ARG_SECTION_NUMBER)), R.layout.student_card),
                                firstVisibleItemPosition);

                        Current_pages[getArguments().getInt(ARG_SECTION_NUMBER)]++;
                    }
                }
            };
        }

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show total pages.
            return 8;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "신규";
                case 1:
                    return "국어";
                case 2:
                    return "영어";
                case 3:
                    return "수학";
                case 4:
                    return "사회";
                case 5:
                    return "과학";
                case 6:
                    return "예체능";
                case 7:
                    return "제2 외국어";
                case 8:
                    return "기타";
            }
            return null;
        }

        @Override
        public int getItemPosition(Object object) {
//            this.notifyDataSetChanged();
            return POSITION_NONE;
        }
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            return 0;
        }
    }

    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final Helper mDropDownHelper;

        public MyAdapter(Context context, ArrayList<String> objects) {
            super(context, android.R.layout.simple_spinner_dropdown_item, objects);
            mDropDownHelper = new Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
            } else {
                view = convertView;
            }

            if (position < 5) {

                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setText(getItem(position));

            }

            return view;
        }

        @Override
        public Resources.Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Resources.Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }

    public static ArrayList LoadMoreStudents(String Address, String Subject, int Current_page) {
        JSONObject jo = new JSONObject();
        ArrayList<Student> resultlist = new ArrayList<>();

        try {
            jo.put("id", Usr_id);
            jo.put("name", name);
            jo.put("case", 2);
            jo.put("page", Current_page);
            jo.put("address", Address);
            jo.put("subject", Subject);

            String str_json = jo.toString();

            Log.d("LoadMoreStudents", "before json: " + str_json);

            PHPRequest request = new PHPRequest(FindPeople_url);

            String result = request.POSTJSON(str_json);

            Log.d("LoadMoreStudents", "after json: " + result);

//                    Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

            JSONArray ja = new JSONArray(result);
//                        JSONArray ja = new JSONArray(students);

            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo2 = ja.getJSONObject(i);
                String name = jo2.getString("name");
                String gender = jo2.getString("gender");
                String address = jo2.getString("address");
                String LessonFee = jo2.getString("LessonFee");
                String LessonSubject = jo2.getString("LessonSubject");
                String LessonStyle = jo2.getString("LessonStyle");
                String dealpossible = jo2.getString("deal_possible");

                Log.d("student", "" + Subject + ")name: " + name + " gender: " + gender + " address: " + address + " LessonFee: " + LessonFee + " LessonSubject: " + LessonSubject + " LessonStyle: " + LessonStyle + " deal_possible: " + dealpossible);

                Student st = new Student();
                st.setName(name);
                st.setGender(gender);
                st.setAddress(address);
                st.setFee(LessonFee);
                st.setStyle(LessonStyle);
                st.setSubject(LessonSubject);
                st.setDealpossible(dealpossible);

                resultlist.add(st);
//                            String total = name + gender + id ;
//                            Log.d("JSONARRAY",""+ i + ":" + tot
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultlist;
    }

    public void setStudents() {
        //jsonObject에 담아서 서버로 보내기

        JSONObject jo = new JSONObject();

        try {
            jo.put("id", Usr_id);
            jo.put("name", name);
            jo.put("case", 1);
            jo.put("page", 0);

            String str_json = jo.toString();

            Log.d("setStudents", "before json: " + str_json);

            PHPRequest request = new PHPRequest(FindPeople_url);

            String result = request.POSTJSON(str_json);

            Log.d("setStudents", "after json: " + result);

//                    Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

            try {
                JSONObject jo3 = new JSONObject(result);
//                        JSONArray ja = new JSONArray(students);
                student_list.clear();

                for (int j = 0; j < Total_student_list.size(); j++) {
                    //담기 전에 초기화
                    Total_student_list.get(j).clear();

                    JSONArray ja = jo3.getJSONArray(subjects[j]);

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo2 = ja.getJSONObject(i);
                        String name = jo2.getString("name");
                        String gender = jo2.getString("gender");
                        String address = jo2.getString("address");
                        String LessonFee = jo2.getString("LessonFee");
                        String LessonSubject = jo2.getString("LessonSubject");
                        String LessonStyle = jo2.getString("LessonStyle");
                        String dealpossible = jo2.getString("deal_possible");

                        Log.d("studet", "" + subjects[j] + ")name: " + name + " gender: " + gender + " address: " + address + " LessonFee: " + LessonFee + " LessonSubject: " + LessonSubject + " LessonStyle: " + LessonStyle + " deal_possible: " + dealpossible);

                        Student st = new Student();
                        st.setName(name);
                        st.setGender(gender);
                        st.setAddress(address);
                        st.setFee(LessonFee);
                        st.setStyle(LessonStyle);
                        st.setSubject(LessonSubject);
                        st.setDealpossible(dealpossible);

                        Total_student_list.get(j).add(st);

                        mSectionsPagerAdapter.notifyDataSetChanged();
//                            String total = name + gender + id ;
//                            Log.d("JSONARRAY",""+ i + ":" + tot
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}


