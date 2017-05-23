package com.example.kimjungwon.lessonapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

import static com.example.kimjungwon.lessonapp.URLconfig.Chatting_Port;
import static com.example.kimjungwon.lessonapp.URLconfig.MyURL;
import static com.example.kimjungwon.lessonapp.URLconfig.RequestLesson_URL;
import static com.example.kimjungwon.lessonapp.URLconfig.Review_URL;
import static com.example.kimjungwon.lessonapp.URLconfig.UpdateConsult_URL;

/**
 * Created by kimjungwon on 2017-04-13.
 */

public class activity_Info_Class extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private static Teacher teacher;

    Button RequestLesson;

    private static String TAG = activity_Info_Class.class.getSimpleName();

    static String job, name, myid;
    Boolean review = false;

    static int lesson_id ;
    static ArrayList<Review> reviews = null;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Intent intent = getIntent();
        myid = intent.getStringExtra("id");
        job = intent.getStringExtra("job");
        name = intent.getStringExtra("name");

        //후기 작성 노티에서 이동했니?
        if(intent.hasExtra("review"))
            review = intent.getBooleanExtra("review",false);

        teacher = (Teacher) intent.getSerializableExtra("person");
        lesson_id = teacher.getLesson().getLesson_id();
        toolbar.setTitle(teacher.getLesson().getLesson_Title());

        //강의 배경
        ImageView imageView = (ImageView) findViewById(R.id.lesson_background);
        imageView.setColorFilter(Color.argb(99, 00, 00, 00));

        String url = teacher.getLesson().getLesson_Background_Image();
        if (url.equals("null")) {
            Glide.with(this).load(R.drawable.background_test).centerCrop().into(imageView);
        } else {
            Glide.with(this).load(MyURL + url).centerCrop().into(imageView);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity_Info_Class.this, "background_img", Toast.LENGTH_SHORT).show();
            }
        });

        //프로필 사진
        ImageView imageView1 = (ImageView) findViewById(R.id.profile_img_teacher);

        if (teacher.getProfile_image().equals("null")) {
            if (teacher.getGender().charAt(0) == 'M') {
                imageView1.setImageResource(R.drawable.ic_male_student);
            } else {
                imageView1.setImageResource(R.drawable.ic_female_student);
            }
        } else {
            Glide.with(getApplicationContext()).load(MyURL + teacher.getProfile_image()).into(imageView1);
        }
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity_Info_Class.this, "profile_img", Toast.LENGTH_SHORT).show();
            }
        });

        TextView nameview = (TextView) findViewById(R.id.info_teacher_name);
        nameview.setText(teacher.getName());


//         Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //과외 완료 인텐트로부터 왔으면 후기 작성 탭으로 자동 이동
        if(review)
            tabLayout.getTabAt(2).select();

        RequestLesson = (Button) findViewById(R.id.RequestLesson);
        RequestLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PHPRequest request = new PHPRequest(RequestLesson_URL);
                    JSONObject jsonObject = new JSONObject();
                    String id = teacher.getId();
                    jsonObject.put("id", id);
                    jsonObject.put("lessontitle",teacher.getLesson().getLesson_Title());
                    jsonObject.put("myid",myid);
                    jsonObject.put("name", name);
                    jsonObject.put("job", job);

                    String jo = jsonObject.toString();
                    Log.d(TAG, "before json: " + jo);
                    String result = request.POSTJSON(jo);
                    Log.d(TAG, "after json: " + result);

                    Toast.makeText(activity_Info_Class.this, result, Toast.LENGTH_SHORT).show();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView;
            int page = getArguments().getInt(ARG_SECTION_NUMBER);

            switch (page) {
//                수업 정보
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_classinfo, container, false);
                    TextView studentlevel = (TextView) rootView.findViewById(R.id.info_studentlevel);
                    TextView info_intro = (TextView) rootView.findViewById(R.id.info_intro);
                    TextView lessonsubject = (TextView) rootView.findViewById(R.id.info_lessonsubject);
                    TextView lessonfee = (TextView) rootView.findViewById(R.id.info_lessonfee);

                    ArrayList<String> levels = teacher.getLesson().getStudentlevel();
                    String level = ArrayListToString(levels);
                    studentlevel.setText(level);

                    String intro = teacher.getLesson().getIntro();
                    info_intro.setText(intro);

                    String fee = ArrayListToFee(teacher.getFee());
                    lessonfee.setText(fee);

                    String subject = ArrayListToString(teacher.getLesson().getLesson_Subject());
                    lessonsubject.setText(subject);

                    String schedule = "";
                    for (int i = 0; i < teacher.getSchedule().size(); i++) {
                        schedule += teacher.getSchedule().get(i);
                        if (i != teacher.getSchedule().size() - 1) {
                            schedule += "\n";
                        }
                    }

                    TextView Schedule = (TextView) rootView.findViewById(R.id.info_schedule);
                    Schedule.setText(schedule);
                    break;
