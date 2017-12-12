package org.levraievangile.Model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import org.levraievangile.View.Interfaces.DownloadView;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Maranatha on 11/12/2017.
 */

public class LoadDownloadAudio extends AsyncTask<Void, Void, Void> {

    private  Context context;
    private  String typeResource;
    private DownloadView.ILoadDownload iLoadDownload;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    private Hashtable<String, String> audiosFiles(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Hashtable<String, String> audioList = null;
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " DESC";
        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);

        if (cursor != null && cursor.getCount() > 0) {
            audioList = new Hashtable<>();
            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                // Save to audioList
                audioList.put("data", data);
                audioList.put("title", title);
                audioList.put("album", album);
                audioList.put("artist", artist);
                audioList.put("duration", duration);
            }
        }
        cursor.close();
        return audioList;
    }

    private Hashtable<String, String> videosFiles(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Hashtable<String, String> videoList = null;
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        //String selection = MediaStore.Video.Media.IS_VIDEO + "!= 0";
        String sortOrder = MediaStore.Video.Media.TITLE + " DESC";
        Cursor cursor = contentResolver.query(uri, null, null, null, sortOrder);

        if (cursor != null && cursor.getCount() > 0) {
            videoList = new Hashtable<>();
            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                // Save to audioList
                videoList.put("data", data);
                videoList.put("title", title);
                videoList.put("album", album);
                videoList.put("artist", artist);
                videoList.put("duration", duration);
            }
        }
        cursor.close();
        return videoList;
    }

    public void initializeData(Context context, String typeResource, DownloadView.ILoadDownload iLoadDownload){
        this.context = context;
        this.typeResource = typeResource;
        this.iLoadDownload = iLoadDownload;
    }
}
