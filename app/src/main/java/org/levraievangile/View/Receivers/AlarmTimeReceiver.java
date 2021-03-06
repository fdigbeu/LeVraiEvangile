package org.levraievangile.View.Receivers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import org.levraievangile.Model.ApiClient;
import org.levraievangile.Model.Audio;
import org.levraievangile.Model.DAOFavoris;
import org.levraievangile.Model.Favoris;
import org.levraievangile.Model.Setting;
import org.levraievangile.Model.Video;
import org.levraievangile.Presenter.CommonPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Activities.HomeActivity;
import org.levraievangile.View.Activities.VideoActivity;
import org.levraievangile.View.Activities.VideoPlayerActivity;
import org.levraievangile.View.Interfaces.AudioView;
import org.levraievangile.View.Interfaces.VideoView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.levraievangile.Presenter.CommonPresenter.KEY_RELOAD_NEW_DATA_GOOD_TO_KNOW;
import static org.levraievangile.Presenter.CommonPresenter.KEY_RELOAD_NEW_DATA_NEWS_YEAR;
import static org.levraievangile.Presenter.CommonPresenter.KEY_SHOW_NEW_AUDIO_NOTIF_PLAYER;
import static org.levraievangile.Presenter.CommonPresenter.KEY_SHOW_NEW_VIDEO_NOTIF_PLAYER;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VALUE_POSITION_VIDEO_SELECTED;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VIDEO_PLAYER_SEND_DATA;
import static org.levraievangile.Presenter.CommonPresenter.saveDataInSharePreferences;

/**
 * Created by Maranatha on 16/12/2017.
 */

public class AlarmTimeReceiver extends BroadcastReceiver {

    private final String shortCode = "all-aujourdhui";
    private final int timeRepeat = 60000*60; //1 min = 60000; // 1000 = 1s

