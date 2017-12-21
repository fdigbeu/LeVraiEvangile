package org.levraievangile.View.Activities;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

import org.levraievangile.Model.Audio;
import org.levraievangile.Model.DownloadFile;
import org.levraievangile.Presenter.CommonPresenter;
import org.levraievangile.Presenter.DownloadPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Adapters.DownloadPagerAdapter;
import org.levraievangile.View.Fragments.DownloadAudioFragment;
import org.levraievangile.View.Fragments.DownloadPdfFragment;
import org.levraievangile.View.Fragments.DownloadVideoFragment;
import org.levraievangile.View.Interfaces.DownloadView;
import org.levraievangile.View.Interfaces.NotificationView;
import org.levraievangile.View.Services.PlayerAudioService;
import org.levraievangile.View.ViewPagers.DownloadViewPager;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

public class DownloadActivity extends AppCompatActivity implements DownloadView.IDownload {

    // Ref interfaces
    private Hashtable<Integer, ArrayList<DownloadFile>> downloadFilesList;
    private DownloadView.IDownloadAudioView iDownloadAudioView;
    private DownloadView.IDownloadVideoView iDownloadVideoView;
    private DownloadView.IDownloadPdfView iDownloadPdfView;
    private DownloadView.IDownloadAudioRecycler iDownloadAudioRecycler;

    // Ref widget
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private View audioPlayerLayout;

    // Ref collections and adapter
    private DownloadPagerAdapter pagerAdapter;
    private ArrayList<Fragment> fragDownloads;
    private ArrayList<String> titleDownload;
    private DownloadViewPager downloadViewPager;

