package org.levraievangile.View.Interfaces;

import android.media.MediaPlayer;

import org.levraievangile.Model.Audio;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Maranatha on 06/12/2017.
 */

public class AudioView {
    public interface IAudio{
        public void initialize();
        public void events();
        public void loadAudioData(ArrayList<Audio> audios, int numberColumns);
        public void scrollAudioDataToPosition(int positionScroll);
        public void instanciateIAudioRecycler(AudioView.IAudioRecycler iAudioRecycler);
        public void progressBarVisibility(int visibility);
        public void recyclerViewVisibility(int visibility);
        public void closeActivity();
        public void modifyHeaderInfos(String typeLibelle);
        public void askPermissionToSaveFile();
        public void modifyBarHeader(String title, String subTitle);
        public void stopRefreshing(boolean refreshing);
        // Audio player
        public void audioPlayerVisibility(int visibility);
        public void textMediaPlayInfoLoading();
        public void activateAudioPlayerWidgets(boolean enable);
        public void loadAudioPlayerAndPlay(Audio audio);
        public void stopOtherMediaPlayerSound(Audio audio);
        public void progressBarAudioPlayerVisibility(int visibility);
        public void playNextAudio();
        public void playPreviousAudio();
        public void playNotificationAudio();
        public void stopNotificationAudio();
        public MediaPlayer getInstanceMediaPlayer();
    }

    // Presenter interface
    public interface IPresenter{}
    
    // AudioRecyclerAdapter interface
    public interface IAudioRecycler{
        public void playNextAudio();
        public void playPreviousAudio();
    }

    public interface IStreamAudio{
        public void streamAudioBeforeLoading();
        public void streamAudioLoading(Audio audio);
        public void streamAudioLoadingFinished();
        public void streamAudioLoadingFailure();
    }

    public interface IApiRessource {
        @GET("webservice/audios/{TYPE}/")
        Call<List<Audio>> getAllAudios(@Path(value = "TYPE", encoded = true) String keyWord);

        @GET("webservice/audios/rechercher/motclef/{KEY_WORD}/")
        Call<List<Audio>> getAllSearchAudios(@Path(value = "KEY_WORD") String keyWord);
    }

}
