package com.example.covidapp.service.sub;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.ParcelUuid;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.covidapp.R;
import com.example.covidapp.service.BluetoothBackgroundService;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BluetoothScan extends Worker {
    private String TAG= BluetoothScan.class.getName();
    private static final UUID MY_UUID_INSECURE = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private ScanCallback mScanCallback;

    public BluetoothScan(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        create();
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.e(TAG,"Starting Scanning");
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        mBluetoothLeScanner.stopScan(mScanCallback);
        mBluetoothLeScanner.startScan(mScanCallback);

        WorkManager wm = WorkManager.getInstance(this.getApplicationContext());
        wm.enqueue(new OneTimeWorkRequest.Builder(BluetoothScan.class).setInitialDelay(300, TimeUnit.SECONDS).build());
        return Result.success();
    }

    @Override
    public void onStopped() {
        super.onStopped();
        mBluetoothLeScanner.stopScan(mScanCallback);
    }

    private void create() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.enable();
        mScanCallback = new ScanCallback() {

            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                Log.e(TAG,result.toString());
                super.onScanResult(callbackType, result);
                if( result == null
                        || result.getDevice() == null
                        || TextUtils.isEmpty(result.getDevice().getName()) )
                    return;

                //StringBuilder builder = new StringBuilder( result.getDevice().getName() );
                StringBuilder builder = new StringBuilder( "");
                builder.append("\n").append(new String(result.getScanRecord().getServiceData(new ParcelUuid(MY_UUID_INSECURE)), StandardCharsets.UTF_8));
                //builder.append("\n").append(new String(result.getScanRecord().getServiceData(result.getScanRecord().getServiceUuids().get(0)), Charset.forName("UTF-8")));
                Log.e( "BLE", "Data from advert " + builder.toString() );
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                for(ScanResult res:results){
                    Log.e(TAG,res.getDevice().getName());
                }
            }

            @Override
            public void onScanFailed(int errorCode) {
                Log.e( "BLE", "Discovery onScanFailed: " + errorCode );
                super.onScanFailed(errorCode);
            }
        };
    }
}
