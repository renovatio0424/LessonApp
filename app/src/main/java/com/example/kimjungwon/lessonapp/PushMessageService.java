package com.example.kimjungwon.lessonapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kimjungwon on 2017-04-21.
 */

public class PushMessageService extends FirebaseMessagingService {
    private static String TAG = PushMessageService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "onMessageReceived");
        sendNotification(remoteMessage.getData().get("message"));

    }

    private void sendNotification(String message) {
        Log.d(TAG,"message: " + message);
        Intent intent;
        String[] arr = message.split("%");
        String title = arr[0];
        String content = arr[1];
        Log.d(TAG,"title: " + title + "\ncontent: " + content);

        if(title.contains("상담")){
            intent = new Intent(this, activity_myconsulting.class);
        }
        //과외 의사소통
        else if(title.contains("과외성사") | title.contains("과외수락") | title.contains("과외거절")){
            intent = new Intent(this, activity_main.class);
        }
        //과외 완료
        else {
            intent = new Intent(this, activity_Info_Class.class);
            String json = arr[2];
            Log.d(TAG,"jsonObject: " + json);
            Teacher teacher = JsonToTeacher(json);
            intent.putExtra("person",teacher);
            intent.putExtra("review",true);
        }
//        if(message.contains("상담")){
//            intent = new Intent(this, activity_myconsulting.class);
//        }else{
//            intent = new Intent(this, activity_main.class);
//        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        DBHelper dbHelper = new DBHelper(this,"MyInfo.db",null,1);

        String id = dbHelper.getResult("MyInfo","id");
        String token = dbHelper.getResult("MyInfo","token");
        String name = dbHelper.getResult("MyInfo","name");
        String job = dbHelper.getResult("MyInfo","job");

        intent.putExtra("id",id);
        intent.putExtra("job",job);
        intent.putExtra("User_name",name);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //notification 커스텀
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    public Teacher JsonToTeacher(String json){
        Teacher teacher = new Teacher();

        JSONObject jo2 = null;
        try {
            jo2 = new JSONObject(json);
            int lesson_id = jo2.getInt("lesson_id");
            String lessontitle = jo2.getString("title");
            String background_img = jo2.getString("lessonbackground");
            String studentlevel = jo2.getString("studentlevel");
            String lessonfee = jo2.getString("studentlevel");
            String lessonsb = jo2.getString("LessonSubject");
            String lessonintro = jo2.getString("intro");
            String lesson_reg_date = jo2.getString("reg_date");
            int hits = jo2.getInt("hits");
            Boolean recruiting = false;
            if(jo2.get("recruiting").equals("true")){
                recruiting = true;
            }

            JSONObject teacher_json = jo2.getJSONObject("teacher");
            String id = teacher_json.getString("id");
            String name = teacher_json.getString("name");
            String gender = teacher_json.getString("gender");
            String age = teacher_json.getString("age");
            String profile_image = teacher_json.getString("profile_image");
            String address = teacher_json.getString("address");
            String LessonFee = teacher_json.getString("LessonFee");
            String LessonSubject = teacher_json.getString("LessonSubject");
            String dealpossible = teacher_json.getString("deal_possible");
            String intro = teacher_json.getString("intro");
            String reg_date = teacher_json.getString("reg_date");
            String last_connect_date = teacher_json.getString("last_connect_date");

            //선생님 추가 정보
            String CollegeName = teacher_json.getString("CollegeName");
            String lessoncategory = teacher_json.getString("lessoncategory");
            String lessonplace = teacher_json.getString("lessonplace");
            String schedule = teacher_json.getString("schedule");


            Lesson lesson = new Lesson();

            lesson.setLesson_id(lesson_id);
            lesson.setLesson_Title(lessontitle);
            lesson.setLesson_Background_Image(background_img);
            lesson.setStudentlevel(studentlevel);
            lesson.setFee(lessonfee);
            lesson.setLesson_Subject(lessonsb);
            lesson.setIntro(lessonintro);
            lesson.setReg_date(lesson_reg_date);
            lesson.setHits(hits);
            lesson.setRecruiting(recruiting);
            Log.d("student", "push teacher info)" +
                    "\n name: " + name +
                    "\n gender: " + gender +
                    "\n profile_image" + profile_image +
                    "\n address: " + address +
                    "\n LessonFee: " + LessonFee +
                    "\n LessonSubject: " + LessonSubject +
                    "\n deal_possible: " + dealpossible +
                    "\n intro: " + intro +
                    "\n reg_date: " + reg_date +
                    "\n last_connect_date: " + last_connect_date);

            teacher = new Teacher();
            teacher.setName(name);
            teacher.setGender(gender);
            teacher.setAge(age);
            teacher.setProfile_image(profile_image);
            teacher.setAddress(address);
            teacher.setFee(LessonFee);
            teacher.setSubject(LessonSubject);
            teacher.setDealpossible(dealpossible);
            teacher.setIntro(intro);
            teacher.setReg_date(reg_date);
            teacher.setLast_connect_date(last_connect_date);

            teacher.setCollegeName(CollegeName);
            teacher.setLessoncategory(lessoncategory);
            teacher.setLessonPlace(lessonplace);
            teacher.setSchedule(schedule);
            teacher.setLesson(lesson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return teacher;
    }
}
