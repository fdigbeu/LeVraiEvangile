package org.levraievangile.View.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import org.levraievangile.Model.Audio;
import org.levraievangile.Presenter.CommonPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Activities.HomeActivity;
import org.levraievangile.View.Interfaces.NotificationView;

import java.util.List;

import static org.levraievangile.Presenter.CommonPresenter.KEY_NOTIFICATION_IS_PLAYING;
import static org.levraievangile.Presenter.CommonPresenter.KEY_NOTIF_AUDIOS_LIST;
import static org.levraievangile.Presenter.CommonPresenter.KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_SRC;
import static org.levraievangile.Presenter.CommonPresenter.KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_TIME_ELAPSED;
import static org.levraievangile.Presenter.CommonPresenter.KEY_NOTIF_PLAYER_PLAY_NEXT;
import static org.levraievangile.Presenter.CommonPresenter.KEY_NOTIF_PLAYER_PLAY_PREVIOUS;
import static org.levraievangile.Presenter.CommonPresenter.KEY_NOTIF_PLAYER_SELECTED;
import static org.levraievangile.Presenter.CommonPresenter.KEY_PLAYER_AUDIO_TO_NOTIF_AUDIO_TIME_ELAPSED;
import static org.levraievangile.Presenter.CommonPresenter.saveDataInSharePreferences;

/**
 * Created by Maranatha on 30/11/2017.
 */

