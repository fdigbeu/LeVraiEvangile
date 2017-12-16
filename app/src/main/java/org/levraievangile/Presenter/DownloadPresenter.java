package org.levraievangile.Presenter;

import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.levraievangile.Model.DownloadFile;
import org.levraievangile.Model.LoadDownloadAudio;
import org.levraievangile.Model.LoadDownloadPdf;
import org.levraievangile.Model.LoadDownloadVideo;
import org.levraievangile.View.Interfaces.DownloadView;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Maranatha on 11/12/2017.
 */

public class DownloadPresenter implements DownloadView.ILoadDownload{
    // Ref interface
    private DownloadView.IDownload iDownload;
    private DownloadView.IDownloadAudioView iDownloadAudioView;
    private DownloadView.IDownloadVideoView iDownloadVideoView;
    private DownloadView.IDownloadPdfView iDownloadPdfView;
    // Ref LoadDownloadFile
    private LoadDownloadAudio loadDownloadAudio;
    private LoadDownloadVideo loadDownloadVideo;
    private LoadDownloadPdf loadDownloadPdf;

    // Constructors
    public DownloadPresenter(DownloadView.IDownload iDownload) {
        this.iDownload = iDownload;
    }

    public DownloadPresenter(DownloadView.IDownloadAudioView iDownloadAudioView) {
        this.iDownloadAudioView = iDownloadAudioView;
    }

    public DownloadPresenter(DownloadView.IDownloadVideoView iDownloadVideoView) {
        this.iDownloadVideoView = iDownloadVideoView;
    }

    public DownloadPresenter(DownloadView.IDownloadPdfView iDownloadPdfView) {
        this.iDownloadPdfView = iDownloadPdfView;
    }

    public DownloadPresenter(DownloadView.IDownload iDownload, DownloadView.IDownloadAudioView iDownloadAudioView) {
        this.iDownload = iDownload;
        this.iDownloadAudioView = iDownloadAudioView;
    }

    public DownloadPresenter(DownloadView.IDownload iDownload, DownloadView.IDownloadVideoView iDownloadVideoView) {
        this.iDownload = iDownload;
        this.iDownloadVideoView = iDownloadVideoView;
    }

    public DownloadPresenter(DownloadView.IDownload iDownload, DownloadView.IDownloadPdfView iDownloadPdfView) {
        this.iDownload = iDownload;
        this.iDownloadPdfView = iDownloadPdfView;
    }

    // Load download data
    public void loadDownloadData(Context context){
        iDownload.initialize();
        iDownload.events();
    }

    // Load download audio data
    public void loadDownloadAudioFragData(View view){
        iDownloadAudioView.initialize(view);
        iDownloadAudioView.events();
    }

    // Load download video data
    public void loadDownloadVideoFragData(View view){
        iDownloadVideoView.initialize(view);
        iDownloadVideoView.events();
    }

    // Load download pdf data
    public void loadDownloadPdfFragData(View view){
        iDownloadPdfView.initialize(view);
        iDownloadPdfView.events();
    }

    // If downloadAudio Fragment Attach Success
    public void downloadAudioFragmentAttachSuccess(Context context){
        if(iDownload != null && iDownloadAudioView != null){
            ArrayList<DownloadFile> downloadFiles = iDownload.getStorageDownloadFilesAudioData();
            if(downloadFiles==null){
                LoadDownloadAudio loadDownloadAudio = new LoadDownloadAudio();
                loadDownloadAudio.initializeData(context, this);
                loadDownloadAudio.execute();
                iDownloadAudioView.progressBarVisibility(View.VISIBLE);
            }
            else {
                iDownloadAudioView.loadDownloadAudioData(downloadFiles, 1);
            }
        }
    }

    // If downloadVideo Fragment Attach Success
    public void downloadVideoFragmentAttachSuccess(Context context){
        if(iDownload != null && iDownloadVideoView != null){
            ArrayList<DownloadFile> downloadFiles = iDownload.getStorageDownloadFilesVideoData();
            if(downloadFiles==null){
                LoadDownloadVideo loadDownloadVideo = new LoadDownloadVideo();
                loadDownloadVideo.initializeData(context, this);
                loadDownloadVideo.execute();
                iDownloadVideoView.progressBarVisibility(View.VISIBLE);
            }
            else {
                iDownloadVideoView.loadDownloadVideoData(downloadFiles, 1);
            }
        }
    }

    // If downloadPdf Fragment Attach Success
    public void downloadPdfFragmentAttachSuccess(Context context){
        if(iDownload != null && iDownloadPdfView != null){
            ArrayList<DownloadFile> downloadFiles = iDownload.getStorageDownloadFilesPdfData();
            if(downloadFiles==null){
                LoadDownloadPdf loadDownloadPdf = new LoadDownloadPdf();
                loadDownloadPdf.initializeData(context, this);
                loadDownloadPdf.execute();
                iDownloadPdfView.progressBarVisibility(View.VISIBLE);
            }
            else {
                iDownloadPdfView.loadDownloadPdfData(downloadFiles, 1);
            }
        }
    }

    // Manage menu Item
    public void retrieveUserAction(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                iDownload.closeActivity();
                break;
        }
    }

    @Override
    public void downloadAudioStarted() {}

    @Override
    public void downloadAudioFinished(ArrayList<DownloadFile> downloadFiles) {
        iDownloadAudioView.loadDownloadAudioData(downloadFiles, 1);
        iDownloadAudioView.progressBarVisibility(View.GONE);
        iDownload.storageDownloadFilesList(0, downloadFiles);
    }

    @Override
    public void downloadAudioFailure() {}

    @Override
    public void downloadVideoStarted() {}

    @Override
    public void downloadVideoFinished(ArrayList<DownloadFile> downloadFiles) {
        iDownloadVideoView.loadDownloadVideoData(downloadFiles, 1);
        iDownloadVideoView.progressBarVisibility(View.GONE);
        iDownload.storageDownloadFilesList(1, downloadFiles);
    }

    @Override
    public void downloadVideoFailure() {}

    @Override
    public void downloadPdfFinished(ArrayList<DownloadFile> downloadFiles) {
        iDownloadPdfView.loadDownloadPdfData(downloadFiles, 1);
        iDownloadPdfView.progressBarVisibility(View.GONE);
        iDownload.storageDownloadFilesList(2, downloadFiles);
    }

    @Override
    public void downloadPdfFailure() {}

    public void cancelAsyntask(){
        if(loadDownloadAudio != null) loadDownloadAudio.cancel(true);
        if(loadDownloadVideo != null) loadDownloadVideo.cancel(true);
        if(loadDownloadPdf != null) loadDownloadPdf.cancel(true);
    }
}
