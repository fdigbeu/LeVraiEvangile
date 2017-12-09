package org.levraievangile.View.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;

import org.levraievangile.Model.Video;
import org.levraievangile.Presenter.VideoPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Adapters.VideoRecyclerAdapter;
import org.levraievangile.View.Interfaces.VideoView;
import org.levraievangile.View.Interfaces.VideoView.IVideo;

import java.util.ArrayList;

import static org.levraievangile.Presenter.CommonPresenter.KEY_VALUE_POSITION_VIDEO_SELECTED;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VALUE_VIDEO_PLAY_NEXT;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VALUE_VIDEO_PLAY_PREVIOUS;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VIDEO_PLAYER_RETURN_DATA;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VIDEO_PLAYER_SEND_DATA;
import static org.levraievangile.Presenter.CommonPresenter.VALUE_VIDEO_SELECTED_REQUEST_CODE;

public class VideoActivity extends AppCompatActivity implements IVideo {

    // Ref widgets
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    // Ref IVideoRecycler
    private VideoView.IVideoRecycler iVideoRecycler;
    // Presenter
    private VideoPresenter videoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        // Load presenter data
        videoPresenter = new VideoPresenter(this);
        videoPresenter.loadVideoData(VideoActivity.this, this.getIntent());
    }

    @Override
    public void initialize() {
        recyclerView = findViewById(R.id.videoRecyclerView);
        progressBar = findViewById(R.id.videoProgressBar);
    }

    @Override
    public void events() {

    }

    @Override
    public void loadVideoData(ArrayList<Video> videos, int numberColumns) {
        GridLayoutManager gridLayout = new GridLayoutManager(VideoActivity.this, numberColumns);
        recyclerView.setLayoutManager(gridLayout);
        recyclerView.setHasFixedSize(true);
        int resLayout = R.layout.item_ressource;
        VideoRecyclerAdapter adapter = new VideoRecyclerAdapter(videos, resLayout, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void scrollVideoDataToPosition(int positionScroll) {
        recyclerView.scrollToPosition(positionScroll);
    }

    @Override
    public void instanciateIVideoRecycler(VideoView.IVideoRecycler iVideoRecycler){
        this.iVideoRecycler = iVideoRecycler;
    }

    @Override
    public void launchVideoToPlay(Video video, int position) {
        Intent intent = new Intent(VideoActivity.this, VideoPlayerActivity.class);
        intent.putExtra(KEY_VIDEO_PLAYER_SEND_DATA, video);
        intent.putExtra(KEY_VALUE_POSITION_VIDEO_SELECTED, position);
        startActivityForResult(intent, VALUE_VIDEO_SELECTED_REQUEST_CODE);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VALUE_VIDEO_SELECTED_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra(KEY_VIDEO_PLAYER_RETURN_DATA);
                switch (result){
                    case KEY_VALUE_VIDEO_PLAY_NEXT:
                        videoPresenter.playNextVideoInPlayer(iVideoRecycler);
                        break;

                    case KEY_VALUE_VIDEO_PLAY_PREVIOUS:
                        videoPresenter.playPreviousVideoInPlayer(iVideoRecycler);
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
    public void progressBarVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void closeActivity() {
        this.finish();
    }
}
