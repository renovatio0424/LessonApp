package com.example.kimjungwon.lessonapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by kimjungwon on 2017-05-02.
 */

public class ChatReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("000 ChatReceiver" , "ChatReceiver called : " + intent.getAction());

        /**
         * 서비스 죽일때 알람으로 다시 서비스 등록
         */
        if(intent.getAction().equals("ACTION.RESTART.ChatService")){

            Log.d("000 ChatReceiver" ,"ACTION.RESTART.ChatService" );

            Intent i = new Intent(context,ChatService.class);
            context.startService(i);
        }

        /**
         * 폰 재시작 할때 서비스 등록
         */
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){

            Log.d("ChatReceiver" , "ACTION_BOOT_COMPLETED" );
            Intent i = new Intent(context,ChatService.class);
            context.startService(i);

        }


    }
}
