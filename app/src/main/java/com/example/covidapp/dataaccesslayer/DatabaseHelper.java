package com.example.covidapp.dataaccesslayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, "SecretID.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table SecretIDDetails(secretid TEXT primary key, time TEXT)");
        DB.execSQL("create Table TempIDDetails(tempid TEXT primary key, time TEXT)");

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

    public Cursor getdata(){
        SQLiteDatabase DB =  this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from SecretIDDetails", null);
        return cursor;
    }

    public Boolean inserttempiddata(String tempid, String time){
        SQLiteDatabase DB =  this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
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

    public Cursor getTempdata(){
        SQLiteDatabase DB =  this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from TempIDDetails", null);
        return cursor;
    }

}