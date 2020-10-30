package com.example.covidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.covidapp.dataaccesslayer.DatabaseHelper;
import com.example.covidapp.httprequest.HttpRequestFactory;
import com.example.covidapp.httprequest.KeyTimePair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class VerificationCode extends AppCompatActivity {

    EditText VC;
    Button firstButton;


    private Response.Listener<String> onResponse = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            boolean isSuccuss = false;
            if (response.toString().contains("200")) {
                isSuccuss = true;
            }
            if(isSuccuss){
                openSuccessDialog();
            }else {
                openFailDialog();
            }
        }
    };

    private Response.ErrorListener onError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            openFailDialog();
            Log.e(VerificationCode.class.getName(), "fail to upload");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);
        firstButton = (Button) findViewById(R.id.button1);
        VC = (EditText) findViewById(R.id.editTextNumberPassword3);

        firstButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //checkPIN();
                // get secrets from db
                List<KeyTimePair> pairs = DatabaseHelper.getInstance(getApplication()).getdata();

                JSONArray list = new JSONArray();
                for(int i =0 ; i < pairs.size(); i+=1){
                    JSONObject o = new JSONObject();
                    try {
                        o.put("secret", pairs.get(i).getSecret());
                        o.put("time", pairs.get(i).getTime());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    list.put(o);
                }

                JSONObject body= new JSONObject();
                try {
                    body.put("secrets", list);
                    body.put("verificationCode", VC.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                HttpRequestFactory.getInstance(getApplicationContext()).submitKeys(body, onResponse, onError);
            }
        });
    }


//    public void checkPIN() {
//        boolean isValid = true;
//        if (isEmpty(VC)) {
//            VC.setError("invalid Verification Code");
//            isValid = false;
//        }
//        if (isValid) {
//            String code = VC.getText().toString();
//            if (code.equals("1234")) {
//                openDialog();
//            } else {
//
//                VC.getText().clear();
//                VC.setError("Wrong PIN");
//            }
//        }
//    }

    public boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    public void openSuccessDialog() {
        SuccessDialog seconddialog = new SuccessDialog();
        seconddialog.show(getSupportFragmentManager(), "first dialog");
    }

    public void openFailDialog() {
        FailDialog seconddialog = new FailDialog();
        seconddialog.show(getSupportFragmentManager(), "first dialog");
    }
}