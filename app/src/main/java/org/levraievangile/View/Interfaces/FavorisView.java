package org.levraievangile.View.Interfaces;

import android.media.MediaPlayer;
import android.view.View;

import org.levraievangile.Model.Audio;
import org.levraievangile.Model.Favoris;
import org.levraievangile.Model.Video;

import java.util.ArrayList;

/**
 * Created by Maranatha on 11/12/2017.
 */

public class FavorisView {
    public interface IFravoris{
        public void initialize();
        public void events();
        public void closeActivity();
        public void activateAudioPlayerWidgets(boolean enable);
        public void loadAudioPlayerAndPlay(final Audio audio);
        public void stopOtherMediaPlayerSound(Audio audio);
        public void progressBarAudioPlayerVisibility(int visibility);
        public void textMediaPlayInfoLoading();
        public void instanciateIFavorisRecycler(FavorisView.IFavorisRecycler iFavorisRecycler);
        public MediaPlayer getInstanceMediaPlayer();
        public void notifyThatAudioIsSelected();
        public void askPermissionToSaveFile();
        public void playNextAudio();
        public void playPreviousAudio();
        public void playNotificationAudio();
        public void stopNotificationAudio();
        public void audioPlayerVisibility(int visibility);
    }

    public interface IPlaceholder{
        public void initialize(View rootView);
        public void events();
        public void loadFavorisAudioData(ArrayList<Favoris> favorites, int numberColumns);
        public void loadFavorisVideoData(ArrayList<Favoris> favorites, int numberColumns);
        public void loadFavorisPdfData(ArrayList<Favoris> favorites, int numberColumns);
        public void progressBarVisibility(int visibility);
        public void launchActivity(String value);
        public void launchVideoToPlay(Video video, int position);
        public void scrollAudioDataToPosition(int positionScroll);
        public void scrollVideoDataToPosition(int positionScroll);
        public void instanciateIFavorisRecycler(FavorisView.IFavorisRecycler iFavorisRecycler);
    }

    public interface IPresenter{}

    // FavorisRecyclerAdapter interface
    public interface IFavorisRecycler{
        public void playNextVideo();
        public void playPreviousVideo();
        public void playNextAudio();
        public void playPreviousAudio();
    }
}
