package org.levraievangile.Presenter;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;

import org.levraievangile.Model.Setting;
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
        try {
            iSplash.initialize();
            iSplash.events();
            iSplash.launchHomeActivity();
            // Remove share preferences data from app
            CommonPresenter.removeSomeSharePreferencesFromApp(context);
            // Initialize user admin level
            CommonPresenter.initializeUserAdminLevel(context);
            // Initialize the settings
            CommonPresenter.initializeAppSetting(context);
            // Initialise notification data
            CommonPresenter.initializeNotificationTimeLapsed(context);
            // Start Download Service receiver
            Intent downloadIntent = new Intent(context, DownloadReceiver.class);
            context.startService(downloadIntent);
            // Verify if alarm is running
            Intent alarm = new Intent(context, AlarmTimeReceiver.class);
            boolean alarmRunning = (PendingIntent.getBroadcast(context, 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
            //--
            Setting mAudioSetting = CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_AUDIO_NOTIFICATION);
            Setting mVideoSetting = CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_VIDEO_NOTIFICATION);
            if(!mAudioSetting.getChoice() && !mVideoSetting.getChoice()){
                if(alarmRunning) {
                    iSplash.stopAlarmService();
                }
            }
            else{
                if(!alarmRunning){
                    iSplash.startAlarmService();
                }
            }
        }
        catch (Exception ex){}
    }

    public void cancelCountDownTimer(CountDownTimer countDownTimer){
        try {
            CommonPresenter.cancelCountDownTimer(countDownTimer);
        }
        catch (Exception ex){}
    }
}
