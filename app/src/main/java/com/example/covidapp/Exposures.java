package com.example.covidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.covidapp.ephId.EphemeralGenerator;
import com.example.covidapp.httprequest.HttpRequestFactory;
import com.example.covidapp.httprequest.KeyTimePair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Collections;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Exposures extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exposures);

       final  TextView status;
        Button firstButton = (Button) findViewById(R.id.button5);
        Boolean exposed = false;
        status = (TextView) findViewById(R.id.editTextTextPersonName2);
       final EphemeralGenerator localList = new EphemeralGenerator();
       final HttpRequestFactory secondList= new  HttpRequestFactory();//////////////
        firstButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                isExposed(status, localList, secondList);
            }
        });
    }

        public void isExposed (TextView status, EphemeralGenerator localList, HttpRequestFactory secondList) throws ParseException {
            boolean exposed = false;
            List<KeyTimePair> L1= secondList.downloadKeys();//string,long
            List<String> L2= localList.getList();/// string,string

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            for (int i=0; i<L1.size();i++) {
                Date d = sdf.parse(L2.get(i));
            }
          /*  try {
                Date d = sdf.parse("20130526160000");
            } catch (ParseException ex) {
                Log.v("Exception", ex.getLocalizedMessage());
            }*/

//           List<String> l = secondList.downloadKeys();
//           for ( int i=0;i<l.size();i++) {
//               System.out.println("Printing list elements " + l.get(i));
//           }
          //  localList.getNthID();

            // List from keysHTTP

            /*
            exposed= !Collections.disjoint(sharedList, personalList);
            if (exposed) {
                status.setText("Status: Positive \n  Please visit the nearest hospital \n and get yourself tested" );
            } else {
                status.setText("Status: Negative \n  You have not contacted any infected person " );
            }*/

        }
    }
