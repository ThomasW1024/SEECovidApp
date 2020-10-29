package com.example.covidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidapp.service.BluetoothBackgroundService;

import static com.example.covidapp.constant.AppConstant.REQUEST_ENABLE_BT;

public class HomeActivity extends AppCompatActivity {
    private BluetoothManager bluetoothManager = null;
    private BluetoothAdapter mBluetoothAdapter;
    private String TAG =MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final TextView firstTextView =(TextView) findViewById(R.id.textView);
        Button firstButton = (Button) findViewById(R.id.button1);
        firstButton.setOnClickListener(new View.OnClickListener() {
          public void onClick(View view)
            {
                openLoginPage();
            }
        });


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
//       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
//        }
        /**
             Intent discoverableIntent =
             new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
             discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
             startActivity(discoverableIntent);
         */
        //TODO move to other part
        //Log.e(TAG,"Starting BluetoothBackgroundService");

    }

    public void openLoginPage() {
        Intent intent = new Intent(this, RegActivity.class);
        startActivity(intent);
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