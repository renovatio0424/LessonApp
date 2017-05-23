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
        Intent intent = new Intent(this, activity_main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("notification",true);


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

        String title = message.contains("문의") ? "과외 상담 문의" : "과외 상담 제안";

        //notification 커스텀
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
