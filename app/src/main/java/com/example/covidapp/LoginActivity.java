package com.example.covidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText pinCode2;
    Button loginButton;
    Button create;
    //  private FirebaseAuth firebaseAuth;
    @Override
   public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pinCode2 = (EditText) findViewById(R.id.editTextNumberPassword);
        loginButton=(Button )  findViewById(R.id.button4);
        create=(Button )  findViewById(R.id.textView3);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkPIN();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegActivity.class);
                startActivity(intent);
            }
        });
    }

        public void checkPIN(){
            boolean isValid = true;
            if (isEmpty(pinCode2)){
                pinCode2.setError("Please provide a PIN code");
                isValid=false;
            }
           if (isValid)
           {
               String pin = pinCode2.getText().toString();
               if ( pin.equals(RegActivity.pass))
               {
                   Intent intent = new Intent(this, TracingOn.class);
                   startActivity(intent);
               }
               else
               {

                   pinCode2.getText().clear();
                   pinCode2.setError("Wrong PIN");
               }
           }
        }

       public  boolean isEmpty (EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
        }

        //   ID=(EditText)findViewById(R.id.editTextTextPersonName2);

        // firebaseAuth = FirebaseAuth.getInstance();

       /*FirebaseUser user = firebaseAuth.getCurrentUser();
       if (user != null) {
           finish();
           Intent intent = new Intent(this, NotificationOn.class);
           //startActivity(new Intent(LoginActivity.this, NotificationOn.this));
           startActivity(intent);
       }
       else
           Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();

       }
    }*/

    }

