package org.levraievangile.Model;

import android.os.AsyncTask;

import org.levraievangile.View.Interfaces.AudioView;

/**
 * Created by Maranatha on 09/12/2017.
 */

public class LoadStreamAudio extends AsyncTask<Void, Void, Void> {

    private AudioView.IStreamAudio iStreamAudio;
    private Audio audio;

    @Override
    protected void onPreExecute() {
        iStreamAudio.streamAudioBeforeLoading();
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        iStreamAudio.streamAudioLoading(audio);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        iStreamAudio.streamAudioLoadingFinished();
        super.onPostExecute(aVoid);
    }

    public void initializeData(Audio audio, AudioView.IStreamAudio iStreamAudio){
        this.audio = audio;
        this.iStreamAudio = iStreamAudio;
    }
}
