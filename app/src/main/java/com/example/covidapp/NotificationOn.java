package com.example.covidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.covidapp.service.MyService;

public class NotificationOn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_on);

        final TextView firstTextView = (TextView) findViewById(R.id.textView);
        Button firstButton = (Button) findViewById(R.id.button2);

        firstButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent serviceIntent = new Intent(NotificationOn.this, MyService.class);
                serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");

                ContextCompat.startForegroundService(NotificationOn.this, serviceIntent);

                openMainPage();
            }
        });

        Button secondButton = (Button) findViewById(R.id.button3);
        secondButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent serviceIntent = new Intent(NotificationOn.this, MyService.class);
                stopService(serviceIntent);
                openMainPage();
            }
        });
    }

    public void openDialog() {
        firstDialog firstdialog = new firstDialog();
        firstdialog.show(getSupportFragmentManager(), "first dialog");
    }

    public void openMainPage() {
        Intent intent = new Intent(this, MainPage.class);
        startActivity(intent);
    }
}
