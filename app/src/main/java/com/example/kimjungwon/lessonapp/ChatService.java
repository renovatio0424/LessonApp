package com.example.kimjungwon.lessonapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.example.kimjungwon.lessonapp.URLconfig.Chatting_Port;
import static com.example.kimjungwon.lessonapp.URLconfig.Server_IP;

/**
 * Created by kimjungwon on 2017-05-02.
 */

public class ChatService extends Service {

    private static final String TAG = ChatService.class.getSimpleName();

    private ArrayList<Chat> msgs;
    private int CreateRoom = 0,DeleteRoom = 1 , Chatting = 2;

    SendThread send ;
    Socket Oursocket;
    String myname ;

    boolean ActivityShowing = false;
    int consult_id ;

    private IBinder mBinder = new ChatServiceBinder();

    NotificationManager Notifi_M;
    Notification Notifi ;


    public class ChatServiceBinder extends Binder{
        public ChatService getService(){
            Log.d(TAG,"getService");
            return ChatService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind");
        ActivityShowing = true;
        return mBinder;
    }

    public interface ICallback{
        public void receiveMsg(String msg);
    }

    private ICallback mCallback;
    SocketClient client;

    public void registerCallback(ICallback cb){
        mCallback = cb;
    }

    public void sendMsg(String protocol){
        Log.d(TAG,"sendMsg: " + protocol);
        send = new SendThread(Oursocket);
        send.protocol = protocol;
        send.start();
    }

    myServiceHandler handler;

    @Override
    public void onCreate() {
        Log.d(TAG,"onCreate()");
        unregisterRestartAlarm();
        Log.d(TAG,"unregisterRestartAlarm()");
        super.onCreate();

        DBHelper dbHelper = new DBHelper(ChatService.this,"MyInfo.db",null,1);
        myname = dbHelper.getResult("MyInfo","name");

        handler = new myServiceHandler();
        client = new SocketClient(Server_IP,Chatting_Port);
        client.start();

        msgs = new ArrayList<>();
    }

//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        startForeground(this);
////        /**
////         * startForeground 를 사용하면 notification 을 보여주어야 하는데 없애기 위한 코드
////         */
////        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
////        Notification notification;
////
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
////
////            notification = new Notification.Builder(getApplicationContext())
////                    .setContentTitle("")
////                    .setContentText("")
////                    .build();
////
////        } else {
////            notification = new Notification(0, "", System.currentTimeMillis());
////            notification.setLatestEventInfo(getApplicationContext(), "", "", null);
////        }
////
////        nm.notify(startId, notification);
////        nm.cancel(startId);
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    public static void startForeground(Service service) {
//        Log.d(TAG, "startForeground()");
//        if (service != null) {
//            try {
//                Notification notification = getNotification(service);
//                if (notification != null) {
//                    service.startForeground(0, notification);
//
//                    //메인
//                    TimerTask testTask = new TimerTask() {
//                        @Override
//                        public void run() {
//                            Log.d(TAG,"it's running !!!!!!");
//                        }
//                    };
//
//                    Timer timer = new Timer();
//                    timer.schedule(testTask,0,3000);
//                }
//            } catch (Exception e) {
//
//            }
//        }
//    }

//    public static Notification getNotification(Context paramContext) {
//        Log.d(TAG, "getNotification");
//        int smallIcon = R.mipmap.ic_launcher;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            smallIcon = R.mipmap.ic_launcher;
//        }
//        Notification notification = new NotificationCompat.Builder(paramContext)
//                .setSmallIcon(smallIcon)
//                .setPriority(NotificationCompat.PRIORITY_MIN)
//                .setAutoCancel(true)
//                .setWhen(0)
//                .setTicker("").build();
//        notification.flags = 16;
//        return notification;
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

        /**
         * 서비스 종료 시 알람 등록을 통해 서비스 재 실행
         */
        registerRestartAlarm();
        Log.d(TAG,"registerRestartAlarm()");
    }

