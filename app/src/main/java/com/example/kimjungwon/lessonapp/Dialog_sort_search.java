package com.example.kimjungwon.lessonapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by kimjungwon on 2017-04-05.
 */

public class Dialog_sort_search extends Dialog implements View.OnClickListener {
    private TextView TitleView;
    private Button CancleBtn, OkBtn;
    private RadioGroup sort_method_group;

    String TAG = Dialog_sort_search.class.getSimpleName();
    String Title,Result,sortmethod,job;

    Context context;

    private OnDismissListener onDismissListener;

    public Dialog_sort_search(Context context, String title, String sortmethod ,String job) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
        this.Title = title;
        this.sortmethod = sortmethod;
        this.job = job;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_sort_search);

        TitleView = (TextView) findViewById(R.id.dialog_title);
        OkBtn = (Button) findViewById(R.id.btn_ok_sort);
        CancleBtn = (Button) findViewById(R.id.btn_cancle_sort);

        sort_method_group = (RadioGroup) findViewById(R.id.RadioGroup);

        switch (sortmethod){
            case "최신 등록순":
                sort_method_group.check(R.id.radioButton1);
                break;
            case "조회순":
                sort_method_group.check(R.id.radioButton2);
                break;
            case "후기 많은 순":
                sort_method_group.check(R.id.radioButton3);
                break;
        }
        sort_method_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id2 = i;

                RadioButton rb = (RadioButton) findViewById(i);
                Result = rb.getText().toString();
                activity_searchpeople.sortmethod = Result;
                Log.d(TAG,"sortmethod: " + activity_searchpeople.sortmethod);
            }
        });

        TitleView.setText(this.Title);
        OkBtn.setOnClickListener(this);
        CancleBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.btn_ok_sort:
                if(onDismissListener != null){
                    onDismissListener.onDismiss(Dialog_sort_search.this);
                }

                this.dismiss();
                break;
            case R.id.btn_cancle_sort:
                this.cancel();
                break;
        }
    }

    @Override
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }
}
