package org.levraievangile.View.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import org.levraievangile.Model.Audio;
import org.levraievangile.Presenter.AudioPresenter;
import org.levraievangile.Presenter.CommonPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Adapters.AudioRecyclerAdapter;
import org.levraievangile.View.Interfaces.AudioView;
import org.levraievangile.View.Interfaces.NotificationView;
import org.levraievangile.View.Services.PlayerAudioService;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.levraievangile.Presenter.CommonPresenter.VALUE_PERMISSION_TO_SAVE_FILE;

public class AudioActivity extends AppCompatActivity implements AudioView.IAudio{

    // Ref widgets
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private View audioPlayerLayout;
    // Ref interface
    private AudioView.IAudioRecycler iAudioRecycler;

    // Ref Audio player Widgets, Time, Mediaplayer
    private ProgressBar audio_player_progressbar;
    private TextView audio_player_titre, audio_player_soustitre;
    private TextView audio_player_time_progress, audio_player_time_end;
    private FloatingActionButton fab_player_download, fab_player_share_app, fab_player_favorite;
    private FloatingActionButton fab_player_volume, fab_player_notification;
    private SeekBar audio_player_seekbar;
    private ImageButton audio_player_previous, audio_player_play, audio_player_next, audio_player_close;
    private MediaPlayer mediaPlayer;
    private double timeElapsed = 0;
    private double finalTime = 0;
    private Handler durationHandler = new Handler();

