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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.example.kimjungwon.lessonapp.URLconfig.MyURL;

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

    private static People person;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Intent intent = getIntent();
        String job = intent.getStringExtra("job");
        if(job.equals("student")){
            person = (Teacher) intent.getSerializableExtra("person");
            toolbar.setTitle(((Teacher)person).getLesson().getLesson_Title());
        }


        //강의 배경
        ImageView imageView = (ImageView) findViewById(R.id.lesson_background);
        imageView.setColorFilter(Color.argb(99,00,00,00));

        String url = ((Teacher)person).getLesson().getLesson_Background_Image();
        if(url.equals("null")){
            Glide.with(this).load(R.drawable.background_test).centerCrop().into(imageView);
        }else{
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

        if(((Teacher)person).getProfile_image().equals("null")){
            if(((Teacher)person).getGender().charAt(0) == 'M'){
//                Glide.with(context).load(R.drawable.ic_male_student).into(holder.profileimg_teacher);
                imageView1.setImageResource(R.drawable.ic_male_student);
            }else{
//                Glide.with(context).load(R.drawable.ic_female_student).into(holder.profileimg_teacher);
                imageView1.setImageResource(R.drawable.ic_female_student);
            }
        }else{
            Glide.with(getApplicationContext()).load(MyURL + ((Teacher)person).getProfile_image()).into(imageView1);
        }
//        Glide.with(this).load(R.drawable.sonnaeun).centerCrop().into(imageView1);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity_Info_Class.this, "profile_img", Toast.LENGTH_SHORT).show();
            }
        });

        TextView nameview = (TextView) findViewById(R.id.info_teacher_name);
        nameview.setText(((Teacher)person).getName());


//        Glide.with(this).load("http://52.79.203.148/uploads/ajsj@ajdj.com_1491713435.jpg").into(imageView1);
//         Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

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

            switch (page){
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_classinfo, container, false);
                    TextView studentlevel = (TextView) rootView.findViewById(R.id.info_studentlevel);
                    TextView info_intro = (TextView) rootView.findViewById(R.id.info_intro);
                    ArrayList<String> levels = ((Teacher)person).getLesson().getStudentlevel();
                    String level = ArrayListToString(levels);
                    studentlevel.setText(level);
                    String intro = ((Teacher)person).getLesson().getIntro();
                    info_intro.setText(intro);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_teacherinfo, container,false);
                    TextView genderview = (TextView) rootView.findViewById(R.id.info_gender);
                    genderview.setText(((Teacher)person).getGender());

                    TextView lpview = (TextView) rootView.findViewById(R.id.info_lessonplace);
                    ArrayList<String> lps = ((Teacher)person).getLessonPlace();
                    String lp = ArrayListToString(lps);
                    lpview.setText(lp);

                    TextView collegeview = (TextView) rootView.findViewById(R.id.info_college);
                    collegeview.setText(((Teacher)person).getCollegeName());

                    TextView tc_intro_view = (TextView) rootView.findViewById(R.id.info_tc_intro);
                    tc_intro_view.setText(((Teacher)person).getIntro());
                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_review,container,false);
                    break;
                default:
                    rootView = inflater.inflate(R.layout.fragment_classinfo, container, false);
                    break;
            }
            return rootView;
        }
    }

    public static String ArrayListToString(ArrayList<String> list){
        String result = "";
        for(int i = 0 ; i < list.size() ; i++){
            result += list.get(i) + "\n";
        }
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
