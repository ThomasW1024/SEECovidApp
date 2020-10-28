package com.example.covidapp.httprequest;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covidapp.ephId.EphemeralGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HttpRequestFactory {
    private static HttpRequestFactory instance;
    private static Context ctx;

    private HttpRequestFactory(Context context) {
        ctx = context;
    }


    public static synchronized HttpRequestFactory getInstance(Context context) {
        if(instance == null) {
            instance = new HttpRequestFactory(context);
        }
        return instance;
    }

    // usage: HttpRequestFactory.getInstance(HomeActivity.this).sendRequest();
    public void sendRequest() {
        Log.e("HTTP", "clickme");
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(ctx);
        String url = "https://10.0.2.2:3000/";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.e("HTTP", response.substring(0, 500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("HTTP", error.getLocalizedMessage());
            }
        });
        queue.add(stringRequest);

    }



    // TODO replicate from above example
//    public boolean getCasesAndMatch(){
//        boolean ifMatched = false;
//        RequestFuture<JSONArray> future = RequestFuture.newFuture();
//        String url = "localhost:4200/cases";
//        HttpRequestQueue.getInstance(this).addToRequestQueue(new JsonArrayRequest(Request.Method.GET, url, null, future, future));
//        try {
//            JSONArray response = future.get();
//            List<String> infectedIds = new ArrayList<>();
//            // TODO need to matches the Service settings
//            int interval = 15;
//            int numberToGenerate = 96;
//            for (int i = 0; i <= response.length(); i += 1) {
//                try {
//                    JSONObject o = response.getJSONObject(i);
//                    // TODO get the secret and time out from object;
//                    String secret = "";
//                    long time = 0;
//                    infectedIds.addAll(EphemeralGenerator.getIDs(secret, time, interval, numberToGenerate));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            // TODO do crossChecking Here
//            ifMatched = true;
//        } catch (InterruptedException e) {
//            // exception handling
//        } catch (ExecutionException e) {
//            // exception handling
//        }
//        return ifMatched;
//    }


}


