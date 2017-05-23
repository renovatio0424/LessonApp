package com.example.kimjungwon.lessonapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by kimjungwon on 2017-04-21.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static String TAG = DBHelper.class.getSimpleName();


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG,"oncreate");

        sqLiteDatabase.execSQL("CREATE TABLE MyInfo (" +
                "id TEXT," +
                "name TEXT," +
                "job TEXT," +
                "token TEXT," +
                "image TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insert(String id, String name, String job, String token, String image) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        String query = "INSERT INTO MyInfo VALUES('" + id + "', '" + name + "', '" + job + "','" + token + "','"+image+"');";
        Log.d(TAG,"insert query: " + query);

        db.execSQL(query);
        db.close();
    }



    public void update(String table, String coulmn, String coulmn_value, String whereClause, String whereArg) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        String query = "UPDATE " + table + " SET " + coulmn+ " = '" + coulmn_value + "' WHERE "+whereClause+" = '" + whereArg + "';";
        Log.d(TAG,"update query: " + query);

        db.execSQL(query);
        db.close();
    }

    public void delete(String table, String coulmn, String coulmn_value) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        String query = "DELETE FROM " + table +" WHERE " + coulmn + "='" + coulmn_value + "';";
        Log.d(TAG,"delete query: " + query);

        db.execSQL(query);
        db.close();
    }

    public String getResult(String table, String coulmn) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = this.getReadableDatabase();
        String result = "";

        String query = "SELECT " + coulmn + " FROM " + table;
        Log.d(TAG,"select query: " + query);

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            result = cursor.getString(0);
        }
        Log.d(TAG,"select result: " + result);
        db.close();
        return result;
    }
}
