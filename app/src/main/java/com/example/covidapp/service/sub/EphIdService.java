package com.example.covidapp.service.sub;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.covidapp.dataaccesslayer.DatabaseHelper;
import com.example.covidapp.ephId.EphemeralGenerator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class EphIdService extends Worker {
    // should be load up from property
    private static int regenRate = 15;
    private static int secretLifeTime = 24 * 60 / regenRate; // number of id would be generated 24 * 60 / regenRate

    private static Date myTime = null;
    private static long counter = 0;
    private static String activatingSecret = null;
    private static String currentID = null;

    DatabaseHelper DB = new DatabaseHelper(this.getApplicationContext());

    public EphIdService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.e("Timer", "I am Doing Job_" + counter);
        execute();

        // manually create restart worker
        /* TODO known bug the Static value will be reset after "KILLING the App"
            have no idea on how to fix,
            cannot query from db to try get latest, since the we don't know where if the secret is end of life or the order/index of tempID
         */
        WorkManager wm = WorkManager.getInstance(this.getApplicationContext());
        wm.enqueue(new OneTimeWorkRequest.Builder(EphIdService.class).setInitialDelay(5, TimeUnit.SECONDS).build());
        return Result.success();
    }

    @Override
    public void onStopped() {
        Log.e("Timer", "I am Stopping");
        super.onStopped();
    }

    public synchronized void execute() {
        Log.e("Timer", "changing ID...");
        //
        if(myTime == null) {
            myTime = Calendar.getInstance().getTime();
        }
        // handle changing secret
        // counter start with 0 so it is greater than or equal
        if (counter >= (long) (secretLifeTime / regenRate) || activatingSecret == null){
            changeSecret();
        }
        // changing tempID
        changeTempID();
    }

    private synchronized void changeSecret(){
        // update generation time
        myTime = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
        activatingSecret = EphemeralGenerator.generateSecret();


        // insert into Db
        Boolean checkinsertdata = DB.insertiddata(activatingSecret, simpleDateFormat.format(myTime));
    }

    private synchronized void changeTempID() {
        // add regeneration Rate into th
        myTime = new Date (myTime.getTime() + regenRate * 1000 * 60);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");

        currentID = EphemeralGenerator.nextID(activatingSecret, myTime.getTime());
        incrementCounter();

        // insert into Db
        Boolean checkinsertdata = DB.inserttempiddata(currentID, simpleDateFormat.format(myTime));
    }

    private void resetCounter(){
        counter = 0;
    }
    private void incrementCounter(){
        counter += 1;
    }
}
