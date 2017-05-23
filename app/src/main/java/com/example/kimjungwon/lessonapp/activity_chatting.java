package com.example.kimjungwon.lessonapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.sql.Array;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.example.kimjungwon.lessonapp.URLconfig.Chatting_Port;
import static com.example.kimjungwon.lessonapp.URLconfig.Server_IP;
import static com.example.kimjungwon.lessonapp.URLconfig.UpdateConsult_URL;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.kimjungwon.lessonapp.ChatService.ChatServiceBinder;

/**
 * Created by kimjungwon on 2017-04-22.
 */

public class activity_chatting extends AppCompatActivity {
    private static final String TAG = activity_chatting.class.getSimpleName();
    Consult consult;
    String id, token, name, job;
    LinearLayout linearLayout;
    Button yes_btn, no_btn, success_btn;

    //상담
    private static int update_confirmation  = 0;
    private static int update_agree         = 1;
    private static int update_disagree      = 2;
    private static int update_success       = 3;

    //채팅
    private static int CreateRoom   = 0;
    private static int DeleteRoom   = 1;
    private static int Chatting     = 2;

    String myid, imageURL;
    String yourid;

    Button Button_send;
    Handler msghandler;
    EditText editText_massage;

    RecyclerView chatview;
    RecyclerAdapter_chat recyclerAdapter_chat;
    ArrayList<Chat> messages;

    ChatDB chatDB;

    private ChatService chatService;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected");
            ChatServiceBinder binder = (ChatServiceBinder) iBinder;
            chatService = binder.getService();
            chatService.registerCallback(mCallback);

            chatService.consult_id = consult.getId();
            if (chatService != null) {
                Log.d(TAG, "chat service is successfully connected");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "onServiceDisconnected");
//                chatService = null;
        }
    };
    final ChatService.ICallback mCallback = new ChatService.ICallback() {
        @Override
        public void receiveMsg(String msg) {
            //데이터를 받아서 새로고침한다
            Chat chat = getChat(msg);
            messages.add(chat);
            Message hdmsg = msghandler.obtainMessage();
            hdmsg.what = 1111;
            msghandler.sendMessage(hdmsg);
        }
    };

    ChatReceiver chatReceiver;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chatting);

        linearLayout = (LinearLayout) findViewById(R.id.btn_layout);

        yes_btn = (Button) findViewById(R.id.yes_btn);
        no_btn = (Button) findViewById(R.id.no_btn);
        success_btn = (Button) findViewById(R.id.success_btn);

        //디비에 저장
        chatDB = new ChatDB(activity_chatting.this, "ChatDB.db", null, 1);

//        yes_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
//
//        no_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new MaterialDialog.Builder(activity_chatting.this)
//                        .title("title")
//                        .items(R.array.amp_subsection_array)
//                        .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
//                            @Override
//                            public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
//                                /**
//                                 * If you use alwaysCallMultiChoiceCallback(), which is discussed below,
//                                 * returning false here won't allow the newly selected check box to actually be selected
//                                 * (or the newly unselected check box to be unchecked).
//                                 * See the limited multi choice dialog example in the sample project for details.
//                                 **/
//                                return true;
//                            }
//                        })
//                        .inputType(InputType.TYPE_CLASS_TEXT)
//                        .input("hint","",null)
//                        .positiveText("choose")
//                        .show();
//
//                new MaterialDialog.Builder(activity_chatting.this)
//                        .iconRes(R.mipmap.ic_launcher)
//                        .limitIconToDefaultSize()
//                        .title("title")
//                        .positiveText("agree")
//                        .negativeText("disagree")
//                        .onAny(new MaterialDialog.SingleButtonCallback() {
//                            @Override
//                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                Toast.makeText(activity_chatting.this, "Prompt checked? " + dialog.isPromptCheckBoxChecked(), Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .checkBoxPromptRes(R.string.dont_ask_again, false, null)
//                        .show();
//            }
//        });


        //채팅 코드
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
//        //현재 액티비티를 보고 있음
//        chatService.ActivityShowing = true;

        consult = (Consult) getIntent().getSerializableExtra("consult");

        if (consult == null) {
            Log.d(TAG, "consult is null!!");
        }

        DBHelper dbHelper = new DBHelper(this, "MyInfo.db", null, 1);

        id = dbHelper.getResult("MyInfo", "id");
        token = dbHelper.getResult("MyInfo", "token");
        name = dbHelper.getResult("MyInfo", "name");
        job = dbHelper.getResult("MyInfo", "job");
        imageURL = dbHelper.getResult("MyInfo", "image");


        Log.d(TAG, "\nselect result: "
                + "\nid: " + consult.getId()
                + "\ncategory: " + consult.getCategory()
                + "\nlessontitle: " + consult.getLessonname()
                + "\nyourname: " + consult.getYourname()
                + "\nstate: " + consult.getState()
                + "\nreason: " + consult.getReason()
                + "\nconfirmation: " + consult.getConfiramtion());

        yourid = consult.getYourname();

