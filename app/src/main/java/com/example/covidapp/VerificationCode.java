package com.example.covidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class VerificationCode extends AppCompatActivity {

    EditText VC;
    Button firstButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);
        firstButton = (Button) findViewById(R.id.button1);
        VC = (EditText) findViewById(R.id.editTextNumberPassword3);

        firstButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                checkPIN();
            }
        });
    }


    public void checkPIN(){
        boolean isValid = true;
        if (isEmpty(VC)){
            VC.setError("invalid Verification Code");
            isValid=false;
        }
        if (isValid)
        {
            String code= VC.getText().toString();
            if ( code.equals("1234"))
            {
                openDialog();
            }
            else
            {

                VC.getText().clear();
                VC.setError("Wrong PIN");
            }
        }
    }

    public  boolean isEmpty (EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    public void openDialog() {
        secondDialog seconddialog = new secondDialog();
        seconddialog.show(getSupportFragmentManager(), "first dialog");
    }

}