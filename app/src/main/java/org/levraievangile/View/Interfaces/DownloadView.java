package org.levraievangile.View.Interfaces;

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
        public void storageDownloadFilesList(int key,  ArrayList<DownloadFile> downloadFilesList);
        public ArrayList<DownloadFile> getStorageDownloadFilesAudioData();
        public ArrayList<DownloadFile> getStorageDownloadFilesVideoData();
        public ArrayList<DownloadFile> getStorageDownloadFilesPdfData();
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
        public void downloadAudioFinished(ArrayList<DownloadFile> downloadFiles);
        public void downloadAudioFailure();
        public void downloadVideoStarted();
        public void downloadVideoFinished(ArrayList<DownloadFile> downloadFiles);
        public void downloadVideoFailure();
        public void downloadPdfFinished(ArrayList<DownloadFile> downloadFiles);
        public void downloadPdfFailure();
    }
}
