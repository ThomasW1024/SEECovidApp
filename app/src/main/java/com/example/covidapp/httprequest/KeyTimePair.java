package com.example.covidapp.httprequest;

import org.json.JSONException;
import org.json.JSONObject;

public class KeyTimePair {
    private String secret;
    private String time;

    public KeyTimePair() {
    }

    public KeyTimePair(JSONObject o) throws JSONException {
        secret = (String) o.get("secret");
        time = (String) o.get("time");
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public KeyTimePair putSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public KeyTimePair putTime(String time) {
        this.time = time;
        return this;
    }

}
