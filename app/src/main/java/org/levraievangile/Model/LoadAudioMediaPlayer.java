package org.levraievangile.Model;

import android.os.AsyncTask;
import android.view.View;

import org.levraievangile.View.Interfaces.DownloadView;
import org.levraievangile.View.Interfaces.FavorisView;

/**
 * Created by Maranatha on 21/12/2017.
 */

public class LoadAudioMediaPlayer extends AsyncTask<Void, Void, Void> {
    private int position;
    private FavorisView.IFravoris iFravoris;
    private DownloadView.IDownload iDownload;
    private Audio audio;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(iDownload != null){
            iDownload.activateAudioPlayerWidgets(false);
            iDownload.progressBarAudioPlayerVisibility(View.VISIBLE);
            iDownload.textMediaPlayInfoLoading();
        }
        else if(iFravoris != null){
            iFravoris.activateAudioPlayerWidgets(false);
            iFravoris.progressBarAudioPlayerVisibility(View.VISIBLE);
            iFravoris.textMediaPlayInfoLoading();
        }
        else{}
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if(iDownload != null) {
            iDownload.loadAudioPlayerAndPlay(audio);
        }
        else if(iFravoris != null){
            iFravoris.loadAudioPlayerAndPlay(audio);
        }
        else{}
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(iDownload != null) {
            iDownload.activateAudioPlayerWidgets(true);
            iDownload.audioPlayerVisibility(View.VISIBLE);
            iDownload.progressBarAudioPlayerVisibility(View.GONE);
        }
        else if(iFravoris != null){
            iFravoris.activateAudioPlayerWidgets(true);
            iFravoris.audioPlayerVisibility(View.VISIBLE);
            iFravoris.progressBarAudioPlayerVisibility(View.GONE);
        }
        else{}
    }

    // Initialize with DownloadActivity IDownload
    public void initLoadAudioMediaPlayer(Audio audio, int position, DownloadView.IDownload iDownload){
        this.position = position;
        this.audio = audio;
        this.iDownload = iDownload;
    }

    // Initialize with FavorisActivity IFavoris
    public void initLoadAudioMediaPlayer(Audio audio, int position, FavorisView.IFravoris iFravoris){
        this.position = position;
        this.audio = audio;
        this.iFravoris = iFravoris;
    }
}
