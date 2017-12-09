package org.levraievangile.View.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
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

import static org.levraievangile.Presenter.CommonPresenter.KEY_VALUE_VIDEO_PLAY_NEXT;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VALUE_VIDEO_PLAY_PREVIOUS;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VIDEO_PLAYER_RETURN_DATA;

public class VideoPlayerActivity extends AppCompatActivity implements VideoPlayerView.IVideoPlayer {

    // Ref widgets
    private VideoView player_video;
    private MediaController mediaController;
    private ProgressBar progress_player_video;
    private TextView title_video;
    private View fab_player_layout;
    private ImageButton btn_video_nav_left, btn_video_nav_right;
    // Ref fab button
    private FloatingActionButton fab_player_download, fab_player_share_app;
    private FloatingActionButton fab_player_favorite;
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
        title_video = (TextView)findViewById(R.id.titre_video);
        progress_player_video = (ProgressBar)findViewById(R.id.progress_player_video);
        player_video = (VideoView)findViewById(R.id.player_video);
        mediaController = new MediaController(VideoPlayerActivity.this, false);
        player_video.setMediaController(mediaController);
        //--
        btn_video_nav_left = findViewById(R.id.btn_video_nav_left);
        btn_video_nav_right = findViewById(R.id.btn_video_nav_right);
        // Fab button
        fab_player_download = findViewById(R.id.fab_player_download);
        fab_player_share_app = findViewById(R.id.fab_player_share_app);
        fab_player_favorite = findViewById(R.id.fab_player_favorite);
        fab_player_favorite.setVisibility(View.GONE);
        fab_player_volume = findViewById(R.id.fab_player_volume);
        fab_player_down = findViewById(R.id.fab_player_down);
    }

    @Override
    public void events() {
        // Fab download video
        fab_player_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerPresenter.retrieveUserAction(VideoPlayerActivity.this, view);
            }
        });
        // Fab share video
        fab_player_share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerPresenter.retrieveUserAction(VideoPlayerActivity.this, view);
            }
        });
        // Fab add to video favorite
        fab_player_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerPresenter.retrieveUserAction(VideoPlayerActivity.this, view);
            }
        });
        // Fab volume video
        fab_player_volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerPresenter.retrieveUserAction(VideoPlayerActivity.this, view);
            }
        });
        //Fab exit
        fab_player_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerPresenter.retrieveUserAction(VideoPlayerActivity.this, view);
            }
        });
        // User clicks on previous video button
        btn_video_nav_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerPresenter.retrieveUserAction(VideoPlayerActivity.this, view);
            }
        });
        // User clicks on next video button
        btn_video_nav_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerPresenter.retrieveUserAction(VideoPlayerActivity.this, view);
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
                playerPresenter.retrieveOnCompletionAction(VideoPlayerActivity.this);
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
    public void showPlayerWidgetsOnTouch() {
        progress_player_video.setVisibility(View.GONE);
        title_video.setVisibility(View.VISIBLE);
        fab_player_layout.setVisibility(View.VISIBLE);
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
    public void downloadVideo(Context context) {
        /*Ressource ressource = (Ressource)getIntent().getSerializableExtra(KEY_VIDEO_PLAYER_SEND_DATA);
        String url = ressource.getUrlacces()+ressource.getSrc();
        String filename = ressource.getSrc();
        String description = "QR-APP-DOWNLOADER ("+ressource.getDuree()+" | "+ressource.getAuteur()+")";
        CommonPresenter.getFileByDownloadManager(context, url, filename, description, "video");
        Toast.makeText(context, context.getResources().getString(R.string.lb_downloading), Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public void shareVideo(Context context) {
        /*Ressource ressource = (Ressource)getIntent().getSerializableExtra(KEY_VIDEO_PLAYER_SEND_DATA);
        CommonPresenter.shareRessource(context, ressource, "video");*/
    }

    @Override
    public void addVideoToFavorite(Context context) {
        /*Ressource ressource = (Ressource)getIntent().getSerializableExtra(KEY_VIDEO_PLAYER_SEND_DATA);
        playerPresenter.saveRessourceVideoData(context, ressource);*/
    }

    @Override
    public void closeActivity() {
        this.finish();
    }

    @Override
    public void pauseNotificationAudio() {
        Intent serviceIntent = new Intent(VideoPlayerActivity.this, PlayerAudioService.class);
        serviceIntent.setAction(NotificationView.ACTION.PAUSE_ACTION);
        startService(serviceIntent);
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
        CommonPresenter.cancelCountDownTimer(downTimer);
        CommonPresenter.stopVideoViewPlayer(player_video);
        super.onBackPressed();
    }
}
