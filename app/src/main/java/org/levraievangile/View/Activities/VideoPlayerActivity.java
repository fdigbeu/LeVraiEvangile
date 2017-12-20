package org.levraievangile.View.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.github.clans.fab.FloatingActionButton;

import org.levraievangile.Model.Video;
import org.levraievangile.Presenter.CommonPresenter;
import org.levraievangile.Presenter.VideoPlayerPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Interfaces.NotificationView;
import org.levraievangile.View.Interfaces.VideoPlayerView;
import org.levraievangile.View.Services.PlayerAudioService;

import static org.levraievangile.Presenter.CommonPresenter.KEY_VALUE_POSITION_VIDEO_SELECTED;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VALUE_VIDEO_PLAY_NEXT;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VALUE_VIDEO_PLAY_PREVIOUS;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VIDEO_PLAYER_RETURN_DATA;
import static org.levraievangile.Presenter.CommonPresenter.VALUE_PERMISSION_TO_SAVE_FILE;

public class VideoPlayerActivity extends AppCompatActivity implements VideoPlayerView.IVideoPlayer {

    // Ref widgets
    private VideoView player_video;
    private MediaController mediaController;
    private ProgressBar progress_player_video;
    private TextView title_video;
    private View fab_player_layout, fab_player_layout_orientation;
    private ImageButton btn_video_nav_left, btn_video_nav_right;
    // Ref fab button
    private FloatingActionButton fab_player_download, fab_player_share_app;
    private FloatingActionButton fab_player_favorite, fab_player_screen_orientation;
    private FloatingActionButton fab_player_volume, fab_player_down;

    private CountDownTimer downTimer;
    private boolean widgetsIsOpened;

