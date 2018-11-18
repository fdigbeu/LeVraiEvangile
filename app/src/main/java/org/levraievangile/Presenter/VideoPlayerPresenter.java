package org.levraievangile.Presenter;

import android.Manifest;
import android.app.Activity;
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
import org.levraievangile.Model.Favoris;
import org.levraievangile.Model.Setting;
import org.levraievangile.Model.Video;
import org.levraievangile.R;
import org.levraievangile.View.Activities.HomeActivity;
import org.levraievangile.View.Interfaces.VideoPlayerView;
import org.levraievangile.View.Interfaces.VideoView;

import java.util.Hashtable;

import static org.levraievangile.Presenter.CommonPresenter.KEY_SETTING_CONCATENATE_VIDEO_READING;
import static org.levraievangile.Presenter.CommonPresenter.KEY_SETTING_WIFI_EXCLUSIF;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VALUE_POSITION_VIDEO_SELECTED;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VIDEO_PLAYER_SEND_DATA;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VIDEO_SELECTED;
import static org.levraievangile.Presenter.CommonPresenter.saveDataInSharePreferences;

/**
 * Created by Maranatha on 27/11/2017.
 */

public class VideoPlayerPresenter {
    // Ref interface
    private VideoPlayerView.IVideoPlayer iVideoPlayer;
    // Ref CountDownTimer
    private CountDownTimer countDownTimer;

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