    /**
     * 알람 매니져에 서비스 등록
     */
    private void registerRestartAlarm() {

        Log.d("000 " + TAG, "registerRestartAlarm");
        Intent intent = new Intent(ChatService.this, ChatReceiver.class);
        intent.setAction("ACTION.RESTART.ChatService");
        PendingIntent sender = PendingIntent.getBroadcast(ChatService.this, 0, intent, 0);

        long firstTime = SystemClock.elapsedRealtime();
        firstTime += 1 * 1000;

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        /**
         * 알람 등록
         */
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 1 * 1000, sender);

    }

    /**
     * 알람 매니져에 서비스 해제
     */
    private void unregisterRestartAlarm() {

        Log.i("000 " +  TAG, "unregisterRestartAlarm");

        Intent intent = new Intent(ChatService.this, ChatReceiver.class);
        intent.setAction("ACTION.RESTART.ChatService");
        PendingIntent sender = PendingIntent.getBroadcast(ChatService.this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        /**
         * 알람 취소
         */
        alarmManager.cancel(sender);
    }

    class SocketClient extends Thread {
        Socket socket;
        boolean threadAlive;
        String ip;
        int port;

        private DataOutputStream output = null;

        public SocketClient(String ip, int port) {
            threadAlive = true;
            this.ip = ip;
            this.port = port;
        }

        @Override
        public void run() {
            try {
                // 연결후 바로 ReceiveThread 시작
                socket = new Socket(ip, port);
                //10초 안에 응답 없으면 종료
//                socket.setSoTimeout(10*1000);
//                socket.setKeepAlive(true);
//                socket.setSoTimeout(10*1000);
                output = new DataOutputStream(socket.getOutputStream());
                Oursocket = socket;

                socket.setKeepAlive(true);
//                socket.setSoTimeout(10*1000);

                Date today = new Date(System.currentTimeMillis());
                SimpleDateFormat time = new SimpleDateFormat("a hh:mm");
                String now = time.format(today);
                output.writeUTF(CreateRoom + "@&" + myname + "@&몰라@&##" + myname + "님이 입장하셨습니다@&" + now + "@&프로필이미지@&" + consult_id);

                Log.d(TAG,"socket is connected ? : " + (socket.isConnected() ? "connected" : "disconnected") );

                ReceiveThread receive = new ReceiveThread(socket);
                receive.start();

            } catch (ConnectException ce){
                Log.d(TAG,"ConnectException !");
                try {
                    Oursocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
//                this.start();

                e.printStackTrace();
            }
        }
    }

    //서버로 부터 받은 메시지를 액티비티로 보냄
    class ReceiveThread extends Thread {
        private Socket socket = null;
        DataInputStream input;

        public ReceiveThread(Socket socket) {
            this.socket = socket;

            try {
                input = new DataInputStream(socket.getInputStream());
            } catch (Exception e) {
            }
        }

        // 메세지 수신후 activity로 전달
        public void run() {
            try {
                String msg = null;
                Log.d(TAG, "receive thread start");
                while (input != null) {
                    msg = input.readUTF();
                    Log.d(TAG, msg);
                    //현재 채팅 액티비티를 보고 있는 경우
                    if(ActivityShowing){
                        mCallback.receiveMsg(msg);
                    }
                    //현재 채팅 액티비티를 안보고 있는 경우 notification을 띄운다
                   else{
                        Log.d(TAG,"notification protocol: " + msg);
                        msgs.add(getChat(msg));
                        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        Message hdmsg = new Message();
                        hdmsg.obj = msg;
                        handler.sendMessage(hdmsg);
                    }
                    Log.d(TAG,"socket is connected ? : " + (socket.isConnected() ? "connected" : "disconnected") );
                }
                Log.d(TAG,"input is null at ReceiveThread method");
            } catch (IOException e) {
                client = new SocketClient(Server_IP,Chatting_Port);
                client.start();
                Log.d(TAG,"socket reconnect! error: " + e );
//                e.printStackTrace();
            }
        }
    }

    class myServiceHandler extends Handler {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(android.os.Message msg) {
            Intent intent = new Intent(ChatService.this, activity_chatting.class);

            String protocol = (String) msg.obj;
            ChatDB chatDB = new ChatDB(ChatService.this,"ChatDB.db",null,1);
            chatDB.insert(protocol);

            String title = getProtocol(protocol)[1];
            String text = getProtocol(protocol)[3];
            String consult_id = getProtocol(protocol)[6];
            ConsultDB consultDB = new ConsultDB(ChatService.this,"Consulting.db",null,1);
            Consult consult = consultDB.getConsult(consult_id);

            Log.d(TAG, "\nselect result: "
                    + "\nid: " + consult.getId()
                    + "\ncategory: " + consult.getCategory()
                    + "\nlessontitle: " + consult.getLessonname()
                    + "\nyourname: " + consult.getYourname()
                    + "\nstate: " + consult.getState()
                    + "\nreason: " + consult.getReason()
                    + "\nconfirmation: " + consult.getConfiramtion());

            intent.putExtra("consult",consult);

            for(int i = 0 ; i < msgs.size() ; i ++){
                Log.d(TAG,"msg| name: " + msgs.get(i).getName());
            }


            PendingIntent pendingIntent = PendingIntent.getActivity(ChatService.this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);

            Notifi = new Notification.Builder(getApplicationContext())
                    .setContentTitle(title)
                    .setContentText(text)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker("알림!!!")
                    .setContentIntent(pendingIntent)
                    .build();

            //소리추가
            Notifi.defaults = Notification.DEFAULT_SOUND;

            //알림 소리를 한번만 내도록
            Notifi.flags = Notification.FLAG_ONLY_ALERT_ONCE;

            //확인하면 자동으로 알림이 제거 되도록
            Notifi.flags = Notification.FLAG_AUTO_CANCEL;


            Notifi_M.notify( 777 , Notifi);
        }
    }

//    public void ShowNotification(String protocol){
//        Intent intent = new Intent(this, activity_chatting.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        String[] message = getProtocol(protocol);
//        int method = Integer.valueOf(message[0]);
//
//        if(method != 3){
//            return;
//        }
//
//        String title = message[1];
//        String text = message[3];
//
//        //notification 커스텀
//        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(title)
//                .setContentText(text)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//    }

    public String[] getProtocol(String protocol){
        String[] result_arr = protocol.split("@&");
        return result_arr;
    }

    //액티비티로 부터 받은 메시지를 서버로 보냄
    class SendThread extends Thread {
        private Socket socket;
        String protocol = "";
        DataOutputStream output;

        public SendThread(Socket socket) {
            this.socket = socket;
            try {
                output = new DataOutputStream(socket.getOutputStream());
            } catch (Exception e) {
            }
        }

        public void run() {
            try {
                Log.d(ACTIVITY_SERVICE, "11111");

                if (output != null) {
                    if (protocol != null) {
//                        socket.setKeepAlive(true);
                        Log.d(TAG, "result_msg: " + protocol);
                        output.writeUTF(protocol);
                    }
                } else{
                    Log.d(TAG, "output is null at Sendthread method");
                }
            } catch (IOException e) {
                client = new SocketClient(Server_IP,Chatting_Port);
                client.start();
                Log.d(TAG,"socket reconnect! error: " + e );
//                e.printStackTrace();
            } catch (NullPointerException npe) {
                client = new SocketClient(Server_IP,Chatting_Port);
                client.start();
                Log.d(TAG,"socket reconnect! error: " + npe );
//                npe.printStackTrace();
            }
        }
    }

    public Chat getChat(String protocol){
        Chat result = new Chat();
        String[] result_arr = protocol.split("@&");
        int method = Integer.valueOf(result_arr[0]);

        switch (method){
            case 0:
                result.ismychat = false;
                result.setName(result_arr[3]);
                break;
            case 1:
                break;
            case 2:
                result.ismychat = false;
                result.setName(result_arr[1]);
                result.setMessage(result_arr[3]);
                result.setTime(result_arr[4]);
                result.setImageURL(result_arr[5]);
                break;
        }

        return result;
    }
}
