package com.example.covidapp.httprequest;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.covidapp.ephId.EphemeralGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HttpRequestQueue {
    private static HttpRequestQueue instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private HttpRequestQueue(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized HttpRequestQueue getInstance(Context context) {
        if (instance == null) {
            instance = new HttpRequestQueue(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    // TODO Put it in Activity / service
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


