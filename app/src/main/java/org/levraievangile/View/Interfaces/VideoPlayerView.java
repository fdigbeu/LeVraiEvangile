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
        public void hideBtnDownloadShareFavorite();
        public void showPlayerWidgetsOnTouch();
        public void playNextVideo();
        public void playPreviousVideo();
        public void closeActivity();
        public void askPermissionToSaveFile();
        public void pauseNotificationAudio();
        public boolean isCurrentVideoIsNotification();
    }

    public interface IPresenter{}
}
