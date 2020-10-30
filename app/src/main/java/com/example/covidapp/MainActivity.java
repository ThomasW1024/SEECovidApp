package com.example.covidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.covidapp.dataaccesslayer.DatabaseHelper;
import com.example.covidapp.httprequest.HttpRequestFactory;
import com.example.covidapp.httprequest.KeyTimePair;
import com.example.covidapp.service.BluetoothBackgroundService;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT=5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }

}