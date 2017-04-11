package com.example.kimjungwon.lessonapp;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

/**
 * Created by kimjungwon on 2017-03-18.
 */

public class activity_main extends TabActivity{

    String Tab1name ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        TabHost tabHost = getTabHost();

        if(getIntent().getStringExtra("job").equals("teacher")){
            Tab1name = "학생찾기";
        }else if(getIntent().getStringExtra("job").equals("student")){
            Tab1name = "과외찾기";
        }else{
            Tab1name = "no job";
        }


        TabHost.TabSpec tabSpecTab1 = tabHost.newTabSpec("TAB1").setIndicator(Tab1name).
                setContent(new Intent(this, activity_searchpeople.class).
                        putExtra("id",getIntent().getStringExtra("id")).
                        putExtra("job",getIntent().getStringExtra("job")).
                        putExtra("name",getIntent().getStringExtra("name"))
                );
        tabHost.addTab(tabSpecTab1);

        TabHost.TabSpec tabSpecTab2 = tabHost.newTabSpec("TAB2").setIndicator("과외 톡").
                setContent(new Intent(this, activity_ChatList.class).
                        putExtra("id",getIntent().getStringExtra("id")).
                        putExtra("job",getIntent().getStringExtra("job")).
                        putExtra("name",getIntent().getStringExtra("name"))
                );
        tabHost.addTab(tabSpecTab2);

        TabHost.TabSpec tabSpecTab3 = tabHost.newTabSpec("TAB3").setIndicator("마이 페이지").
                setContent(new Intent(this, activity_mypage.class).
                        putExtra("id",getIntent().getStringExtra("id")).
                        putExtra("job",getIntent().getStringExtra("job")).
                        putExtra("name",getIntent().getStringExtra("name"))
                );
        tabHost.addTab(tabSpecTab3);

        tabHost.setCurrentTab(0);

    }
}
