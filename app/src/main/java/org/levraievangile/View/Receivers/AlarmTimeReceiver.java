package org.levraievangile.View.Receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import static org.levraievangile.Presenter.CommonPresenter.KEY_RELOAD_NEW_DATA_GOOD_TO_KNOW;
import static org.levraievangile.Presenter.CommonPresenter.KEY_RELOAD_NEW_DATA_NEWS_YEAR;
import static org.levraievangile.Presenter.CommonPresenter.saveDataInSharePreferences;

/**
 * Created by Maranatha on 16/12/2017.
 */

public class AlarmTimeReceiver extends BroadcastReceiver {

    private final int timeRepeat = 60000*60; //1 min = 60000; // 1000 = 1s

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "LVE");
        //Acquire the lock
        wl.acquire();
        //--

        // TODO = Make functionality here

        // Notify tonreload new data
        notifyToReloadNewData(context);

        //Release the lock
        wl.release();
    }


    public void startTimerAlarm(Context context)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmTimeReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), timeRepeat , pi);
    }

    public void stopTimerAlarm(Context context)
    {
        Intent intent = new Intent(context, AlarmTimeReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public void setOnetimeTimer(Context context){
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmTimeReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
    }

    private void notifyToReloadNewData(Context context){
        saveDataInSharePreferences(context, KEY_RELOAD_NEW_DATA_NEWS_YEAR, "YES");
        saveDataInSharePreferences(context, KEY_RELOAD_NEW_DATA_GOOD_TO_KNOW, "YES");
    }
}
