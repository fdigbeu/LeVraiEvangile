package org.levraievangile.View.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import org.levraievangile.Model.Audio;
import org.levraievangile.Model.Favoris;
import org.levraievangile.Model.Video;
import org.levraievangile.Presenter.CommonPresenter;
import org.levraievangile.Presenter.FavorisPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Adapters.FavorisRecyclerAdapter;
import org.levraievangile.View.Interfaces.FavorisView;
import org.levraievangile.View.Interfaces.NotificationView;
import org.levraievangile.View.Services.PlayerAudioService;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.levraievangile.Presenter.CommonPresenter.KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_SRC;
import static org.levraievangile.Presenter.CommonPresenter.KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_TIME_ELAPSED;
import static org.levraievangile.Presenter.CommonPresenter.KEY_SHORT_CODE;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VALUE_POSITION_VIDEO_SELECTED;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VALUE_VIDEO_PLAY_NEXT;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VALUE_VIDEO_PLAY_PREVIOUS;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VIDEO_PLAYER_RETURN_DATA;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VIDEO_PLAYER_SEND_DATA;
import static org.levraievangile.Presenter.CommonPresenter.VALUE_PERMISSION_TO_SAVE_FILE;
import static org.levraievangile.Presenter.CommonPresenter.VALUE_VIDEO_SELECTED_REQUEST_CODE;

public class FavorisActivity extends AppCompatActivity implements FavorisView.IFravoris {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Toolbar toolbar;
    private TabLayout tabLayout;

    private View audioPlayerLayout;

