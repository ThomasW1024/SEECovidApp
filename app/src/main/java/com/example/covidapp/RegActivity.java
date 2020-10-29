package com.example.covidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegActivity extends AppCompatActivity {

    public static EditText pinCode;
 //   private EditText userID;
    public Button regButton;
    public static  String pass;
    SharedPreferences sharedPref;
  //  private FirebaseAuth firebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        if(havePass()){
            changePage();
        }
        pinCode = (EditText) findViewById(R.id.editTextNumberPassword2);
        //   userID = (EditText) findViewById(R.id.editTextTextPersonName);
        regButton = (Button) findViewById(R.id.button);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass= pinCode.getText().toString();
                storePass(pass);
                checkData();
            }
        });
    }

    private boolean havePass() {
        pass = sharedPref.getString(getString(R.string.pass_code), null);
        return  pass != null;
    }

    private void storePass(String pass){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.pass_code), pass);
        editor.apply();
    }

     boolean isEmpty ( EditText text)
     {
         CharSequence str = text.getText().toString();
         return TextUtils.isEmpty(str);
     }
     private void changePage(){
         Intent intent = new Intent(this, LoginActivity.class);
         startActivity(intent);
     }
    void checkData(){
        if ( isEmpty(pinCode))
        {
            pinCode.setError("PIN code is required");
        }
        else
        {
            changePage();
        }
        //  firebase = FirebaseAuth.getInstance();

    /*    regButton.setOnClickListener(new View.OnClickListener() {
            public void onClick ( View view)
            {
                if (validatePIN())
                {
                    String userPIN= pinCode.getText().toString().trim();
                    String userID= pinCode.getText().toString().trim();

                    firebase.createUserWithEmailAndPassword(userID,userPIN).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegActivity.this, "Registeration Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent( RegActivity.this, LoginActivity.class));
                            }
                            else   Toast.makeText(RegActivity.this, "Registeration unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    });
                   // firebase.signInWithCustomToken(userPIN).addOnCompleteListener(new );
                }
            }
        });*/
    }
/*
    private Boolean validatePIN(){
        Boolean result = false;
        String PIN = pinCode.getText().toString();
        String ID = userID.getText().toString();

        if ( PIN.isEmpty())
        {
            Toast.makeText(this, "Please enter a 4-digits code", Toast.LENGTH_SHORT).show();
        }
        else result = true;

        return result;
    }*/

}