package org.levraievangile.View.Interfaces;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;

import org.levraievangile.Model.Audio;
import org.levraievangile.Model.DownloadFile;
import org.levraievangile.Model.Video;

import java.util.ArrayList;

/**
 * Created by Maranatha on 11/12/2017.
 */

public class DownloadView {
    public interface IDownload{
        public void initialize();
        public void events();
        public void closeActivity();
        public void instanciateIDownloadAudioRecycler(DownloadView.IDownloadAudioRecycler iDownloadAudioRecycler);
        public MediaPlayer getInstanceMediaPlayer();
        public void storageDownloadFilesList(int key,  ArrayList<DownloadFile> downloadFilesList);
        public ArrayList<DownloadFile> getStorageDownloadFilesAudioData();
        public ArrayList<DownloadFile> getStorageDownloadFilesVideoData();
        public ArrayList<DownloadFile> getStorageDownloadFilesPdfData();
        public void onAudioSelected(Audio audio, int position);
        public void audioPlayerVisibility(int visibility);
        public void activateAudioPlayerWidgets(boolean enable);
        public void loadAudioPlayerAndPlay(final Audio audio);
        public void stopOtherMediaPlayerSound(Audio audio);
        public void progressBarAudioPlayerVisibility(int visibility);
        public void textMediaPlayInfoLoading();
        public void playNextAudio();
        public void playPreviousAudio();
        public void playNotificationAudio();
        public void stopNotificationAudio();
    }

    public interface IDownloadAudioView{
        public void initialize(View rootView);
        public void events();
        public void loadDownloadAudioData(ArrayList<DownloadFile> downloads, int numberColumns);
        public void progressBarVisibility(int visibility);
        //--
        public void scrollAudioDataToPosition(int positionScroll);
        public void instanciateIDownloadAudioRecycler(DownloadView.IDownloadAudioRecycler iDownloadAudioRecycler);
        public void launchAudioToPlay(Audio audio, int position);
    }

    public interface IDownloadVideoView{
        public void initialize(View rootView);
        public void events();
        public void loadDownloadVideoData(ArrayList<DownloadFile> downloads, int numberColumns);
        public void progressBarVisibility(int visibility);
        //--
        public void scrollVideoDataToPosition(int positionScroll);
        public void instanciateIDownloadVideoRecycler(DownloadView.IDownloadVideoRecycler iDownloadVideoRecycler);
        public void launchVideoToPlay(Video video, int position);
    }

    // DownloadRecyclerAdapter interface for videos list
    public interface IDownloadVideoRecycler{
        public void playNextVideo();
        public void playPreviousVideo();
    }

    // DownloadRecyclerAdapter interface for audios list
    public interface IDownloadAudioRecycler{
        public void playNextAudio();
        public void playPreviousAudio();
    }

    // DownloadRecyclerAdapter interface for pdf list
    public interface IDownloadPdfRecycler{
    }

    public interface IDownloadPdfView{
        public void initialize(View rootView);
        public void events();
        public void loadDownloadPdfData(ArrayList<DownloadFile> downloads, int numberColumns);
        public void progressBarVisibility(int visibility);
        //--
        public void instanciateIDownloadPdfRecycler(DownloadView.IDownloadPdfRecycler iDownloadPdfRecycler);
        public void readPdfFile(String filepath);
    }

    public interface IPresenter{}

    public interface ILoadDownload{
        public void downloadAudioStarted();
        public void downloadAudioFinished(Context context, ArrayList<DownloadFile> downloadFiles);
        public void downloadAudioFailure();
        public void downloadVideoStarted();
        public void downloadVideoFinished(ArrayList<DownloadFile> downloadFiles);
        public void downloadVideoFailure();
        public void downloadPdfFinished(ArrayList<DownloadFile> downloadFiles);
        public void downloadPdfFailure();
    }
}
