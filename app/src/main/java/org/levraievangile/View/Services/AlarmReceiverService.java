package org.levraievangile.View.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Maranatha on 16/12/2017.
 */

public class MultiTaskService extends Service {

    private boolean isRunning;
    private Context context;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        this.context = this;
        this.isRunning = false;
    }

    private Runnable myTask = new Runnable() {
        public void run() {
            stopSelf();
        }
    };

    @Override
    public void onDestroy() {
        this.isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!this.isRunning) {
            this.isRunning = true;
        }
        return START_STICKY;
    }
}
