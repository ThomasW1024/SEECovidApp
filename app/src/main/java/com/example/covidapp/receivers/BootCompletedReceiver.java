package com.example.covidapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.covidapp.service.BluetoothBackgroundService;


public class BootCompletedReceiver extends BroadcastReceiver {

    private String TAG=BootCompletedReceiver.class.getName();
    @Override
    public void onReceive(Context context, Intent arg1) {
        Log.wtf(TAG, "starting service...");
        context.startService(new Intent(context, BluetoothBackgroundService.class));
    }

}