package org.levraievangile.View.Interfaces;

import android.content.Context;

import org.levraievangile.Model.Video;

/**
 * Created by Maranatha on 27/11/2017.
 */

public class VideoPlayerView {
    // VideoPlayerActivity interface
    public interface IVideoPlayer{
        public void hideHeader();
        public void initialize();
        public void events();
        public void displayPlayer(Video ressource, int largeurScreen, int hauteurScreen);
        public void fabVisibility(int visibility);
        public void headerVisibility(int visibility);
        public void progressBarVisibility(int visibility);
        public void btnNavigationVisibility(int visibility);
        public void showPlayerWidgetsOnTouch();
        public void playNextVideo();
        public void playPreviousVideo();
        public void downloadVideo(Context context);
        public void shareVideo(Context context);
        public void addVideoToFavorite(Context context);
        public void closeActivity();
        public void pauseNotificationAudio();
    }

    public interface IPresenter{}
}
