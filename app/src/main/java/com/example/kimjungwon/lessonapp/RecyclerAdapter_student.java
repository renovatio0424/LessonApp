package com.example.kimjungwon.lessonapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.example.kimjungwon.lessonapp.URLconfig.MyURL;

/**
 * Created by kimjungwon on 2017-02-25.
 */

public class RecyclerAdapter_student extends RecyclerView.Adapter<RecyclerAdapter_student.mViewHolder>{

    Context context;
    private ArrayList<People> studentList;
    int item_layout;
    
    public RecyclerAdapter_student(Context context, ArrayList<People> students, int item_layout){
        this.context = context;
        this.studentList = students;
        this.item_layout = item_layout;
    }
    
    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_student,null);
        
        return new mViewHolder(v);
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, int position) {
        Student student = (Student) studentList.get(position);

//        holder.profileImage.setImageURI(Uri.parse(student.getProfile_image()));
        if(student.getProfile_image().equals("null")){
            if(student.getGender().equals("남자")){
                holder.profileImage.setImageResource(R.drawable.ic_male_student);
            }else{
                holder.profileImage.setImageResource(R.drawable.ic_female_student);
            }
        }else{
            Glide.with(context).load(MyURL + student.getProfile_image()).into(holder.profileImage);
        }

        holder.name_view.setText(student.getName());
        holder.place_view.setText(student.getAddress());

        String subject_R = "";
        String fee_R = "";

//        subject_R = student.getSubject().get(0);

        //희망 과목: 1)ㅁㅁ2)ㅇㅇ3)ㄴㄴ
        for(int i = 0 ; i < student.getSubject().size() ; i++){
            int num = i + 1;
            if(!student.getSubject().get(i).equals("없음")) {
                if(i != 0){
                    subject_R += "\n";
                }
                String sb[] = student.getSubject().get(i).split(" - ");
                //국어 - 수능국어
                subject_R += ""+num+") "+sb[1];
            }
        }

        //과외비: 주 1회 2시간 25만원

        fee_R += student.getFee().get(2) + "만원";
//        for(int i = 0 ; i < student.getFee().size() ; i++){
//            switch (i){
//                case 0:
//                    fee_R += "주 " + student.getFee().get(i) + "회 ";
//                    break;
//                case 1:
//                    fee_R += student.getFee().get(i) + "시간 ";
//                    break;
//                case 2:
//                    fee_R += student.getFee().get(i) + "만원";
//                    break;
//            }
//        }

        holder.subject_view.setText(subject_R);
        holder.fee_view.setText(fee_R);
    }

    public class mViewHolder extends RecyclerView.ViewHolder{
        ImageView profileImage;
        TextView name_view;
        TextView subject_view;
        TextView place_view;
        TextView fee_view;

        public mViewHolder(View itemView) {
            super(itemView);
            profileImage = (ImageView) itemView.findViewById(R.id.student_profile_Img);
            name_view = (TextView) itemView.findViewById(R.id.student_name);
            subject_view = (TextView) itemView.findViewById(R.id.student_subject);
            place_view = (TextView) itemView.findViewById(R.id.student_place);
            fee_view = (TextView) itemView.findViewById(R.id.student_fee);
        }
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public void addItem(String name, String place, String subject, String fee){
        Student student = new Student();

//        student.setProfile_image(URL);
        student.setName(name);
        student.setAddress(place);
        student.setSubject(subject);
        student.setFee(fee);

        studentList.add(student);
    }
}
