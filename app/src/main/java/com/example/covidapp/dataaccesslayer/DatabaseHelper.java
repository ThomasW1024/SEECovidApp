package com.example.covidapp.dataaccesslayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.covidapp.constant.AppConstant;
import com.example.covidapp.httprequest.KeyTimePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper instance;

    private DatabaseHelper(Context context) {
        super(context, "SecretID.db", null, 1);
    }

    public static synchronized DatabaseHelper getInstance(Context ctx) {
        if (instance == null)
            instance = new DatabaseHelper(ctx);
        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table IF NOT EXISTS SecretIDDetails(secretid TEXT primary key, time TEXT)");
        DB.execSQL("create Table IF NOT EXISTS TempIDDetails(tempid TEXT primary key, time TEXT)");

        Log.e("Hello", "msg");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL("drop Table if exists SecretIDDetails");
        DB.execSQL("drop Table if exists TempIDDetails");
    }

    public Boolean insertiddata(String secretId, String time){
        SQLiteDatabase DB =  this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("secretid", secretId);
        contentValues.put("time", time);

        long result = DB.insert("SecretIDDetails", null, contentValues);
        if(result ==1){
            return false;
        }
        else{
            return true;
        }
    }

    public List<KeyTimePair> getdata(){
        SQLiteDatabase DB =  this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from SecretIDDetails", null);
        List<KeyTimePair> list = new ArrayList<>();
        while(cursor.moveToNext()){
            list.add(new KeyTimePair()
                    .putSecret(cursor.getString(0))
                    .putTime(cursor.getString(1))
            );
        }
        cursor.close();
        return list;
    }

    public Boolean inserttempiddata(String tempid){
        SQLiteDatabase DB =  this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String time = new SimpleDateFormat(AppConstant.DATE_FORMAT).format(Calendar.getInstance().getTime());
        contentValues.put("tempid", tempid);
        contentValues.put("time", time);
        long result = DB.insert("TempIDDetails", null, contentValues);
        if(result ==1){
            return false;
        }
        else{
            return true;
        }
    }

    public List<KeyTimePair> getTempdata(){
        SQLiteDatabase DB =  this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from TempIDDetails", null);
        List<KeyTimePair> list = new ArrayList<>();
        while(cursor.moveToNext()){
            list.add(new KeyTimePair()
                    .putSecret(cursor.getString(0))
                    .putTime(cursor.getString(1))
            );
        }
        cursor.close();
        return list;
    }

}