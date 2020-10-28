package com.example.covidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainPage extends AppCompatActivity {

    Button ButtonVer;
    Button firstButtonCheck;
    Button firstButtonLearn;
    Button firstButtonHelp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);


        ButtonVer = (Button) findViewById(R.id.B2);
        firstButtonCheck= (Button) findViewById(R.id.B1);
        firstButtonLearn = (Button) findViewById(R.id.B4);
        firstButtonHelp = (Button) findViewById(R.id.B3);

        firstButtonCheck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, Exposures.class);
                startActivity(intent);
            }
        });

        ButtonVer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, VerificationCode.class);
                startActivity(intent);
            }
        });

        firstButtonHelp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

            }
        });

        firstButtonLearn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            }
        });
    }



}