    // Presenter
    private VideoPlayerPresenter playerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_player);
        // Load video player data
        playerPresenter = new VideoPlayerPresenter(this);
        playerPresenter.loadVideoPlayerData(VideoPlayerActivity.this, this.getIntent());
    }

    @Override
    public void hideHeader() {
        getSupportActionBar().hide();
    }

    @Override
    public void initialize() {
        fab_player_layout = findViewById(R.id.fab_player_layout);
        fab_player_layout_orientation = findViewById(R.id.fab_player_layout_orientation);
        title_video = findViewById(R.id.titre_video);
        progress_player_video = findViewById(R.id.progress_player_video);
        player_video = findViewById(R.id.player_video);
        mediaController = new MediaController(VideoPlayerActivity.this, false);
        player_video.setMediaController(mediaController);
        //--
        btn_video_nav_left = findViewById(R.id.btn_video_nav_left);
        btn_video_nav_right = findViewById(R.id.btn_video_nav_right);
        // Fab button
        fab_player_download = findViewById(R.id.fab_player_download);
        fab_player_share_app = findViewById(R.id.fab_player_share_app);
        fab_player_favorite = findViewById(R.id.fab_player_favorite);
        fab_player_screen_orientation = findViewById(R.id.fab_player_screen_orientation);
        fab_player_volume = findViewById(R.id.fab_player_volume);
        fab_player_down = findViewById(R.id.fab_player_down);
    }

    @Override
    public void events() {
        // Fab download video
        fab_player_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerPresenter.retrieveUserAction(view);
            }
        });
        // Fab share video
        fab_player_share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerPresenter.retrieveUserAction(view);
            }
        });
        // Fab add to video favorite
        fab_player_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerPresenter.retrieveUserAction(view);
            }
        });
        // Fab screen orientation
        fab_player_screen_orientation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerPresenter.retrieveUserAction(view);
            }
        });
        // Fab volume video
        fab_player_volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerPresenter.retrieveUserAction(view);
            }
        });
        //Fab exit
        fab_player_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerPresenter.retrieveUserAction(view);
            }
        });
        // User clicks on previous video button
        btn_video_nav_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerPresenter.retrieveUserAction(view);
            }
        });
        // User clicks on next video button
        btn_video_nav_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerPresenter.retrieveUserAction(view);
            }
        });
    }

    @Override
    public void displayPlayer(Video ressource, int largeurScreen, int hauteurScreen) {
        title_video.setText(ressource.getTitre());
        // Media player
        Uri mUriVideo = Uri.parse(ressource.getUrlacces()+ressource.getSrc());
        player_video.setVideoURI(mUriVideo);
        player_video.setMinimumWidth(largeurScreen);
        player_video.setMinimumHeight(hauteurScreen);
        player_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                player_video.start();
                player_video.requestFocus();
                player_video.setBackgroundColor(Color.TRANSPARENT);
                playerPresenter.managePlayerVisibility();
                //--
                int childs = mediaController.getChildCount();
                for (int i = 0; i < childs; i++)
                {
                    View child = mediaController.getChildAt(i);
                    child.setVisibility (View.VISIBLE);
                }
            }
        });

        player_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playerPresenter.retrieveOnCompletionAction();
            }
        });

        player_video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                progress_player_video.setVisibility(View.GONE);
                title_video.setVisibility(View.GONE);
                return true;
            }
        });
    }

    @Override
    public void fabVisibility(int visibility) {
        fab_player_layout.setVisibility(visibility);
        fab_player_layout_orientation.setVisibility(visibility);
    }

    @Override
    public void headerVisibility(int visibility) {
        title_video.setVisibility(visibility);
    }

    @Override
    public void progressBarVisibility(int visibility) {
        progress_player_video.setVisibility(visibility);
    }

    @Override
    public void btnNavigationVisibility(int visibility) {
        btn_video_nav_left.setVisibility(visibility);
        btn_video_nav_right.setVisibility(visibility);
    }

    @Override
    public void hideBtnDownloadShareFavorite() {
        fab_player_download.setVisibility(View.GONE);
        fab_player_share_app.setVisibility(View.GONE);
        fab_player_favorite.setVisibility(View.GONE);
    }

    @Override
    public void showPlayerWidgetsOnTouch() {
        progress_player_video.setVisibility(View.GONE);
        title_video.setVisibility(View.VISIBLE);
        fab_player_layout.setVisibility(View.VISIBLE);
        fab_player_layout_orientation.setVisibility(View.VISIBLE);
        btn_video_nav_left.setVisibility(View.VISIBLE);
        btn_video_nav_right.setVisibility(View.VISIBLE);
        widgetsIsOpened = true;
        downTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {}

            @Override
            public void onFinish() {
                title_video.setVisibility(View.GONE);
                fab_player_layout.setVisibility(View.GONE);
                fab_player_layout_orientation.setVisibility(View.GONE);
                btn_video_nav_left.setVisibility(View.GONE);
                btn_video_nav_right.setVisibility(View.GONE);
                widgetsIsOpened = false;
            }
        }.start();
    }

    @Override
    public void playNextVideo() {
        Intent intent = new Intent();
        intent.putExtra(KEY_VIDEO_PLAYER_RETURN_DATA, KEY_VALUE_VIDEO_PLAY_NEXT);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void playPreviousVideo() {
        Intent intent = new Intent();
        intent.putExtra(KEY_VIDEO_PLAYER_RETURN_DATA, KEY_VALUE_VIDEO_PLAY_PREVIOUS);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        playerPresenter.showWidgetsOnTouchEvent(widgetsIsOpened);
        return super.onTouchEvent(event);
    }

    @Override
    public void closeActivity() {
        playerPresenter.cancelCountDownTimer(downTimer);
        playerPresenter.stopVideoViewPlayer(player_video);
        this.finish();
    }

    @Override
    public void pauseNotificationAudio() {
        Intent serviceIntent = new Intent(VideoPlayerActivity.this, PlayerAudioService.class);
        serviceIntent.setAction(NotificationView.ACTION.PAUSE_ACTION);
        startService(serviceIntent);
    }

    @Override
    public boolean isCurrentVideoIsNotification() {
        String currentData = this.getIntent().getStringExtra(KEY_VALUE_POSITION_VIDEO_SELECTED);
        return (currentData == null || currentData.isEmpty() ? false : true);
    }

    @Override
    public void askPermissionToSaveFile() {
        int permissionCheck = ContextCompat.checkSelfPermission(VideoPlayerActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(VideoPlayerActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, VALUE_PERMISSION_TO_SAVE_FILE);
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
                    Toast.makeText(VideoPlayerActivity.this, getResources().getString(R.string.lb_storage_file_require), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            if (hasFocus) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }

    @Override
    public void onBackPressed() {
        playerPresenter.onActivityBackPressed(VideoPlayerActivity.this);
    }
}