//                선생님 정보
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_teacherinfo, container, false);
                    TextView genderview = (TextView) rootView.findViewById(R.id.info_gender);
                    String gender = teacher.getGender();
                    genderview.setText(gender);

                    TextView lpview = (TextView) rootView.findViewById(R.id.info_lessonplace);
                    ArrayList<String> lps = teacher.getLessonPlace();
                    String lp = ArrayListToString(lps);
                    lpview.setText(lp);

                    TextView collegeview = (TextView) rootView.findViewById(R.id.info_college);
                    collegeview.setText(teacher.getCollegeName());

                    TextView tc_intro_view = (TextView) rootView.findViewById(R.id.info_tc_intro);
                    tc_intro_view.setText(teacher.getIntro());

                    TextView ageview = (TextView) rootView.findViewById(R.id.info_age);
                    ageview.setText(teacher.getAge());
                    break;
//                후기
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_review, container, false);
                    RecyclerView recyclerView = (RecyclerView)rootView.findViewById(R.id.review_view);

                    try {
                        reviews = new UpdateReviewTask(getContext()).execute(lesson_id).get();
                        for(int i = 0 ; i < reviews.size() ; i++){
                            Review rv = reviews.get(i);
                            Log.d(TAG,"updateReviewTask\nindex: " + (i+1) + "\ncontent: " +rv.getContent() );
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

//                    ArrayList<Review> reviews = new ArrayList<>();
//                    for(int i = 0 ; i < 20 ; i ++){
//                        Review review = new Review();
//                        review.setUser_name("유저 " + (i + 1));
//                        review.setRating(i % 5);
//                        review.setContent("내용 " + (i + 1));
//                        reviews.add(review);
//                    }

                    final TextView total_review_num = (TextView) rootView.findViewById(R.id.total_review_tv);
                    total_review_num.setText("총 후기 "+ reviews.size() + "개");
                    final RecyclerAdapter_review recyclerAdapter_review = new RecyclerAdapter_review(getContext(),reviews,R.layout.card_review);
                    recyclerView.setAdapter(recyclerAdapter_review);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(linearLayoutManager);

                    //새로고침 버튼
                    ImageView refresh = (ImageView) rootView.findViewById(R.id.refresh_iv);
                    refresh.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                recyclerAdapter_review.clear();
                                recyclerAdapter_review.addAll(new UpdateReviewTask(getContext()).execute(lesson_id).get());
                                recyclerAdapter_review.notifyDataSetChanged();
                                total_review_num.setText("총 후기 "+ recyclerAdapter_review.getItemCount() + "개");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    //후기 작성 버튼
                    Button write_btn = (Button) rootView.findViewById(R.id.write_btn);
                    write_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                PHPRequest phpRequest = new PHPRequest(Review_URL);
                                JSONObject jsonObject1 = new JSONObject();
                                jsonObject1.put("case",2);
                                jsonObject1.put("lesson_id",lesson_id);
                                jsonObject1.put("usr_id",myid);
                                Log.d(TAG,"lesson_id: " + lesson_id + "\nusr_id: " + myid);
                                String duplicate = phpRequest.POSTJSON(jsonObject1.toString());
                                Log.d(TAG,"duplicate: " + duplicate);
                                if(!duplicate.equals("true")){
                                    Toast.makeText(getContext(), duplicate, Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            new MaterialDialog.Builder(getContext())
                                    .title("과외 후기")
                                    .customView(R.layout.dialog_write_review,true)
                                    .positiveText("작성")
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                            try {
                                                PHPRequest phpRequest = new PHPRequest(Review_URL);
                                                JSONObject jsonObject = new JSONObject();
                                                RatingBar ratingbar = (RatingBar) dialog.findViewById(R.id.write_rating);
                                                EditText content_et = (EditText) dialog.findViewById(R.id.write_content);

                                                float rating = ratingbar.getRating();
                                                String content = content_et.getText().toString();

                                                // 0 : write_review case
                                                jsonObject.put("case",0);
                                                jsonObject.put("lesson_id",lesson_id);
                                                jsonObject.put("usr_id",myid);
                                                jsonObject.put("rating",rating);
                                                jsonObject.put("content",content);

                                                String before = jsonObject.toString();

                                                String result = phpRequest.POSTJSON(before);
                                                Toast.makeText(getContext(),result, Toast.LENGTH_SHORT).show();
                                                Log.d(TAG,"lesson_id: " + lesson_id +
                                                        "\nusr_id: " + myid +
                                                        "\nrating: " + rating +
                                                        "\ncontent: " + content +
                                                        "\nresult: " + result);
                                                recyclerAdapter_review.clear();
                                                recyclerAdapter_review.addAll(new UpdateReviewTask(getContext()).execute(lesson_id).get());
                                                recyclerAdapter_review.notifyDataSetChanged();
                                                total_review_num.setText("총 후기 "+ recyclerAdapter_review.getItemCount() + "개");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            } catch (MalformedURLException e) {
                                                e.printStackTrace();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            } catch (ExecutionException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    })
                                    .negativeText("취소")
                                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();

                        }
                    });
                    break;
                default:
                    rootView = inflater.inflate(R.layout.fragment_classinfo, container, false);
                    break;
            }
            return rootView;
        }
    }

    public static String ArrayListToString(ArrayList<String> list) {
        String result = "";
        for (int i = 0; i < list.size(); i++) {
            result += list.get(i) + "\n";
        }
        return result;
    }

    public static String ArrayListToFee(ArrayList<String> list) {
        String result = "주 " + list.get(0) + "회 " + list.get(1) + "시간 " + list.get(2) + "만원";
        return result;
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
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "강의 정보";
                case 1:
                    return "선생님 정보";
                case 2:
                    return "후기";
            }
            return null;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    public static class UpdateReviewTask extends AsyncTask<Integer, Void, ArrayList> {
        MaterialDialog progress_dialog;
        Context context;

        public UpdateReviewTask(Context context){
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            progress_dialog = new MaterialDialog.Builder(context)
                    .progress(true,0)
                    .show();
        }

        @Override
        protected ArrayList<Review> doInBackground(Integer... Integers) {
            int lesson_id = Integers[0];
            ArrayList<Review> result = new ArrayList<>();
            String after = null;
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("case",1);
                jsonObject.put("lesson_id",lesson_id);
                String before = jsonObject.toString();
                Log.d(TAG,"before: " + before);
                PHPRequest phpRequest = new PHPRequest(Review_URL);
                after = phpRequest.POSTJSON(before);
                Log.d(TAG,"after: " + after);
                result = StringToReview(after);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(ArrayList result) {
            progress_dialog.dismiss();
        }
    }

    public static ArrayList<Review> StringToReview(String result) throws JSONException {
        JSONArray jsonArray = new JSONArray(result);

        ArrayList<Review> results = new ArrayList<>();
        for (int i = 0 ; i < jsonArray.length() ; i++){
            Review review = new Review();
            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
            review.setId(Integer.parseInt(jsonObject2.getString("id")));
            review.setUser_img(jsonObject2.getString("user_img"));
            review.setUser_name((String) jsonObject2.get("user_name"));
            review.setContent(jsonObject2.getString("content"));
            review.setRating(Float.parseFloat(jsonObject2.getString("rating")));
            review.setDate((String) jsonObject2.get("date"));
            results.add(review);
        }

        return results;
    }
}
