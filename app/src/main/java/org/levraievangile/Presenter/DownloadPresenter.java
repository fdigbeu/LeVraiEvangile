package org.levraievangile.Presenter;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.levraievangile.Model.Audio;
import org.levraievangile.Model.DownloadFile;
import org.levraievangile.Model.LoadAudioMediaPlayer;
import org.levraievangile.Model.LoadDownloadAudio;
import org.levraievangile.Model.LoadDownloadPdf;
import org.levraievangile.Model.LoadDownloadVideo;
import org.levraievangile.Model.Video;
import org.levraievangile.R;
import org.levraievangile.View.Interfaces.DownloadView;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static org.levraievangile.Presenter.CommonPresenter.KEY_AUDIO_SELECTED;
import static org.levraievangile.Presenter.CommonPresenter.KEY_NOTIF_AUDIOS_LIST;
import static org.levraievangile.Presenter.CommonPresenter.KEY_NOTIF_PLAYER_PLAY_NEXT;
import static org.levraievangile.Presenter.CommonPresenter.KEY_NOTIF_PLAYER_PLAY_PREVIOUS;
import static org.levraievangile.Presenter.CommonPresenter.KEY_NOTIF_PLAYER_SELECTED;
import static org.levraievangile.Presenter.CommonPresenter.KEY_PLAYER_AUDIO_TO_NOTIF_AUDIO_TIME_ELAPSED;

/**
 * Created by Maranatha on 11/12/2017.
 */

public class DownloadPresenter implements DownloadView.ILoadDownload{
    // Ref interface
    private DownloadView.IDownload iDownload;
    private DownloadView.IDownloadAudioView iDownloadAudioView;
    private DownloadView.IDownloadVideoView iDownloadVideoView;
    private DownloadView.IDownloadPdfView iDownloadPdfView;
    // Ref audio media player
    private LoadAudioMediaPlayer loadAudioMediaPlayer;
    // Ref LoadDownloadFile
    private LoadDownloadAudio loadDownloadAudio;
    private LoadDownloadVideo loadDownloadVideo;
    private LoadDownloadPdf loadDownloadPdf;

    // Constructors
    public DownloadPresenter(DownloadView.IDownload iDownload) {
        this.iDownload = iDownload;
    }

    public DownloadPresenter(DownloadView.IDownloadAudioView iDownloadAudioView) {
        this.iDownloadAudioView = iDownloadAudioView;
    }

    public DownloadPresenter(DownloadView.IDownloadVideoView iDownloadVideoView) {
        this.iDownloadVideoView = iDownloadVideoView;
    }

    public DownloadPresenter(DownloadView.IDownloadPdfView iDownloadPdfView) {
        this.iDownloadPdfView = iDownloadPdfView;
    }

    public DownloadPresenter(DownloadView.IDownload iDownload, DownloadView.IDownloadAudioView iDownloadAudioView) {
        this.iDownload = iDownload;
        this.iDownloadAudioView = iDownloadAudioView;
    }

    public DownloadPresenter(DownloadView.IDownload iDownload, DownloadView.IDownloadVideoView iDownloadVideoView) {
        this.iDownload = iDownload;
        this.iDownloadVideoView = iDownloadVideoView;
    }

    public DownloadPresenter(DownloadView.IDownload iDownload, DownloadView.IDownloadPdfView iDownloadPdfView) {
        this.iDownload = iDownload;
        this.iDownloadPdfView = iDownloadPdfView;
    }

    // Load download data
    public void loadDownloadData(Context context){
        iDownload.initialize();
        iDownload.events();
    }

    // Load download audio data
    public void loadDownloadAudioFragData(View view){
        iDownloadAudioView.initialize(view);
        iDownloadAudioView.events();
    }

    // Load download video data
    public void loadDownloadVideoFragData(View view){
        iDownloadVideoView.initialize(view);
        iDownloadVideoView.events();
    }

    // Load download pdf data
    public void loadDownloadPdfFragData(View view){
        iDownloadPdfView.initialize(view);
        iDownloadPdfView.events();
    }

