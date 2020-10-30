package com.example.covidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.covidapp.constant.AppConstant;
import com.example.covidapp.dataaccesslayer.DatabaseHelper;
import com.example.covidapp.ephId.EphemeralGenerator;
import com.example.covidapp.httprequest.HttpRequestFactory;
import com.example.covidapp.httprequest.KeyTimePair;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Collections;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Exposures extends AppCompatActivity {

    private TextView statusView;

    private com.android.volley.Response.Listener<JSONArray> onResponse = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            Log.e("Response", "success");
            List<KeyTimePair> list = new ArrayList<>();
            for (int i = 0; i < response.length(); i += 1) {
                try {
                    KeyTimePair pair = new KeyTimePair(response.getJSONObject(i));
                    list.add(pair);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            try {
                isExposed(list);
            } catch (ParseException e) {
                Log.e("Exposures", String.valueOf(e.getStackTrace()));
            }
        }
    };

    private
    com.android.volley.Response.ErrorListener onError = new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exposures);

        statusView = (TextView) findViewById(R.id.editTextTextPersonName2);

        Button firstButton = (Button) findViewById(R.id.checkExposure);
        firstButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                HttpRequestFactory.getInstance(getApplicationContext()).downloadKeys(onResponse, onError);
            }
        });
    }

        public void isExposed (List<KeyTimePair> secretFromTheServer) throws ParseException {
            boolean exposed = false;
            // recompute the secret key from server's secret.
            List<String> TempIDFromServer = new ArrayList<>();
            for(int i = 0; i < secretFromTheServer.size(); i+=1){
                KeyTimePair item = secretFromTheServer.get(i);
                Date d =  new SimpleDateFormat(AppConstant.DATE_FORMAT).parse(item.getTime());
                List<String> tempIDs = EphemeralGenerator.getIDs(item.getSecret(), d.getTime());
                for(int j = 0; j < tempIDs.size(); j+=1){
                    String id = tempIDs.get(j);
                    id = id.replaceAll("-","");
                    id = id.substring(id.length() -27, id.length());
                    TempIDFromServer.add(id);
                }
            }
            // get all the tempID stored in Android
            List<String> contactedTempID= DatabaseHelper.getInstance(getApplicationContext()).getTempdata();

            // compare
            exposed = Collections.disjoint(TempIDFromServer, contactedTempID);
            if(exposed){
                statusView.setText("Status: Positive");
            }else {
                statusView.setText("Status: Negative");
            }
        }
    }
