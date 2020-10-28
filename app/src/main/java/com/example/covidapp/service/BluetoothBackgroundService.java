package com.example.covidapp.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.AdvertisingSet;
import android.bluetooth.le.AdvertisingSetCallback;
import android.bluetooth.le.AdvertisingSetParameters;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.covidapp.ephId.EphemeralGenerator;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class BluetoothBackgroundService extends Service {
    private String TAG=BluetoothBackgroundService.class.getName();
    private BluetoothManager bluetoothManager = null;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner = null;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final UUID MY_UUID_INSECURE = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private AdvertisingSet currentAdvertisingSet;
    private boolean is2msupported=true;
    private boolean isextendedadvertisingsupported=true;
    public  final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public Random RANDOM = new Random();
    private boolean mScanning=false;
    private Handler handler = null;
    private Runnable runnableCode = null;
    private static final long SCAN_PERIOD = 10000;
    public BluetoothBackgroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private ScanCallback mScanCallback = new ScanCallback() {


        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.wtf(TAG,result.toString());
            super.onScanResult(callbackType, result);
            if( result == null
                    || result.getDevice() == null
                    || TextUtils.isEmpty(result.getDevice().getName()) )
                return;

            //StringBuilder builder = new StringBuilder( result.getDevice().getName() );
            StringBuilder builder = new StringBuilder( "");
            builder.append("\n").append(new String(result.getScanRecord().getServiceData(new ParcelUuid(MY_UUID_INSECURE)), StandardCharsets. UTF_8));
            //builder.append("\n").append(new String(result.getScanRecord().getServiceData(result.getScanRecord().getServiceUuids().get(0)), Charset.forName("UTF-8")));
            Log.wtf( "BLE", "Data from advert " + builder.toString() );

        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            for(ScanResult res:results){
                Log.wtf(TAG,res.getDevice().getName());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.wtf( "BLE", "Discovery onScanFailed: " + errorCode );
            super.onScanFailed(errorCode);
        }
    };

    @SuppressLint("NewApi")
    @Override
    public void onCreate() {

        Log.wtf(TAG,"Service created");

        //keep this service running
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyApp::MyWakelockTag");
        wakeLock.acquire();
        handler = new Handler();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }
        else{
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }

        mBluetoothAdapter.enable();

        if( !BluetoothAdapter.getDefaultAdapter().isMultipleAdvertisementSupported() ) {
            Toast.makeText( this, "Multiple advertisement not supported", Toast.LENGTH_SHORT ).show();

        }
        // Check if all features are supported
        if (!mBluetoothAdapter.isLe2MPhySupported()) {
            Log.wtf(TAG, "2M PHY not supported!");
            is2msupported=false;
            return;
        }
        if (!mBluetoothAdapter.isLeExtendedAdvertisingSupported()) {
            Log.wtf(TAG, "LE Extended Advertising not supported!");
            isextendedadvertisingsupported=false;
            return;
        }

        //Toast.makeText(this, "Service was Created", Toast.LENGTH_LONG).show();
    }



    @Override
    public int onStartCommand(Intent pIntent, int flags, int startId) {
        try{
            bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
            scanLeDevice();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        Log.wtf(TAG,"Starting advertise");
        BluetoothLeAdvertiser advertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        //BluetoothLeAdvertiser advertiser = BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();

        Log.wtf(TAG,"Device name "+mBluetoothAdapter.getName());
        int devicenamelen=mBluetoothAdapter.getName().length();
        int maxDataLength = mBluetoothAdapter.getLeMaximumAdvertisingDataLength();

        int hashlength=maxDataLength-devicenamelen-5;
        Log.wtf(TAG,"Device name length "+String.valueOf(devicenamelen));
        Log.wtf(TAG,"Maximum data length "+String.valueOf(maxDataLength));
        AdvertisingSetParameters.Builder builder = (new AdvertisingSetParameters.Builder())
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



        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode( AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY )
                .setTxPowerLevel( AdvertiseSettings.ADVERTISE_TX_POWER_HIGH )
                .setConnectable(false)
                .build();

        ParcelUuid pUuid = new ParcelUuid(MY_UUID_INSECURE);
        String secret=EphemeralGenerator.generateSecret();
        Calendar cal = Calendar.getInstance();
        long time =  cal.getTimeInMillis();
        String iddata= EphemeralGenerator.nextID(secret,time).replaceAll("-","");
        Log.wtf(TAG,iddata);
        iddata =getValidId(iddata);
        iddata=getId(6).toLowerCase();
        Log.wtf(TAG,"Hash length is "+String.valueOf(hashlength));
        Log.wtf(TAG,iddata);
        AdvertiseData data = new AdvertiseData.Builder()
                .setIncludeDeviceName( true )
                .addServiceUuid( pUuid )
                .addServiceData( pUuid, iddata.getBytes(Charset.forName("UTF-8") ) )
                .build();

        AdvertiseData scanResponse = new AdvertiseData.Builder()
                .setIncludeDeviceName(true)
                .build();


        AdvertisingSetCallback callback = new AdvertisingSetCallback() {
            @Override
            public void onAdvertisingSetStarted(AdvertisingSet advertisingSet, int txPower, int status) {
                Log.wtf(TAG, "onAdvertisingSetStarted(): txPower:" + txPower + " , status: "
                        + status);
                if (status==AdvertisingSetCallback.ADVERTISE_FAILED_ALREADY_STARTED)
                    Log.wtf(TAG,"ADVERTISE_FAILED_ALREADY_STARTED");
                else if (status==AdvertisingSetCallback.ADVERTISE_FAILED_FEATURE_UNSUPPORTED)
                    Log.wtf(TAG,"ADVERTISE_FAILED_FEATURE_UNSUPPORTED");
                else if (status==AdvertisingSetCallback.ADVERTISE_FAILED_DATA_TOO_LARGE)
                    Log.wtf(TAG,"ADVERTISE_FAILED_DATA_TOO_LARGE");
                else if (status==AdvertisingSetCallback.ADVERTISE_FAILED_INTERNAL_ERROR)
                    Log.wtf(TAG,"ADVERTISE_FAILED_INTERNAL_ERROR");
                else if (status==AdvertisingSetCallback.ADVERTISE_FAILED_TOO_MANY_ADVERTISERS)
                    Log.wtf(TAG,"ADVERTISE_FAILED_TOO_MANY_ADVERTISERS");
                else if (status==AdvertisingSetCallback.ADVERTISE_SUCCESS)
                    Log.wtf(TAG,"ADVERTISE_SUCCESS");
                else
                    Log.wtf(TAG,"UNKNOWN STATUS");
                if(advertisingSet!=null) {
                    Log.wtf("BLE", "Advertising onAdvertisingSetStarted: " + advertisingSet.toString());
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
                    /**currentAdvertisingSet.setAdvertisingParameters(parameters.setTxPowerLevel
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
                Log.wtf(TAG, "onAdvertisingDataSet() :status:" + status);

            }

            @Override
            public void onScanResponseDataSet(AdvertisingSet advertisingSet, int status) {
                Log.wtf(TAG, "onScanResponseDataSet(): status:" + status);
            }

            @Override
            public void onAdvertisingSetStopped(AdvertisingSet advertisingSet) {
                Log.wtf(TAG, "onAdvertisingSetStopped():");
            }
        };
        advertiser.startAdvertisingSet(parameters, data, scanResponse, null, null, callback);


        return START_STICKY;
    }

    private void scanLeDevice() {
        if (!mScanning) {
            // Stops scanning after a pre-defined scan period.
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    Log.wtf(TAG,"Stopping scanning");
                    bluetoothLeScanner.stopScan(mScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            Log.wtf(TAG,"Started scanning");
            //

            runnableCode = new Runnable() {
                @Override
                public void run() {

                    //bluetoothLeScanner.startScan(buildScanFilters(),buildScanSettings(),mScanCallback);
                    bluetoothLeScanner.startScan(mScanCallback);
                    handler.postDelayed(this, 20000);
                }
            };
// Start the initial runnable task by posting through the handler
            handler.post(runnableCode);
        } else {
            mScanning = false;
            Log.wtf(TAG,"Stop scanning");
            bluetoothLeScanner.stopScan(mScanCallback);
        }
    }

    private ScanSettings buildScanSettings() {
        ScanSettings.Builder builder = new ScanSettings.Builder();
        builder.setScanMode(ScanSettings.SCAN_MODE_LOW_POWER);
        return builder.build();
    }

    private List<ScanFilter> buildScanFilters() {
        List<ScanFilter> scanFilters = new ArrayList<>();

        ScanFilter.Builder builder = new ScanFilter.Builder();
        // Comment out the below line to see all BLE devices around you
        builder.setServiceUuid(new ParcelUuid(MY_UUID_INSECURE));
        scanFilters.add(builder.build());

        return scanFilters;
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
