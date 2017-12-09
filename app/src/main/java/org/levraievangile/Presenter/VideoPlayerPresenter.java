package org.levraievangile.Presenter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;


import org.levraievangile.Model.Video;
import org.levraievangile.R;
import org.levraievangile.View.Interfaces.VideoPlayerView;

import java.util.Hashtable;

import static org.levraievangile.Presenter.CommonPresenter.KEY_VIDEO_PLAYER_SEND_DATA;

/**
 * Created by Maranatha on 27/11/2017.
 */

public class VideoPlayerPresenter {
    // Ref interface
    private VideoPlayerView.IVideoPlayer iVideoPlayer;

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
                Video ressource = (Video) intent.getSerializableExtra(KEY_VIDEO_PLAYER_SEND_DATA);
                Hashtable<String, Integer> resolutionEcran = CommonPresenter.getScreenSize(context);
                iVideoPlayer.displayPlayer(ressource, resolutionEcran.get("largeur"), resolutionEcran.get("hauteur"));
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
    public void retrieveOnCompletionAction(Context context){
        iVideoPlayer.playNextVideo();
    }

    // Manage all video player button
    public void retrieveUserAction(Context context, View view){
        try {
            switch (view.getId()){
                // Download video
                case R.id.fab_player_download:
                    /*Setting mSetting = CommonPresenter.getSettingObjectFromSharePreferences(context, KEY_SETTING_WIFI_EXCLUSIF);
                    if(mSetting.getChoice()){
                        if(CommonPresenter.isMobileWIFIConnected(context)){
                            iVideoPlayer.downloadQRVideo(context);
                        }
                        else{
                            String title = context.getResources().getString(R.string.lb_wifi_only);
                            String message = context.getResources().getString(R.string.lb_wifi_exclusif_message);
                            CommonPresenter.showMessage(context, title, message, false);
                        }
                    }
                    else{
                        iVideoPlayer.downloadVideo(context);
                    }*/
                    break;

                // Share video
                case R.id.fab_player_share_app:
                    //iVideoPlayer.shareQRVideo(context);
                    break;

                // Add to video favorite
                case R.id.fab_player_favorite:
                    //iVideoPlayer.addQRVideoToFavorite(context);
                    break;

                // Open volume for video player
                case R.id.fab_player_volume:
                    //CommonPresenter.getApplicationVolume(context);
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

    // Save Video data
    public void saveRessourceVideoData(Context context, Video ressource){
        /*String typeRessource = "video";
        DAORessource daoRessource = new DAORessource(context);
        if(!daoRessource.isRessourceExists(ressource.getSrc())){
            int totalBeforeInsert = daoRessource.getAllByTypeRessource(typeRessource).size();
            daoRessource.insertData(ressource.getTitre(), ressource.getEtat(), ressource.getSrc(), typeRessource, ressource.getAuteur(), ressource.getUrlacces(), ressource.getDuree());
            int totalAfterInsert = daoRessource.getAllByTypeRessource(typeRessource).size();
            if(totalBeforeInsert < totalAfterInsert){
                Toast.makeText(context, context.getString(R.string.video_add_to_favorite), Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(context, context.getString(R.string.video_already_add_to_favorite), Toast.LENGTH_SHORT).show();
        }*/
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
