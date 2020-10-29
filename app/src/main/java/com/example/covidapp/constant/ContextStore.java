package com.example.covidapp.constant;

import java.util.UUID;

public class ContextStore {
    private String tempID;
    private static ContextStore store;
    private ContextStore(){
        //placeholder prevent error, will be replace as soon as keyGen start
        tempID = "PLACEHOLDER";
    }

    public static synchronized ContextStore getInstance(){
        if (store == null)
            store = new ContextStore();
        return store;
    }

    public synchronized String getTempID() {
        return tempID;
    }

    public synchronized void setTempID(String tempID) {
        this.tempID = tempID;
    }
}
