package com.example.covidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidapp.service.BluetoothBackgroundService;

public class MainActivity extends AppCompatActivity {
    private BluetoothManager bluetoothManager = null;
    private BluetoothAdapter mBluetoothAdapter;
    private static int SPLASH_TIME_OUT=5000;
    private static final int REQUEST_ENABLE_BT = 1;
    private String TAG=MainActivity.class.getName();
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 465;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return;
        }
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this,"Your device does not support bluetooth LE", Toast.LENGTH_SHORT).show();
            finish();
        }
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
        }
        /** Intent discoverableIntent =
                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
         */
        //Log.wtf(TAG,"Starting BluetoothBackgroundService");
        //startService(new Intent(this, BluetoothBackgroundService.class));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                finish();
            }
        },SPLASH_TIME_OUT);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        try {

            switch (requestCode) {
                case PERMISSION_REQUEST_COARSE_LOCATION: {
                    Log.wtf(TAG, "Permission granted for access coarse location");
                    Log.wtf(TAG,"Starting BluetoothBackgroundService");
                    startService(new Intent(this, BluetoothBackgroundService.class));
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            //finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}