//        if (consult.getCategory().equals("문의") && job.equals("student") |
//                consult.getCategory().equals("제안") && job.equals("teacher") |
//                consult.getState().equals("과외 완료")) {
//            linearLayout.setVisibility(View.GONE);
//        }

        if (consult.getState().equals("상담중")) {

        } else if (consult.getState().equals("과외중")) {
            yes_btn.setVisibility(View.GONE);
            no_btn.setVisibility(View.GONE);

            //선생님일 경우에만 과외 완료 버튼을 보여준다
            if(job.equals("teacher")){
                success_btn.setVisibility(View.VISIBLE);
            }
        } else if (consult.getState().equals("과외 완료")) {
            linearLayout.setVisibility(View.GONE);
        }

        //상담 확인 업데이트
        final ConsultDB consultDB = new ConsultDB(activity_chatting.this, "Consulting.db", null, 1);
        consult.setConfiramtion(1);
        consultDB.insert(consult);

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("case",update_confirmation);
            jsonObject.put("id", consult.getId());
            jsonObject.put("state", consult.getState());
            jsonObject.put("reason", consult.getReason());
            jsonObject.put("confirmation", 1);

            String before = jsonObject.toString();

            new UpdateConsultTask().execute(before);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(activity_chatting.this)
                        .title("과외를 수락하시겠습니까?")
                        .content("확인을 누르시면 과외가 진행됩니다")
                        .positiveText("확인")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                consult.setState("수락 대기중");
                                consultDB.insert(consult);

                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("case",update_agree);
                                    jsonObject.put("id", consult.getId());
                                    jsonObject.put("state", consult.getState());
                                    jsonObject.put("confirmation", consult.getConfiramtion());
                                    jsonObject.put("user_id",id);
                                    jsonObject.put("job",job);
                                    String before = jsonObject.toString();

                                    new UpdateConsultTask().execute(before);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                yes_btn.setVisibility(View.GONE);
                                no_btn.setVisibility(View.GONE);
                                if(job.equals("teacher"))
                                    success_btn.setVisibility(View.VISIBLE);
                            }
                        })
                        .negativeText("취소")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Boolean wrapInScrollView = true;
                final MaterialDialog materialDialog = new MaterialDialog.Builder(activity_chatting.this)
                        .title("거절 사유")
                        .customView(R.layout.dialog_reason, wrapInScrollView)
                        .positiveText("확인")
                        .negativeText("취소")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.reason_group);
                                //체크한 라디오 버튼
                                RadioButton radioButton = (RadioButton) dialog.findViewById(radioGroup.getCheckedRadioButtonId());
                                EditText reason_et = (EditText) dialog.findViewById(R.id.reasion_editText);

                                String reason;

                                //기타 선택시
                                if(radioButton.getId() == R.id.reason_box4){
                                    //edittext에서 문자열 가져오기
                                    reason = reason_et.getText().toString();
                                }else{
                                    //radiobutton에서 문자열 가져오기
                                    reason = radioButton.getText().toString();
                                }

                                //디비에 사유 업데이트
                                consult.setReason(reason);
                                Log.d(TAG,"reason" + reason);
                                consult.setState("과외 거절");
                                consultDB.insert(consult);
                                JSONObject jsonObject = new JSONObject();


                                try {
                                    jsonObject.put("case",update_disagree);
                                    jsonObject.put("id", consult.getId());
                                    jsonObject.put("state", consult.getState());
                                    jsonObject.put("reason", consult.getReason());
                                    jsonObject.put("confirmation", consult.getConfiramtion());
                                    jsonObject.put("job",job);

                                    String before = jsonObject.toString();

                                    new UpdateConsultTask().execute(before);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.d(TAG,"update reason: " + consultDB.getConsult(String.valueOf(consult.getId())).getReason());
                                //서버에 사유 업데이트
                            }
                        })
                        .show();

                View dialogview = materialDialog.getCustomView();
                RadioButton reason_box4 = (RadioButton) dialogview.findViewById(R.id.reason_box4);
                final EditText reason_et = (EditText) dialogview.findViewById(R.id.reasion_editText);

                //기타를 입력했을 경우 입력창 보이기 or 안보이기
                reason_box4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b){
                            reason_et.setVisibility(View.VISIBLE);
                        }else{
                            reason_et.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        success_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(activity_chatting.this)
                        .title("과외가 끝나셨습니까?")
                        .content("과외가 완료 될 경우\n학생에게 후기작성 권한이 주어집니다")
                        .positiveText("확인")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                Toast.makeText(activity_chatting.this, "확인!", Toast.LENGTH_SHORT).show();
                                consult.setState("과외 완료");
                                consultDB.insert(consult);
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("case",update_success);
                                    jsonObject.put("id",consult.getId());
                                    jsonObject.put("state",consult.getState());
                                    jsonObject.put("job",job);

                                    String before = jsonObject.toString();
                                    new UpdateConsultTask().execute(before);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                success_btn.setVisibility(View.GONE);
                            }
                        })
                        .negativeText("취소")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                Toast.makeText(activity_chatting.this, "취소!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });


        chatview = (RecyclerView) findViewById(R.id.chatting_view);

        messages = new ArrayList<>();

        //디비에 있는 채팅 내용 꺼내기
        messages = chatDB.getAllResult();

        Log.d(TAG, "get ChatDB");
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getName().equals(name)) {
                Log.d(TAG, "chat name: " + messages.get(i).getName());
                messages.get(i).ismychat = true;
            }
        }

        recyclerAdapter_chat = new RecyclerAdapter_chat(getApplicationContext(),
                messages, R.layout.item_message);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        chatview.setLayoutManager(linearLayoutManager);
        chatview.setAdapter(recyclerAdapter_chat);
        chatview.scrollToPosition(messages.size() - 1);

        myid = name;
