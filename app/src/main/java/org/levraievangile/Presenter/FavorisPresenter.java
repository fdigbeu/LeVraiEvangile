package org.levraievangile.Presenter;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.levraievangile.Model.Audio;
import org.levraievangile.Model.DAOFavoris;
import org.levraievangile.Model.Favoris;
import org.levraievangile.Model.LoadAudioMediaPlayer;
import org.levraievangile.Model.Video;
import org.levraievangile.R;
import org.levraievangile.View.Interfaces.FavorisView;

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

public class FavorisPresenter {
    // Ref interface
    private FavorisView.IFravoris iFravoris;

    private FavorisView.IPlaceholder iPlaceholder;

    // Ref audio media player
    private LoadAudioMediaPlayer loadAudioMediaPlayer;

    // Constructor
    public FavorisPresenter(FavorisView.IFravoris iFravoris) {
        this.iFravoris = iFravoris;
    }

    public FavorisPresenter(FavorisView.IPlaceholder iPlaceholder) {
        this.iPlaceholder = iPlaceholder;
    }

    // Load favoris data
    public void loadFavorisData(Context context){
        iFravoris.initialize();
        iFravoris.events();
    }

    public void loadPlaceHolderData(Context context, View rootView, int positionFrag){
        iPlaceholder.initialize(rootView);
        iPlaceholder.events();
        iPlaceholder.progressBarVisibility(View.VISIBLE);
        //--
        loadFragmentData(context, positionFrag);
    }

    // Refresh all data
    public void loadFragmentData(Context context, int positionFrag){
        try {
            // Load Audio files
            switch (positionFrag){
                // AUDIOS
                case 0:
                    DAOFavoris daoAudioFavoris = new DAOFavoris(context);
                    ArrayList<Favoris> audioList = daoAudioFavoris.getAllData("audio");
                    iPlaceholder.loadFavorisAudioData(audioList, 1);
                    iPlaceholder.progressBarVisibility(View.GONE);
                    //--
                    ArrayList<Audio> audios = CommonPresenter.getAudiosListByFavorisList(audioList);
                    CommonPresenter.saveDataInSharePreferences(context, KEY_NOTIF_AUDIOS_LIST, audios.toString());
                    break;
                // VIDEOS
                case 1:
                    DAOFavoris daoVideoFavoris = new DAOFavoris(context);
                    ArrayList<Favoris> videoList = daoVideoFavoris.getAllData("video");
                    iPlaceholder.loadFavorisVideoData(videoList, 1);
                    iPlaceholder.progressBarVisibility(View.GONE);
                    break;
                // PDF
                case 2:
                    DAOFavoris daoPdfFavoris = new DAOFavoris(context);
                    ArrayList<Favoris> pdfList = daoPdfFavoris.getAllData("pdf");
                    iPlaceholder.loadFavorisPdfData(pdfList, 1);
                    iPlaceholder.progressBarVisibility(View.GONE);
                    break;

            }
        }
        catch (Exception ex){}
    }

