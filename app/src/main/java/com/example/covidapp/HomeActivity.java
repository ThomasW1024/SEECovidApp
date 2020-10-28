package com.example.covidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final TextView firstTextView =(TextView) findViewById(R.id.textView);
        Button firstButton = (Button) findViewById(R.id.button1);
        firstButton.setOnClickListener(new View.OnClickListener() {
          public void onClick(View view)
            {
                openLoginPage();
            }
        });

    }

    public void openLoginPage() {
        Intent intent = new Intent(this, RegActivity.class);
        startActivity(intent);
    }
}