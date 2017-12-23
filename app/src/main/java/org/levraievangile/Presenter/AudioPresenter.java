package org.levraievangile.Presenter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.levraievangile.Model.ApiClient;
import org.levraievangile.Model.Audio;
import org.levraievangile.Model.DAOFavoris;
import org.levraievangile.Model.Favoris;
import org.levraievangile.Model.LoadStreamAudio;
import org.levraievangile.Model.Setting;
import org.levraievangile.R;
import org.levraievangile.View.Interfaces.AudioView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.levraievangile.Presenter.CommonPresenter.KEY_ALL_AUDIOS_LIST;
import static org.levraievangile.Presenter.CommonPresenter.KEY_AUDIO_SELECTED;
import static org.levraievangile.Presenter.CommonPresenter.KEY_FORM_SEARCH_WORD;
import static org.levraievangile.Presenter.CommonPresenter.KEY_IS_USER_LEVEL_ADMIN;
import static org.levraievangile.Presenter.CommonPresenter.KEY_NOTIF_AUDIOS_LIST;
import static org.levraievangile.Presenter.CommonPresenter.KEY_NOTIF_PLAYER_PLAY_NEXT;
import static org.levraievangile.Presenter.CommonPresenter.KEY_NOTIF_PLAYER_PLAY_PREVIOUS;
import static org.levraievangile.Presenter.CommonPresenter.KEY_NOTIF_PLAYER_SELECTED;
import static org.levraievangile.Presenter.CommonPresenter.KEY_PLAYER_AUDIO_TO_NOTIF_AUDIO_TIME_ELAPSED;
import static org.levraievangile.Presenter.CommonPresenter.KEY_SETTING_CONCATENATE_AUDIO_READING;
import static org.levraievangile.Presenter.CommonPresenter.KEY_SETTING_WIFI_EXCLUSIF;
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
        if(intent != null && intent.getStringExtra(KEY_FORM_SEARCH_WORD) == null) {
            try {
                String userLevel = CommonPresenter.getDataFromSharePreferences(context, KEY_IS_USER_LEVEL_ADMIN);
                String shortCodeLevel = userLevel.equalsIgnoreCase("YES") ? "/acces-admin" : "";
                //Get list audio by short-code
                final String shortCode = intent.getStringExtra(KEY_SHORT_CODE);
                iAudio.modifyHeaderInfos(CommonPresenter.getLibelleByTypeShortcode(shortCode));
                if(CommonPresenter.isMobileConnected(context)) {
                    Call<List<Audio>> callAudios = ApiClient.getApiClientLeVraiEvangile().create(AudioView.IApiRessource.class).getAllAudios(shortCode+shortCodeLevel);
                    callAudios.enqueue(new Callback<List<Audio>>() {
                        @Override
                        public void onResponse(Call<List<Audio>> call, Response<List<Audio>> response) {
                            ArrayList<Audio> audios = (ArrayList<Audio>) response.body();
                            final String keyShortCode = KEY_ALL_AUDIOS_LIST + "-" + shortCode;
                            if(audios != null) {
                                CommonPresenter.saveDataInSharePreferences(context, keyShortCode, audios.toString());
                                CommonPresenter.saveDataInSharePreferences(context, KEY_NOTIF_AUDIOS_LIST, audios.toString());
                            }
                            iAudio.loadAudioData(audios, 1);
                            iAudio.progressBarVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<List<Audio>> call, Throwable t) {
                            String key = KEY_ALL_AUDIOS_LIST+"-"+shortCode;
                            ArrayList<Audio> audios = CommonPresenter.getAllAudiosByKey(context, key);
                            iAudio.loadAudioData(audios, 1);
                            iAudio.progressBarVisibility(View.GONE);
                        }
                    });
                }
                else{
                    String key = KEY_ALL_AUDIOS_LIST+"-"+shortCode;
                    ArrayList<Audio> audios = CommonPresenter.getAllAudiosByKey(context, key);
                    iAudio.loadAudioData(audios, 1);
                    iAudio.progressBarVisibility(View.GONE);
                    //--
                    if(audios.size()==0){
                        // Display no connection message
                        CommonPresenter.showNoConnectionMessage(context, true);
                    }
                }
            }
            catch (Exception ex){}
        }
        else{
            // If it's to search
            final String keyWord = intent.getStringExtra(KEY_FORM_SEARCH_WORD);
            Call<List<Audio>> callAudios = ApiClient.getApiClientLeVraiEvangile().create(AudioView.IApiRessource.class).getAllSearchAudios(keyWord);
            callAudios.enqueue(new Callback<List<Audio>>() {
                @Override
                public void onResponse(Call<List<Audio>> call, Response<List<Audio>> response) {
                    ArrayList<Audio> audios = (ArrayList<Audio>) response.body();
                    CommonPresenter.saveDataInSharePreferences(context, KEY_NOTIF_AUDIOS_LIST, audios.toString());
                    iAudio.loadAudioData(audios, 1);
                    iAudio.progressBarVisibility(View.GONE);
                    iAudio.modifyBarHeader("Recherche d'audios", "TOTAL : "+audios.size()+" | MOT CLÉ : "+keyWord);
                }

                @Override
                public void onFailure(Call<List<Audio>> call, Throwable t) {
                    iAudio.progressBarVisibility(View.GONE);
                    iAudio.modifyBarHeader("Recherche d'audios", "Aucun audio trouvé | MOT CLÉ : "+keyWord);
                }
            });
        }
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
            ArrayList<Audio> mList = CommonPresenter.getAllAudiosByKey(context, KEY_NOTIF_AUDIOS_LIST);
            int previousPosition = CommonPresenter.getNotifPlayerPreviousValue(position, mList.size());
            CommonPresenter.saveDataInSharePreferences(context, KEY_NOTIF_PLAYER_PLAY_PREVIOUS, ""+previousPosition);
            int nextPosition = CommonPresenter.getNotifPlayerNextValue(position, mList.size());
            CommonPresenter.saveDataInSharePreferences(context, KEY_NOTIF_PLAYER_PLAY_NEXT, ""+nextPosition);
        }
        else{
            // Show no connection message
            CommonPresenter.showNoConnectionMessage(context, false);
        }
    }

    // Manage menu Item
    public void retrieveUserAction(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                iAudio.closeActivity();
                break;
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
                    playAudioNotification(view.getContext(), mediaPlayer);
                    break;

                case R.id.audio_player_close:
                    closeAudioMediaPlayer(mediaPlayer);
                    break;
            }
        }
        catch (Exception ex){}
    }

    // Play audio notification
    private void playAudioNotification(Context context, MediaPlayer mediaPlayer){
        CommonPresenter.saveDataInSharePreferences(context, KEY_PLAYER_AUDIO_TO_NOTIF_AUDIO_TIME_ELAPSED, ""+mediaPlayer.getCurrentPosition());
        closeAudioMediaPlayer(mediaPlayer);
        iAudio.playNotificationAudio();
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
                    if(isAuthorizeDownload) {
                        downloadThisAudio(view.getContext());
                    }
                    break;

                case R.id.fab_player_share_app:
                    CommonPresenter.shareAudio(view.getContext(), audioSelected);
                    break;

                case R.id.fab_player_favorite:
                    DAOFavoris daoFavoris = new DAOFavoris(view.getContext());
                    if(!daoFavoris.isFavorisExists(audioSelected.getSrc(), "audio")){
                        Favoris favoris = new Favoris(audioSelected.getId(), "audio", audioSelected.getMipmap(), audioSelected.getUrlacces(), audioSelected.getSrc(), audioSelected.getTitre(), audioSelected.getAuteur(), audioSelected.getDuree(), audioSelected.getDate(), audioSelected.getType_libelle(), audioSelected.getType_shortcode(), audioSelected.getId());
                        daoFavoris.insertData(favoris);
                        Toast.makeText(view.getContext(), view.getContext().getResources().getString(R.string.audio_add_to_favorite), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(view.getContext(), view.getContext().getResources().getString(R.string.audio_already_add_to_favorite), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
        catch (Exception ex){}
    }


    // When the audio is finished
    public void retrieveOnCompletionAction(Context context){
        Setting mSetting = CommonPresenter.getSettingObjectFromSharePreferences(context, KEY_SETTING_CONCATENATE_AUDIO_READING);
        if(mSetting.getChoice()) {
            iAudio.playNextAudio();
        }
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
                iAudio.askPermissionToSaveFile();
            }
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