    // Manage menu Item
    public void retrieveUserAction(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                iFravoris.closeActivity();
                break;
        }
    }

    // Launch activity
    public void launchActivity(String value){
        iPlaceholder.launchActivity(value);
    }

    /**
     * Play video player
     * @param context
     * @param video
     * @param position
     */
    public void playLVEVideoPlayer(Context context, Video video, int position){
        if(CommonPresenter.isMobileConnected(context)){
            iPlaceholder.launchVideoToPlay(video, position);
        }
        else{
            String title = context.getResources().getString(R.string.no_connection);
            String message = context.getResources().getString(R.string.detail_no_connection);
            CommonPresenter.showMessage(context, title.toUpperCase(), message, false);
        }
    }

    /**
     * Scroll video data items to positon
     * @param position
     */
    public void srcollVideoDataItemsToPosition(int position){
        iPlaceholder.scrollVideoDataToPosition(position);
    }

    /**
     * Play next video from FavorisRecyclerAdapter
     * @param iFavorisRecycler
     */
    public void playNextVideoInPlayer(FavorisView.IFavorisRecycler iFavorisRecycler){
        if(iFavorisRecycler != null){
            iFavorisRecycler.playNextVideo();
            Log.i("TAG_NEXT_VIDEO", "TAG_NEXT_VIDEO : iFavorisRecycler != null");
        }
    }

    /**
     * Play previous video from FavorisRecyclerAdapter
     * @param iFavorisRecycler
     */
    public void playPreviousVideoInPlayer(FavorisView.IFavorisRecycler iFavorisRecycler){
        if(iFavorisRecycler != null){
            iFavorisRecycler.playPreviousVideo();
            Log.i("TAG_PREVIOUS_VIDEO", "TAG_PREVIOUS_VIDEO : iFavorisRecycler != null");
        }
    }

    // Set FavorisActivity FavorisRecyclerAdapter Attribute
    public void retrieveAndSetIFavorisRecyclerReference(FavorisView.IFavorisRecycler iFavorisRecycler){
        if(iPlaceholder != null){
            iPlaceholder.instanciateIFavorisRecycler(iFavorisRecycler);
        }
        else if(iFravoris != null){
            iFravoris.instanciateIFavorisRecycler(iFavorisRecycler);
        }
        else{}
    }

    // Manage audio player button
    public void retrieveUserAction(View view){
        Audio audioSelected = CommonPresenter.getAudioSelected(view.getContext());
        try {
            switch (view.getId()){
                // Play next audio
                case R.id.audio_player_next:
                    iFravoris.playNextAudio();
                    break;

                // Play previous audio
                case R.id.audio_player_previous:
                    iFravoris.playPreviousAudio();
                    break;

                // Display volume
                case R.id.fab_player_volume:
                    CommonPresenter.getApplicationVolume(view.getContext());
                    break;

                case R.id.fab_player_download:
                    downloadThisAudio(view.getContext());
                    break;

                case R.id.fab_player_share_app:
                    CommonPresenter.shareAudio(view.getContext(), audioSelected);
                    break;

                case R.id.fab_player_favorite:
                    Toast.makeText(view.getContext(), view.getContext().getResources().getString(R.string.audio_already_add_to_favorite), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        catch (Exception ex){}
    }

    // When the audio is finished
    public void retrieveOnCompletionAction(){
        iFravoris.playNextAudio();
    }

    // Download audio
    private void downloadThisAudio(Context context){
        Audio audioSelected = CommonPresenter.getAudioSelected(context);
        if(audioSelected != null){
            if(CommonPresenter.isStorageDownloadFileAccepted(context)){
                String url = audioSelected.getUrlacces()+audioSelected.getSrc();
                String filename = audioSelected.getSrc();
                String description = "LVE-APP-DOWNLOADER ("+audioSelected.getDuree()+" | "+audioSelected.getAuteur()+")";
                CommonPresenter.getFileByDownloadManager(context, url, filename, description, "audio");
                Toast.makeText(context, context.getResources().getString(R.string.lb_downloading), Toast.LENGTH_SHORT).show();
            }
            else{
                iFravoris.askPermissionToSaveFile();
            }
        }
    }

    /**
     * Enable Floating Action Button
     * @param enable
     */
    public void activateAllWidgets(boolean enable){
        iFravoris.activateAudioPlayerWidgets(enable);
        // Stop Notification
        iFravoris.stopNotificationAudio();
    }


    /**
     * Stop all media sound
     * @param audio
     */
    public void stopAllOtherMediaSound(Audio audio){
        iFravoris.stopOtherMediaPlayerSound(audio);
    }

    /**
     * Stop audio Media player
     * @param mediaPlayer
     */
    public void stopMediaPlayer(MediaPlayer mediaPlayer){
        CommonPresenter.stopMediaPlayer(mediaPlayer);
    }

    // Close media player
    private void closeAudioMediaPlayer(MediaPlayer mediaPlayer){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        iFravoris.audioPlayerVisibility(View.GONE);
    }

    /**
     * Scroll audio data items to positon
     * @param position
     */
    public void srcollAudioDataItemsToPosition(int position){
        if(iPlaceholder != null) {
            iPlaceholder.scrollAudioDataToPosition(position);
        }
    }

    /**
     * Play next audio from FavorisRecyclerAdapter
     * @param iFavorisRecycler
     */
    public void playNextAudioInPlayer(FavorisView.IFavorisRecycler iFavorisRecycler){
        if(iFavorisRecycler != null){
            iFavorisRecycler.playNextAudio();
        }
    }

    /**
     * Play previous audio from FavorisRecyclerAdapter
     * @param iFavorisRecycler
     */
    public void playPreviousAudioInPlayer(FavorisView.IFavorisRecycler iFavorisRecycler){
        if(iFavorisRecycler != null){
            iFavorisRecycler.playPreviousAudio();
        }
    }

    // Play audio notification
    private void playAudioNotification(Context context, MediaPlayer mediaPlayer){
        CommonPresenter.saveDataInSharePreferences(context, KEY_PLAYER_AUDIO_TO_NOTIF_AUDIO_TIME_ELAPSED, ""+mediaPlayer.getCurrentPosition());
        closeAudioMediaPlayer(mediaPlayer);
        iFravoris.playNotificationAudio();
    }


    // Retrieve position page
    public void retrieveUserAction(int pagePosition, ImageButton playButton, boolean isAudioSelected){
        MediaPlayer mediaPlayer = iFravoris.getInstanceMediaPlayer();
        switch (pagePosition){
            // AUDIOS
            case 0:
                if(mediaPlayer != null && !mediaPlayer.isPlaying()){
                    if(isAudioSelected){
                        playButton.performClick();
                        iFravoris.audioPlayerVisibility(View.VISIBLE);
                    }
                }
                else{
                    iFravoris.audioPlayerVisibility(View.GONE);
                }
                break;
            // VIDEOS
            case 1:
                if(mediaPlayer != null && mediaPlayer.isPlaying()){
                    playButton.performClick();
                }
                iFravoris.audioPlayerVisibility(View.GONE);
                break;
            // PDFS
            case 2:
                if(mediaPlayer != null && mediaPlayer.isPlaying()){
                    playButton.performClick();
                }
                iFravoris.audioPlayerVisibility(View.GONE);
                break;
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

    // Activate audio player and play
    public void playLVEAudioPlayer(Context context, Audio audio, int position){
        // Save audio selected
        CommonPresenter.saveDataInSharePreferences(context, KEY_AUDIO_SELECTED, audio.toString());
        if(iFravoris != null){
            iFravoris.notifyThatAudioIsSelected();
            if(CommonPresenter.isMobileConnected(context)){
                loadAudioMediaPlayer = new LoadAudioMediaPlayer();
                loadAudioMediaPlayer.initLoadAudioMediaPlayer(audio, position, iFravoris);
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
}