//        client = new SocketClient(Server_IP, Chatting_Port);
//        client.from = myid;
//        client.to = yourid;
//        client.start();
        editText_massage = (EditText) findViewById(R.id.editText_massage);

        Button_send = (Button) findViewById(R.id.Button_send);

        Button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText_massage.getText().toString() != null) {
                    //내가 보낸 메시지를 서비스로 보내기
                    String msg = editText_massage.getText().toString();
                    String protocol = setProtocol(Chatting, myid, yourid, msg, imageURL);

                    //디비에 저장
                    chatDB = new ChatDB(activity_chatting.this, "ChatDB.db", null, 1);
                    chatDB.insert(protocol);

                    //서비스로 메시지 보내기
                    chatService.sendMsg(protocol);

                    //내가 보낸 메시지를 리사이클러뷰에 띄우기
                    Chat mychat = new Chat();
                    mychat.ismychat = true;
                    mychat.setMessage(msg);

                    Date today = new Date(System.currentTimeMillis());
                    SimpleDateFormat time = new SimpleDateFormat("a hh:mm");
                    String now = time.format(today);
                    mychat.setTime(now);
                    messages.add(mychat);

                    recyclerAdapter_chat.notifyDataSetChanged();
                    //최하단으로 바꾸기
                    chatview.scrollToPosition(messages.size() - 1);

                    editText_massage.setText("");
                }
            }
        });

        msghandler = new Handler() {
            @Override
            public void handleMessage(Message hdmsg) {
                if (hdmsg.what == 1111) {
                    recyclerAdapter_chat.notifyDataSetChanged();
                    chatview.scrollToPosition(messages.size() - 1);
                }
            }
        };

        Intent intent = new Intent(activity_chatting.this, ChatService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        chatReceiver = new ChatReceiver();
        IntentFilter intentFilter = new IntentFilter("com.example.kimjungwon.lessonapp.ChatService");
        registerReceiver(chatReceiver, intentFilter);
    }

    private class UpdateConsultTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String before = strings[0];

            Log.d(TAG, "before: " + before);
            PHPRequest phpRequest = null;
            String result = null;
            try {
                phpRequest = new PHPRequest(UpdateConsult_URL);
                result = phpRequest.POSTJSON(before);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "after: " + result);

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject rjo = null;
            if(result != null){
                Toast.makeText(activity_chatting.this, result, Toast.LENGTH_SHORT).show();
            }
            Log.d(TAG, "Consult Update result: " + result);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
