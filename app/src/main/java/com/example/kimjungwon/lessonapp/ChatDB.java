package com.example.kimjungwon.lessonapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by kimjungwon on 2017-05-11.
 */

public class ChatDB extends SQLiteOpenHelper {
    String TAG = ChatDB.class.getSimpleName();

    public ChatDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "oncreate");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS ChatDB (" +
                "id INTEGER PRIMARY KEY   AUTOINCREMENT," +
                "protocol TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insert(String protocol) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        String query;

        Log.d(TAG, "insert start");

        // DB에 입력한 값으로 행 추가
        query = "INSERT INTO ChatDB (protocol) VALUES('" + protocol + "')";
        Log.d(TAG, "insert query: " + query);

        db.execSQL(query);
        db.close();
    }

    public ArrayList getAllResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Chat> Chats = new ArrayList<>();

        String query = "SELECT * FROM ChatDB order by id asc";
        Log.d(TAG, "select query: " + query);

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {

            Log.d(TAG, "column name: " + cursor.getColumnName(1));
            int id = cursor.getInt(0);
            String protocol = cursor.getString(1);//protocol
            Chats.add(getChat(protocol));
        }

        db.close();
        return Chats;
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