        if(intent != null) {
            boolean continueAction = false;
            videoSelected = (Video) intent.getSerializableExtra(KEY_VIDEO_PLAYER_SEND_DATA);
            if(videoSelected.getId()==0){
                iVideoPlayer.hideBtnDownloadShareFavorite();
                continueAction = true;
            }
            else{
                // Verify connexion state
                if(CommonPresenter.isMobileConnected(context)) {
                    continueAction = true;
                }
                else{
                    String title = context.getResources().getString(R.string.no_connection);
                    String message = context.getResources().getString(R.string.detail_no_connection);
                    CommonPresenter.showMessage(context, title.toUpperCase(), message, true);
                }
            }
            //--
            if(continueAction){
                Hashtable<String, Integer> resolutionEcran = CommonPresenter.getScreenSize(context);
                iVideoPlayer.displayPlayer(videoSelected, resolutionEcran.get("largeur"), resolutionEcran.get("hauteur"));
                // Save video selected data
                saveDataInSharePreferences(context, KEY_VIDEO_SELECTED, videoSelected.toString());
            }
            // Show Top buttom during 2 secondes
            try {
                iVideoPlayer.fabTopVisibility(View.VISIBLE);
                countDownTimer = new CountDownTimer(2000, 500) {
                    @Override
                    public void onTick(long l) {}

                    @Override
                    public void onFinish() {
                        if(countDownTimer != null){
                            try {
                                iVideoPlayer.fabTopVisibility(View.GONE);
                                countDownTimer.cancel();
                            }
                            catch (Exception ex){}
                        }
                    }
                }.start();
            }
            catch (Exception ex){}
        }
        else{
            iVideoPlayer.closeActivity();
        }
    }

    // Mange back pressed
    public void onActivityBackPressed(Context context){
        if(iVideoPlayer.isCurrentVideoIsNotification()){
            Intent intent = new Intent(context, HomeActivity.class);
            context.startActivity(intent);
            ((Activity)context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        //--
        iVideoPlayer.closeActivity();
    }

    // When the video is finished
    public void retrieveOnCompletionAction(Context context){
        // If user accepts automatic reading
        Setting mSetting = CommonPresenter.getSettingObjectFromSharePreferences(context, KEY_SETTING_CONCATENATE_VIDEO_READING);
        if(mSetting.getChoice()) {
            iVideoPlayer.playNextVideo();
        }
    }

    // Play video with local player
    public void playVideoWithLoacalPlayer(Context context, Intent intent){
        try {
            if(intent != null && iVideoPlayer != null){
                videoSelected = (Video) intent.getSerializableExtra(KEY_VIDEO_PLAYER_SEND_DATA);
                if(videoSelected != null){
                    String urlVideo = videoSelected.getUrlacces()+videoSelected.getSrc();
                    CommonPresenter.playVideoFromUrl(context, urlVideo);
                    iVideoPlayer.closeActivity();
                }
            }
        }
        catch (Exception ex){}
    }

    // Manage all video player button
    public void retrieveUserAction(View view){
        try {
            switch (view.getId()){
                // Download video
                case R.id.fab_player_download:
                    boolean isAuthorizeDownload = false;
                    Setting mSetting = CommonPresenter.getSettingObjectFromSharePreferences(view.getContext(), KEY_SETTING_WIFI_EXCLUSIF);
                    if(mSetting.getChoice()){
                        if(CommonPresenter.isMobileWIFIConnected(view.getContext())){
                            isAuthorizeDownload = true;
                        }
                        else{
                            CommonPresenter.showWifiExclusiveMessage(view.getContext());
                        }
                    }
                    else {
                        isAuthorizeDownload = true;
                    }
                    //--
                    if(isAuthorizeDownload){
                        downloadThisVideo(view.getContext());
                    }
                    break;

                // Share video
                case R.id.fab_player_share_app:
                    CommonPresenter.shareVideo(view.getContext(), videoSelected);
                    break;

                // Add to video favorite
                case R.id.fab_player_favorite:
                    DAOFavoris daoFavoris = new DAOFavoris(view.getContext());
                    if(!daoFavoris.isFavorisExists(videoSelected.getSrc(), "video")){

                        Favoris favoris = new Favoris(videoSelected.getId(), "video", videoSelected.getMipmap(), videoSelected.getUrlacces(), videoSelected.getSrc(), videoSelected.getTitre(), videoSelected.getAuteur(), videoSelected.getDuree(), videoSelected.getDate(), videoSelected.getType_libelle(), videoSelected.getType_shortcode(), videoSelected.getId());
                        daoFavoris.insertData(favoris);
                        CommonPresenter.showMessageSnackBar(view, view.getContext().getResources().getString(R.string.video_add_to_favorite));
                    }
                    else{
                        CommonPresenter.showMessageSnackBar(view, view.getContext().getResources().getString(R.string.video_already_add_to_favorite));
                    }
                    break;

                // Orientation of screen
                case R.id.fab_player_screen_orientation:
                    CommonPresenter.changeActivityOrientation(view.getContext());
                    break;

                // Open volume for video player
                case R.id.fab_player_volume:
                    CommonPresenter.getApplicationVolume(view.getContext());
                    break;

                    // Hide video player
                case R.id.fab_player_down:
                    if(iVideoPlayer.isCurrentVideoIsNotification()){
                        Intent intent = new Intent(view.getContext(), HomeActivity.class);
                        view.getContext().startActivity(intent);
                        ((Activity)view.getContext()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                    //--
                    iVideoPlayer.closeActivity();
                    break;

                // Play previous video
                case R.id.btn_video_nav_left:
                    // Is it video notification
                    if(iVideoPlayer.isCurrentVideoIsNotification()){
                        Intent intent = new Intent(view.getContext(), HomeActivity.class);
                        view.getContext().startActivity(intent);
                        ((Activity)view.getContext()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        //--
                        iVideoPlayer.closeActivity();
                    }
                    else{
                        iVideoPlayer.playPreviousVideo();
                    }
                    break;

                // Play next video
                case R.id.btn_video_nav_right:
                    // Is it video notification
                    if(iVideoPlayer.isCurrentVideoIsNotification()){
                        Intent intent = new Intent(view.getContext(), HomeActivity.class);
                        view.getContext().startActivity(intent);
                        ((Activity)view.getContext()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        //--
                        iVideoPlayer.closeActivity();
                    }
                    else {
                        iVideoPlayer.playNextVideo();
                    }
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
                View view = CommonPresenter.getViewInTermsOfContext(context);
                CommonPresenter.showMessageSnackBar(view, context.getResources().getString(R.string.lb_downloading));
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
