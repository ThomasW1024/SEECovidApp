package com.example.covidapp.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.covidapp.MainActivity;
import com.example.covidapp.R;
import com.example.covidapp.service.sub.EphIdService;

/**
 * how to start this server:
 *     public void startService() {
 *         Intent serviceIntent = new Intent(this, MyService.class);
 *         serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
 *
 *         ContextCompat.startForegroundService(this, serviceIntent);
 *     }
 *
 *     public void stopService() {
 *         Intent serviceIntent = new Intent(this, MyService.class);
 *         stopService(serviceIntent);
 *     }
 */

public class MyService extends Service {

    public MyService() {
    }

    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        // Noti bar settings
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();

        // start service
        startForeground(1, notification);

        //do heavy work on a background thread

        // TODO device base communication
        // please query tempID from DB

        // regenerate EphID
        WorkManager wm = WorkManager.getInstance(this);
        // using manual regeneration method, see the Work class
        WorkRequest wr =  new OneTimeWorkRequest.Builder(EphIdService.class).build();
        wm.enqueue(wr);
//        // using scheduler to restart work, but it have a min 15 min timer on restart
//        PeriodicWorkRequest pwr =  new PeriodicWorkRequest.Builder(TimedWork.class, 15, TimeUnit.MINUTES).build();
//        wm.enqueueUniquePeriodicWork("IDServer", ExistingPeriodicWorkPolicy.KEEP,  pwr);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        WorkManager.getInstance(this).cancelAllWork();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
