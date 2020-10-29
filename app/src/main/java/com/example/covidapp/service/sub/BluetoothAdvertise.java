package com.example.covidapp.service.sub;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertisingSet;
import android.bluetooth.le.AdvertisingSetCallback;
import android.bluetooth.le.AdvertisingSetParameters;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.os.ParcelUuid;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.covidapp.ephId.EphemeralGenerator;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BluetoothAdvertise extends Worker {
    private String TAG= BluetoothAdvertise.class.getName();

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;
    private static final UUID MY_UUID_INSECURE = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private AdvertisingSet currentAdvertisingSet;
    private AdvertisingSetCallback mAdvertisingSetCallback;
    private boolean is2msupported=true;
    private boolean isextendedadvertisingsupported=true;
    public  final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public Random RANDOM = new Random();
    private boolean mScanning=false;
    private Runnable runnableCode = null;
    private static final long SCAN_PERIOD = 10000;

    public BluetoothAdvertise(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        create();
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.e(TAG,"Starting advertise");

        //BluetoothLeAdvertiser advertiser = BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();

        Log.e(TAG,"Device name "+mBluetoothAdapter.getName());
        int devicenamelen=mBluetoothAdapter.getName().length();
        int maxDataLength = mBluetoothAdapter.getLeMaximumAdvertisingDataLength();

        int hashlength=maxDataLength-devicenamelen-5;
//        Log.e(TAG,"Device name length "+String.valueOf(devicenamelen));
//        Log.e(TAG,"Maximum data length "+String.valueOf(maxDataLength));

        // create parameter for the Advertising
        AdvertisingSetParameters.Builder builder = new AdvertisingSetParameters.Builder()
                .setLegacyMode(true) // True by default, but set here as a reminder.
                .setConnectable(false)
                .setInterval(AdvertisingSetParameters.INTERVAL_HIGH)
                .setTxPowerLevel(AdvertisingSetParameters.TX_POWER_HIGH);

        if(isextendedadvertisingsupported){
            builder.setPrimaryPhy(BluetoothDevice.PHY_LE_1M);
        }

        if(is2msupported){
            builder.setSecondaryPhy(BluetoothDevice.PHY_LE_2M);
        }

        AdvertisingSetParameters parameters = builder.build();

//        AdvertiseSettings settings = new AdvertiseSettings.Builder()
//                .setAdvertiseMode( AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY )
//                .setTxPowerLevel( AdvertiseSettings.ADVERTISE_TX_POWER_HIGH )
//                .setConnectable(false)
//                .build();

        ParcelUuid pUuid = new ParcelUuid(MY_UUID_INSECURE);
        String iddata= EphemeralGenerator.nextID(EphemeralGenerator.generateSecret(),Calendar.getInstance().getTimeInMillis()).replaceAll("-","");
        Log.v(TAG,"EphId" + iddata);
        iddata =getValidId(iddata);
        iddata=getId(6).toLowerCase();
//        Log.v(TAG,"Hash length is "+String.valueOf(hashlength));
        Log.e(TAG,"After Hash:" + iddata);

        mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        // stop then restart
        mBluetoothLeAdvertiser.stopAdvertisingSet(mAdvertisingSetCallback);
        mBluetoothLeAdvertiser.startAdvertisingSet(
                parameters,
                new AdvertiseData.Builder()
                .setIncludeDeviceName( false )
                .addServiceUuid( pUuid )
                .addServiceData( pUuid, iddata.getBytes(StandardCharsets.UTF_8) )
                .build(),
                new AdvertiseData.Builder()
                .setIncludeDeviceName(false)
                .build(),
                null, null, mAdvertisingSetCallback);

        WorkManager wm = WorkManager.getInstance(this.getApplicationContext());
        wm.enqueue(new OneTimeWorkRequest.Builder(BluetoothAdvertise.class).setInitialDelay(5, TimeUnit.SECONDS).build());
        return Result.success();
    }

    @Override
    public void onStopped() {
        super.onStopped();
        mBluetoothLeAdvertiser.stopAdvertisingSet(mAdvertisingSetCallback);
    }


    private void create(){
        WorkManager wm = WorkManager.getInstance(this.getApplicationContext());
        //keep this service running
        PowerManager powerManager = (PowerManager) this.getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyApp::MyWakelockTag");
        wakeLock.acquire(20000);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.enable();

//        if( !BluetoothAdapter.getDefaultAdapter().isMultipleAdvertisementSupported() ) {
//            Toast.makeText( this, "Multiple advertisement not supported", Toast.LENGTH_SHORT ).show();
//        }


        // create AdvertisingSet Callback
        mAdvertisingSetCallback = new AdvertisingSetCallback() {
            @Override
            public void onAdvertisingSetStarted(AdvertisingSet advertisingSet, int txPower, int status) {
                Log.e(TAG, "onAdvertisingSetStarted(): txPower:" + txPower + " , status: "
                        + status);
                if (status==AdvertisingSetCallback.ADVERTISE_FAILED_ALREADY_STARTED)
                    Log.e(TAG,"ADVERTISE_FAILED_ALREADY_STARTED");
                else if (status==AdvertisingSetCallback.ADVERTISE_FAILED_FEATURE_UNSUPPORTED)
                    Log.e(TAG,"ADVERTISE_FAILED_FEATURE_UNSUPPORTED");
                else if (status==AdvertisingSetCallback.ADVERTISE_FAILED_DATA_TOO_LARGE)
                    Log.e(TAG,"ADVERTISE_FAILED_DATA_TOO_LARGE");
                else if (status==AdvertisingSetCallback.ADVERTISE_FAILED_INTERNAL_ERROR)
                    Log.e(TAG,"ADVERTISE_FAILED_INTERNAL_ERROR");
                else if (status==AdvertisingSetCallback.ADVERTISE_FAILED_TOO_MANY_ADVERTISERS)
                    Log.e(TAG,"ADVERTISE_FAILED_TOO_MANY_ADVERTISERS");
                else if (status==AdvertisingSetCallback.ADVERTISE_SUCCESS)
                    Log.e(TAG,"ADVERTISE_SUCCESS");
                else
                    Log.e(TAG,"UNKNOWN STATUS");
                if(advertisingSet!=null) {
                    Log.e("BLE", "Advertising onAdvertisingSetStarted: " + advertisingSet.toString());
                }

                currentAdvertisingSet = advertisingSet;
                if(currentAdvertisingSet!=null) {
                    // Can also stop and restart the advertising
                    currentAdvertisingSet.enableAdvertising(false, 0, 0);
                    // Wait for onAdvertisingEnabled callback...
                    currentAdvertisingSet.enableAdvertising(true, 0, 0);
                    // Wait for onAdvertisingEnabled callback...

                    // Or modify the parameters - i.e. lower the tx power
                    currentAdvertisingSet.enableAdvertising(false, 0, 0);
                    // Wait for onAdvertisingEnabled callback...
                    /*
                        currentAdvertisingSet.setAdvertisingParameters(parameters.setTxPowerLevel
                        (AdvertisingSetParameters.TX_POWER_LOW).build());
                     */
                    // Wait for onAdvertisingParametersUpdated callback...
                    currentAdvertisingSet.enableAdvertising(true, 0, 0);
                    // Wait for onAdvertisingEnabled callback...
                    // When done with the advertising:
                    //stopAdvertisingSet(callback);
                }
            }

            @Override
            public void onAdvertisingDataSet(AdvertisingSet advertisingSet, int status) {
                Log.e(TAG, "onAdvertisingDataSet() :status:" + status);
            }

            @Override
            public void onScanResponseDataSet(AdvertisingSet advertisingSet, int status) {
                Log.e(TAG, "onScanResponseDataSet(): status:" + status);
            }

            @Override
            public void onAdvertisingSetStopped(AdvertisingSet advertisingSet) {
                Log.e(TAG, "onAdvertisingSetStopped():");
            }
        };

        // Check if all features are supported
        if (!mBluetoothAdapter.isLe2MPhySupported()) {
            Log.e(TAG, "2M PHY not supported!");
            is2msupported=false;
        }
        if (!mBluetoothAdapter.isLeExtendedAdvertisingSupported()) {
            Log.e(TAG, "LE Extended Advertising not supported!");
            isextendedadvertisingsupported=false;
        }
    }

    public static String getValidId(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == 'x') {
            str = str.substring(0, str.length() - 10);
        }
        return str;
    }

    public  String getId(int len) {
        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }

        return sb.toString();
    }




}
