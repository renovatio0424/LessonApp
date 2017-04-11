package com.example.kimjungwon.lessonapp;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.StringTokenizer;

/**
 * Created by kimjungwon on 2017-03-25.
 */

public class Dialog_TimeInsert extends Dialog implements View.OnClickListener{
    private TextView TitleView,StartView,EndView,InsertView;
    private Button CancleBtn,OkBtn;
    private String title,start,end;
    TimePickerDialog timePickerDialog;
    Context context;
    private OnDismissListener onDismissListener;


    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public Dialog_TimeInsert(Context context, String title,
                             String start, String end, TextView insetview) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
        this.title = title;
        this.start = start;
        this.end = end;
        this.InsertView = insetview;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_time);

        TitleView = (TextView) findViewById(R.id.dialog_title);
        StartView = (TextView) findViewById(R.id.startview);
        EndView = (TextView) findViewById(R.id.endview);
        OkBtn = (Button) findViewById(R.id.btn_ok);
        CancleBtn = (Button) findViewById(R.id.btn_cancle);

        TitleView.setText(title);

        StartView.setOnClickListener(this);
        EndView.setOnClickListener(this);
        CancleBtn.setOnClickListener(this);
        OkBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.startview:
                timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        String time = String.format("%02d:%02d",hour,minute);
                        StartView.setText(time);
                        start = time;
                    }
                }, 12, 00, false);
                timePickerDialog.setTitle("시작 시간을 입력해주세요");
                timePickerDialog.show();
                break;
            case R.id.endview:
                timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        String time = String.format("%02d:%02d",hour,minute);
                        EndView.setText(time);
                        end = time;
                    }
                }, 12, 00, false);
//                timePickerDialog.setTitle("종료 시간을 입력해주세요");
                timePickerDialog.show();
                break;
            case R.id.btn_ok:
                if(onDismissListener != null){
                    onDismissListener.onDismiss(Dialog_TimeInsert.this);
                }

                if(!TimeCheck(StartView.getText().toString(),EndView.getText().toString())){
                    Toast.makeText(context, "시간을 정확히 입력해주세요", Toast.LENGTH_SHORT).show();
                    break;
                }

                InsertView.setText(start + "~" + end);
                this.dismiss();
                break;
            case R.id.btn_cancle:
                this.cancel();
                break;
        }
    }

    @Override
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public String getStart(){
        return this.StartView.getText().toString();
    }

    public String getEnd(){
        return this.EndView.getText().toString();
    }

    public boolean TimeCheck(String StartTime,String EndTime){
        StringTokenizer st = new StringTokenizer(StartTime,":");
        StringTokenizer st2 = new StringTokenizer(EndTime, ":");

        int[] time1 = new int[2];
        int[] time2 = new int[2];

        int i = 0;

        while(st.hasMoreTokens()){
            String t1 = st.nextToken();
            String t2 = st2.nextToken();

            time1[i] = Integer.valueOf(t1);
            time2[i] = Integer.valueOf(t2);

            i++;
        }

        if(time1[0]-time2[0]<0){
            return true;
        }else if(time1[0] == time2[0] && time1[1] - time2[1] <0){
            return true;
        }

        return false;
    }

}

