package org.levraievangile.View.Interfaces;

import android.view.View;

import org.levraievangile.Model.DownloadFile;

import java.util.ArrayList;

/**
 * Created by Maranatha on 11/12/2017.
 */

public class DownloadView {
    public interface IDownload{
        public void initialize();
        public void events();
    }

    public interface IPlaceholder{
        public void initialize(View rootView);
        public void events();
        public void loadDownloadAudioData(ArrayList<DownloadFile> downloads, int numberColumns);
        public void loadDownloadVideoData(ArrayList<DownloadFile> downloads, int numberColumns);
        public void loadDownloadPdfData(ArrayList<DownloadFile> downloads, int numberColumns);
        public void progressBarVisibility(int visibility);
    }

    public interface IPresenter{}

    public interface ILoadDownload{
        public void downloadAudioStarted();
        public void downloadAudioFinished(ArrayList<DownloadFile> downloadFiles);
        public void downloadAudioFailure();
        public void downloadVideoStarted();
        public void downloadVideoFinished(ArrayList<DownloadFile> downloadFiles);
        public void downloadVideoFailure();
    }
}