    private boolean isAudioSelected;

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
    private DownloadPresenter downloadPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        // Load download data
        downloadFilesList = new Hashtable<>();
        downloadPresenter = new DownloadPresenter(this);
        downloadPresenter.loadDownloadData(DownloadActivity.this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_download, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                downloadPresenter.retrieveUserAction(item);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initialize() {
        // Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tabs);

        audioPlayerLayout = findViewById(R.id.audioPlayerLayout);

        // Toolbar contents
        titleDownload = new ArrayList<>();
        titleDownload.add(getResources().getString(R.string.tab_text_audio));
        titleDownload.add(getResources().getString(R.string.tab_text_video));
        titleDownload.add(getResources().getString(R.string.tab_text_pdf));

        // ViewPager contents
        fragDownloads = new ArrayList<>();
        fragDownloads.add(Fragment.instantiate(this, DownloadAudioFragment.class.getName()));
        fragDownloads.add(Fragment.instantiate(this, DownloadVideoFragment.class.getName()));
        fragDownloads.add(Fragment.instantiate(this, DownloadPdfFragment.class.getName()));
        this.pagerAdapter  = new DownloadPagerAdapter(super.getSupportFragmentManager(), fragDownloads, titleDownload);

        // Set up the ViewPager with the sections adapter.
        downloadViewPager = findViewById(R.id.container);
        downloadViewPager.setAdapter(this.pagerAdapter);
        downloadViewPager.setPagingEnabled(true);

        // TabLayout
        tabLayout.setupWithViewPager(downloadViewPager);

        // Display Home Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Widgets Audio player
        audio_player_progressbar = findViewById(R.id.audio_player_progressbar);
        audio_player_titre = findViewById(R.id.audio_player_titre);
        audio_player_time_progress = findViewById(R.id.audio_player_time_progress);
        audio_player_soustitre = findViewById(R.id.audio_player_soustitre);
        audio_player_time_end = findViewById(R.id.audio_player_time_end);
        fab_player_download = findViewById(R.id.fab_player_download);
        fab_player_download.setVisibility(View.GONE);
        fab_player_share_app = findViewById(R.id.fab_player_share_app);
        fab_player_share_app.setVisibility(View.GONE);
        fab_player_favorite = findViewById(R.id.fab_player_favorite);
        fab_player_favorite.setVisibility(View.GONE);
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
        // Download View Pager
        downloadViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                downloadPresenter.retrieveUserAction(position, audio_player_play, isAudioSelected);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(downloadViewPager));
        // User click on play button
        audio_player_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadPresenter.retrievePlayPauseAction(mediaPlayer, audio_player_play);
            }
        });
        // User click on Next button
        audio_player_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadPresenter.retrieveUserAction(view);
            }
        });
        // User clicks on Previous button
        audio_player_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadPresenter.retrieveUserAction(view);
            }
        });
        // User clicks to close player
        audio_player_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadPresenter.retrieveUserAction(view, mediaPlayer);
            }
        });
        // User clicks to hide audio player
        fab_player_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadPresenter.retrieveUserAction(view, mediaPlayer);
            }
        });
        // User clicks to have volume
        fab_player_volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadPresenter.retrieveUserAction(view);
            }
        });
    }

    @Override
    public void onAudioSelected(Audio audio, int position) {
        isAudioSelected = true;
        downloadPresenter.playLVEAudioPlayer(DownloadActivity.this, audio, position);
    }

    @Override
    public void closeActivity() {
        downloadPresenter.cancelAsyntask();
        downloadPresenter.stopMediaPlayer(mediaPlayer);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        downloadPresenter.cancelAsyntask();
        downloadPresenter.stopMediaPlayer(mediaPlayer);
        super.onBackPressed();
    }

    // Get Media player instance
    @Override
    public MediaPlayer getInstanceMediaPlayer(){
        return mediaPlayer;
    }


    @Override
    public void instanciateIDownloadAudioRecycler(DownloadView.IDownloadAudioRecycler iDownloadAudioRecycler) {
        this.iDownloadAudioRecycler = iDownloadAudioRecycler;
    }

    // Ref interfaces instanciation
    public void instanciateIDownloadAudioView(DownloadView.IDownloadAudioView iDownloadAudioView) {
        this.iDownloadAudioView = iDownloadAudioView;
    }

    public void instanciateIDownloadVideoView(DownloadView.IDownloadVideoView iDownloadVideoView) {
        this.iDownloadVideoView = iDownloadVideoView;
    }

    public void instanciateIDownloadPdfView(DownloadView.IDownloadPdfView iDownloadPdfView) {
        this.iDownloadPdfView = iDownloadPdfView;
    }

    // Storage download files list
    @Override
    public void storageDownloadFilesList(int key,  ArrayList<DownloadFile> downloadFilesList) {
        this.downloadFilesList.put(key, downloadFilesList);
    }
    // Get Storage download audios files
    @Override
    public ArrayList<DownloadFile> getStorageDownloadFilesAudioData(){
        try {
            return downloadFilesList.get(0);
        }
        catch (Exception ex){}
        return null;
    }
    // Get Storage download videos files
    @Override
    public ArrayList<DownloadFile> getStorageDownloadFilesVideoData(){
        try {
            return downloadFilesList.get(1);
        }
        catch (Exception ex){}
        return null;
    }
    // Get Storage download pdf files
    @Override
    public ArrayList<DownloadFile> getStorageDownloadFilesPdfData(){
        try {
            return downloadFilesList.get(2);
        }
        catch (Exception ex){}
        return null;
    }

    @Override
    public void audioPlayerVisibility(int visibility) {
        audioPlayerLayout.setVisibility(visibility);
    }

    ///////////////////////////////////////////////// PLAYER AUDIO ////////////////////////////////////////////////////
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
        downloadPresenter.stopMediaPlayer(mediaPlayer);

        mediaPlayer = new MediaPlayer();

        downloadPresenter.stopAllOtherMediaSound(audio);

        // If media is prepared...
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                try {
                    mediaPlayer.start();
                    //--
                    finalTime = mediaPlayer.getDuration();
                    timeElapsed = mediaPlayer.getCurrentPosition();
                    audio_player_seekbar.setProgress((int) timeElapsed);
                    audio_player_seekbar.setMax((int) finalTime);
                    durationHandler.postDelayed(updateSeekBarTime, 100);
                    //--
                    downloadPresenter.activateAllWidgets(true);
                    audio_player_progressbar.setVisibility(View.GONE);
                    //--
                    String dateFormat = CommonPresenter.changeFormatDate(audio.getDate());
                    String durationFormat = CommonPresenter.changeFormatDuration(audio.getDuree());
                    audio_player_titre.setText(audio.getTitre());
                    audio_player_soustitre.setText(durationFormat+" | "+audio.getAuteur());
                    audio_player_play.setBackgroundResource(R.drawable.btn_media_player_pause);
                }
                catch (Exception ex){}
            }
        });

        // If media finished work
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                downloadPresenter.retrieveOnCompletionAction();
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
    public void textMediaPlayInfoLoading() {
        audio_player_titre.setText(getResources().getString(R.string.lb_audio_player_title));
        audio_player_soustitre.setText(getResources().getString(R.string.lb_audio_player_date_auteur));
        audio_player_play.setBackgroundResource(R.drawable.btn_media_player_play);
    }

    @Override
    public void playNextAudio() {
        downloadPresenter.playNextAudioInPlayer(iDownloadAudioRecycler);
    }

    @Override
    public void playPreviousAudio() {
        downloadPresenter.playPreviousAudioInPlayer(iDownloadAudioRecycler);
    }

    @Override
    public void playNotificationAudio() {
        isAudioSelected = false;
        Intent serviceIntent = new Intent(DownloadActivity.this, PlayerAudioService.class);
        serviceIntent.setAction(NotificationView.ACTION.STARTFOREGROUND_ACTION);
        startService(serviceIntent);
    }

    @Override
    public void stopNotificationAudio(){
        Intent serviceIntent = new Intent(DownloadActivity.this, PlayerAudioService.class);
        serviceIntent.setAction(NotificationView.ACTION.STOPFOREGROUND_ACTION);
        startService(serviceIntent);
    }
}
