package org.levraievangile.Presenter;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;

import org.levraievangile.View.Interfaces.SplashView;
import org.levraievangile.View.Receivers.AlarmTimeReceiver;
import org.levraievangile.View.Receivers.DownloadReceiver;

/**
 * Created by Maranatha on 16/12/2017.
 */

public class SplashPresenter {
    private SplashView.ISplash iSplash;

    public SplashPresenter(SplashView.ISplash iSplash) {
        this.iSplash = iSplash;
    }

    public void loadSplashData(Context context){
        iSplash.initialize();
        iSplash.events();
        iSplash.launchHomeActivity();
        // Verify if alarm is running
        Intent alarm = new Intent(context, AlarmTimeReceiver.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(context, 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
        if(!alarmRunning){
            iSplash.startAlarmService();
        }
        // Initialize the settings
        CommonPresenter.initializeAppSetting(context);
        // Initialise notification data
        CommonPresenter.initializeNotificationTimeLapsed(context);
        // Start Download Service receiver
        Intent downloadIntent = new Intent(context, DownloadReceiver.class);
        context.stopService(downloadIntent);
    }

    public void cancelCountDownTimer(CountDownTimer countDownTimer){
        CommonPresenter.cancelCountDownTimer(countDownTimer);
    }
}
