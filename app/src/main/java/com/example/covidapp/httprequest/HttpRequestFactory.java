package com.example.covidapp.httprequest;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covidapp.ephId.EphemeralGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HttpRequestFactory {
    private static HttpRequestFactory instance;
    private static Context ctx;
    RequestQueue queue ;

    private HttpRequestFactory(Context context) {
        ctx = context;
        queue = Volley.newRequestQueue(ctx);
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
        String url = "https://10.0.2.2:3000/";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // textView.setText("Response: " + response.toString());
                        Log.e("HTTP", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("HTTP", error.getLocalizedMessage());
            }
        });
        queue.add(stringRequest);

    }

    public void submitKeys(JSONObject jsonBody ){
        Log.e("HTTP", "submitKeys");
        String url ="https://10.0.2.2:3000/security_code";
        final String requestBody = jsonBody.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.e("HTTP", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("HTTP", error.getLocalizedMessage());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode); //+ new String(response.data);
                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        queue.add(stringRequest);
    }

    public void downloadKeys(){
        Log.e("HTTP", "downloadKeys");
        String url ="https://10.0.2.2:3000/get_secret_list";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Response", "success");

                        List<KeyTimePair> list = new ArrayList<>();
                        for (int i=0; i < response.length(); i+=1) {
                            try {
                                KeyTimePair pair = new KeyTimePair(response.getJSONObject(i));
                                list.add(pair);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Response", "fail");
                    }
                });
        queue.add(jsonArrayRequest);
    }

    public void submitKeys(JSONObject jsonBody,Response.Listener<String> onResponse,Response.ErrorListener onError ){
        Log.e("HTTP", "submitKeys");
        String url ="https://10.0.2.2:3000/security_code";
        final String requestBody = jsonBody.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,onResponse, onError) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode); //+ new String(response.data);
                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        queue.add(stringRequest);
    }

    public void downloadKeys(Response.Listener<JSONArray> onResponse,Response.ErrorListener onError){
        Log.e("HTTP", "downloadKeys");
        String url ="https://10.0.2.2:3000/get_secret_list";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, onResponse, onError);
        queue.add(jsonArrayRequest);
    }

}