    // Presenter
    private AudioPresenter audioPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        // Load presenter data
        audioPresenter = new AudioPresenter(this);
        audioPresenter.loadAudioData(AudioActivity.this, this.getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_audio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initialize() {
        recyclerView = findViewById(R.id.audioRecyclerView);
        progressBar = findViewById(R.id.audioProgressBar);
        audioPlayerLayout =  findViewById(R.id.audioPlayerLayout);

        // Display Home Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Widgets Audio player
        audio_player_progressbar = findViewById(R.id.audio_player_progressbar);
        audio_player_titre = findViewById(R.id.audio_player_titre);
        audio_player_time_progress = findViewById(R.id.audio_player_time_progress);
        audio_player_soustitre = findViewById(R.id.audio_player_soustitre);
        audio_player_time_end = findViewById(R.id.audio_player_time_end);
        fab_player_download = findViewById(R.id.fab_player_download);
        fab_player_share_app = findViewById(R.id.fab_player_share_app);
        fab_player_favorite = findViewById(R.id.fab_player_favorite);
        fab_player_volume = findViewById(R.id.fab_player_volume);
        fab_player_notification = findViewById(R.id.fab_player_notification);
        audio_player_seekbar = findViewById(R.id.audio_player_seekbar);
        audio_player_previous = findViewById(R.id.audio_player_previous);
        audio_player_play = findViewById(R.id.audio_player_play);
        audio_player_next = findViewById(R.id.audio_player_next);
        audio_player_close = findViewById(R.id.audio_player_close);
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public void events() {
        // User click on play button
        audio_player_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioPresenter.retrievePlayPauseAction(mediaPlayer, audio_player_play);
            }
        });
        // User click on Next button
        audio_player_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioPresenter.retrieveUserAction(view);
            }
        });
        // User clicks on Previous button
        audio_player_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioPresenter.retrieveUserAction(view);
            }
        });
        // User clicks to close player
        audio_player_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioPresenter.retrieveUserAction(view, mediaPlayer);
            }
        });
        // User clicks to hide audio player
        fab_player_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioPresenter.retrieveUserAction(view, mediaPlayer);
            }
        });
        // User clicks to have volume
        fab_player_volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioPresenter.retrieveUserAction(view);
            }
        });
        // User clicks on download button
        fab_player_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioPresenter.retrieveUserAction(view);
            }
        });
        // User clicks on share button
        fab_player_share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioPresenter.retrieveUserAction(view);
            }
        });
        // User clicks on add to favorite button
        fab_player_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioPresenter.retrieveUserAction(view);
            }
        });
    }

    @Override
    public void loadAudioData(ArrayList<Audio> audios, int numberColumns) {
        GridLayoutManager gridLayout = new GridLayoutManager(AudioActivity.this, numberColumns);
        recyclerView.setLayoutManager(gridLayout);
        recyclerView.setHasFixedSize(true);
        int resLayout = R.layout.item_ressource;
        AudioRecyclerAdapter adapter = new AudioRecyclerAdapter(audios, resLayout, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void scrollAudioDataToPosition(int positionScroll) {
        recyclerView.scrollToPosition(positionScroll);
    }

    @Override
    public void instanciateIAudioRecycler(AudioView.IAudioRecycler iAudioRecycler) {
        this.iAudioRecycler = iAudioRecycler;
    }

    @Override
    public void activateAudioPlayerWidgets(boolean enable) {
        fab_player_notification.setEnabled(enable);
        fab_player_volume.setEnabled(enable);
        fab_player_download.setEnabled(enable);
        fab_player_share_app.setEnabled(enable);
        fab_player_favorite.setEnabled(enable);
        audio_player_seekbar.setEnabled(enable);
        audio_player_previous.setEnabled(enable);
        audio_player_play.setEnabled(enable);
        audio_player_next.setEnabled(enable);
    }

    @Override
    public void loadAudioPlayerAndPlay(final Audio audio) {
        // If seekbar changed
        audio_player_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                durationHandler.removeCallbacks(updateSeekBarTime);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                durationHandler.removeCallbacks(updateSeekBarTime);
                finalTime = mediaPlayer.getDuration();
                timeElapsed = seekBar.getProgress();
                //---
                mediaPlayer.seekTo((int)timeElapsed);
                double timeRemaining = timeElapsed;
                audio_player_time_progress.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));
                audio_player_time_end.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) finalTime), TimeUnit.MILLISECONDS.toSeconds((long) finalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))));
                durationHandler.postDelayed(updateSeekBarTime, 100);
            }
        });

        // Prepare media
        audioPresenter.stopMediaPlayer(mediaPlayer);

        mediaPlayer = new MediaPlayer();

        audioPresenter.stopAllOtherMediaSound(audio);

        // If media is prepared...
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                try {
                    mediaPlayer.start();
                    finalTime = mediaPlayer.getDuration();
                    timeElapsed = mediaPlayer.getCurrentPosition();
                    audio_player_seekbar.setProgress((int) timeElapsed);
                    audio_player_seekbar.setMax((int) finalTime);
                    durationHandler.postDelayed(updateSeekBarTime, 100);
                    //--
                    audioPresenter.activateAllWidgets(true);
                    audio_player_progressbar.setVisibility(View.GONE);
                    //--
                    String dateFormat = CommonPresenter.changeFormatDate(audio.getDate());
                    String durationFormat = CommonPresenter.changeFormatDuration(audio.getDuree());
                    audio_player_titre.setText(audio.getTitre());
                    audio_player_soustitre.setText(dateFormat+" | "+durationFormat+" | "+audio.getAuteur());
                    audio_player_play.setBackgroundResource(R.drawable.btn_media_player_pause);
                }
                catch (Exception ex){}
            }
        });

        // If media finished work
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                audioPresenter.retrieveOnCompletionAction(AudioActivity.this);
            }
        });

        // Return TRUE to avoid to call onCompletion() twice.
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                audio_player_progressbar.setVisibility(View.GONE);
                return true;
            }
        });
    }

    // Pause to other media player
    @Override
    public void stopOtherMediaPlayerSound(Audio audio){
        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        AudioManager.OnAudioFocusChangeListener focusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) :
                        Log.i("TAG_AUDIO_PAYER", "AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                        break;
                    case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) :
                        Log.i("TAG_AUDIO_PAYER", "AudioManager.AUDIOFOCUS_LOSS_TRANSIENT");
                        break;

                    case (AudioManager.AUDIOFOCUS_LOSS) :
                        Log.i("TAG_AUDIO_PAYER", "AudioManager.AUDIOFOCUS_LOSS");
                        break;

                    case (AudioManager.AUDIOFOCUS_GAIN) :
                        Log.i("TAG_AUDIO_PAYER", "AudioManager.AUDIOFOCUS_GAIN");
                        break;
                    default: break;
                }
            }
        };

        // Request audio focus for playback
        int result = am.requestAudioFocus(focusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.i("TAG_AUDIO_PAYER", "AudioManager.AUDIOFOCUS_REQUEST_GRANTED");
            Uri mUri = Uri.parse(audio.getUrlacces()+audio.getSrc());
            try {
                mediaPlayer.setDataSource(getApplicationContext(), mUri);
                mediaPlayer.prepareAsync();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //handler to change seekBarTime
    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            try {
                finalTime = mediaPlayer.getDuration();
                timeElapsed = mediaPlayer.getCurrentPosition();
                audio_player_seekbar.setProgress((int) timeElapsed);
                audio_player_seekbar.setMax((int) finalTime);
                //set time remaing
                double timeRemaining = timeElapsed;
                audio_player_time_progress.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));
                audio_player_time_end.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) finalTime), TimeUnit.MILLISECONDS.toSeconds((long) finalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))));
                durationHandler.postDelayed(updateSeekBarTime, 100);
            }
            catch (Exception ex){}
        }
    };

    @Override
    public void progressBarAudioPlayerVisibility(int visibility) {
        audio_player_progressbar.setVisibility(visibility);
    }

    @Override
    public void launchNotificationAudio(){
        Intent serviceIntent = new Intent(AudioActivity.this, PlayerAudioService.class);
        serviceIntent.setAction(NotificationView.ACTION.STARTFOREGROUND_ACTION);
        startService(serviceIntent);
    }

    @Override
    public void progressBarVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }
    
    @Override
    public void playNextAudio() {
        audioPresenter.playNextAudioInPlayer(iAudioRecycler);
    }

    @Override
    public void playPreviousAudio() {
        audioPresenter.playPreviousAudioInPlayer(iAudioRecycler);
    }

    @Override
    public void playNotificationAudio() {
        audioPresenter.notificationPlayAudio();
    }

    @Override
    public void stopNotificationAudio(){
        Intent serviceIntent = new Intent(AudioActivity.this, PlayerAudioService.class);
        serviceIntent.setAction(NotificationView.ACTION.STOPFOREGROUND_ACTION);
        startService(serviceIntent);
    }


    @Override
    public void closeActivity() {
        onBackPressed();
    }

    @Override
    public void modifyHeaderInfos(String typeLibelle) {
        getSupportActionBar().setTitle(getResources().getString(R.string.tab_text_audio)+" ("+typeLibelle+")");
    }

    @Override
    public void audioPlayerVisibility(int visibility) {
        audioPlayerLayout.setVisibility(visibility);
    }

    @Override
    public void textMediaPlayInfoLoading() {
        audio_player_titre.setText(getResources().getString(R.string.lb_audio_player_title));
        audio_player_soustitre.setText(getResources().getString(R.string.lb_audio_player_date_auteur));
        audio_player_play.setBackgroundResource(R.drawable.btn_media_player_play);
    }


    @Override
    public void askPermissionToSaveFile() {
        int permissionCheck = ContextCompat.checkSelfPermission(AudioActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AudioActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, VALUE_PERMISSION_TO_SAVE_FILE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case VALUE_PERMISSION_TO_SAVE_FILE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // Ceate folders
                    CommonPresenter.createFolder();
                }
                else {
                    Toast.makeText(AudioActivity.this, getResources().getString(R.string.lb_storage_file_require), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        audioPresenter.stopMediaPlayer(mediaPlayer);
        super.onBackPressed();
    }
}
