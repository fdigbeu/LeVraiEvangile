package org.levraievangile.View.Interfaces;

import org.levraievangile.Model.Audio;
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
        public void launchNotificationAudio();
        public void closeActivity();
        public void modifyHeaderInfos(String typeLibelle);
        public void askPermissionToSaveFile();
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
        Call<List<Audio>> getAllAudios(@Path(value = "TYPE") String keyWord);
    }

}
