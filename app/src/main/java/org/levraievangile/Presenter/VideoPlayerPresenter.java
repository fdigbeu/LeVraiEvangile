package org.levraievangile.Presenter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import org.levraievangile.Model.DAOFavoris;
import org.levraievangile.Model.Video;
import org.levraievangile.R;
import org.levraievangile.View.Interfaces.VideoPlayerView;
import org.levraievangile.View.Interfaces.VideoView;

import java.util.Hashtable;

import static org.levraievangile.Presenter.CommonPresenter.KEY_VIDEO_PLAYER_SEND_DATA;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VIDEO_SELECTED;
import static org.levraievangile.Presenter.CommonPresenter.saveDataInSharePreferences;

/**
 * Created by Maranatha on 27/11/2017.
 */

public class VideoPlayerPresenter {
    // Ref interface
    private VideoPlayerView.IVideoPlayer iVideoPlayer;

    // Ref video
    private Video videoSelected;

    // Constructor
    public VideoPlayerPresenter(VideoPlayerView.IVideoPlayer iVideoPlayer) {
        this.iVideoPlayer = iVideoPlayer;
    }

    // Load video player data
    public void loadVideoPlayerData(Context context, Intent intent){
        iVideoPlayer.hideHeader();
        iVideoPlayer.initialize();
        iVideoPlayer.events();
        //--
        iVideoPlayer.progressBarVisibility(View.VISIBLE);
        iVideoPlayer.headerVisibility(View.GONE);
        iVideoPlayer.fabVisibility(View.GONE);
        iVideoPlayer.btnNavigationVisibility(View.GONE);
        iVideoPlayer.pauseNotificationAudio();
        // Verify connexion state
        if(CommonPresenter.isMobileConnected(context)) {
            if(intent != null) {
                videoSelected = (Video) intent.getSerializableExtra(KEY_VIDEO_PLAYER_SEND_DATA);
                Hashtable<String, Integer> resolutionEcran = CommonPresenter.getScreenSize(context);
                iVideoPlayer.displayPlayer(videoSelected, resolutionEcran.get("largeur"), resolutionEcran.get("hauteur"));
                // Save video selected data
                saveDataInSharePreferences(context, KEY_VIDEO_SELECTED, videoSelected.toString());
            }
            else{
                iVideoPlayer.closeActivity();
            }
        }
        else{
            String title = context.getResources().getString(R.string.no_connection);
            String message = context.getResources().getString(R.string.detail_no_connection);
            CommonPresenter.showMessage(context, title.toUpperCase(), message, true);
        }
    }

    // When the video is finished
    public void retrieveOnCompletionAction(){
        iVideoPlayer.playNextVideo();
    }

    // Manage all video player button
    public void retrieveUserAction(View view){
        try {
            switch (view.getId()){
                // Download video
                case R.id.fab_player_download:
                    downloadThisVideo(view.getContext());
                    break;

                // Share video
                case R.id.fab_player_share_app:
                    CommonPresenter.shareVideo(view.getContext(), videoSelected);
                    break;

                // Add to video favorite
                case R.id.fab_player_favorite:
                    DAOFavoris daoFavoris = new DAOFavoris(view.getContext());
                    if(!daoFavoris.isFavorisExists(videoSelected.getSrc())){
                        daoFavoris.insertData("video",""+videoSelected.getMipmap(), videoSelected.getUrlacces(), videoSelected.getSrc(), videoSelected.getTitre(), videoSelected.getAuteur(), videoSelected.getDuree(), videoSelected.getType_libelle(), videoSelected.getType_shortcode(), ""+videoSelected.getId());
                        Toast.makeText(view.getContext(), view.getContext().getResources().getString(R.string.video_add_to_favorite), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(view.getContext(), view.getContext().getResources().getString(R.string.video_already_add_to_favorite), Toast.LENGTH_SHORT).show();
                    }
                    break;

                // Open volume for video player
                case R.id.fab_player_volume:
                    CommonPresenter.getApplicationVolume(view.getContext());
                    break;

                    // Hide video player
                case R.id.fab_player_down:
                    iVideoPlayer.closeActivity();
                    break;

                // Play previous video
                case R.id.btn_video_nav_left:
                    iVideoPlayer.playPreviousVideo();
                    break;

                // Play next video
                case R.id.btn_video_nav_right:
                    iVideoPlayer.playNextVideo();
                    break;
            }
        }
        catch (Exception ex){}
    }

    // Download video
    private void downloadThisVideo(Context context){
        if(videoSelected != null){
            if(CommonPresenter.isStorageDownloadFileAccepted(context)){
                String url = videoSelected.getUrlacces()+videoSelected.getSrc();
                String filename = videoSelected.getSrc();
                String description = "LVE-APP-DOWNLOADER ("+videoSelected.getDuree()+" | "+videoSelected.getAuteur()+")";
                CommonPresenter.getFileByDownloadManager(context, url, filename, description, "video");
                Toast.makeText(context, context.getResources().getString(R.string.lb_downloading), Toast.LENGTH_SHORT).show();
                Log.i("TAG_DOWNLOAD_FILE", "URL = "+url);
            }
            else{
                iVideoPlayer.askPermissionToSaveFile();
            }
        }
    }

    // Cancel count down timer
    public void cancelCountDownTimer(CountDownTimer countDownTimer){
        CommonPresenter.cancelCountDownTimer(countDownTimer);
    }

    // Stop video player
    public void stopVideoViewPlayer(android.widget.VideoView videoView){
        CommonPresenter.stopVideoViewPlayer(videoView);
    }

    // Manage player video visibility
    public void managePlayerVisibility(){
        iVideoPlayer.showPlayerWidgetsOnTouch();
    }

    // Manage widgets wen user touchs screen
    public void showWidgetsOnTouchEvent(boolean isWidgetsOpened){
        if(!isWidgetsOpened){
            iVideoPlayer.showPlayerWidgetsOnTouch();
        }
    }
}