    private FavorisView.IFavorisRecycler iFavorisRecycler;
    private FavorisView.IPlaceholder iPlaceholder;

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
    private FavorisPresenter favorisPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoris);
        // Load data favoris
        favorisPresenter = new FavorisPresenter(this);
        favorisPresenter.loadFavorisData(FavorisActivity.this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favoris, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                favorisPresenter.retrieveUserAction(item);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initialize() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = findViewById(R.id.tabs);

        audioPlayerLayout = findViewById(R.id.audioPlayerLayout);

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
        // Download View Pager
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                favorisPresenter.retrieveUserAction(tab.getPosition(), audio_player_play, isAudioSelected);
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                favorisPresenter.retrieveUserAction(tab.getPosition(), audio_player_play, isAudioSelected);
                mViewPager.setCurrentItem(tab.getPosition());
            }
        });

        // User click on play button
        audio_player_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favorisPresenter.retrievePlayPauseAction(mediaPlayer, audio_player_play);
            }
        });
        // User click on Next button
        audio_player_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favorisPresenter.retrieveUserAction(view);
            }
        });
        // User clicks on Previous button
        audio_player_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favorisPresenter.retrieveUserAction(view);
            }
        });
        // User clicks to close player
        audio_player_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favorisPresenter.retrieveUserAction(view, mediaPlayer);
            }
        });
        // User clicks to hide audio player
        fab_player_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favorisPresenter.retrieveUserAction(view, mediaPlayer);
            }
        });
        // User clicks to have volume
        fab_player_volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favorisPresenter.retrieveUserAction(view);
            }
        });
        // User clicks on download button
        fab_player_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favorisPresenter.retrieveUserAction(view);
            }
        });
        // User clicks on share button
        fab_player_share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favorisPresenter.retrieveUserAction(view);
            }
        });
        // User clicks on add to favorite button
        fab_player_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favorisPresenter.retrieveUserAction(view);
            }
        });
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements FavorisView.IPlaceholder {
        // Ref widgets
        private RecyclerView favorisRecyclerView;
        private ProgressBar favorisProgressBar;
        // Ref IFavorisRecycler
        private FavorisView.IFavorisRecycler iFavorisRecycler;
        // Ref IFavoris
        private FavorisView.IFravoris iFravoris;
        // Presenter
        private FavorisPresenter favorisPresenter;
        //--
        private int fragNumber;
        
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_favoris, container, false);

            // Show data
            fragNumber = getArguments().getInt(ARG_SECTION_NUMBER)-1;
            favorisPresenter = new FavorisPresenter(this);
            favorisPresenter.loadPlaceHolderData(getActivity(), rootView, fragNumber);
            return rootView;
        }

        @Override
        public void initialize(View rootView) {
            favorisRecyclerView = rootView.findViewById(R.id.favorisRecyclerView);
            favorisProgressBar = rootView.findViewById(R.id.favorisProgressBar);
        }

        @Override
        public void events() {

        }

        @Override
        public void loadFavorisAudioData(ArrayList<Favoris> favorites, int numberColumns) {
            GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), numberColumns);
            favorisRecyclerView.setLayoutManager(gridLayout);
            favorisRecyclerView.setHasFixedSize(true);
            FavorisRecyclerAdapter adapter = new FavorisRecyclerAdapter(favorites, this, iFravoris);
            favorisRecyclerView.setAdapter(adapter);
        }

        @Override
        public void scrollAudioDataToPosition(int positionScroll) {
            favorisRecyclerView.scrollToPosition(positionScroll);
        }

        @Override
        public void loadFavorisVideoData(ArrayList<Favoris> favorites, int numberColumns) {
            GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), numberColumns);
            favorisRecyclerView.setLayoutManager(gridLayout);
            favorisRecyclerView.setHasFixedSize(true);
            FavorisRecyclerAdapter adapter = new FavorisRecyclerAdapter(favorites, this);
            favorisRecyclerView.setAdapter(adapter);
        }

        @Override
        public void loadFavorisPdfData(ArrayList<Favoris> favorites, int numberColumns) {
            GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), numberColumns);
            favorisRecyclerView.setLayoutManager(gridLayout);
            favorisRecyclerView.setHasFixedSize(true);
            FavorisRecyclerAdapter adapter = new FavorisRecyclerAdapter(favorites, this);
            favorisRecyclerView.setAdapter(adapter);
        }

        @Override
        public void scrollVideoDataToPosition(int positionScroll) {
            favorisRecyclerView.scrollToPosition(positionScroll);
        }

        @Override
        public void progressBarVisibility(int visibility) {
            favorisProgressBar.setVisibility(visibility);
        }


        @Override
        public void launchActivity(String value) {
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra(KEY_SHORT_CODE, value);
            startActivity(intent);
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        @Override
        public void launchVideoToPlay(Video video, int position) {
            Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
            intent.putExtra(KEY_VIDEO_PLAYER_SEND_DATA, video);
            intent.putExtra(KEY_VALUE_POSITION_VIDEO_SELECTED, position);
            startActivityForResult(intent, VALUE_VIDEO_SELECTED_REQUEST_CODE);
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == VALUE_VIDEO_SELECTED_REQUEST_CODE) {
                if (resultCode == Activity.RESULT_OK) {
                    String result = data.getStringExtra(KEY_VIDEO_PLAYER_RETURN_DATA);
                    switch (result){
                        case KEY_VALUE_VIDEO_PLAY_NEXT:
                            favorisPresenter.playNextVideoInPlayer(iFavorisRecycler);
                            break;

                        case KEY_VALUE_VIDEO_PLAY_PREVIOUS:
                            favorisPresenter.playPreviousVideoInPlayer(iFavorisRecycler);
                            break;
                    }
                }
                else if (resultCode == Activity.RESULT_CANCELED) {
                    Log.i("TAG_VIDEO_FRAGMENT", "Activity.RESULT_CANCELED = "+requestCode);
                    Log.i("TAG_VIDEO_FRAGMENT", "Activity.RESULT_CANCELED = "+resultCode);
                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }

        @Override
        public void instanciateIFavorisRecycler(FavorisView.IFavorisRecycler iFavorisRecycler) {
            this.iFavorisRecycler = iFavorisRecycler;
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            context = getActivity();
            iFravoris =(FavorisView.IFravoris) context;
            ((FavorisActivity)context).instanciateIPlaceholder(this);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    @Override
    public void closeActivity() {
        favorisPresenter.stopMediaPlayer(mediaPlayer);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        favorisPresenter.stopMediaPlayer(mediaPlayer);
        super.onBackPressed();
    }


    @Override
    public void audioPlayerVisibility(int visibility) {
        audioPlayerLayout.setVisibility(visibility);
    }

    // Get Media player instance
    @Override
    public MediaPlayer getInstanceMediaPlayer(){
        return mediaPlayer;
    }

    @Override
    public void notifyThatAudioIsSelected() {
        isAudioSelected = true;
    }

    // Instanciate Iplaceholder from FavorisActivity
    public void instanciateIPlaceholder(FavorisView.IPlaceholder iPlaceholder) {
        this.iPlaceholder = iPlaceholder;
    }

    @Override
    public void askPermissionToSaveFile() {
        int permissionCheck = ContextCompat.checkSelfPermission(FavorisActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(FavorisActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, VALUE_PERMISSION_TO_SAVE_FILE);
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
                    Toast.makeText(FavorisActivity.this, getResources().getString(R.string.lb_storage_file_require), Toast.LENGTH_LONG).show();
                }
                break;
        }
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
        favorisPresenter.stopMediaPlayer(mediaPlayer);

        mediaPlayer = new MediaPlayer();

        favorisPresenter.stopAllOtherMediaSound(audio);

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
                    favorisPresenter.activateAllWidgets(true);
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
                favorisPresenter.retrieveOnCompletionAction(FavorisActivity.this);
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
                // Retrieve time to continue
                String timeElapse = CommonPresenter.getDataFromSharePreferences(FavorisActivity.this, KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_TIME_ELAPSED);
                if(timeElapse != null && !timeElapse.equalsIgnoreCase("0")){
                    String audioSrc = CommonPresenter.getDataFromSharePreferences(FavorisActivity.this, KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_SRC);
                    if(audioSrc != null && !audioSrc.isEmpty()) {
                        mediaPlayer.seekTo(Integer.parseInt(timeElapse));
                        CommonPresenter.saveDataInSharePreferences(FavorisActivity.this, KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_TIME_ELAPSED, "0");
                        CommonPresenter.saveDataInSharePreferences(FavorisActivity.this, KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_SRC, "");
                    }
                }
                //--
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
    public void instanciateIFavorisRecycler(FavorisView.IFavorisRecycler iFavorisRecycler) {
       this.iFavorisRecycler = iFavorisRecycler;
    }

    @Override
    public void playNextAudio() {
        favorisPresenter.playNextAudioInPlayer(iFavorisRecycler);
    }

    @Override
    public void playPreviousAudio() {
        favorisPresenter.playPreviousAudioInPlayer(iFavorisRecycler);
    }

    @Override
    public void playNotificationAudio() {
        isAudioSelected = false;
        Intent serviceIntent = new Intent(FavorisActivity.this, PlayerAudioService.class);
        serviceIntent.setAction(NotificationView.ACTION.STARTFOREGROUND_ACTION);
        startService(serviceIntent);
    }

    @Override
    public void stopNotificationAudio(){
        Intent serviceIntent = new Intent(FavorisActivity.this, PlayerAudioService.class);
        serviceIntent.setAction(NotificationView.ACTION.STOPFOREGROUND_ACTION);
        startService(serviceIntent);
    }
}
