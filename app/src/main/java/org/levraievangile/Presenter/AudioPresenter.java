package org.levraievangile.Presenter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.levraievangile.Model.ApiClient;
import org.levraievangile.Model.Audio;
import org.levraievangile.Model.LoadStreamAudio;
import org.levraievangile.R;
import org.levraievangile.View.Interfaces.AudioView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.levraievangile.Presenter.CommonPresenter.KEY_ALL_AUDIOS_LIST;
import static org.levraievangile.Presenter.CommonPresenter.KEY_AUDIO_SELECTED;
import static org.levraievangile.Presenter.CommonPresenter.KEY_NOTIF_AUDIOS_LIST;
import static org.levraievangile.Presenter.CommonPresenter.KEY_NOTIF_AUDIO_TIME_ELAPSED;
import static org.levraievangile.Presenter.CommonPresenter.KEY_NOTIF_PLAYER_PLAY_NEXT;
import static org.levraievangile.Presenter.CommonPresenter.KEY_NOTIF_PLAYER_PREVIOUS;
import static org.levraievangile.Presenter.CommonPresenter.KEY_NOTIF_PLAYER_SELECTED;
import static org.levraievangile.Presenter.CommonPresenter.KEY_SHORT_CODE;

/**
 * Created by Maranatha on 06/12/2017.
 */

public class AudioPresenter implements AudioView.IStreamAudio {
    // Ref interface
    private AudioView.IAudio iAudio;

    private LoadStreamAudio loadStreamAudio;

    // Ref interface retrofit
    private AudioView.IApiRessource iApiRessource;

    public AudioPresenter(AudioView.IAudio iAudio) {
        this.iAudio = iAudio;
    }

    public void loadAudioData(final Context context, Intent intent){
        iAudio.initialize();
        iAudio.events();
        iAudio.askPermissionToSaveFile();
        iAudio.progressBarVisibility(View.VISIBLE);
        //--
        if(CommonPresenter.isMobileConnected(context)) {
            if(intent != null) {
                //Get list audio by short-code
                final String shortCode = intent.getStringExtra(KEY_SHORT_CODE);
                Call<List<Audio>> callAudios = ApiClient.getApiClientLeVraiEvangile().create(AudioView.IApiRessource.class).getAllAudios(shortCode);
                callAudios.enqueue(new Callback<List<Audio>>() {
                    @Override
                    public void onResponse(Call<List<Audio>> call, Response<List<Audio>> response) {
                        ArrayList<Audio> audios = (ArrayList<Audio>)response.body();
                        final String keyShortCode = KEY_ALL_AUDIOS_LIST+"-"+shortCode;
                        CommonPresenter.saveDataInSharePreferences(context, keyShortCode, audios.toString());
                        CommonPresenter.saveDataInSharePreferences(context, KEY_NOTIF_AUDIOS_LIST, audios.toString());
                        iAudio.loadAudioData(audios, 1);
                        iAudio.progressBarVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<List<Audio>> call, Throwable t) {
                        ArrayList<Audio> audios = CommonPresenter.getAllAudioSavedBy(context, shortCode);
                        iAudio.loadAudioData(audios, 1);
                        iAudio.progressBarVisibility(View.GONE);
                    }
                });
            }
            else{
                iAudio.closeActivity();
            }
        }
    }

    // Play notification audio
    public void notificationPlayAudio(){
        iAudio.launchNotificationAudio();
    }


    /**
     * Scroll audio data items to positon
     * @param position
     */
    public void srcollAudioDataItemsToPosition(int position){
        iAudio.scrollAudioDataToPosition(position);
    }

    /**
     * Play next audio from AudioRecyclerAdapter
     * @param iAudioRecycler
     */
    public void playNextAudioInPlayer(AudioView.IAudioRecycler iAudioRecycler){
        if(iAudioRecycler != null){
            iAudioRecycler.playNextAudio();
        }
    }

    /**
     * Play previous audio from AudioRecyclerAdapter
     * @param iAudioRecycler
     */
    public void playPreviousAudioInPlayer(AudioView.IAudioRecycler iAudioRecycler){
        if(iAudioRecycler != null){
            iAudioRecycler.playPreviousAudio();
        }
    }
    
    // Set AudioActivity AudioRecyclerAdapteur Attribute
    public void retrieveAndSetIAudioRecyclerReference(AudioView.IAudioRecycler iAudioRecycler){
        if(iAudio != null){
            iAudio.instanciateIAudioRecycler(iAudioRecycler);
        }
    }

