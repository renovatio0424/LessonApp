package com.example.kimjungwon.lessonapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import static com.example.kimjungwon.lessonapp.URLconfig.MyURL;
import static com.example.kimjungwon.lessonapp.URLconfig.RequestLesson_URL;

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

    String job, name, myid;

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

        teacher = (Teacher) intent.getSerializableExtra("person");
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
//        Glide.with(this).load("http://52.79.203.148/uploads/ajsj@ajdj.com_1491713435.jpg").centerCrop().into(imageView);

        //프로필 사진
        ImageView imageView1 = (ImageView) findViewById(R.id.profile_img_teacher);

        if (teacher.getProfile_image().equals("null")) {
            if (teacher.getGender().charAt(0) == 'M') {
//                Glide.with(context).load(R.drawable.ic_male_student).into(holder.profileimg_teacher);
                imageView1.setImageResource(R.drawable.ic_male_student);
            } else {
//                Glide.with(context).load(R.drawable.ic_female_student).into(holder.profileimg_teacher);
                imageView1.setImageResource(R.drawable.ic_female_student);
            }
        } else {
            Glide.with(getApplicationContext()).load(MyURL + teacher.getProfile_image()).into(imageView1);
        }
//        Glide.with(this).load(R.drawable.sonnaeun).centerCrop().into(imageView1);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity_Info_Class.this, "profile_img", Toast.LENGTH_SHORT).show();
            }
        });

        TextView nameview = (TextView) findViewById(R.id.info_teacher_name);
        nameview.setText(teacher.getName());


//        Glide.with(this).load("http://52.79.203.148/uploads/ajsj@ajdj.com_1491713435.jpg").into(imageView1);
//         Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

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
    }
}
