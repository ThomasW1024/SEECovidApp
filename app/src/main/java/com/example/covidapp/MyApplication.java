package com.example.covidapp;

import android.app.Application;
import android.util.Log;

import androidx.work.Configuration;

public class MyApplication extends Application implements Configuration.Provider {

    @Override
    public void onCreate() {
        super.onCreate();
        //Parse SDK stuff goes here
    }

    @Override
    public Configuration getWorkManagerConfiguration() {
        Configuration myConfig = new Configuration.Builder()
                .setMinimumLoggingLevel(Log.INFO)
                .build();
        return myConfig;
    }
}