    // If downloadAudio Fragment Attach Success
    public void downloadAudioFragmentAttachSuccess(Context context){
        if(iDownload != null && iDownloadAudioView != null){
            ArrayList<DownloadFile> downloadFiles = iDownload.getStorageDownloadFilesAudioData();
            if(downloadFiles==null){
                LoadDownloadAudio loadDownloadAudio = new LoadDownloadAudio();
                loadDownloadAudio.initializeData(context, this);
                loadDownloadAudio.execute();
                iDownloadAudioView.progressBarVisibility(View.VISIBLE);
            }
            else {
                iDownloadAudioView.loadDownloadAudioData(downloadFiles, 1);
            }
        }
    }

    // If downloadVideo Fragment Attach Success
    public void downloadVideoFragmentAttachSuccess(Context context){
        if(iDownload != null && iDownloadVideoView != null){
            ArrayList<DownloadFile> downloadFiles = iDownload.getStorageDownloadFilesVideoData();
            if(downloadFiles==null){
                LoadDownloadVideo loadDownloadVideo = new LoadDownloadVideo();
                loadDownloadVideo.initializeData(context, this);
                loadDownloadVideo.execute();
                iDownloadVideoView.progressBarVisibility(View.VISIBLE);
            }
            else {
                iDownloadVideoView.loadDownloadVideoData(downloadFiles, 1);
            }
        }
    }

    // If downloadPdf Fragment Attach Success
    public void downloadPdfFragmentAttachSuccess(Context context){
        if(iDownload != null && iDownloadPdfView != null){
            ArrayList<DownloadFile> downloadFiles = iDownload.getStorageDownloadFilesPdfData();
            if(downloadFiles==null){
                LoadDownloadPdf loadDownloadPdf = new LoadDownloadPdf();
                loadDownloadPdf.initializeData(context, this);
                loadDownloadPdf.execute();
                iDownloadPdfView.progressBarVisibility(View.VISIBLE);
            }
            else {
                iDownloadPdfView.loadDownloadPdfData(downloadFiles, 1);
            }
        }
    }

