package com.example.kimjungwon.lessonapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by kimjungwon on 2017-04-25.
 */

public class ConsultDB extends SQLiteOpenHelper {
    String TAG = ConsultDB.class.getSimpleName();

    public ConsultDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "oncreate");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS ConsultDB (" +
                "id INTEGER," +
                "category TEXT," +
                "lessontitle TEXT," +
                "yourname TEXT," +
                "state TEXT," +
                "reason TEXT," +
                "confirmation INTEGER);");

//        sqLiteDatabase.execSQL("INSERT INTO ConsultDB VALUES('','','','','','','');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insert(Consult consult) {
        int id = consult.getId();
        String category = consult.getCategory();
        String LessonTitle = consult.getLessonname();
        String yourname = consult.getYourname();
        String State = consult.getState();
        String Reason = consult.getReason();
        int confirmation = consult.getConfiramtion();

        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        String query;

        Log.d(TAG, "insert start");

        //중복일 경우
        if (!isDuplicated(id)) {
            // DB에 입력한 값으로 행 추가
            query = "INSERT INTO ConsultDB VALUES(" + id + "," +
                    "'" + category + "'," +
                    "'" + LessonTitle + "'," +
                    "'" + yourname + "'," +
                    "'" + State + "'," +
                    "'" + Reason + "'," +
                    "" + confirmation + ");";
            Log.d(TAG, "insert query: " + query);
        }
        //중복 데이터가 없을 경우
        else {
            query = "UPDATE ConsultDB SET " +
                    "category = '" + category + "', " +
                    "lessontitle = '" + LessonTitle + "', " +
                    "yourname = '" + yourname + "', " +
                    "state = '" + State + "', " +
                    "reason = '" + Reason + "', " +
                    "confirmation = " + confirmation + " " +
                    "WHERE id = " + id +";";
            Log.d(TAG, "update query: " + query);
        }

        db.execSQL(query);
        db.close();
    }


    public void update(String table, String coulmn, String coulmn_value, String whereClause, String whereArg) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        String query = "UPDATE " + table + " SET " + coulmn + " = '" + coulmn_value + "' WHERE " + whereClause + " = '" + whereArg + "';";
        Log.d(TAG, "update query: " + query);

        db.execSQL(query);
        db.close();
    }

    public void delete(String coulmn, String coulmn_value) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        String query = "DELETE FROM ConsultDB ";
        Log.d(TAG, "delete query: " + query);

        db.execSQL(query);
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();

        String query = "DELETE FROM ConsultDB";
        Log.d(TAG, "delete all");

        db.execSQL(query);
        db.close();

    }

    public Boolean isDuplicated(int id) {
        Boolean result = false;

        SQLiteDatabase db = this.getReadableDatabase();



        String query = "SELECT * FROM ConsultDB WHERE id = " + id;
        Cursor cursor = db.rawQuery(query, null);
        Log.d(TAG, "duplicate count = " + cursor.getCount());


        while(cursor.moveToNext()){
            Log.d(TAG,"cursor.getInt(0)= " + cursor.getInt(0));

            if (cursor.getInt(0) == id) {
                result = true;
            }
        }

        Log.d(TAG, "isDuplicated: " + result);
        return result;
    }

    public Boolean hasNewData(String table) {
        SQLiteDatabase db = this.getReadableDatabase();

        //확인안한 문의 찾기
        String query = "SELECT * FROM ConsultDB WHERE confirmation = 0";
        Cursor cursor = db.rawQuery(query, null);

        Boolean result = false;

        //확인 안한 문의가 있다면?? TRUE 없다면 FALSE
        Log.d(TAG, "new data count = " + cursor.getCount());
        int count = 0 ;

        while(cursor.moveToNext()){
            Log.d(TAG,"cursor.getInt(6)= " + cursor.getInt(6));
            if (cursor.getInt(6) == 0) {
                count++;
            }
        }

        if(count > 0){
            result = true;
        }else{
            result = false;
        }

        db.close();
        return result;
    }

    public ArrayList getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Consult> consults = new ArrayList<>();

        String query = "SELECT * FROM ConsultDB";
        Log.d(TAG, "select query: " + query);

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {

            Log.d(TAG,"column name: " + cursor.getColumnName(1));
            int id = cursor.getInt(0);
            String category = cursor.getString(1);//category
            String lessontitle = cursor.getString(2);//LessonTitle
            String yourname = cursor.getString(3);//student_id
            String State = cursor.getString(4);//State
            String Reason = cursor.getString(5);//Reason
            int confirmation = cursor.getInt(6);//confirmation

            consults.add(new Consult(id, lessontitle, category, yourname, Reason, State, confirmation));
        }

        for (int i = 0; i < consults.size(); i++) {
            Log.d(TAG, "\nselect result: "
                    + "\nid: " + consults.get(i).getId()
                    + "\ncategory: " + consults.get(i).getCategory()
                    + "\nlessontitle: " + consults.get(i).getLessonname()
                    + "\nyourname: " + consults.get(i).getYourname()
                    + "\nstate: " + consults.get(i).getState()
                    + "\nreason: " + consults.get(i).getReason()
                    + "\nconfirmation: " + consults.get(i).getConfiramtion());
        }
        db.close();
        return consults;
    }

    public Consult getConsult(String consult_id){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM ConsultDB WHERE id = " + consult_id + ";";

        Log.d(TAG,query);

        Cursor cursor = db.rawQuery(query, null);
        Consult consult = null;
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String category = cursor.getString(1);//category
            String lessontitle = cursor.getString(2);//LessonTitle
            String yourname = cursor.getString(3);//student_id
            String State = cursor.getString(4);//State
            String Reason = cursor.getString(5);//Reason
            int confirmation = cursor.getInt(6);//confirmation

            consult = new Consult(id, lessontitle, category, yourname, Reason, State, confirmation);
        }

        Log.d(TAG, "\nselect result: "
                + "\nid: " + consult.getId()
                + "\ncategory: " + consult.getCategory()
                + "\nlessontitle: " + consult.getLessonname()
                + "\nyourname: " + consult.getYourname()
                + "\nstate: " + consult.getState()
                + "\nreason: " + consult.getReason()
                + "\nconfirmation: " + consult.getConfiramtion());
        db.close();

        return consult;
    }
}