public class PlayerAudioService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnInfoListener {
    private RemoteViews views;
    private RemoteViews bigViews;
    private MediaPlayer mediaPlayer = null;
    private Notification.Builder notificationBuilder;
    private Notification status;
    private List<Audio> audios;
    private int positionSelected;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            if (intent.getAction().equals(NotificationView.ACTION.STARTFOREGROUND_ACTION)) {
                audios = CommonPresenter.getAllAudiosByKey(getApplicationContext(), KEY_NOTIF_AUDIOS_LIST);
                positionSelected = Integer.parseInt(CommonPresenter.getDataFromSharePreferences(getApplicationContext(), KEY_NOTIF_PLAYER_SELECTED));
                saveDataInSharePreferences(getApplicationContext(), KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_SRC, ""+audios.get(positionSelected).getSrc());
                //--
                showNotification();
                // Play Media player
                playAudioMediaPlayer();
                // Notify that notification is playing
                saveDataInSharePreferences(getApplicationContext(), KEY_NOTIFICATION_IS_PLAYING, "YES");
            }
            else if (intent.getAction().equals(NotificationView.ACTION.PLAY_ACTION)) {
                // If notif player is playing
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    // Change Image on reading player
                    views.setImageViewResource(R.id.notif_player_play, R.drawable.btn_media_player_play);
                    bigViews.setImageViewResource(R.id.notif_player_play, R.drawable.btn_media_player_play);
                }
                else{
                    mediaPlayer.start();
                    // Change Image on reading player
                    views.setImageViewResource(R.id.notif_player_play, R.drawable.btn_media_player_pause);
                    bigViews.setImageViewResource(R.id.notif_player_play, R.drawable.btn_media_player_pause);
                    //--
                    stopOtherMediaPlayerSound();
                }
                startForeground(NotificationView.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
            }
            else if (intent.getAction().equals(NotificationView.ACTION.PREVIOUS_ACTION)) {
                positionSelected = Integer.parseInt(CommonPresenter.getDataFromSharePreferences(getApplicationContext(), KEY_NOTIF_PLAYER_PLAY_PREVIOUS));
                //--
                saveDataInSharePreferences(getApplicationContext(), KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_SRC, ""+audios.get(positionSelected).getSrc());
                //--
                CommonPresenter.saveNotificationParameters(getApplicationContext(), positionSelected, audios.size());
                // Play Media player
                playAudioMediaPlayer();
            }
            else if (intent.getAction().equals(NotificationView.ACTION.NEXT_ACTION)) {
                positionSelected = Integer.parseInt(CommonPresenter.getDataFromSharePreferences(getApplicationContext(), KEY_NOTIF_PLAYER_PLAY_NEXT));
                //--
                saveDataInSharePreferences(getApplicationContext(), KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_SRC, ""+audios.get(positionSelected).getSrc());
                //--
                CommonPresenter.saveNotificationParameters(getApplicationContext(), positionSelected, audios.size());
                // Play Media player
                playAudioMediaPlayer();
            }
            else if (intent.getAction().equals(NotificationView.ACTION.PAUSE_ACTION)) {
                if(mediaPlayer != null && mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    // Change Image on reading player
                    views.setImageViewResource(R.id.notif_player_play, R.drawable.btn_media_player_play);
                    bigViews.setImageViewResource(R.id.notif_player_play, R.drawable.btn_media_player_play);
                }
            }
            else if (intent.getAction().equals(NotificationView.ACTION.STOPFOREGROUND_ACTION)) {
                // Close Media player
                if(mediaPlayer != null && mediaPlayer.isPlaying()){
                    saveDataInSharePreferences(getApplicationContext(), KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_TIME_ELAPSED, ""+mediaPlayer.getCurrentPosition());
                    saveDataInSharePreferences(getApplicationContext(), KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_SRC, ""+audios.get(positionSelected).getSrc());
                    mediaPlayer.stop();
                    // Notify that notification is not playing
                    saveDataInSharePreferences(getApplicationContext(), KEY_NOTIFICATION_IS_PLAYING, "NO");
                }
                //--
                stopForeground(true);
                stopSelf();
            }
        }
        catch (Exception ex){}
        //--
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CommonPresenter.stopMediaPlayer(mediaPlayer);
        // Notify that notification is not playing
        saveDataInSharePreferences(getApplicationContext(), KEY_NOTIFICATION_IS_PLAYING, "NO");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        positionSelected = Integer.parseInt(CommonPresenter.getDataFromSharePreferences(getApplicationContext(), KEY_NOTIF_PLAYER_PLAY_NEXT));
        //--
        CommonPresenter.saveNotificationParameters(getApplicationContext(), positionSelected, audios.size());
        // Play Media player
        playAudioMediaPlayer();
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what){
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                // Display buffering
                views.setTextViewText(R.id.notif_player_title, getResources().getString(R.string.lb_buffering_title));
                views.setTextViewText(R.id.notif_player_subtitle, getResources().getString(R.string.lb_buffering_title));
                bigViews.setTextViewText(R.id.notif_player_title, getResources().getString(R.string.lb_buffering_title));
                bigViews.setTextViewText(R.id.notif_player_subtitle, getResources().getString(R.string.lb_buffering_detail));
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                // Display stream success
                // Small Notif audio Player : widget title is hide so info is written in subtitle
                views.setTextViewText(R.id.notif_player_title, audios.get(positionSelected).getTitre());
                views.setTextViewText(R.id.notif_player_subtitle, audios.get(positionSelected).getTitre());
                // Large Notif audio player : No widgets hide
                bigViews.setTextViewText(R.id.notif_player_title, audios.get(positionSelected).getTitre());
                String tagDuree[] = audios.get(positionSelected).getDuree().split(":");
                bigViews.setTextViewText(R.id.notif_player_subtitle, ((Integer.parseInt(tagDuree[0])*60)+Integer.parseInt(tagDuree[1]))+"min | "+audios.get(positionSelected).getAuteur());
                break;
        }
        //--
        viewNormalColor();
        //--
        startForeground(NotificationView.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        // Display stream success
        mediaPlayer.start();
        // Retrieve time to continue
        String timeElapse = CommonPresenter.getDataFromSharePreferences(getApplicationContext(), KEY_PLAYER_AUDIO_TO_NOTIF_AUDIO_TIME_ELAPSED);
        if(timeElapse != null && !timeElapse.equalsIgnoreCase("0")){
            mediaPlayer.seekTo(Integer.parseInt(timeElapse));
            saveDataInSharePreferences(getApplicationContext(), KEY_PLAYER_AUDIO_TO_NOTIF_AUDIO_TIME_ELAPSED, "0");
        }
        // Small Notif audio Player : widget title is hide so info is written in subtitle
        views.setTextViewText(R.id.notif_player_title, audios.get(positionSelected).getTitre());
        views.setTextViewText(R.id.notif_player_subtitle, audios.get(positionSelected).getTitre());
        // Large Notif audio player : No widgets hide
        bigViews.setTextViewText(R.id.notif_player_title, audios.get(positionSelected).getTitre());
        String tagDuree[] = audios.get(positionSelected).getDuree().split(":");
        bigViews.setTextViewText(R.id.notif_player_subtitle, ((Integer.parseInt(tagDuree[0])*60)+Integer.parseInt(tagDuree[1]))+"min | "+audios.get(positionSelected).getAuteur());
        // File audio is playing : Button in Pause Position
        views.setImageViewResource(R.id.notif_player_play, R.drawable.btn_media_player_pause);
        bigViews.setImageViewResource(R.id.notif_player_play, R.drawable.btn_media_player_pause);
        //--
        viewNormalColor();
        //--
        startForeground(NotificationView.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        // Display erreur message
        if(CommonPresenter.isMobileConnected(getApplicationContext())){
            // Small Notif audio Player : widget title is hide so info is written in subtitle
            views.setTextViewText(R.id.notif_player_title, getResources().getString(R.string.lb_impossible_toplay_detail));
            views.setTextViewText(R.id.notif_player_subtitle, getResources().getString(R.string.lb_impossible_toplay_detail));
            // Large Notif audio player : No widgets hide
            bigViews.setTextViewText(R.id.notif_player_title, getResources().getString(R.string.lb_impossible_toplay));
            bigViews.setTextViewText(R.id.notif_player_subtitle, getResources().getString(R.string.lb_impossible_toplay_detail));
        }
        else{
            // Small Notif audio Player : widget title is hide so info is written in subtitle
            views.setTextViewText(R.id.notif_player_title, getResources().getString(R.string.no_connection));
            views.setTextViewText(R.id.notif_player_subtitle, getResources().getString(R.string.no_connection));
            // Large Notif audio player : No widgets hide
            bigViews.setTextViewText(R.id.notif_player_title, getResources().getString(R.string.lb_impossible_toplay));
            bigViews.setTextViewText(R.id.notif_player_subtitle, getResources().getString(R.string.no_connection));
        }
        // File is not playing : Play position
        views.setImageViewResource(R.id.notif_player_play, R.drawable.btn_media_player_play);
        bigViews.setImageViewResource(R.id.notif_player_play, R.drawable.btn_media_player_play);
        //--
        viewFailureColor();
        //--
        startForeground(NotificationView.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
        return false;
    }

    private void showNotification() {
        // Using RemoteViews to bind custom layouts into Notification
        views = new RemoteViews(getPackageName(), R.layout.notification_audio_player_small);
        bigViews = new RemoteViews(getPackageName(), R.layout.notification_audio_player_large);

        // showing default album image
        views.setImageViewBitmap(R.id.notif_logo, NotificationView.getDefaultAlbumArt(this));
        bigViews.setImageViewBitmap(R.id.notif_logo, NotificationView.getDefaultAlbumArt(this));

        // Redirection when user clicks on this notification
        Intent notificationIntent = new Intent(this, HomeActivity.class);
        notificationIntent.setAction(NotificationView.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        // Previous pending
        Intent previousIntent = new Intent(this, PlayerAudioService.class);
        previousIntent.setAction(NotificationView.ACTION.PREVIOUS_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0, previousIntent, 0);

        // Play pending
        Intent playIntent = new Intent(this, PlayerAudioService.class);
        playIntent.setAction(NotificationView.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0, playIntent, 0);

        // Next pending
        Intent nextIntent = new Intent(this, PlayerAudioService.class);
        nextIntent.setAction(NotificationView.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0, nextIntent, 0);

        // Close pending
        Intent closeIntent = new Intent(this, PlayerAudioService.class);
        closeIntent.setAction(NotificationView.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0, closeIntent, 0);

        // Binding views images and events
        views.setOnClickPendingIntent(R.id.notif_player_previous, ppreviousIntent);
        views.setImageViewResource(R.id.notif_player_previous, R.drawable.btn_media_player_previous);
        views.setOnClickPendingIntent(R.id.notif_player_play, pplayIntent);
        views.setImageViewResource(R.id.notif_player_play, R.drawable.btn_media_player_play);
        views.setOnClickPendingIntent(R.id.notif_player_next, pnextIntent);
        views.setImageViewResource(R.id.notif_player_next, R.drawable.btn_media_player_next);
        views.setOnClickPendingIntent(R.id.notif_player_close, pcloseIntent);
        views.setImageViewResource(R.id.notif_player_close, R.drawable.ic_player_media_cancel_32dp);

        // Binding bigViews images and events
        bigViews.setOnClickPendingIntent(R.id.notif_player_previous, ppreviousIntent);
        bigViews.setImageViewResource(R.id.notif_player_previous, R.drawable.btn_media_player_previous);
        bigViews.setOnClickPendingIntent(R.id.notif_player_play, pplayIntent);
        bigViews.setImageViewResource(R.id.notif_player_play, R.drawable.btn_media_player_play);
        bigViews.setOnClickPendingIntent(R.id.notif_player_next, pnextIntent);
        bigViews.setImageViewResource(R.id.notif_player_next, R.drawable.btn_media_player_next);
        bigViews.setOnClickPendingIntent(R.id.notif_player_close, pcloseIntent);
        bigViews.setImageViewResource(R.id.notif_player_close, R.drawable.ic_player_media_cancel_32dp);

        // Views default image
        views.setTextViewText(R.id.notif_player_title, getResources().getString(R.string.lb_buffering_title));
        views.setTextViewText(R.id.notif_player_subtitle, getResources().getString(R.string.lb_buffering_title));

        // bigViews default image
        bigViews.setTextViewText(R.id.notif_player_title, getResources().getString(R.string.lb_buffering_title));
        bigViews.setTextViewText(R.id.notif_player_subtitle, getResources().getString(R.string.lb_buffering_detail));

        // Color of each view and bigView
        viewNormalColor();

        // Notification instance
        notificationBuilder = new Notification.Builder(this);
        //-- Compatibilité Android 8 et Plus
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            final String LVE_CHANNEL_ID = "le_vrai_evangile_channel";
            final CharSequence name = "LeVraiEvangile";
            final int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notifChannel = new NotificationChannel(LVE_CHANNEL_ID, name, importance);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(notifChannel);
            notificationBuilder.setChannelId(LVE_CHANNEL_ID);
        }
        //--
        //notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        status = notificationBuilder.build();
        status.contentView = views;
        status.bigContentView = bigViews;
        status.flags = Notification.FLAG_ONGOING_EVENT;
        status.icon = R.mipmap.logo;
        status.contentIntent = pendingIntent;

        // Playing now
        startForeground(NotificationView.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
    }

    // View normal color
    private void viewNormalColor(){
        views.setTextColor(R.id.notif_player_title, Color.WHITE);
        views.setTextColor(R.id.notif_player_subtitle, Color.rgb(18, 17, 74));
        bigViews.setTextColor(R.id.notif_player_title, Color.WHITE);
        bigViews.setTextColor(R.id.notif_player_subtitle, Color.rgb(18, 17, 74));
    }

    // View echec color
    private void viewFailureColor(){
        views.setTextColor(R.id.notif_player_title, Color.RED);
        views.setTextColor(R.id.notif_player_subtitle, Color.RED);
        bigViews.setTextColor(R.id.notif_player_title, Color.RED);
        bigViews.setTextColor(R.id.notif_player_subtitle, Color.RED);
    }

    // Play audio media player
    private void playAudioMediaPlayer(){
        CommonPresenter.stopMediaPlayer(mediaPlayer);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        //--
        stopOtherMediaPlayerSound();
        //--
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnInfoListener(this);
    }

    // Pause to other media player
    private void stopOtherMediaPlayerSound(){
        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        AudioManager.OnAudioFocusChangeListener focusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) :
                        Log.i("TAG_NOTIF_AUDIO_PAYER", "AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                        break;
                    case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) :
                        Log.i("TAG_NOTIF_AUDIO_PAYER", "AudioManager.AUDIOFOCUS_LOSS_TRANSIENT");
                        break;

                    case (AudioManager.AUDIOFOCUS_LOSS) :
                        Log.i("TAG_NOTIF_AUDIO_PAYER", "AudioManager.AUDIOFOCUS_LOSS");
                        break;

                    case (AudioManager.AUDIOFOCUS_GAIN) :
                        Log.i("TAG_NOTIF_AUDIO_PAYER", "AudioManager.AUDIOFOCUS_GAIN");
                        break;
                    default: break;
                }
            }
        };

        // Request audio focus for playback
        int result = am.requestAudioFocus(focusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.i("TAG_NOTIF_AUDIO_PAYER", "AudioManager.AUDIOFOCUS_REQUEST_GRANTED");
            Uri mUri = Uri.parse(audios.get(positionSelected).getUrlacces()+audios.get(positionSelected).getSrc());
            try {
                mediaPlayer.setDataSource(getApplicationContext(), mUri);
                mediaPlayer.prepareAsync();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