    /**
     * Play audio player
     * @param context
     * @param audio
     * @param position
     */
    public void playLVEAudioPlayer(Context context, Audio audio, int position){
        // Save audio selected
        CommonPresenter.saveDataInSharePreferences(context, KEY_AUDIO_SELECTED, audio.toString());
        //--
        if(CommonPresenter.isMobileConnected(context)){
            iAudio.audioPlayerVisibility(View.VISIBLE);
            loadStreamAudio = new LoadStreamAudio();
            loadStreamAudio.initializeData(audio, this);
            loadStreamAudio.execute();
            // Save for notification data
            CommonPresenter.saveDataInSharePreferences(context, KEY_NOTIF_PLAYER_SELECTED, ""+position);
            ArrayList<Audio> mList = CommonPresenter.getAllNotificationAudios(context);
            int previousPosition = CommonPresenter.getNotifPlayerPreviousValue(position, mList.size());
            CommonPresenter.saveDataInSharePreferences(context, KEY_NOTIF_PLAYER_PREVIOUS, ""+previousPosition);
            int nextPosition = CommonPresenter.getNotifPlayerNextValue(position, mList.size());
            CommonPresenter.saveDataInSharePreferences(context, KEY_NOTIF_PLAYER_PLAY_NEXT, ""+nextPosition);
        }
        else{
            String title = context.getResources().getString(R.string.no_connection);
            String message = context.getResources().getString(R.string.detail_no_connection);
            CommonPresenter.showMessage(context, title.toUpperCase(), message, false);
        }
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
                    CommonPresenter.saveDataInSharePreferences(view.getContext(), KEY_NOTIF_AUDIO_TIME_ELAPSED, ""+mediaPlayer.getCurrentPosition());
                    closeAudioMediaPlayer(mediaPlayer);
                    iAudio.playNotificationAudio();
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
        iAudio.audioPlayerVisibility(View.GONE);
    }


    // Manage audio player button
    public void retrieveUserAction(View view){
        Audio audioSelected = CommonPresenter.getAudioSelected(view.getContext());
        try {
            switch (view.getId()){
                // Play next audio
                case R.id.audio_player_next:
                    iAudio.playNextAudio();
                    break;

                // Play previous audio
                case R.id.audio_player_previous:
                    iAudio.playPreviousAudio();
                    break;

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
                    //Log.i("TAG_FAVORITE", "URL = "+audioSelected.getSrc()+audioSelected.getUrlacces());
                    break;
            }
        }
        catch (Exception ex){}
    }


    // When the audio is finished
    public void retrieveOnCompletionAction(Context context){
        iAudio.playNextAudio();
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
                Log.i("TAG_DOWNLOAD_FILE", "URL = "+url);
            }
            else{
                iAudio.askPermissionToSaveFile();
            }
        }
        else{

            Log.i("TAG_METHODE", "downloadThisAudio() : audioSelected == NULL");
        }
    }

    /**
     * Enable Floating Action Button
     * @param enable
     */
    public void activateAllWidgets(boolean enable){
        iAudio.activateAudioPlayerWidgets(enable);
        // Stop Notification
        iAudio.stopNotificationAudio();
    }

    /**
     * Stop audio Media player
     * @param mediaPlayer
     */
    public void stopMediaPlayer(MediaPlayer mediaPlayer){
        CommonPresenter.stopMediaPlayer(mediaPlayer);
    }

    /**
     * Stop all medi sound
     * @param audio
     */
    public void stopAllOtherMediaSound(Audio audio){
        iAudio.stopOtherMediaPlayerSound(audio);
    }

    // Config before load stream audio
    @Override
    public void streamAudioBeforeLoading() {
        iAudio.progressBarAudioPlayerVisibility(View.VISIBLE);
        iAudio.textMediaPlayInfoLoading();
        iAudio.activateAudioPlayerWidgets(false);
    }

    // When stream audio is loading
    @Override
    public void streamAudioLoading(Audio audio) {
        iAudio.loadAudioPlayerAndPlay(audio);
    }

    // When load stream audio is finished
    @Override
    public void streamAudioLoadingFinished() {
        iAudio.progressBarAudioPlayerVisibility(View.GONE);
        iAudio.activateAudioPlayerWidgets(true);
    }

    // When load stream audio makes error
    @Override
    public void streamAudioLoadingFailure() {

    }
}
