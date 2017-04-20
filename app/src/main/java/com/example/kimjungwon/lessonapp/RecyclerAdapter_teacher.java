package com.example.kimjungwon.lessonapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.example.kimjungwon.lessonapp.URLconfig.ImageLoad_URL;
import static com.example.kimjungwon.lessonapp.URLconfig.MyURL;

/**
 * Created by kimjungwon on 2017-04-12.
 */

public class RecyclerAdapter_teacher extends RecyclerView.Adapter<RecyclerAdapter_teacher.mViewHolder>{
    String TAG = RecyclerAdapter_teacher.class.getSimpleName();
    Context context;
    private ArrayList<People> TeacherList;
    int item_layout;

    public RecyclerAdapter_teacher(Context context, ArrayList<People> Teachers, int item_layout){
        this.context = context;
        this.TeacherList = Teachers;
        this.item_layout = item_layout;
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_lesson,parent,false);

        return new mViewHolder(v);
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, int position) {

        Teacher teacher = (Teacher) TeacherList.get(position);
        Lesson lesson = teacher.getLesson();


        Log.d(TAG,"profileimg: " + teacher.getProfile_image());

        //프로필 이미지
        if(teacher.getProfile_image().equals("null")){
            if(teacher.getGender().charAt(0) == 'M'){
//                Glide.with(context).load(R.drawable.ic_male_student).into(holder.profileimg_teacher);
                holder.profileimg_teacher.setImageResource(R.drawable.ic_male_student);
            }else{
//                Glide.with(context).load(R.drawable.ic_female_student).into(holder.profileimg_teacher);
                holder.profileimg_teacher.setImageResource(R.drawable.ic_female_student);
            }
        }else{
//            MyURL+"/uploads/naeun@naver.com_1492222678.jpg";
            Glide.with(context).load(MyURL + teacher.getProfile_image()).into(holder.profileimg_teacher);
            Log.d(TAG,"profile URL: " + MyURL + teacher.getProfile_image());
        }

        //배경 이미지
        holder.lesson_background.setColorFilter(Color.argb(99, 00, 00, 00));

        if(lesson.getLesson_Background_Image().equals("null")){
            Glide.with(context).load(R.drawable.background_test).centerCrop().into(holder.lesson_background);
        }else {
            Glide.with(context).load(MyURL + lesson.getLesson_Background_Image()).centerCrop().into(holder.lesson_background);
            Log.d(TAG,"background URL: " + MyURL + lesson.getLesson_Background_Image());
        }

        holder.Name_teacher.setText(teacher.getName());
//        holder.Lesson_Title.setText(teacher.getName() + "의 강의!");
//        holder.Lesson_Place.setText("과외 장소");
//        holder.Lesson_Subject.setText("과외 과목");
//        holder.Lesson_Fee.setText("과외비");
//        holder.Review_count.setText("후기 수");
        holder.Lesson_Title.setText(lesson.getLesson_Title());

        String place = "";
        for(int i = 0 ; i < teacher.getLessonPlace().size() ; i++){
            place += teacher.getLessonPlace().get(i) + "\n";
        }

        holder.collge_name.setText(teacher.getCollegeName());

//        holder.Lesson_Place.setText(place);


        String fee = "주" + lesson.getFee().get(0) + "회 " +
                lesson.getFee().get(2) +"만원";
        holder.Lesson_Fee.setText(fee);

        String lesson_subject = "";
        for(int i = 0 ; i < lesson.getLesson_Subject().size() ; i++){
            lesson_subject += lesson.getLesson_Subject().get(i).split(" - ")[1];
            if(i != lesson.getLesson_Subject().size() - 1){
                lesson_subject += " / ";
            }
        }

        holder.Lesson_Subject.setText(lesson_subject);
        holder.Review_count.setText(""+lesson.getHits());

    }

    public class mViewHolder extends RecyclerView.ViewHolder{
        ImageView profileimg_teacher,lesson_background;
        TextView Name_teacher, Lesson_Title, Lesson_Subject, collge_name, Lesson_Fee, Review_count;
//        int height,width;
        public mViewHolder(View itemView) {
            super(itemView);
//            if(itemView.getMeasuredHeight() != 0){
//                height = itemView.getMeasuredHeight();
//            }
//            if(itemView.getMeasuredWidth() != 0 ){
//                width = itemView.getMeasuredWidth();
//            }
//            Log.d(TAG,"height: " + height + "\nwidth: " + width);
            profileimg_teacher = (ImageView) itemView.findViewById(R.id.profile_img_tc);
            Name_teacher = (TextView) itemView.findViewById(R.id.name_tc);
            Lesson_Title = (TextView) itemView.findViewById(R.id.lesson_title);
            Lesson_Subject = (TextView) itemView.findViewById(R.id.lesson_subject);
            collge_name = (TextView) itemView.findViewById(R.id.college_name);
            Lesson_Fee = (TextView) itemView.findViewById(R.id.lesson_fee);
            Review_count = (TextView) itemView.findViewById(R.id.review_count);
            lesson_background = (ImageView) itemView.findViewById(R.id.lesson_background);
        }
    }

    @Override
    public int getItemCount() {
        return TeacherList.size();
    }

}
