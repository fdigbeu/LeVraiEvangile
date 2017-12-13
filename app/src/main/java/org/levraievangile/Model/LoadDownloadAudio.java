package org.levraievangile.Model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import org.levraievangile.Presenter.CommonPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Interfaces.DownloadView;

import java.util.ArrayList;

/**
 * Created by Maranatha on 11/12/2017.
 */

public class LoadDownloadAudio extends AsyncTask<Void, Void, ArrayList<DownloadFile>> {

    private  Context context;
    private DownloadView.ILoadDownload iLoadDownload;
    private ArrayList<DownloadFile> audioList = null;

    @Override
    protected void onPreExecute() {
        iLoadDownload.downloadAudioStarted();
        super.onPreExecute();
    }

    @Override
    protected ArrayList<DownloadFile> doInBackground(Void... voids) {
        ArrayList<DownloadFile> downloadFiles = audiosFiles(context);
        return downloadFiles;
    }

    @Override
    protected void onPostExecute(ArrayList<DownloadFile> downloadFiles) {
        super.onPostExecute(downloadFiles);
        iLoadDownload.downloadAudioFinished(downloadFiles);
    }

    private ArrayList<DownloadFile> audiosFiles(Context context) {
        // Get Lve audio saved
        audioList = new ArrayList<>();
        DAOFavoris daoFavoris = new DAOFavoris(context);
        ArrayList<Favoris> mList = daoFavoris.getAllData("download_audio");
        if(mList != null && mList.size() > 0){
            for (int i = mList.size()-1; i > 0; i--){
                Favoris favoris = mList.get(i);
                String data = CommonPresenter.getAudioPath()+"/"+favoris.getSrc();
                String title = favoris.getTitre();
                String album = favoris.getType_libelle();
                String artist = favoris.getAuteur();
                String duration = favoris.getDuree();
                int mipmap = favoris.getMipmap();
                String shortcode = "download-audio";
                String date = favoris.getDate();
                audioList.add(new DownloadFile(data, title, album, artist, duration, mipmap, date, shortcode));
            }
        }
        //--
        //getAllAudiosStorage();
        //--
        return audioList;
    }

    private void getAllAudiosStorage(){
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.DATE_ADDED + " DESC";
        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);
        //--
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String date = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED));
                // Save to audioList;
                if(!data.contains("LVE/Audios/")) {
                    audioList.add(new DownloadFile(data, title, album, artist, CommonPresenter.getHourMinuteSecondBy(Integer.parseInt(duration)), R.mipmap.sm_audio, date, "download-audio"));
                }
            }
        }
        cursor.close();
    }

    public void initializeData(Context context, DownloadView.ILoadDownload iLoadDownload){
        this.context = context;
        this.iLoadDownload = iLoadDownload;
    }
}