    @Override
    public void onReceive(final Context context, Intent intent) {
        try {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "LVE");
            //Acquire the lock
            wl.acquire();
            //--
            if(CommonPresenter.isMobileConnected(context)) {
                // User accepts video notification
                Setting mVideoSetting = CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_VIDEO_NOTIFICATION);
                if(mVideoSetting.getChoice()){
                    // Get all videos posted to day, notify one and save
                    Call<List<Video>> callVideos = ApiClient.getApiClientLeVraiEvangile().create(VideoView.IApiRessource.class).getAllVideos(shortCode);
                    callVideos.enqueue(new Callback<List<Video>>() {
                        @Override
                        public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                            ArrayList<Video> videos = (ArrayList<Video>) response.body();
                            // Get all videos notification and delete
                            if(videos.size()==0) {
                                DAOFavoris daoFavoris = new DAOFavoris(context);
                                ArrayList<Favoris> allVideos = daoFavoris.getAllData("notif_video_today");
                                if(allVideos.size() >= 50){
                                    for (int i=0; i<allVideos.size(); i++){
                                        DAOFavoris daoFav = new DAOFavoris(context);
                                        daoFav.deleteDataBy(allVideos.get(i).getId());
                                    }
                                }
                            }
                            else{
                                // Launch video notification
                                int increment = 0;
                                for (int i=0; i<videos.size(); i++){
                                    increment++;
                                    DAOFavoris daoFavoris = new DAOFavoris(context);
                                    Video video = videos.get(i);
                                    if(!daoFavoris.isFavorisExists(video.getSrc(), "notif_video_today") && increment == 1){
                                        Log.i("TAG_NOTIF_VIDEOS_TODAY", "video.getTitre() = "+video.getTitre());
                                        Favoris favoris = new Favoris(video.getId(), "notif_video_today", video.getMipmap(), video.getUrlacces(), video.getSrc(), video.getTitre(), video.getAuteur(), video.getDuree(), video.getDate(), video.getType_libelle(), video.getType_shortcode(), video.getId());
                                        daoFavoris.insertData(favoris);
                                        notification(context, favoris);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Video>> call, Throwable t) {}
                    });
                }

                // User accepts audio notification
                Setting mAudioSetting = CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_AUDIO_NOTIFICATION);
                if (mAudioSetting.getChoice()){
                    // Get all audios posted to day, notify one and save
                    Call<List<Audio>> callAudios = ApiClient.getApiClientLeVraiEvangile().create(AudioView.IApiRessource.class).getAllAudios(shortCode);
                    callAudios.enqueue(new Callback<List<Audio>>() {
                        @Override
                        public void onResponse(Call<List<Audio>> call, Response<List<Audio>> response) {
                            ArrayList<Audio> audios = (ArrayList<Audio>) response.body();
                            // Get all audios notification and delete
                            if(audios.size()==0) {
                                DAOFavoris daoFavoris = new DAOFavoris(context);
                                ArrayList<Favoris> allAudios = daoFavoris.getAllData("notif_audio_today");
                                if(allAudios.size() >= 50){
                                    for (int i=0; i<allAudios.size(); i++){
                                        DAOFavoris daoFav = new DAOFavoris(context);
                                        daoFav.deleteDataBy(allAudios.get(i).getId());
                                    }
                                }
                            }
                            else{
                                // Launch audio notification
                                int increment = 0;
                                for (int i=0; i<audios.size(); i++){
                                    increment++;
                                    DAOFavoris daoFavoris = new DAOFavoris(context);
                                    Audio audio = audios.get(i);
                                    if(!daoFavoris.isFavorisExists(audio.getSrc(), "notif_audio_today") && increment == 1){
                                        Log.i("TAG_NOTIF_AUDIOS_TODAY", "audio.getTitre() = "+audio.getTitre());
                                        Favoris favoris = new Favoris(audio.getId(), "notif_audio_today", audio.getMipmap(), audio.getUrlacces(), audio.getSrc(), audio.getTitre(), audio.getAuteur(), audio.getDuree(), audio.getDate(), audio.getType_libelle(), audio.getType_shortcode(), audio.getId());
                                        daoFavoris.insertData(favoris);
                                        notification(context, favoris);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Audio>> call, Throwable t) {}
                    });
                }
            }

            // Notify to reload new data
            notifyToReloadNewData(context);

            //Release the lock
            wl.release();
        }
        catch (Exception ex){}
    }

    /**
     * Start timer alarm
     * @param context
     */
    public void startTimerAlarm(Context context)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmTimeReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), timeRepeat , pi);
    }

    /**
     * Stop timer alarm
     * @param context
     */
    public void stopTimerAlarm(Context context)
    {
        Intent intent = new Intent(context, AlarmTimeReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    /**
     * Set ontime timer
     * @param context
     */
    public void setOnetimeTimer(Context context){
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmTimeReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
    }

    /**
     * Notify to reload new data
     * @param context
     */
    private void notifyToReloadNewData(Context context){
        saveDataInSharePreferences(context, KEY_RELOAD_NEW_DATA_NEWS_YEAR, "YES");
        saveDataInSharePreferences(context, KEY_RELOAD_NEW_DATA_GOOD_TO_KNOW, "YES");
    }

    private void notification(Context context, Favoris favoris){
        int notifID = (int)((new Date().getTime()/1000L) % Integer.MAX_VALUE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "LVE");
        if(favoris.getType().equalsIgnoreCase("notif_audio_today")){
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.lg_audio));
            mBuilder.setSmallIcon(R.mipmap.sm_audio);
            mBuilder.setTicker(context.getResources().getString(R.string.lb_notif_new_audio));
        }
        else if(favoris.getType().equalsIgnoreCase("notif_video_today")){
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.lg_video));
            mBuilder.setSmallIcon(R.mipmap.sm_video);
            mBuilder.setTicker(context.getResources().getString(R.string.lb_notif_new_video));
        }
        else{}
        mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentTitle(favoris.getTitre());
        String durationFormat = CommonPresenter.changeFormatDuration(favoris.getDuree());
        mBuilder.setContentText(durationFormat+" | "+favoris.getAuteur());
        //--
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(HomeActivity.class);
        //-- Transmission des infos
        Intent resultIntent = null;
        if(favoris.getType().equalsIgnoreCase("notif_audio_today")){
            resultIntent = new Intent(context, HomeActivity.class);
            Audio audio = new Audio(favoris.getRessource_id(), favoris.getUrlacces(), favoris.getSrc(), favoris.getTitre(), favoris.getAuteur(), favoris.getDuree(), favoris.getDate(), favoris.getType_libelle(), favoris.getType_shortcode(), CommonPresenter.getMipmapByTypeShortcode(favoris.getType_shortcode()));
            resultIntent.putExtra(KEY_SHOW_NEW_AUDIO_NOTIF_PLAYER, audio);
        }
        else if(favoris.getType().equalsIgnoreCase("notif_video_today")){
            resultIntent = new Intent(context, VideoPlayerActivity.class);
            Video video = new Video(favoris.getRessource_id(), favoris.getUrlacces(), favoris.getSrc(), favoris.getTitre(), favoris.getAuteur(), favoris.getDuree(), favoris.getDate(), favoris.getType_libelle(), favoris.getType_shortcode(), CommonPresenter.getMipmapByTypeShortcode(favoris.getType_shortcode()));
            resultIntent.putExtra(KEY_SHOW_NEW_VIDEO_NOTIF_PLAYER, video);
            resultIntent.putExtra(KEY_VIDEO_PLAYER_SEND_DATA, video);
            resultIntent.putExtra(KEY_VALUE_POSITION_VIDEO_SELECTED, "notification");
        }
        else{}
        //--
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notifID, mBuilder.build());
    }
}
