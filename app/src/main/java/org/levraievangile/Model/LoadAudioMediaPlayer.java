package org.levraievangile.Model;

import android.os.AsyncTask;
import android.view.View;

import org.levraievangile.R;
import org.levraievangile.View.Interfaces.DownloadView;

/**
 * Created by Maranatha on 21/12/2017.
 */

public class LoadAudioMediaPlayer extends AsyncTask<Void, Void, Void> {
    private int position;
    private DownloadView.IDownload iDownload;
    private Audio audio;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        iDownload.activateAudioPlayerWidgets(false);
        iDownload.progressBarAudioPlayerVisibility(View.VISIBLE);
        iDownload.textMediaPlayInfoLoading();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        iDownload.loadAudioPlayerAndPlay(audio);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        iDownload.activateAudioPlayerWidgets(true);
        iDownload.audioPlayerVisibility(View.VISIBLE);
        iDownload.progressBarAudioPlayerVisibility(View.GONE);
    }

    public void initLoadAudioMediaPlayer(Audio audio, int position, DownloadView.IDownload iDownload){
        this.position = position;
        this.audio = audio;
        this.iDownload = iDownload;
    }
}