    // Manage menu Item
    public void retrieveUserAction(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                iDownload.closeActivity();
                break;
        }
    }

    /**
     * Play next audio from DownloadRecyclerAdapter
     * @param iDownloadAudioRecycler
     */
    public void playNextAudioInPlayer(DownloadView.IDownloadAudioRecycler iDownloadAudioRecycler){
        if(iDownloadAudioRecycler != null){
            iDownloadAudioRecycler.playNextAudio();
        }
    }

    /**
     * Play previous audio from DownloadRecyclerAdapter
     * @param iDownloadAudioRecycler
     */
    public void playPreviousAudioInPlayer(DownloadView.IDownloadAudioRecycler iDownloadAudioRecycler){
        if(iDownloadAudioRecycler != null){
            iDownloadAudioRecycler.playPreviousAudio();
        }
    }

    // Play audio notification
    private void playAudioNotification(Context context, MediaPlayer mediaPlayer){
        CommonPresenter.saveDataInSharePreferences(context, KEY_PLAYER_AUDIO_TO_NOTIF_AUDIO_TIME_ELAPSED, ""+mediaPlayer.getCurrentPosition());
        closeAudioMediaPlayer(mediaPlayer);
        iDownload.playNotificationAudio();
    }

    // When user clicks to play/Pause
    public void retrievePlayPauseAction(MediaPlayer mediaPlayer, ImageButton imageButton){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            imageButton.setBackgroundResource(R.drawable.btn_media_player_play);
        }
        else{
            mediaPlayer.start();
            imageButton.setBackgroundResource(R.drawable.btn_media_player_pause);
        }
    }

    // Manage audio player button
    public void retrieveUserAction(View view, MediaPlayer mediaPlayer){
        try {
            switch (view.getId()){
                // Play notification
                case R.id.fab_player_notification:
                    playAudioNotification(view.getContext(), mediaPlayer);
                    break;

                case R.id.audio_player_close:
                    closeAudioMediaPlayer(mediaPlayer);
                    break;
            }
        }
        catch (Exception ex){}
    }

    // Close media player
    private void closeAudioMediaPlayer(MediaPlayer mediaPlayer){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        iDownload.audioPlayerVisibility(View.GONE);
    }

    // Manage audio player button
    public void retrieveUserAction(View view){
        try {
            switch (view.getId()){
                // Play next audio
                case R.id.audio_player_next:
                    iDownload.playNextAudio();
                    break;

                // Play previous audio
                case R.id.audio_player_previous:
                    iDownload.playPreviousAudio();
                    break;

                // Display volume
                case R.id.fab_player_volume:
                    CommonPresenter.getApplicationVolume(view.getContext());
                    break;
            }
        }
        catch (Exception ex){}
    }

    // Retrieve position page
    public void retrieveUserAction(int pagePosition, ImageButton playButton, boolean isAudioSelected){
        MediaPlayer mediaPlayer = iDownload.getInstanceMediaPlayer();
        switch (pagePosition){
            // AUDIOS
            case 0:
                if(mediaPlayer != null && !mediaPlayer.isPlaying()){
                    if(isAudioSelected){
                        playButton.performClick();
                        audioPlayerVisibility(View.VISIBLE);
                    }
                }
                else{
                    audioPlayerVisibility(View.GONE);
                }
                break;
            // VIDEOS
            case 1:
                if(mediaPlayer != null && mediaPlayer.isPlaying()){
                    playButton.performClick();
                }
                audioPlayerVisibility(View.GONE);
                break;
            // PDFS
            case 2:
                if(mediaPlayer != null && mediaPlayer.isPlaying()){
                    playButton.performClick();
                }
                audioPlayerVisibility(View.GONE);
                break;
        }
    }



    // When the audio is finished
    public void retrieveOnCompletionAction(){
        iDownload.playNextAudio();
    }

    /**
     * Enable Floating Action Button
     * @param enable
     */
    public void activateAllWidgets(boolean enable){
        iDownload.activateAudioPlayerWidgets(enable);
        // Stop Notification
        iDownload.stopNotificationAudio();
    }


    /**
     * Stop all medi sound
     * @param audio
     */
    public void stopAllOtherMediaSound(Audio audio){
        iDownload.stopOtherMediaPlayerSound(audio);
    }

    /**
     * Stop audio Media player
     * @param mediaPlayer
     */
    public void stopMediaPlayer(MediaPlayer mediaPlayer){
        CommonPresenter.stopMediaPlayer(mediaPlayer);
    }

    // Activate audio player and play
    public void playLVEAudioPlayer(Context context, Audio audio, int position){
        // Save audio selected
        CommonPresenter.saveDataInSharePreferences(context, KEY_AUDIO_SELECTED, audio.toString());
        if(iDownload != null){
            if(CommonPresenter.isMobileConnected(context)){
                loadAudioMediaPlayer = new LoadAudioMediaPlayer();
                loadAudioMediaPlayer.initLoadAudioMediaPlayer(audio, position, iDownload);
                loadAudioMediaPlayer.execute();
                // Save for notification data
                CommonPresenter.saveDataInSharePreferences(context, KEY_NOTIF_PLAYER_SELECTED, ""+position);
                ArrayList<Audio> mList = CommonPresenter.getAllAudiosByKey(context, KEY_NOTIF_AUDIOS_LIST);
                int previousPosition = CommonPresenter.getNotifPlayerPreviousValue(position, mList.size());
                CommonPresenter.saveDataInSharePreferences(context, KEY_NOTIF_PLAYER_PLAY_PREVIOUS, ""+previousPosition);
                int nextPosition = CommonPresenter.getNotifPlayerNextValue(position, mList.size());
                CommonPresenter.saveDataInSharePreferences(context, KEY_NOTIF_PLAYER_PLAY_NEXT, ""+nextPosition);
            }
            else{
                String title = context.getResources().getString(R.string.no_connection);
                String message = context.getResources().getString(R.string.detail_no_connection);
                CommonPresenter.showMessage(context, title.toUpperCase(), message, false);
            }
        }
    }

    // Manage AUDIO : Selected From DownloadRecyclerAdapter
    public void retrieveAudioSelected(Context context, Audio audio, int position){
        if(CommonPresenter.isMobileConnected(context)){
            iDownload.onAudioSelected(audio, position);
        }
        else{
            String title = context.getResources().getString(R.string.no_connection);
            String message = context.getResources().getString(R.string.detail_no_connection);
            CommonPresenter.showMessage(context, title.toUpperCase(), message, false);
        }
    }

    @Override
    public void downloadAudioStarted() {}

    @Override
    public void downloadAudioFinished(Context context, ArrayList<DownloadFile> downloadFiles) {
        iDownloadAudioView.loadDownloadAudioData(downloadFiles, 1);
        iDownloadAudioView.progressBarVisibility(View.GONE);
        iDownload.storageDownloadFilesList(0, downloadFiles);
        //--
        ArrayList<Audio> audiosList = CommonPresenter.getAudiosListByDownloadFilesList(downloadFiles);
        CommonPresenter.saveDataInSharePreferences(context, KEY_NOTIF_AUDIOS_LIST, audiosList.toString());
    }

    @Override
    public void downloadAudioFailure() {}

    @Override
    public void downloadVideoStarted() {}

    @Override
    public void downloadVideoFinished(ArrayList<DownloadFile> downloadFiles) {
        iDownloadVideoView.loadDownloadVideoData(downloadFiles, 1);
        iDownloadVideoView.progressBarVisibility(View.GONE);
        iDownload.storageDownloadFilesList(1, downloadFiles);
    }

    @Override
    public void downloadVideoFailure() {}

    @Override
    public void downloadPdfFinished(ArrayList<DownloadFile> downloadFiles) {
        iDownloadPdfView.loadDownloadPdfData(downloadFiles, 1);
        iDownloadPdfView.progressBarVisibility(View.GONE);
        iDownload.storageDownloadFilesList(2, downloadFiles);
    }

    @Override
    public void downloadPdfFailure() {}

    public void cancelAsyntask(){
        if(loadDownloadAudio != null) loadDownloadAudio.cancel(true);
        if(loadDownloadVideo != null) loadDownloadVideo.cancel(true);
        if(loadDownloadPdf != null) loadDownloadPdf.cancel(true);
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Scroll video data items to positon
     * @param position
     */
    public void srcollVideoDataItemsToPosition(int position){
        iDownloadVideoView.scrollVideoDataToPosition(position);
    }

    /**
     * Play next video from DownloadRecyclerAdapter
     * @param iDownloadVideoRecycler
     */
    public void playNextVideoInPlayer(DownloadView.IDownloadVideoRecycler iDownloadVideoRecycler){
        if(iDownloadVideoRecycler != null){
            iDownloadVideoRecycler.playNextVideo();
        }
    }

    /**
     * Play previous video from DownloadRecyclerAdapter
     * @param iDownloadVideoRecycler
     */
    public void playPreviousVideoInPlayer(DownloadView.IDownloadVideoRecycler iDownloadVideoRecycler){
        if(iDownloadVideoRecycler != null){
            iDownloadVideoRecycler.playPreviousVideo();
        }
    }

    // Set DownloadVideoFragment DownloadRecyclerAdapter Attribute
    public void retrieveAndSetIDownloadVideoRecyclerReference(DownloadView.IDownloadVideoRecycler iDownloadVideoRecycler){
        if(iDownloadVideoView != null){
            iDownloadVideoView.instanciateIDownloadVideoRecycler(iDownloadVideoRecycler);
        }
    }

    /**
     * Audio player visibility
     * @param visibility
     */
    public void audioPlayerVisibility(int visibility){
        iDownload.audioPlayerVisibility(visibility);
    }


    /**
     * Scroll audio data items to positon
     * @param position
     */
    public void srcollAudioDataItemsToPosition(int position){
        iDownloadAudioView.scrollAudioDataToPosition(position);
    }

    /**
     * Read pdf file
     * @param path
     */
    public void readPdfFile(String path){
        if(iDownloadPdfView != null){
            try {
                iDownloadPdfView.readPdfFile(path);
            }
            catch (Exception ex){}
        }
    }

    // Set DownloadPdfFragment DownloadRecyclerAdapter Attribute
    public void retrieveAndSetIDownloadPdfRecyclerReference(DownloadView.IDownloadPdfRecycler iDownloadPdfRecycler){
        if(iDownloadPdfView != null){
            iDownloadPdfView.instanciateIDownloadPdfRecycler(iDownloadPdfRecycler);
        }
    }

    // Set DownloadAudioFragment DownloadRecyclerAdapter Attribute
    public void retrieveAndSetIDownloadAudioRecyclerReference(DownloadView.IDownloadAudioRecycler iDownloadAudioRecycler){
        if(iDownloadAudioView != null){
            iDownloadAudioView.instanciateIDownloadAudioRecycler(iDownloadAudioRecycler);
        }
        else if(iDownload != null){
            iDownload.instanciateIDownloadAudioRecycler(iDownloadAudioRecycler);
        }
        else{}
    }

    /**
     * Play video player
     * @param video
     * @param position
     */
    public void playLVEVideoPlayer(Video video, int position){
        iDownloadVideoView.launchVideoToPlay(video, position);
    }
}