//        unregisterReceiver(chatReceiver);
//        Log.d(TAG,"unregisterReceiver()");
//        IntentFilter intentFilter = new IntentFilter("com.example.kimjungwon.lessonapp.ChatService");
//        registerReceiver(chatReceiver,intentFilter);
//        Log.d(TAG,"registerReceiver()");
        chatService.ActivityShowing = false;

        unregisterReceiver(chatReceiver);
        Log.d(TAG, "unregisterReceiver()");
        unbindService(connection);
        Log.d(TAG, "unbindService()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    //    class SocketClient extends Thread {
//        boolean threadAlive;
//        String ip;
//        int port;
//
//        String from, to, image;
//
//        private DataOutputStream output = null;
//
//        public SocketClient(String ip, int port) {
//            threadAlive = true;
//            this.ip = ip;
//            this.port = port;
//        }
//
//        @Override
//        public void run() {
//
//            try {
//                // 연결후 바로 ReceiveThread 시작
//                socket = new Socket(ip, port);
//                //inputStream = socket.getInputStream();
//                output = new DataOutputStream(socket.getOutputStream());
//                //내가 누구인지 보내기
//                output.writeUTF(setProtocol(CreateRoom, from, to, null, null));
//
//                receive = new ReceiveThread(socket);
//                receive.start();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    class ReceiveThread extends Thread {
//        private Socket socket = null;
//        DataInputStream input;
//
//        public ReceiveThread(Socket socket) {
//            this.socket = socket;
//            try {
//                input = new DataInputStream(socket.getInputStream());
//            } catch (Exception e) {
//            }
//        }
//
//        // 메세지 수신후 Handler로 전달
//        public void run() {
//            try {
//                String msg = null;
//                Log.d(TAG, "receive thread start");
//                while (input != null) {
//                    msg = input.readUTF();
//                    Log.d(TAG, msg);
//
//                    //핸들러가 refresh하도록 하기
//                    Message hdmsg = msghandler.obtainMessage();
//                    hdmsg.what = 1111;
//                    hdmsg.obj = msg;
//                    msghandler.sendMessage(hdmsg);
//                    messages.add(getChat(msg));
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    class SendThread extends Thread {
//        private Socket socket;
//        String sendmsg = editText_massage.getText().toString();
//        DataOutputStream output;
//
//        String from, to, imageURL;
//
//        public SendThread(Socket socket) {
//            this.socket = socket;
//            try {
//                output = new DataOutputStream(socket.getOutputStream());
//            } catch (Exception e) {
//            }
//        }
//
//        public void run() {
//            try {
//                Log.d(ACTIVITY_SERVICE, "11111");
//
//                String result_msg = setProtocol(Chatting, from, to, sendmsg,imageURL);
//
//                //내가 보낸 메시지
//                Chat mychat = new Chat();
//                mychat.ismychat = true;
//                mychat.setMessage(sendmsg);
//                Date today = new Date(System.currentTimeMillis());
//                SimpleDateFormat time = new SimpleDateFormat("a hh:mm");
//                String now = time.format(today);
//                mychat.setTime(now);
//
//                messages.add(mychat);
//                if (output != null) {
//                    if (sendmsg != null) {
//                        Log.d(TAG, "result_msg: " + result_msg);
//                        output.writeUTF(result_msg);
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (NullPointerException npe) {
//                npe.printStackTrace();
//            }
//        }
//    }

    public String setProtocol(int Method, String from, String to, String Message, String MyImageURL) {
        String result = "";
        Date today = new Date(System.currentTimeMillis());
        SimpleDateFormat time = new SimpleDateFormat("a hh:mm");
        String now = time.format(today);

        switch (Method) {
            //create
            case 0:
                result += CreateRoom + "@&" + from + "@&@&##" + from + "님이 입장하셨습니다@&@&@&" + consult.getId();
                break;
            //delete
            case 1:
                result += DeleteRoom + "@&" + from + "@&" + to + "@&" + "noMessage@&" + now + "@&" + MyImageURL + "@&" + consult.getId();
                break;
            //chatting
            case 2:
                result += Chatting + "@&" + from + "@&" + to + "@&" + Message + "@&" + now + "@&" + MyImageURL + "@&" + consult.getId();
                break;
        }
        return result;
    }

    public String getProtocol(String protocol) {
        String result = "";
        String[] result_arr = protocol.split("@&");
        int method = Integer.valueOf(result_arr[0]);

        switch (method) {
            //create
            case 0:
                result += result_arr[3];
                Log.d("getProtocol", "msg: " + result);
                break;
            //delete
            case 1:
                result += result_arr[3];
                break;
            //chatting
            case 2:
                result += result_arr[1] + ": " + result_arr[3] + "(" + result_arr[4] + ")";
                break;
        }
        return result;
    }

    public Chat getChat(String protocol) {
        Chat result = new Chat();
        String[] result_arr = protocol.split("@&");
        int method = Integer.valueOf(result_arr[0]);

        switch (method) {
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
